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

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.TelephoneNumberImpl;
import org.json.JSONException;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.shared.JaxrConstants;

public class JsonTelephoneNumber extends JsonRegistryEntry {

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public JsonTelephoneNumber(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Convert TelephoneNUmber into JSON object
     *
	 * @param telephoneNumber
	 * @throws JAXRException
	 * @throws JSONException
	 */
	public void set(TelephoneNumberImpl telephoneNumber) throws JAXRException, JSONException {

		/*
		 * Country code
		 */
		String countryCode = telephoneNumber.getCountryCode();
		put(JaxrConstants.RIM_COUNTRY_CODE, countryCode);
		
		/*
		 * Area code
		 */
		String areaCode = telephoneNumber.getAreaCode();
		put(JaxrConstants.RIM_AREA_CODE, areaCode);

		/*
		 * Phone number
		 */
		String phoneNumber = telephoneNumber.getNumber();
		put(JaxrConstants.RIM_PHONE_NUMBER, phoneNumber);

		String extension = telephoneNumber.getExtension();
		put(JaxrConstants.RIM_PHONE_EXTENSION, extension);

	}


}
