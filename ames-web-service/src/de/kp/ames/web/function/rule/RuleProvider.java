package de.kp.ames.web.function.rule;
/**
 *	Copyright 2012 Dr. Krusche & Partner PartG
 *
 *	AMES-Web-Service is free software: you can redistribute it and/or 
 *	modify it under the terms of the GNU General Public License 
 *	as published by the Free Software Foundation, either version 3 of 
 *	the License, or (at your option) any later version.
 *
 *	AMES- Web-Service is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * 
 *  See the GNU General Public License for more details. 
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this software. If not, see <http://www.gnu.org/licenses/>.
 *
 */

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.freebxml.omar.client.xml.registry.infomodel.AssociationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ConceptImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExternalLinkImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceBindingImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceImpl;
import org.freebxml.omar.client.xml.registry.infomodel.SlotImpl;
import org.freebxml.omar.client.xml.registry.infomodel.SpecificationLinkImpl;
import org.freebxml.omar.common.CanonicalSchemes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.format.StringOutputStream;
import de.kp.ames.web.core.format.xml.XmlObject;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.util.FileUtil;

public class RuleProvider extends JaxrDQM {

	/*
	 * Canonical Classification Nodes
	 */
	private static String ASSOCIATION      = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_Association;
	private static String EXTERNAL_LINK    = CanonicalSchemes.CANONICAL_OBJECT_TYPE_CODE_ExternalLink;
	private static String EXTRINSIC_OBJECT = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_ExtrinsicObject;
	private static String ORGANIZATION     = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_Organization;
	private static String REGISTRY_OBJECT  = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_RegistryObject;
	private static String REGISTRY_PACKAGE = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_RegistryPackage;
	private static String SERVICE          = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_Service;
	private static String USER             = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_User;

	/*
	 * Namespace
	 */
	private static String SM_PRE = "sm";
	private static String SM_NS  = "urn:de:kp:xsd:sm:1.0";

	/*
	 * OASIS ebRIM components
	 */
	private String ASSOCIATION_TAG      		= "Association";
	private String CLASSIFICATION_TAG      		= "Classification";
	private String CLASSIFICATION_LIST_TAG 		= "ClassificationList";
	private String EXTERNAL_LINK_TAG   			= "ExternalLink";	
	private String EXTRINSIC_OBJECT_TAG 		= "ExtrinsicObject";	
	private String ORGANIZATION_TAG     		= "Organization";	
	private String REGISTRY_OBJECT_TAG  		= "RegistryObject";
	private String REGISTRY_OBJECT_LIST_TAG 	= "RegistryObjectList";
	private String REGISTRY_PACKAGE_TAG 		= "RegistryPackage";
	private String SERVICE_TAG 					= "Service";
	private String USER_TAG          			= "User";	
	
	private String SLOT_TAG             		= "Slot";
	private String SLOT_LIST_TAG        		= "SlotList";
	private String VALUE_TAG            		= "Value";
	private String VALUE_LIST_TAG       		= "ValueList";

