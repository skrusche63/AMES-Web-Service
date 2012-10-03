package de.kp.ames.web.function.domain.model;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.domain.model
 *  Module: PartyObject
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #domain #function #model #object #party  #web
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

import org.freebxml.omar.client.xml.registry.infomodel.EmailAddressImpl;
import org.freebxml.omar.client.xml.registry.infomodel.PersonNameImpl;
import org.freebxml.omar.client.xml.registry.infomodel.PostalAddressImpl;
import org.freebxml.omar.client.xml.registry.infomodel.TelephoneNumberImpl;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.shared.constants.JaxrConstants;

public class PartyObject extends BusinessObject {
	
	/*
	 * Person name
	 */
    public static String RIM_FIRST_NAME  = JaxrConstants.RIM_FIRST_NAME;
    public static String RIM_MIDDLE_NAME = JaxrConstants.RIM_MIDDLE_NAME;
    public static String RIM_LAST_NAME   = JaxrConstants.RIM_LAST_NAME;
	
	/* 
	 * Postal address
	 */
	private static String RIM_COUNTRY           = JaxrConstants.RIM_COUNTRY;
	private static String RIM_STATE_OR_PROVINCE = JaxrConstants.RIM_STATE_OR_PROVINCE;
	private static String RIM_POSTAL_CODE       = JaxrConstants.RIM_POSTAL_CODE;
	private static String RIM_CITY              = JaxrConstants.RIM_CITY;
	private static String RIM_STREET            = JaxrConstants.RIM_STREET;
	private static String RIM_STREET_NUMBER     = JaxrConstants.RIM_STREET_NUMBER;

	/* 
	 * Telephone number
	 */
	private static String RIM_COUNTRY_CODE   	= JaxrConstants.RIM_COUNTRY_CODE;
	private static String RIM_AREA_CODE       	= JaxrConstants.RIM_AREA_CODE;
	private static String RIM_PHONE_NUMBER    	= JaxrConstants.RIM_PHONE_NUMBER;
	private static String RIM_PHONE_EXTENSION 	= JaxrConstants.RIM_PHONE_EXTENSION;

	/* 
	 * Email address
	 */
	private static String RIM_EMAIL = JaxrConstants.RIM_EMAIL;

	public PartyObject(JaxrHandle jaxrHandle, JaxrLCM jaxrLCM) {
		super(jaxrHandle, jaxrLCM);
	}


	/**
	 * A helper method to create an email address from a JSON Object
	 * @param jEmailAddress
	 * @return
	 * @throws Exception
	 */
	protected EmailAddressImpl createEmailAddress(JSONObject jEmailAddress) throws Exception {
		
        String email = jEmailAddress.has(RIM_EMAIL) ? jEmailAddress.getString(RIM_EMAIL) : "";       
        return jaxrLCM.createEmailAddress(email);

	}
	
	/**
	 * A helper method to create a person name from a JSON object
	 * 
	 * @param jPersonName
	 * @return
	 * @throws Exception
	 */
	protected PersonNameImpl createPersonName(JSONObject jPersonName) throws Exception {

		String firstName  = jPersonName.has(RIM_FIRST_NAME) ? jPersonName.getString(RIM_FIRST_NAME) : "";
        String middleName = jPersonName.has(RIM_MIDDLE_NAME) ? jPersonName.getString(RIM_MIDDLE_NAME) : "";

        String lastName = jPersonName.has(RIM_LAST_NAME) ? jPersonName.getString(RIM_LAST_NAME) : null;
        if (lastName == null) throw new Exception("[PartyLCM] No last name provided.");
       
        return jaxrLCM.createPersonName(firstName, middleName, lastName);
	
	}
	
	/**
	 * A helper method to create a postal address from a JSON object
	 * 
	 * @param jPostalAddress
	 * @return
	 * @throws Exception
	 */
	protected PostalAddressImpl createPostalAddress(JSONObject jPostalAddress) throws Exception  {

		String country         = jPostalAddress.has(RIM_COUNTRY) ? jPostalAddress.getString(RIM_COUNTRY) : "";
        String stateOrProvince = jPostalAddress.has(RIM_STATE_OR_PROVINCE) ? jPostalAddress.getString(RIM_STATE_OR_PROVINCE) : "";

        String postalCode = jPostalAddress.has(RIM_POSTAL_CODE) ? jPostalAddress.getString(RIM_POSTAL_CODE) : "";
        String city	      = jPostalAddress.has(RIM_CITY) ? jPostalAddress.getString(RIM_CITY) : "";

        String street		= jPostalAddress.has(RIM_STREET) ? jPostalAddress.getString(RIM_STREET) : "";
        String streetNumber = jPostalAddress.has(RIM_STREET_NUMBER) ? jPostalAddress.getString(RIM_STREET_NUMBER) : "";
       
        return jaxrLCM.createPostalAddress(streetNumber, street, city, stateOrProvince, country, postalCode);

	}

	/**
	 * A helper method to create a telephone number from a JSON object
	 * 
	 * @param jObject
	 * @return
	 * @throws Exception
	 */
	protected TelephoneNumberImpl createTelephoneNumber(JSONObject jObject) throws Exception {

		TelephoneNumberImpl telephoneNumber = jaxrLCM.createTelephoneNumber();
        
        String countryCode = jObject.has(RIM_COUNTRY_CODE) ? jObject.getString(RIM_COUNTRY_CODE) : "";
        telephoneNumber.setCountryCode(countryCode);

        String areaCode = jObject.has(RIM_AREA_CODE) ? jObject.getString(RIM_AREA_CODE) : "";
        telephoneNumber.setAreaCode(areaCode);
 
        String number = jObject.has(RIM_PHONE_NUMBER) ? jObject.getString(RIM_PHONE_NUMBER) : "";
        telephoneNumber.setNumber(number);

        String extension = jObject.has(RIM_PHONE_EXTENSION) ? jObject.getString(RIM_PHONE_EXTENSION) : "";
        telephoneNumber.setExtension(extension);

        return telephoneNumber;
        
	}

	
}
