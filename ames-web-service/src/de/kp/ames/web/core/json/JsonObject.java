package de.kp.ames.web.core.json;
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

import java.util.Locale;

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.json.JSONException;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrBase;
import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;

/**
 * This class is part of the JSON layer on top of
 * the JAXR layer
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */
public class JsonObject extends JSONObject {
	
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
	public JsonObject(JaxrHandle jaxrHandle) {
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
     * Get JSON representation from RegistryObject
     * 
     * @return
     */
    public JSONObject get() {   
    	return (JSONObject)this;
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
		put(JaxrConstants.RIM_NAME, name);
		
		/*
		 * Convert description
		 */
		String description = jaxrBase.getDescription(ro, locale);
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

		
		// - VERSION					
		String version = ro.getVersionName();
		put(JaxrConstants.RIM_VERSION, version);

    }
     
}

