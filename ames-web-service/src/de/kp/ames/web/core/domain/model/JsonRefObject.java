package de.kp.ames.web.core.domain.model;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.domain.model
 *  Module: JsonRefObject
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #domain #json #model #object #ref #web
 * </SemanticAssist>
 *
 */


import java.util.Locale;

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.json.JSONException;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.shared.constants.JaxrConstants;

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

public class JsonRefObject extends JsonRegistryObject {

	/**
	 * Constuctor
	 * 
	 * @param jaxrHandle
	 */
	public JsonRefObject(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

    /* (non-Javadoc)
     * @see de.kp.ames.web.core.domain.JsonRegistryObject#set(org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl, java.util.Locale)
     */
    public void set(RegistryObjectImpl ro, Locale locale) throws JSONException, JAXRException {   
   	
		/*
		 * Convert identifier
		 */
    	put(JaxrConstants.RIM_ID, ro.getId());
		
		/* 
		 * Convert name
		 */
		String name = jaxrBase.getName(ro);
    	/*
    	 * If no matching locale string exists, get the closest match
    	 */
    	name = (name == "") ? ro.getDisplayName() : name;
    	put(JaxrConstants.RIM_NAME, name);

		/*
		 * Slots
		 */
		JSONObject jSlot = getSlots(ro);
		put(JaxrConstants.RIM_SLOT, jSlot.toString());

    }

}
