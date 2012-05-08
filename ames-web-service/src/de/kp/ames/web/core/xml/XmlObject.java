package de.kp.ames.web.core.xml;
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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.common.BindingUtility;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectListType;
import org.w3c.dom.Document;

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
	 */
	public void set(RegistryObjectImpl ro) throws JAXBException {
		
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
	 */
	private JAXBElement<RegistryObjectListType> createRegistryObjectList(RegistryObjectImpl ro) {
		// TODO
		return null;
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
