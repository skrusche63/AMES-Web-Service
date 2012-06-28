package de.kp.ames.web.core.format.kml;
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
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.registry.JAXRException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.freebxml.omar.client.xml.registry.infomodel.SlotImpl;
import org.freebxml.omar.common.CanonicalSchemes;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.kp.ames.web.core.format.StringOutputStream;
import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;

/**
 * This class is part of the KML layer on top of
 * the JAXR layer
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */
public class KmlObject {
	
	private static String KML_NS  = "http://www.opengis.net/kml/2.2";
	private static String KML_PRE = "kml";
	
	/*
	 * Supported KML tags
	 */
	private static String COORDINATES_TAG   = "coordinates";
	private static String DATA_TAG          = "Data";
	private static String DESCRIPTION_TAG   = "description";
	private static String EXTENDED_DATA_TAG = "ExtendedData";
	private static String HREF_TAG          = "href";
	private static String ICON_TAG          = "Icon";
	private static String ICON_STYLE_TAG    = "IconStyle";
	private static String ID_TAG            = "id";
	private static String KML_TAG           = "kml";
	private static String NAME_TAG          = "name";
	private static String PLACEMARK_TAG     = "Placemark";
	private static String POINT_TAG         = "Point";
	private static String SCALE_TAG         = "scale";
	private static String STYLE_TAG         = "Style";
	private static String STYLE_URL_TAG     = "styleUrl";
	private static String TRACK_TAG         = "track";
	private static String TYPE_TAG          = "type";
	private static String VALUE_TAG         = "value";
	
	private static String SCALE = "scale=";
	
	private ArrayList<String> placemarks = new ArrayList<String>();

	private static String ASSOCIATION      = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_Association;
	private static String REGISTRY_PACKAGE = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_RegistryPackage;

	/*
	 * Reference to the KML Document
	 */
	private Document kmlDoc;

	/*
	 * Reference to KML element
	 */
	Element kmlElem;
	
	/*
	 * Reference to caller's JaxrHandle
	 */
	@SuppressWarnings("unused")
	private JaxrHandle jaxrHandle;
	
	/*
	 * Reference to Jaxr DQM Functionality
	 */
	private JaxrDQM jaxrDQM;

	/**
	 * Constructor requires JaxrHandle
	 * 
	 * @param jaxrHandle
	 */
	public KmlObject(JaxrHandle jaxrHandle) {
		/*
		 * Register JaxrHandle of caller's user 
		 */
		this.jaxrHandle = jaxrHandle;
		
		/*
		 * Build accessor to Jaxr DQM functionality
		 */
		this.jaxrDQM = new JaxrDQM(jaxrHandle);		
	}
	
	/**
	 * Retrieve RegistryObject in KML representation
	 * 
	 * @return
	 */
	public Document get() {
		return this.kmlDoc;
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

	        DOMSource domSource = new DOMSource(kmlDoc.getDocumentElement());
            OutputStream output = new StringOutputStream();
            
            StreamResult result = new StreamResult( output );
            transformer.transform( domSource, result );

            xml = output.toString();
            
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return xml;
        
	}

	/**
	 * Convert RegistryObject into KML object
	 * 
	 * @param ro
	 * @throws Exception 
	 * @throws DOMException 
	 */
	public void set(RegistryObjectImpl ro) throws DOMException, Exception {
		
		/*
		 * Create KML Document and associated
		 * KML element
		 */
		try {

			createKmlDoc();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		if (kmlDoc == null) return;

		/*
		 * Create KML entry from Registry Object
		 */
		createKmlEntry(ro);

	}
	
	/**
	 * Create KML entry from Registry Object
	 * 
	 * @param ro
	 * @throws Exception
	 */
	private void createKmlEntry(RegistryObjectImpl ro) throws Exception {

		/* 
		 * Make sure that a certain placemark is not created twice
		 */
		if (placemarks.contains(ro.getId())) return;

		/*
		 * Associations are not supported
		 */
		String objectType = jaxrDQM.getObjectType(ro);
		if (objectType.equals(ASSOCIATION)) return;

		/*
		 * Append Style element
		 */
		Element styleElem = createStyleElem(ro);
    	kmlElem.appendChild(styleElem);
    			
		/*
		 * Append Placemark
		 */
		Element placeMarkElem = createPlaceMarkElem(ro);
		kmlElem.appendChild(placeMarkElem);

		if (objectType.equals(REGISTRY_PACKAGE)) {

			List<RegistryObjectImpl> members = jaxrDQM.getPackageMembers((RegistryPackageImpl)ro);
			for (RegistryObjectImpl member:members) {
				createKmlEntry(member);
			}
			
		}

	}
	
	/**
	 * @throws ParserConfigurationException
	 */
	private void createKmlDoc() throws ParserConfigurationException {
	    
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);

		kmlDoc = factory.newDocumentBuilder().newDocument();

		String kmlName = KML_PRE + ":" + KML_TAG;
    	kmlElem = kmlDoc.createElementNS(KML_NS, kmlName);
    	
    	kmlDoc.appendChild(kmlElem);

	}
	
