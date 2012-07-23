package de.kp.ames.web.core.domain.model;
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

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.AuditableEventImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ConceptImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.shared.constants.JaxrConstants;

/**
 * This class is part of the JSON layer on top of
 * the JAXR layer
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */
public class JsonRegistryObject extends JsonRegistryEntry implements IJsonRegistryObject {
	
	/**
	 * Constructor requires JaxrHandle
	 * 
	 * @param jaxrHandle
	 */
	public JsonRegistryObject(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
     * Convert registry object into JSON object
     * 
	 * @param ro
	 * @throws JSONException
	 * @throws JAXRException
	 */
	public void set(RegistryObjectImpl ro) throws JSONException, JAXRException {
		set(ro, jaxrBase.defaultLocale);
	}
	
    /**
     * Convert registry object into JSON object
     * 
     * @param ro
     * @param locale
     * @return
     * @throws JAXRException 
     * @throws JSONException 
     * @throws Exception
     */
    public void set(RegistryObjectImpl ro, Locale locale) throws JSONException, JAXRException {    	
  	
		/*
		 * Convert identifier    	
		 */
    	put(JaxrConstants.RIM_ID, ro.getId());
    	put(JaxrConstants.RIM_LID, ro.getLid());
		
		/* 
		 * Convert name
		 */
    	String name = jaxrBase.getName(ro, locale);
    	name = (name == null) ? "" : name;
		
    	put(JaxrConstants.RIM_NAME, name);
		
		/*
		 * Convert description
		 */
		String description = jaxrBase.getDescription(ro, locale);
		description = (description == null) ? "" : description;
		
		put(JaxrConstants.RIM_DESC, description);

		/*
		 * Convert object type
		 */
    	String objectType = ro.getObjectType().getKey().getId();
    	put(JaxrConstants.RIM_TYPE, objectType);
    	   	    	
		/*
		 * Convert home
		 */
    	String home = jaxrBase.getHome(ro);
		put(JaxrConstants.RIM_HOME, home);
		
		/*
		 * Convert status
		 */
		String status = jaxrBase.getStatus(ro);
		put(JaxrConstants.RIM_STATUS, status);
		
		/* 
		 * Convert version					
		 */
		String version = ro.getVersionName();
		put(JaxrConstants.RIM_VERSION, version);

		/*
		 * Convert author
		 */
		String author = jaxrBase.getAuthor(ro);
		put(JaxrConstants.RIM_AUTHOR, author);
		
		/*
		 * Convert owner
		 */
		String owner = jaxrBase.getOwner(ro);
		put(JaxrConstants.RIM_OWNER, owner);
		
		/*
		 * Convert timestamp & events
		 */
		AuditableEventImpl auditableEvent = jaxrBase.getLastEvent(ro);
		
		Timestamp lastModified = (auditableEvent==null) ? null : auditableEvent.getTimestamp();
		if (lastModified != null) {

			/*
			 * Timestamp
			 */
			long milliseconds = lastModified.getTime() + (lastModified.getNanos() / 1000000);
			put(JaxrConstants.RIM_TIMESTAMP, new Date(milliseconds).toString());			
			
			/*
			 * Event
			 */
			String eventType = jaxrBase.getLastEventType(ro);
			put(JaxrConstants.RIM_EVENT, eventType);

		}
				
		/*
		 * Classifications
		 */
		JSONArray jClases = getClassifications(ro);
		put(JaxrConstants.RIM_CLAS, jClases.toString());

		/*
		 * Slots
		 */
		JSONObject jSlot = getSlots(ro);
		put(JaxrConstants.RIM_SLOT, jSlot.toString());

    }
    
    /**
     * A helper method to retrieve the classifications 
     * of a certain registry object as a JSONArray
     * 
     * @param ro
     * @return
     * @throws JAXRException
     * @throws JSONException
     */
    private JSONArray getClassifications(RegistryObjectImpl ro) throws JAXRException, JSONException {

		JSONArray jArray = new JSONArray();
		
		Collection<?> classifications = ro.getClassifications();
		Iterator<?> iter = classifications.iterator();

		while (iter.hasNext()) {

			ClassificationImpl clas = (ClassificationImpl) iter.next();
			ConceptImpl cpt = (ConceptImpl) clas.getConcept();
				
			if (cpt != null) jArray.put(cpt.getId());

		}

		return jArray;
		
	}

}

