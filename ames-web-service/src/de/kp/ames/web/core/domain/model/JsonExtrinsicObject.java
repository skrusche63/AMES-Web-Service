package de.kp.ames.web.core.domain.model;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.domain.model
 *  Module: JsonExtrinsicObject
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #domain #extrinsic #json #model #object #web
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

import java.util.Locale;

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.json.JSONException;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.shared.constants.JaxrConstants;

public class JsonExtrinsicObject extends JsonRegistryObject {

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public JsonExtrinsicObject(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
     * Convert extrinsic object into JSON object
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
    	 * Convert registry object
    	 */
    	super.set(ro, locale);
    	
    	/*
    	 * Convert extrinsic object specific information
    	 */
    	ExtrinsicObjectImpl eo = (ExtrinsicObjectImpl)ro;
    	
    	/*
    	 * Mimetype
    	 */
		String mimeType = eo.getMimeType();    		
		put(JaxrConstants.RIM_MIME, mimeType);
   	
    }

}