	private String NAME_TAG             		= "Name";
	private String DESCRIPTION_TAG      		= "Description";
	private String VERSION_INFO_TAG     		= "VersionInfo";
		
	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public RuleProvider(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Determine the xml document of the reasoner, that 
	 * refers to a Rule-based transformation
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public FileUtil getReasoner(String uid) throws Exception {
		
		String xml = reasonerToXml(uid);
		if (xml == null) throw new Exception("[RuleProvider] Conversion of Reasoner with id <" + uid + "> failed.");

		FileUtil reasoner = new FileUtil(xml.getBytes(GlobalConstants.UTF_8), GlobalConstants.MT_XML);
		reasoner.setIdentifier(uid);	
    
    	return reasoner;
		
	}
	
	/**
	 * Determine the xml document that is the source of 
	 * an xsl transformation by its unique identifier
	 * 
	 * @param uid
	 * @return
	 */
	public FileUtil getSource(String uid) {

		FileUtil source = null;
		
		try {
			
			/* 
			 * Determine registry object from unique identifier
			 */
			RegistryObjectImpl ro = getRegistryObjectById(uid);
			if (ro == null) return source;

	    	String objectType = ro.getObjectType().getKey().getId();	    	
	    	if (objectType.startsWith(EXTRINSIC_OBJECT)) {

	    		/* 
	    		 * Extrinsic objects must be restricted to xml documents; 
	    		 * this is asserted by evaluating the assciated mimetype
	    		 */
	    		
	    		ExtrinsicObjectImpl eo = (ExtrinsicObjectImpl)ro;
	    		
	    		String mimeType = eo.getMimeType().toLowerCase();   
	    		if (mimeType.equals(GlobalConstants.MT_XML) == false) return source;

				/* 
				 * Determine repository item as inputstream		
				 */
				DataHandler handler = eo.getRepositoryItem();
				if (handler != null) {
					
					source = new FileUtil();
					
					source.setIdentifier(uid);
					source.setInputStream(handler.getInputStream(), mimeType);

				}
	    		
	    		
	    	} else {
	    		
	    		/* 
	    		 * Determine xml representation of the respective non-extrinsic object;
	    		 * it is essential for the subsequent xsl transformation, that the xml
	    		 * representation refers to a registry object list
	    		 */
	    		XmlObject xmlObject = new XmlObject(jaxrHandle);
	    		xmlObject.set(ro);
	    		
				String xml = xmlObject.toXml();
				if (xml == null) return source;
				
				source = new FileUtil(xml.getBytes(GlobalConstants.UTF_8), GlobalConstants.MT_XML);
				source.setIdentifier(uid);	
	    		
	    	}
	    
	    	return source;
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {}

		return null;

	}
	
	/**
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	private String reasonerToXml(String uid) throws Exception {

		/* 
		 * Determine reasoner from unique identifier
		 */
		RegistryObjectImpl ro = getRegistryObjectById(uid);
		if (ro == null) throw new Exception("[RuleProvider] Reasoner with id <" + uid +"> does not exist.");

		/*
		 * Create Xml Document (W3C)
		 */
		Document xmlDoc = createXmlDoc();
			
		/* 
		 * An xml document that represents a certain registry object
		 * is always represented as a registry object list, even if 
		 * only one element is provided
		 */
     	Element root = xmlDoc.createElementNS(SM_NS,  SM_PRE + ":" + REGISTRY_OBJECT_LIST_TAG);    	
    	xmlDoc.appendChild(root);

    	/*
    	 * Create reasoner element
    	 */
    	Element eReasoner = createReasoner(ro, xmlDoc);
       	root.appendChild(eReasoner);
   	
		return toXml(xmlDoc);

	}

	/**
	 * Create Reasoner (service) in an XML representation
	 * 
	 * @param ro
	 * @param xmlDoc
	 * @return
	 * @throws Exception
	 */
	private Element createReasoner(RegistryObjectImpl ro, Document xmlDoc) throws Exception {

		/* 
		 * The xml representation of a reasoner is a registry package
		 * as this description is relevant for the respective RuleML
		 * transformation 
		 */
    	Element eReasoner = xmlDoc.createElementNS(SM_NS, SM_PRE + ":" + REGISTRY_PACKAGE_TAG);
		
		/*
		 * Fill common registry object information
		 */
		fillRegistryObject(ro, xmlDoc, eReasoner);
		
		/*
		 * A reasoner (ruleset) is a service object that is specified
		 * by a set of rule objects (registry packages)
		 */
		ServiceImpl service = (ServiceImpl)ro;
		
		/* 
		 * Determine rules (registry packages) from service
		 */
		List<RegistryPackageImpl> rules = getRules(service);
		if (rules.isEmpty()) return eReasoner;
		
		/* 
		 * Add registry object list to ruleset
		 */
		Element eRuleSet = createElementNS(xmlDoc, REGISTRY_PACKAGE);
		eReasoner.appendChild(eRuleSet);
		
		for (RegistryPackageImpl rule:rules) {
			eRuleSet.appendChild(toElement(rule, xmlDoc));		
		}

		return eReasoner;
		
	}
	
	/**
	 * A helper method to represent a RegistryObject 
	 * as a W3C Dom element; the structure of the
	 * element is adapted to the Rule-based processing
	 * and differs slightly from the JAXB representation
	 * 
	 * @param ro
	 * @param xmlDoc
	 * @param elem
	 * @throws Exception
	 */
	private void fillRegistryObject(RegistryObjectImpl ro, Document xmlDoc, Element elem) throws Exception {

		/* 
		 * rim.id (identifiable)		
		 */
		elem.setAttribute("id", ro.getId());		

		/*
		 * rim.home (identifiable)		
		 */
		elem.setAttribute("home", ro.getHome());

		/* 
		 * rim.slots (identifiable)
		 */
		Element eSlotList = xmlDoc.createElementNS(SM_NS, SM_PRE + ":" + SLOT_LIST_TAG);
		
		List<SlotImpl> slotList = getSlotList(ro);
		for (SlotImpl slot:slotList) {

			Element eSlot = xmlDoc.createElementNS(SM_NS, SM_PRE + ":" + SLOT_TAG);

			String name = slot.getName();				
			String type = slot.getSlotType();

			eSlot.setAttribute("name",     name);
			eSlot.setAttribute("slotType", type);
			
			/*
			 * Slot values
			 */
			Element eValueList = xmlDoc.createElementNS(SM_NS, SM_PRE + ":" + VALUE_LIST_TAG);
			
			List<String> valueList = getValueList(slot);
			for (String value:valueList) {

				Element eValue = xmlDoc.createElementNS(SM_NS, SM_PRE + ":" + VALUE_TAG);
				eValue.appendChild(xmlDoc.createTextNode(value));
				
				eValueList.appendChild(eValue);
				
			}

			eSlot.appendChild(eValueList);
			eSlotList.appendChild(eSlot);
			
		}

		elem.appendChild(eSlotList);

		/* 
		 * rim.lid (registry object)
		 */
		elem.setAttribute("lid", ro.getLid());

		/* 
		 * rim.objectType (registry object)
		 */
    	String otype = ro.getObjectType().getKey().getId();
		elem.setAttribute("objectType", otype);

		/* 
		 * rim.status (registry object)
		 */
		elem.setAttribute("status", getStatus(ro));

		/* 
		 * rim.name (registry object)
		 */
		Element eName = xmlDoc.createElementNS(SM_NS, SM_PRE + ":" + NAME_TAG);
		eName.appendChild(xmlDoc.createTextNode(getName(ro)));

		elem.appendChild(eName);
				
		/* 
		 * rim.description (registry object)
		 */
		Element eDesc = xmlDoc.createElementNS(SM_NS, SM_PRE + ":" + DESCRIPTION_TAG);
		eDesc.appendChild(xmlDoc.createTextNode(getDescription(ro)));

		elem.appendChild(eDesc);

		/* 
		 * rim.versionInfo (registry object)
		 */
		Element eVersion = xmlDoc.createElementNS(SM_NS, SM_PRE + ":" + VERSION_INFO_TAG);
		eVersion.appendChild(xmlDoc.createTextNode(ro.getVersionName()));

		elem.appendChild(eVersion);
		
		/* 
		 * rim.classification (registry object)
		 */
		Element eClasList = xmlDoc.createElementNS(SM_NS, SM_PRE + ":" + CLASSIFICATION_LIST_TAG);

		Collection<?> classifications = ro.getClassifications();
		if (classifications.size() > 0) {
			
			Iterator<?> iter = classifications.iterator();
			while (iter.hasNext()) {

				ClassificationImpl clas = (ClassificationImpl) iter.next();
				ConceptImpl concept = (ConceptImpl) clas.getConcept();
				
				if (concept != null) {

					Element eClas = xmlDoc.createElementNS(SM_NS, SM_PRE + ":" + CLASSIFICATION_TAG);
					eClas.setAttribute("classificationNode", concept.getId());
					
					eClasList.appendChild(eClas);
					
				}
				
			}
			
		}
		
		elem.appendChild(eClasList);
		
		/* 
		 * Object type specific corrections
		 */
    	if (otype.startsWith(ASSOCIATION)) {    		

    		AssociationImpl as = (AssociationImpl)ro;
    		
    		String source = as.getSourceObject().getKey().getId();
    		String target = as.getTargetObject().getKey().getId();

			// associations have a default association type assigned; semantic description
			// is done by using the classification mechanism

			/* 
			 * rim.associationType (association)		
			 */
    		String associationType = as.getAssociationType().getKey().getId();
			elem.setAttribute("associationType", associationType);
			
			/* 
			 * rim.sourceObject (association)
			 */
			elem.setAttribute("sourceObject", source);
			
			/* 
			 * rim.targetObject (association)
			 */
			elem.setAttribute("targetObject", target);

			
		} else if (otype.equals(EXTERNAL_LINK)) {
			/* 
			 * rim.externalURI (external link)
			 */
    		String uri = ((ExternalLinkImpl)ro).getExternalURI();    		
			elem.setAttribute("externalURI", uri);

			
		} else if (otype.equals(EXTRINSIC_OBJECT)) {
			/* 
			 * rim.mimeType (extrinsic object)
			 */
    		String mimetype = ((ExtrinsicObjectImpl)ro).getMimeType();    		
			elem.setAttribute("mimeType", mimetype);
			
			
		} else if (otype.equals(ORGANIZATION)) {
			// actually there is nothing to add

		} else if (otype.equals(USER)) {
			// actually there is nothing to add
		}
		

	}

	/**
	 * A helper method to convert a RegistryObject into a
	 * W3C Dom Element
	 *  
	 * @param ro
	 * @param xmlDoc
	 * @return
	 * @throws Exception
	 */
	public Element toElement(RegistryObjectImpl ro, Document xmlDoc) throws Exception {

		String type = getObjectType(ro);
		Element eRegistryObject = createElementNS(xmlDoc, type);

		/*
		 * Fill element from RegistryObject
		 */
		fillRegistryObject(ro, xmlDoc, eRegistryObject);

		/*
		 * Process members
		 */
		if (type.startsWith(REGISTRY_PACKAGE)) {

			List<RegistryObjectImpl> members = getPackageMembers((RegistryPackageImpl)ro);		
			if (members.size() > 0) {				

				Element eMembers = xmlDoc.createElementNS(SM_NS, SM_PRE + ":" + REGISTRY_OBJECT_LIST_TAG);  
				eRegistryObject.appendChild(eMembers);

				for (RegistryObjectImpl member:members) {
					eMembers.appendChild(toElement(member, xmlDoc));			
				}				
			}

		}

		return eRegistryObject;
		
	}
	
	/**
	 * A helper method to create a certain Xml element from
	 * the object type of a RegistryObject
	 * 
	 * @param xmlDoc
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private Element createElementNS(Document xmlDoc, String type) throws Exception {

		Element elem = null;
		if (type.equals(ASSOCIATION))
			elem = xmlDoc.createElementNS(SM_NS,  SM_PRE + ":" + ASSOCIATION_TAG); 
						
		else if (type.equals(EXTERNAL_LINK))
			elem = xmlDoc.createElementNS(SM_NS,  SM_PRE + ":" + EXTERNAL_LINK_TAG); 
				
		else if (type.equals(EXTRINSIC_OBJECT))
			elem = xmlDoc.createElementNS(SM_NS,  SM_PRE + ":" + EXTRINSIC_OBJECT_TAG); 

		else if (type.equals(ORGANIZATION))
			elem = xmlDoc.createElementNS(SM_NS,  SM_PRE + ":" + ORGANIZATION_TAG); 

		else if (type.equals(REGISTRY_OBJECT))
			elem = xmlDoc.createElementNS(SM_NS,  SM_PRE + ":" + REGISTRY_OBJECT_TAG); 
					
		else if (type.startsWith(REGISTRY_PACKAGE))
			elem = xmlDoc.createElementNS(SM_NS,  SM_PRE + ":" + REGISTRY_PACKAGE_TAG); 

		else if (type.startsWith(SERVICE))
			elem = xmlDoc.createElementNS(SM_NS,  SM_PRE + ":" + SERVICE_TAG); 
		
		else if (type.startsWith(USER))
			elem = xmlDoc.createElementNS(SM_NS,  SM_PRE + ":" + USER_TAG); 

		return elem;
		
	}

	/**
	 * A helper method to retrieve the W3C document
	 * 
	 * @throws ParserConfigurationException
	 */
	private Document createXmlDoc() throws ParserConfigurationException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);

