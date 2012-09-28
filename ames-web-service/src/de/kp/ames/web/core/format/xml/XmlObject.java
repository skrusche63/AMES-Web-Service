package de.kp.ames.web.core.format.xml;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.format.xml
 *  Module: XmlObject
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #format #object #web #xml
 * </SemanticAssist>
 *
 */

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
import java.util.Properties;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.PersonName;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.User;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.freebxml.omar.client.xml.registry.infomodel.AssociationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.EmailAddressImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExternalLinkImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.InternationalStringImpl;
import org.freebxml.omar.client.xml.registry.infomodel.LocalizedStringImpl;
import org.freebxml.omar.client.xml.registry.infomodel.OrganizationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.PostalAddressImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.freebxml.omar.client.xml.registry.infomodel.TelephoneNumberImpl;
import org.freebxml.omar.client.xml.registry.infomodel.UserImpl;
import org.freebxml.omar.common.BindingUtility;
import org.freebxml.omar.common.CanonicalSchemes;
import org.oasis.ebxml.registry.bindings.rim.AssociationType1;
import org.oasis.ebxml.registry.bindings.rim.ClassificationType;
import org.oasis.ebxml.registry.bindings.rim.EmailAddressType;
import org.oasis.ebxml.registry.bindings.rim.ExternalLinkType;
import org.oasis.ebxml.registry.bindings.rim.ExtrinsicObjectType;
import org.oasis.ebxml.registry.bindings.rim.InternationalStringType;
import org.oasis.ebxml.registry.bindings.rim.LocalizedStringType;
import org.oasis.ebxml.registry.bindings.rim.ObjectFactory;
import org.oasis.ebxml.registry.bindings.rim.OrganizationType;
import org.oasis.ebxml.registry.bindings.rim.PersonNameType;
import org.oasis.ebxml.registry.bindings.rim.PostalAddressType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectListType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;
import org.oasis.ebxml.registry.bindings.rim.RegistryPackageType;
import org.oasis.ebxml.registry.bindings.rim.TelephoneNumberType;
import org.oasis.ebxml.registry.bindings.rim.UserType;
import org.oasis.ebxml.registry.bindings.rim.VersionInfoType;
import org.w3c.dom.Document;

import de.kp.ames.web.core.format.StringOutputStream;
import de.kp.ames.web.core.regrep.JaxrBase;
import de.kp.ames.web.core.regrep.JaxrHandle;

/**
 * This class is part of the XML layer on top of
 * the JAXR layer
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */

public class XmlObject {

	/*
	 * Reference to the XML Document
	 */
	private Document xmlDoc;

	/*
	 * Reference to caller's JaxrHandle
	 */
	@SuppressWarnings("unused")
	private JaxrHandle jaxrHandle;

	/*
	 * Reference to Jaxr Base Functionality
	 */
	private JaxrBase jaxrBase;

	/*
	 * Reference to RIM Factory
	 */
	ObjectFactory rimFactory;

	/*
	 * List of supported registry objects
	 */
	private static String ASSOCIATION      = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_Association;
	private static String EXTERNAL_LINK    = CanonicalSchemes.CANONICAL_OBJECT_TYPE_CODE_ExternalLink;
	private static String EXTRINSIC_OBJECT = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_ExtrinsicObject;
	private static String ORGANIZATION     = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_Organization;
	private static String REGISTRY_OBJECT  = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_RegistryObject;
	private static String REGISTRY_PACKAGE = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_RegistryPackage;
	private static String USER             = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_User;
	
	/**
	 * Constructor requires JaxrHandle
	 * 
	 * @param jaxrHandle
	 */
	public XmlObject(JaxrHandle jaxrHandle) {
		/*
		 * Register JaxrHandle of caller's user 
		 */
		this.jaxrHandle = jaxrHandle;
		
		/*
		 * Build accessor to Jaxr base functionality
		 */
		this.jaxrBase = new JaxrBase(jaxrHandle);		
		
		/*
		 * Build accessor to ebRIM Factory
		 */
		this.rimFactory = BindingUtility.getInstance().rimFac;

	}