	/**
	 * Convert RegistryObject content into KML placemark
	 * 
	 * @param ro
	 * @throws Exception 
	 * @throws DOMException 
	 */
	private Element createPlaceMarkElem(RegistryObjectImpl ro) throws DOMException, Exception {

		// registry object to placemark

		// we have to determine the respective geo position for the
		// registry object; in case of no coordinates present, no
		// placemark is created
		
		String lat = null;
		String lon = null;
		String alt = "0";
		
		/*
		 * Latitude & Longitude
		 */
		lat = getSlotValue(ro, JaxrConstants.SLOT_LATITUDE);
		lon = getSlotValue(ro, JaxrConstants.SLOT_LONGITUDE);
		
		/*
		 * Placemark element 
		 */
		String elemName = KML_PRE + ":" + PLACEMARK_TAG;
    	Element placeMarkElem = kmlDoc.createElementNS(KML_NS, elemName);
		
		/*
		 * Name & description are retrieved
		 */
		Element nameElem = createNameElem(ro);
		placeMarkElem.appendChild(nameElem);
		
		Element descElem = createDescriptionElem(ro);
		placeMarkElem.appendChild(descElem);
	
		/* 
		 * Create KML point element
		 */
		if ((lat != null) && (lon != null)) placeMarkElem.appendChild(createPointElem(lat, lon, alt));

		/* 
		 * Create KML style url element
		 */
		placeMarkElem.appendChild(createStyleUrlElem(ro));
		
		/* 
		 * Create extended data
		 */
		placeMarkElem.appendChild(createExtendedData(ro));
		
		/* 
		 * Register placemark
		 */
		placemarks.add(ro.getId());
		
		return placeMarkElem;
		
	}

	/**
	 * Retrieve single slot value by name in
	 * String representation
	 * 
	 * @param ro
	 * @param name
	 * @return
	 * @throws JAXRException
	 */
	private String getSlotValue(RegistryObjectImpl ro, String name) throws JAXRException {
		
		SlotImpl slot = (SlotImpl)ro.getSlot(name);
		if (slot == null) return null;

		Collection<?> values = slot.getValues();
		if (values.size() == 0) return null;
		
		return (String)values.toArray()[0];

	}
	
	/**
	 * A helper method to create a KML name element
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	private Element createNameElem(RegistryObjectImpl ro) throws JAXRException {
		
    	Element nameElem = kmlDoc.createElementNS(KML_NS, KML_PRE + ":" + NAME_TAG);

		String name = jaxrDQM.getName(ro);
		nameElem.setTextContent(name);
		
		return nameElem;

	}

	/**
	 * A helper method to create a KML description element
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	private Element createDescriptionElem(RegistryObjectImpl ro) throws JAXRException {
		
    	Element descElem = kmlDoc.createElementNS(KML_NS, KML_PRE + ":" + DESCRIPTION_TAG);

		String description = jaxrDQM.getDescription(ro);
		descElem.setTextContent(description);
		
		return descElem;

	}

	/**
	 * Create KML styleUrl element
	 * 
	 * @param ro
	 * @throws JAXRException
	 */
	private Element createStyleUrlElem(RegistryObjectImpl ro) throws JAXRException {

    	Element styleUrlElem = kmlDoc.createElementNS(KML_NS, KML_PRE + ":" + STYLE_URL_TAG);
    	
    	styleUrlElem.setTextContent("#" + ro.getId());    	
    	return styleUrlElem;
    	
	}
	