		return factory.newDocumentBuilder().newDocument();

	}

	/**
	 * A helper method to serialize a W3C document
	 * 
	 * @param xmlDoc
	 * @return
	 * @throws Exception
	 */
	private String toXml(Document xmlDoc) throws Exception {
        	
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
            
        Properties outFormat = new Properties();
       
        outFormat.setProperty( OutputKeys.INDENT, "no" );
        
        outFormat.setProperty( OutputKeys.METHOD, "xml" );
        outFormat.setProperty( OutputKeys.OMIT_XML_DECLARATION, "no" );
        
        outFormat.setProperty( OutputKeys.VERSION, "1.0" );
        outFormat.setProperty( OutputKeys.ENCODING, "UTF-8" );
        
        transformer.setOutputProperties( outFormat );

        DOMSource domSource = new DOMSource(xmlDoc.getDocumentElement());
        OutputStream output = new StringOutputStream();
        
        StreamResult result = new StreamResult( output );
        transformer.transform( domSource, result );

        return output.toString();
       
	}
	
	/**
	 * Rules are specification objects of a certain reasoner (service)
	 * 
	 * @param service
	 * @return
	 * @throws Exception
	 */
	private List<RegistryPackageImpl> getRules(ServiceImpl service) throws Exception {
		
		ArrayList<RegistryPackageImpl> rules = new ArrayList<RegistryPackageImpl>();
		
		Collection<?> bindings = service.getServiceBindings();
		if ((bindings == null) || (bindings.size() == 0)) return null;
		
		/* 
		 * first take binding of the respective service into account
		 */
		ServiceBindingImpl binding = (ServiceBindingImpl) bindings.toArray()[0];

		/* 
		 * Next the specification links of the respective binding are determined
		 */
		Collection<?> specs = binding.getSpecificationLinks();
		if ((specs == null) || (specs.size() == 0)) return null;
		
		Iterator<?> iterator = specs.iterator();
		while (iterator.hasNext()) {
			
			SpecificationLinkImpl spec = (SpecificationLinkImpl) iterator.next();
			RegistryPackageImpl rule   = (RegistryPackageImpl)spec.getSpecificationObject();

			rules.add(rule);
		}
		
		return rules;

	}

}