	/**
	 * @return
	 */
	public String toXml()  {
		
        String xml = null;
        try {
        	
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

            xml = output.toString();
            
            System.out.println("====> XmlObject.toXml: " + xml);
            
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return xml;
        
	}

	/**
	 * Retrieve RegistryObject in XML representation
	 * 
	 * @return
	 */
	public Document get() {
		return this.xmlDoc;
	}
		
	/**
	 * Convert RegistryObject into XML object 
	 * @param ro
	 * @throws JAXBException
	 * @throws JAXRException 
	 */
	public void set(RegistryObjectImpl ro) throws JAXBException, JAXRException {
		
		/*
		 * Create XML Document
		 */
		try {
			createXmlDoc();
		
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		if (xmlDoc == null) return;
		
		Marshaller marshaller = BindingUtility.getInstance().getJAXBContext().createMarshaller();
		marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		/*
		 * A registry object is always returned as the first
		 * element of a list of registry objects
		 */
		JAXBElement<RegistryObjectListType> registryObjectList = createRegistryObjectList(ro);
		marshaller.marshal(registryObjectList, xmlDoc);
		
	}
	
	/**
	 * A helper method to create a JAXB element from a
	 * certain registry object
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException 
	 */
	private JAXBElement<RegistryObjectListType> createRegistryObjectList(RegistryObjectImpl ro) throws JAXRException {
		
		RegistryObjectListType listType = createRegistryObjectListType(ro);
		return rimFactory.createRegistryObjectList(listType);

	}
	
	/**
	 * A helper method to add a JAXB representation of
	 * a RegistryObject to the RegistryObjectListType
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException 
	 */
	private RegistryObjectListType createRegistryObjectListType(RegistryObjectImpl ro) throws JAXRException {

		RegistryObjectListType listType = rimFactory.createRegistryObjectListType();
		listType.getIdentifiable().add(createRegistryObject(ro));
		
		return listType;
	
	}
	
	/**
	 * A helper method to create a JAXB representation of
	 * a Registry Object
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException 
	 */
	private JAXBElement<RegistryObjectType> createRegistryObject(RegistryObjectImpl ro) throws JAXRException {
		
		RegistryObjectType registryObjectType = createRegistryObjectType(ro);
		return rimFactory.createRegistryObject(registryObjectType);

	}
	
	/**
	 * A helper method to convert a registry object into
	 * its XML binding object
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	private RegistryObjectType createRegistryObjectType(RegistryObjectImpl ro) throws JAXRException {

		RegistryObjectType registryObjectType = null;
		
		String objectType = jaxrBase.getObjectType(ro);
		if (objectType.equals(ASSOCIATION)) {
			/*
			 * Association Binding
			 */
			registryObjectType = createAssociationType((AssociationImpl)ro);

		} else if (objectType.equals(EXTERNAL_LINK)) {
			/*
			 * ExternalLink Binding
			 */
			registryObjectType = createExternalLinkType((ExternalLinkImpl)ro);
			
		} else if (objectType.equals(EXTRINSIC_OBJECT)) {
			/*
			 * ExtrinsicObject Binding
			 */
			registryObjectType = createExtrinsicObjectType((ExtrinsicObjectImpl)ro);
			
		} else if (objectType.equals(ORGANIZATION)) {
			/*
			 * Organization Binding
			 */
			registryObjectType = createOrganizationType((OrganizationImpl)ro);
			
		} else if (objectType.equals(REGISTRY_OBJECT)) {	
			/*
			 * RegistryObject Binding
			 */
			registryObjectType = rimFactory.createRegistryObjectType();

		} else if (objectType.equals(REGISTRY_PACKAGE)) {	
			/*
			 * RegistryPackage Binding
			 */
			registryObjectType = createRegistryPackageType((RegistryPackageImpl)ro);

		} else if (objectType.equals(USER)) {	
			/*
			 * User Binding
			 */
			registryObjectType = createUserType((UserImpl)ro);
			
		} else {
			/*
			 * All other registry objects
			 */
			registryObjectType = rimFactory.createRegistryObjectType();

		}
		
		/*
		 * Identifier
		 */		
		registryObjectType.setId(ro.getId());
		registryObjectType.setLid(ro.getLid());
		
		/*
		 * Home
		 */
		registryObjectType.setHome(jaxrBase.getHome(ro));
		
		/*
		 * Name
		 */
		registryObjectType.setName(createInternationalStringType((InternationalStringImpl)ro.getName()));
		
		/*
		 * Description
		 */
		registryObjectType.setDescription(createInternationalStringType((InternationalStringImpl)ro.getDescription()));
		
		/*
		 * ObjectType
		 */
		registryObjectType.setObjectType(objectType);
		
		/*
		 * Status
		 */
		registryObjectType.setStatus(jaxrBase.getStatus(ro));
		
		/*
		 * VersionInfo
		 */
		registryObjectType.setVersionInfo(createVersionInfoType(ro));
		
		/*
		 * Classification
		 */
		Collection<?> cs = ro.getClassifications();
		Iterator<?> iter = cs.iterator();
		
		while (iter.hasNext()) {
			ClassificationImpl c = (ClassificationImpl)iter.next();
			registryObjectType.getClassification().add(createClassificationType(c));
		}
		
		return registryObjectType;
		
	}
	