	/**
	 * Create KML point element
	 * 
	 * @param lat
	 * @param lon
	 * @param alt
	 * @return
	 * @throws Exception
	 */
	private Element createPointElem(String lat, String lon, String alt) throws Exception {

    	Element pointElem = kmlDoc.createElementNS(KML_NS, KML_PRE + ":" + POINT_TAG);
		
		// altitude is set to '0'
		Element coordinateElem =  kmlDoc.createElementNS(KML_NS, KML_PRE + ":" + COORDINATES_TAG);
		if ((lat != null) && (lon != null)) coordinateElem.setTextContent(lon+","+lat+","+alt);
		
		pointElem.appendChild(coordinateElem);		
		return pointElem;
		
	}
	
	/**
	 * Create KML Style element
	 * 
	 * @param ro
	 * @return
	 * @throws Exception
	 */
	private Element createStyleElem(RegistryObjectImpl ro) throws Exception {

		String symbol = null;
		boolean scaled = false;
		
		symbol = getSlotValue(ro, JaxrConstants.SLOT_SYMBOL);

		/* 
		 * Check whether the respective symbol is from a scaling symbolizer
		 */
		if ((symbol != null) && symbol.contains(SCALE)) {
			scaled = true;			
		}
		
		/* 
		 * Create Style element
		 */
		Element styleElem =  kmlDoc.createElementNS(KML_NS, KML_PRE + ":" + STYLE_TAG);
		styleElem.setAttribute(ID_TAG, ro.getId());
		
		Element iconStyleElem = kmlDoc.createElementNS(KML_NS, KML_PRE + ":" + ICON_STYLE_TAG);	
		
		Element scaleElem     = kmlDoc.createElementNS(KML_NS, KML_PRE + ":" + SCALE_TAG);
		scaleElem.setTextContent("1.5");
		
		Element iconElem = kmlDoc.createElementNS(KML_NS, KML_PRE + ":" + ICON_TAG);

		Element hrefElem = kmlDoc.createElementNS(KML_NS, KML_PRE + ":" + HREF_TAG);
		if (symbol != null) hrefElem.setTextContent(symbol);

		iconElem.appendChild(hrefElem);
		if (scaled) iconStyleElem.appendChild(scaleElem);

		iconStyleElem.appendChild(iconElem);		
		styleElem.appendChild(iconStyleElem);
		
		return styleElem;
		
	}

	/**
	 * Create KML Extended Data element
	 * 
	 * @param ro
	 * @return
	 * @throws Exception
	 */
	private Element createExtendedData(RegistryObjectImpl ro) throws Exception {

		Element extendedDataElem = kmlDoc.createElementNS(KML_NS, KML_PRE + ":" + EXTENDED_DATA_TAG);

		/* 
		 * id
		 */
		extendedDataElem.appendChild(createDataElem(ID_TAG, ro.getId()));

		/* 
		 * type 
		 */
		extendedDataElem.appendChild(createDataElem(TYPE_TAG, jaxrDQM.getObjectType(ro)));

		/* 
		 * track
		 */
		String track = getSlotValue(ro, JaxrConstants.SLOT_TRACK);
		if (track != null) {
			extendedDataElem.appendChild(createDataElem(TRACK_TAG, track));

		}
		
		return extendedDataElem;
		
	}
	
	/**
	 * Create KML Data element
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	private Element createDataElem(String name, String value) {

		Element dataElem = kmlDoc.createElementNS(KML_NS, KML_PRE + ":" + DATA_TAG);
		dataElem.setAttribute(NAME_TAG, name);
		
		Element valueElem = kmlDoc.createElementNS(KML_NS, KML_PRE + ":" + VALUE_TAG);
		valueElem.setTextContent(value);
		
		dataElem.appendChild(valueElem);
		
		return dataElem;

	}

}
