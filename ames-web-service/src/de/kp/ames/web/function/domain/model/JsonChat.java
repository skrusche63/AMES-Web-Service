package de.kp.ames.web.function.domain.model;
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

import java.io.InputStream;
import java.util.Locale;

import javax.activation.DataHandler;
import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.json.JSONException;
import org.json.JSONObject;

import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.domain.model.JsonExtrinsicObject;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.shared.IconConstants;
import de.kp.ames.web.shared.JaxrConstants;

public class JsonChat extends JsonExtrinsicObject {

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public JsonChat(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}
    /* (non-Javadoc)
     * @see de.kp.ames.web.core.domain.model.JsonExtrinsicObject#set(org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl, java.util.Locale)
     */

	public void set(RegistryObjectImpl ro, Locale locale) throws JSONException, JAXRException {    
    	
    	/*
    	 * Convert extrinsic object
    	 */
    	super.set(ro, locale);
 
    	/*
    	 * Convert "subject" and "from"
    	 */
    	String from    = "";
    	String subject = "";
    	
    	try {

    		ExtrinsicObjectImpl eo = (ExtrinsicObjectImpl)ro;
    		DataHandler dataHandler = eo.getRepositoryItem();
    	
    		InputStream stream = dataHandler.getInputStream();
    		byte[] bytes = FileUtil.getByteArrayFromInputStream(stream);
    		
    		JSONObject jMail = new JSONObject(new String(bytes, GlobalConstants.UTF_8));
    		
    		from    = jMail.getString(JaxrConstants.RIM_FROM);
    		subject = jMail.getString(JaxrConstants.RIM_SUBJECT);
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		
    	}
    	
    	put(JaxrConstants.RIM_FROM, from);
    	put(JaxrConstants.RIM_SUBJECT, subject);

    	/*
    	 * Convert icon
    	 */
    	put(JaxrConstants.RIM_ICON, IconConstants.CHAT);

    }

}