	/**
	 * A helper method to convert an international string
	 * 
	 * @param internationalString
	 * @return
	 * @throws JAXRException
	 */
	private InternationalStringType createInternationalStringType(InternationalStringImpl internationalString) throws JAXRException {
		
		InternationalStringType isType = rimFactory.createInternationalStringType();
		
		Collection<?> localizedStrings = internationalString.getLocalizedStrings();
		Iterator<?> iterator = localizedStrings.iterator();
		
		while (iterator.hasNext()) {
			
			LocalizedStringType lsType = rimFactory.createLocalizedStringType();
			LocalizedStringImpl localizedString = (LocalizedStringImpl)iterator.next();
			
			/*
			 * Charset
			 */
			lsType.setCharset(localizedString.getCharsetName());
			
			/*
			 * Language
			 */
			String language = localizedString.getLocale().toString().replace('_','-');
			lsType.setLang(language);

			/*
			 * Value
			 */
			lsType.setValue(localizedString.getValue());
			
			/*
			 * Add to international string
			 */
			isType.getLocalizedString().add(lsType);

		}
		
		return isType;
		
	}
	
	/**
	 * A helper method to convert the version info
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	private VersionInfoType createVersionInfoType(RegistryObjectImpl ro) throws JAXRException {
		
		VersionInfoType versionInfoType = rimFactory.createVersionInfoType();		
		versionInfoType.setVersionName(ro.getVersionName());
		
		return versionInfoType;
		
	}
	
	/**
	 * A helper method to convert an association
	 * 
	 * @param a
	 * @return
	 * @throws JAXRException 
	 */
	private AssociationType1 createAssociationType(AssociationImpl a) throws JAXRException {
		
		AssociationType1 associationType = rimFactory.createAssociationType1();

		/*
		 * Association Type
		 */
		Concept cpt = a.getAssociationType();
		associationType.setAssociationType(cpt.getKey().getId());
		
		/*
		 * Source Object
		 */
		RegistryObject so = a.getSourceObject();
		associationType.setSourceObject(so.getKey().getId());
		
		/*
		 * Target Object
		 */
		RegistryObject to = a.getTargetObject();
		associationType.setTargetObject(to.getKey().getId());
				
		return associationType;
		
	}
	
	/**
	 * A helper method to convert a classification
	 * 
	 * @param c
	 * @return
	 * @throws JAXRException
	 */
	private ClassificationType createClassificationType(ClassificationImpl c) throws JAXRException {
		
		ClassificationType classificationType = rimFactory.createClassificationType();
		
		/*
		 * Classification Node
		 */
		Concept cpt = c.getConcept();
		if (cpt != null) classificationType.setClassificationNode(cpt.getKey().getId());
		
		/*
		 * Classification Scheme
		 */
		ClassificationScheme cs = c.getClassificationScheme();
		if (cs != null) classificationType.setClassificationScheme(cpt.getKey().getId());
		
		/*
		 * Classified Object
		 */
		RegistryObject co = c.getClassifiedObject();
		if (co != null) classificationType.setClassifiedObject(co.getKey().getId());
		
		return classificationType;
		
	}
	
