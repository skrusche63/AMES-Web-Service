package de.kp.ames.web.core.domain.model;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.domain.model
 *  Module: JsonPersonName
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #domain #json #model #name #person #web
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

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.PersonNameImpl;
import org.json.JSONException;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.shared.constants.JaxrConstants;

public class JsonPersonName extends JsonRegistryEntry {

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public JsonPersonName(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Convert PersonName into JSON object
     *
	 * @param personName
	 * @throws JAXRException
	 * @throws JSONException
	 */
	public void set(PersonNameImpl personName) throws JAXRException, JSONException {

	   	/* 
	   	 * Convert first name
	   	 */
	   	String firstName = personName.getFirstName();
	   	put(JaxrConstants.RIM_FIRST_NAME, firstName);

	   	/* 
	   	 * Convert middle name
	   	 */
	   	String middleName = personName.getMiddleName();
	   	put(JaxrConstants.RIM_MIDDLE_NAME, middleName);

	   	/* 
	   	 * Convert last name
	   	 */
	   	String lastName = personName.getLastName();
	   	put(JaxrConstants.RIM_LAST_NAME, lastName);
 
	}

}
