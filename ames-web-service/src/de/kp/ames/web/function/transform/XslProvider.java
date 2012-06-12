package de.kp.ames.web.function.transform;
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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.activation.DataHandler;
import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceBindingImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceImpl;
import org.freebxml.omar.client.xml.registry.infomodel.SpecificationLinkImpl;
import org.freebxml.omar.common.CanonicalSchemes;

import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.format.xml.XmlObject;
import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.util.FileUtil;

public class XslProvider extends JaxrDQM {

	private static String EXTRINSIC_OBJECT = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_ExtrinsicObject;
	private static String SERVICE_OBJECT   = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_Service;

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public XslProvider(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}
	
	/**
	 * This is a public method to retrieve a single DTD from the
	 * respective extrinsic object; a DTD document may be referenced
	 * by a certain XSL stylesheet, e.g. to provide a set of global
	 * parameters.
	 * 
	 * @param uid
	 * @return
	 */
	public FileUtil getDtd(String uid) {

		FileUtil dtd = null;
				
		try {
			/* 
			 * Determine registry object from unique identifier
			 */
			RegistryObjectImpl ro = getRegistryObjectById(uid);
			if (ro == null) return dtd;

			String objectType = ro.getObjectType().getKey().getId();	    	
	    	if (objectType.startsWith(EXTRINSIC_OBJECT) == false) return dtd;

    		ExtrinsicObjectImpl eo = (ExtrinsicObjectImpl)ro;

			/* 
			 * Determine XSL transformation as input stream		
			 */
			DataHandler handler = eo.getRepositoryItem();
			if (handler == null) return dtd;
			
			dtd = new FileUtil();
			
			dtd.setIdentifier(uid);
			dtd.setInputStream(handler.getInputStream(), eo.getMimeType());
			
			return dtd;

		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {}
		
		return null;
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
	 * This is a public method to retrieve a single stylesheet from the
	 * respective extrinsic object
	 * 
	 * @param uid
	 * @return
	 */
	public FileUtil getStylesheet(String uid) {

		FileUtil stylesheet = null;

		try {
			
			/* 
			 * Determine extrinsic object from unique identifier
			 */
			RegistryObjectImpl ro = getRegistryObjectById(uid);
			if (ro == null) return stylesheet;

	    	String objectType = ro.getObjectType().getKey().getId();	    	
	    	if (objectType.startsWith(EXTRINSIC_OBJECT) == false) return stylesheet;

    		ExtrinsicObjectImpl eo = (ExtrinsicObjectImpl)ro;

			/* 
			 * Determine XSL transformation as input stream		
			 */
			DataHandler handler = eo.getRepositoryItem();
			if (handler == null) return null;
			
			stylesheet = new FileUtil();
			
			stylesheet.setIdentifier(uid);
			stylesheet.setInputStream(handler.getInputStream(), eo.getMimeType());
			
			return stylesheet;

		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {}
		
		return null;
		
	}
	
	/**
	 * Retrieve ordered list of xsl files (stylesheets)
	 * 
	 * @param uid
	 * @return
	 */
	public Collection<FileUtil> getStylesheets(String uid) {

		Map<Integer, FileUtil> collector = new TreeMap<Integer, FileUtil>(new Comparator<Integer>(){
			public int compare(Integer seqno1, Integer seqno2) {
				return seqno1.compareTo(seqno2);
			}
		});
		
		/*
		 * Stylesheets are profiled as ordered specification objects;
		 * this implies, that a certain transformation is represented
		 * by an OASIS ebRIM Service Object
		 */
		try {
			
			/* 
			 * Determine service object from unique identifier
			 */
			RegistryObjectImpl ro = getRegistryObjectById(uid);
			if (ro == null) return null;

	    	String objectType = ro.getObjectType().getKey().getId();	    	
	    	if (objectType.startsWith(SERVICE_OBJECT) == false) return null;

    		ServiceImpl service = (ServiceImpl)ro;

			Collection<?> bindings = service.getServiceBindings();
			if ((bindings == null) || (bindings.size() == 0)) return null;
			
			/* 
			 * Take the first binding of the respective service into account
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
				
				Object[] values = spec.getSlot(JaxrConstants.SLOT_SEQNO).getValues().toArray();
				int rimSeqNo = Integer.parseInt((String)values[0]);

				ExtrinsicObjectImpl eo = (ExtrinsicObjectImpl)spec.getSpecificationObject();
	
				/* 
				 * Determine XSL transformation as input stream		
				 */
				DataHandler handler = eo.getRepositoryItem();
				if (handler != null) {

					FileUtil file = new FileUtil(handler.getInputStream(), eo.getMimeType());					
					file.setIdentifier(eo.getId());
					
					collector.put(rimSeqNo, file);
				}
				
			}
			
			return collector.values();

		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {}
		
		return null;
	}
	
 }