	/**
	 * A helper method to convert an external link
	 * 
	 * @param el
	 * @return
	 * @throws JAXRException 
	 */
	private ExternalLinkType createExternalLinkType(ExternalLinkImpl el) throws JAXRException {
		
		ExternalLinkType externalLinkType = rimFactory.createExternalLinkType();
		
		/*
		 * External Uri
		 */
		externalLinkType.setExternalURI(el.getExternalURI());
		
		return externalLinkType;
		
	}

	/**
	 * A helper method to convert an extrinsic object
	 * 
	 * @param eo
	 * @return
	 * @throws JAXRException
	 */
	private ExtrinsicObjectType createExtrinsicObjectType(ExtrinsicObjectImpl eo) throws JAXRException {
		
		ExtrinsicObjectType extrinsicObjectType = rimFactory.createExtrinsicObjectType();
		
		/*
		 * Content Version Info
		 */
		VersionInfoType versionInfoType = rimFactory.createVersionInfoType();		
		versionInfoType.setVersionName(eo.getContentVersionName());
		
		extrinsicObjectType.setContentVersionInfo(versionInfoType);
		
		/*
		 * Opaque
		 */
		extrinsicObjectType.setIsOpaque(eo.isOpaque());
		
		/*
		 * Mimetype
		 */
		extrinsicObjectType.setMimeType(eo.getMimeType());

		return extrinsicObjectType;
		
	}
	
	/**
	 * A helper method to convert an organization
	 * 
	 * @param o
	 * @return
	 * @throws JAXRException
	 */
	private OrganizationType createOrganizationType(OrganizationImpl o) throws JAXRException {
		
		OrganizationType organizationType = rimFactory.createOrganizationType();
		
		/*
		 * Email address
		 */
		organizationType.getEmailAddress().addAll(createEmailAddresses(o.getEmailAddresses()));
		
		/*
		 * Postal address
		 */
		organizationType.getAddress().addAll(createPostalAddresses(o.getPostalAddresses()));
		
		/*
		 * Telephone number
		 */
		organizationType.getTelephoneNumber().addAll(createTelephoneNumbers(o.getTelephoneNumbers()));
		
		/*
		 * Primary contact
		 */
		User primaryContact = o.getPrimaryContact();
		if (primaryContact != null) organizationType.setPrimaryContact(primaryContact.getKey().getId());
		
		return organizationType;
		
	}

	/**
	 * A helper method to convert a registry package
	 * 
	 * @param rp
	 * @return
	 * @throws JAXRException 
	 */
	private RegistryPackageType createRegistryPackageType(RegistryPackageImpl rp) throws JAXRException {
		
		RegistryPackageType registryPackageType = rimFactory.createRegistryPackageType();
		
		/*
		 * Initialize member list
		 */
		RegistryObjectListType registryObjectListType = rimFactory.createRegistryObjectListType();
		registryPackageType.setRegistryObjectList(registryObjectListType);
		/*
		 * Registry Object List
		 */
		Set<?> members = rp.getRegistryObjects();
		Iterator<?> iter = members.iterator();
		
		while (iter.hasNext()) {
			RegistryObjectImpl member = (RegistryObjectImpl)iter.next();
			registryPackageType.getRegistryObjectList().getIdentifiable().add(createRegistryObject(member));
		}
		
		return registryPackageType;
		
	}
	
	/**
	 * A helper method to convert a user
	 * 
	 * @param u
	 * @return
	 * @throws JAXRException
	 */
	private UserType createUserType(UserImpl u) throws JAXRException {
		
		UserType userType = rimFactory.createUserType();
		
		/*
		 * Email address
		 */
		userType.getEmailAddress().addAll(createEmailAddresses(u.getEmailAddresses()));
		
		/*
		 * Postal address
		 */
		userType.getAddress().addAll(createPostalAddresses(u.getPostalAddresses()));
		
		/*
		 * Telephone number
		 */
		userType.getTelephoneNumber().addAll(createTelephoneNumbers(u.getTelephoneNumbers()));
		
		/*
		 * Person name
		 */
		userType.setPersonName(createPersonNameType(u.getPersonName()));
		
		return userType;
		
	}

