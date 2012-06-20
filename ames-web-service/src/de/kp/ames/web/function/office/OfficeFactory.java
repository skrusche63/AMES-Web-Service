package de.kp.ames.web.function.office;
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

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import de.kp.ames.web.core.util.FileUtil;

public class OfficeFactory {

	/* 
	 * Namespace uris to determine a certain ODF Builder
	 * (note, that these uris MUST match a certain XSL)
	 */
	private static String ODP_NS = "urn:de:kp:xsd:odp:1.0";
	private static String ODS_NS = "urn:de:kp:xsd:ods:1.0";
	private static String ODT_NS = "urn:de:kp:xsd:odt:1.0";

	private FileUtil file;
	
	/**
	 * Constructor
	 * 
	 * @param file
	 */
	public OfficeFactory(FileUtil file) {
		this.file = file;
	}
	
	/**
	 * Retrieve registered OfficeBuilder for a certain product
	 * 
	 * @return
	 * @throws Exception
	 */
	public OfficeBuilder getOfficeBuilder() throws Exception {
		
		OfficeBuilder builder = null;
		
		/*
		 * Get namespaces & retrieve office builder
		 */
		ArrayList<String> namespaces = getXMLNamespaces();
		for (String namespace:namespaces) {
			
			if (namespace.equals(ODP_NS)) {
				
				builder = new OdpBuilder(file);
				break;
				
			} else if (namespace.equals(ODS_NS)) {
				
				builder = new OdsBuilder(file);
				break;
				
			} else if (namespace.equals(ODT_NS)) {
				
				builder = new OdtBuilder(file);
				break;
				
			} else {
				throw new Exception("[OfficeFactory] Namespace <" + namespace + "> is not supported.");
			}
			
		}
		
		return builder;
		
	}
	
	public OfficeConverter getOfficeConverter() {
		
		OfficeConverter converter = new OfficeConverterImpl(file);
		return converter;
		
	}

	/**
	 * A helper method to retrieve assigned namespaces
	 * for a certain XML file
	 * 
	 * @return
	 */
	private ArrayList<String> getXMLNamespaces() {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);

		ArrayList<String> namespaces = new ArrayList<String>();
		
		try {	    
			/*
			 * Retrieve root element
			 */
			Document xmlDoc = factory.newDocumentBuilder().parse(file.getInputStream());
			Element rootElement = xmlDoc.getDocumentElement();
			
			/*
			 * Retrieve namespaces
			 */
			namespaces = getElementNS(rootElement);
			
			
		} catch (Exception e) {
			// do nothing
		}

		return namespaces;
		
	}

	/**
	 * A helper method to retrieved declared
	 * namespaces for a certain element
	 * 
	 * @param elem
	 * @return
	 */
	private ArrayList<String> getElementNS(Element elem) {
		
		ArrayList<String> namespaces = new ArrayList<String>();
		NamedNodeMap attributes = elem.getAttributes();
		
		int card = attributes.getLength();
		for (int i=0; i < card; i++) {
			
			Node attribute = attributes.item(i);
			String name = attribute.getNodeName();
			
			if (name.startsWith("xmlns")) {
				
				String namespace = attribute.getNodeValue();
				namespaces.add(namespace);
				
			}
		}
		
		return namespaces;
		
	}

}
