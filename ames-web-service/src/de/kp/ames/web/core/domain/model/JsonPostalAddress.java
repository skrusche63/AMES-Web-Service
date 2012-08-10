package de.kp.ames.web.core.domain.model;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.domain.model
 *  Module: JsonPostalAddress
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #address #core #domain #json #model #postal #web
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

import org.freebxml.omar.client.xml.registry.infomodel.PostalAddressImpl;
import org.json.JSONException;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.shared.constants.JaxrConstants;

public class JsonPostalAddress extends JsonRegistryEntry {

	/**
	 * @param jaxrHandle
	 */
	public JsonPostalAddress(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Convert PostalAddress into JSON object
	 * 
	 * @param postalAddress
	 * @throws JSONException
	 * @throws JAXRException
	 */
	public void set(PostalAddressImpl postalAddress) throws JSONException, JAXRException  {
    	
		/*
		 * Country
		 */
		String country = postalAddress.getCountry();
 
       	country = (country == null) ? "" : country;
       	put(JaxrConstants.RIM_COUNTRY, country);
       	
       	/*
       	 * State or Province
       	 */
       	String state =  postalAddress.getStateOrProvince();
       	
       	state = (state == null) ? "" : state;
       	put(JaxrConstants.RIM_STATE_OR_PROVINCE, state);
 
       	/*
       	 * Postal Code
       	 */
       	String code = postalAddress.getPostalCode();
       	code = (code == null) ? "" : code;
       	      	
       	put(JaxrConstants.RIM_POSTAL_CODE, code);
 
       	/*
       	 * City
       	 */
       	String city = postalAddress.getCity();
       	city = (city == null) ? "" : city;
              	
       	put(JaxrConstants.RIM_CITY, city);

       	/*
       	 * Street
       	 */
       	String street = postalAddress.getStreet();
       	street = (street == null) ? "" : street;
       	
       	put(JaxrConstants.RIM_STREET, street);
       	
       	/*
       	 * Street number
       	 */
       	String number = postalAddress.getStreetNumber();
       	number = (number == null) ? "" : number;
       	
    	put(JaxrConstants.RIM_STREET_NUMBER, number);
   
    }

}
