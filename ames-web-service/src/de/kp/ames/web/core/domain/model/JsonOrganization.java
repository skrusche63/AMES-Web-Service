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

import java.util.Collection;
import java.util.Locale;

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.EmailAddressImpl;
import org.freebxml.omar.client.xml.registry.infomodel.OrganizationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.PostalAddressImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.TelephoneNumberImpl;
import org.freebxml.omar.client.xml.registry.infomodel.UserImpl;
import org.json.JSONException;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.shared.JaxrConstants;

public class JsonOrganization extends JsonRegistryObject {

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public JsonOrganization(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

    /**
     * Convert organization into JSON object
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
    	 * Convert organization specific information
    	 */
    	OrganizationImpl org = (OrganizationImpl)ro;
 
    	/*
    	 * Convert postal address
    	 */
    	PostalAddressImpl postalAddress = (PostalAddressImpl)org.getPostalAddress();
    	
    	JsonPostalAddress jPostalAddress = new JsonPostalAddress(jaxrHandle);
    	jPostalAddress.set(postalAddress);
    	
    	put(JaxrConstants.RIM_ADDRESS, jPostalAddress.get().toString());

    	/*
    	 * Convert email address
    	 */
    	
    	Collection<?>emailAddresses = org.getEmailAddresses();
    	if (emailAddresses.iterator().hasNext()) {
    		
    		EmailAddressImpl emailAddress = (EmailAddressImpl)emailAddresses.iterator().next();
    		
    		String address = emailAddress.getAddress();
    		address = (address == null) ? " - " : address;
    		
    		put(JaxrConstants.RIM_EMAIL, address);
        		
    	} else {
    		
    		put(JaxrConstants.RIM_EMAIL, " - ");
     		
    	}
   	
    	/* 
    	 * Convert telephone number
    	 */
    	Collection<?>telephoneNumbers = org.getTelephoneNumbers();
    	if (telephoneNumbers.iterator().hasNext()) {
    		
    		TelephoneNumberImpl telephoneNumber = (TelephoneNumberImpl)telephoneNumbers.iterator().next();
    		   		
        	JsonTelephoneNumber jTelephoneNumber = new JsonTelephoneNumber(jaxrHandle);
        	jTelephoneNumber.set(telephoneNumber);
        	
        	put(JaxrConstants.RIM_PHONE, jTelephoneNumber.get().toString());
   	    
    	} else {
 
        	put(JaxrConstants.RIM_PHONE, new JSONObject().toString());
        	 
    	}
    	
    	/*
    	 * Convert primary contact
    	 */
    	UserImpl contact = (UserImpl)org.getPrimaryContact();
    	if (contact == null) {;
	    	
	    	put(JaxrConstants.RIM_CONTACT, new JSONObject().toString());

    	} else {
    		
	    	JsonUser jUser = new JsonUser(jaxrHandle);
	    	jUser.set(contact);
	    	
	    	put(JaxrConstants.RIM_CONTACT, jUser.get().toString());
	    	
    	}

    }
    
}