	/**
	 * A helper method to convert email addresses
	 * 
	 * @param emailAddresses
	 * @return
	 * @throws JAXRException
	 */
	private Collection<EmailAddressType> createEmailAddresses(Collection<?> emailAddresses) throws JAXRException {
		
		ArrayList<EmailAddressType> emailAddressTypeList = new ArrayList<EmailAddressType>();
		
		Iterator<?> iter = emailAddresses.iterator();
		while (iter.hasNext()) {
			EmailAddressImpl emailAddress = (EmailAddressImpl)iter.next();
			EmailAddressType emailAddressType = rimFactory.createEmailAddressType();

			emailAddressType.setAddress(emailAddress.getAddress());
			emailAddressType.setType(emailAddress.getType());
			
			emailAddressTypeList.add(emailAddressType);
			
		}
		
		return emailAddressTypeList;
		
	}
	
	/**
	 * A helper method to convert postal addresses
	 * 
	 * @param postalAddresses
	 * @return
	 * @throws JAXRException
	 */
	private Collection<PostalAddressType> createPostalAddresses(Collection<?> postalAddresses) throws JAXRException {

		ArrayList<PostalAddressType> postalAddressTypeList = new ArrayList<PostalAddressType>();
		
		Iterator<?> iter = postalAddresses.iterator();
		while (iter.hasNext()) {
			PostalAddressImpl postalAddress = (PostalAddressImpl)iter.next();
			PostalAddressType postalAddressType = rimFactory.createPostalAddressType();
			
			/*
			 * City
			 */
			postalAddressType.setCity(postalAddress.getCity());
			
			/*
			 * Country
			 */
			postalAddressType.setCountry(postalAddress.getCountry());
			
			/*
			 * Postal code
			 */
			postalAddress.setPostalCode(postalAddress.getPostalCode());
			
			/*
			 * State or province
			 */
			postalAddress.setStateOrProvince(postalAddress.getStateOrProvince());
			
			/*
			 * Street
			 */
			postalAddress.setStreet(postalAddress.getStreet());
			
			/*
			 * Street number
			 */
			postalAddress.setStreetNumber(postalAddress.getStreetNumber());
			
			postalAddressTypeList.add(postalAddressType);
			
		}
		
		return postalAddressTypeList;

	}
	
	/**
	 * A helper method to create telephone numbers
	 * 
	 * @param telephoneNumbers
	 * @return
	 * @throws JAXRException
	 */
	private Collection<TelephoneNumberType> createTelephoneNumbers(Collection<?> telephoneNumbers) throws JAXRException {
		
		ArrayList<TelephoneNumberType> telephoneNumberTypeList = new ArrayList<TelephoneNumberType>();

		Iterator<?> iter = telephoneNumbers.iterator();
		while (iter.hasNext()) {
			TelephoneNumberImpl telephoneNumber = (TelephoneNumberImpl)iter.next();
			TelephoneNumberType telephoneNumberType = rimFactory.createTelephoneNumberType();
			
			/*
			 * Area code
			 */
			telephoneNumberType.setAreaCode(telephoneNumber.getAreaCode());
			
			/*
			 * Country code
			 */
			telephoneNumberType.setCountryCode(telephoneNumber.getCountryCode());
			
			/*
			 * Extension
			 */
			telephoneNumberType.setExtension(telephoneNumber.getExtension());
			
			/*
			 * Number
			 */
			telephoneNumberType.setNumber(telephoneNumber.getNumber());
			
			telephoneNumberTypeList.add(telephoneNumberType);
			
		}
		
		return telephoneNumberTypeList;
		
	}
	
	/**
	 * A helper method to convert a person name
	 * 
	 * @param personName
	 * @return
	 * @throws JAXRException
	 */
	private PersonNameType createPersonNameType(PersonName personName) throws JAXRException {
		
		PersonNameType personNameType = rimFactory.createPersonNameType();
		
		/*
		 * First name
		 */
		if (personName.getFirstName() != null) personNameType.setFirstName(personName.getFirstName());
		
		/*
		 * Middle name
		 */
		if (personName.getMiddleName() != null) personNameType.setMiddleName(personName.getMiddleName());
		
		/*
		 * Last name
		 */
		personNameType.setLastName(personName.getLastName());
		
		return personNameType;
		
	}
	
	/**
	 * A helper method to retrieve the W3C document
	 * 
	 * @throws ParserConfigurationException
	 */
	private void createXmlDoc() throws ParserConfigurationException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);

		xmlDoc = factory.newDocumentBuilder().newDocument();

	}
	
}
