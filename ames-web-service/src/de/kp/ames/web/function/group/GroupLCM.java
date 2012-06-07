package de.kp.ames.web.function.group;
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
import org.freebxml.omar.client.xml.registry.infomodel.OrganizationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.PostalAddressImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.SlotImpl;
import org.freebxml.omar.client.xml.registry.infomodel.TelephoneNumberImpl;
import org.json.JSONObject;

import de.kp.ames.web.core.format.json.JsonConstants;
import de.kp.ames.web.core.reactor.ReactorImpl;
import de.kp.ames.web.core.reactor.ReactorParams;
import de.kp.ames.web.core.reactor.ReactorParams.RAction;
import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.regrep.JaxrTransaction;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.function.FncConstants;

public class GroupLCM extends JaxrLCM {

	private static String MISSING_PARAMETERS   = "Please provide valid parameters.";
	private static String SUCCESSFULLY_CREATED = "Organization successfully created.";
	
	/*
	 * Registry object
	 */
	private static String RIM_DESC = JaxrConstants.RIM_DESC;
	private static String RIM_ID   = JaxrConstants.RIM_ID;
	private static String RIM_NAME = JaxrConstants.RIM_NAME;

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
	 * Telefone number
	 */
	private static String RIM_COUNTRY_CODE   	= JaxrConstants.RIM_COUNTRY_CODE;
	private static String RIM_AREA_CODE       	= JaxrConstants.RIM_AREA_CODE;
	private static String RIM_PHONE_NUMBER    	= JaxrConstants.RIM_PHONE_NUMBER;
	private static String RIM_PHONE_EXTENSION 	= JaxrConstants.RIM_PHONE_EXTENSION;

	/* 
	 * Email address
	 */
	private static String RIM_EMAIL = JaxrConstants.RIM_EMAIL;

	/*
	 * Symbol
	 */
	private static String RIM_SYMBOL = JaxrConstants.RIM_SYMBOL;
	
	public GroupLCM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Create or update a certain organization
	 * 
	 * @param data
	 * @return
	 */
	public String submitCommunity(String data) throws Exception {

		JSONObject jResponse = new JSONObject();
			
		/*
		 * Prepare (pessimistic) response message
		 */
		String message = MISSING_PARAMETERS;
		
		jResponse.put(JsonConstants.J_SUCCESS, false);
		jResponse.put(JsonConstants.J_MESSAGE, message);

		/*
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();

		/*
		 * Initialize data
		 */
		JSONObject jForm = new JSONObject(data);
			
		OrganizationImpl org = null;
		
		/* 
		 * Determine whether this request references an existing 
		 * organization or intents to create a new one
		 */
			
		String source = jForm.has(RIM_ID) ? jForm.getString(RIM_ID) : null;			
		if (source == null) {
				
				/* 
				 * Name				
				 */
				String rimName = jForm.getString(RIM_NAME);
				org = createOrganization(rimName);

				/* 
				 * Unique identifier
				 */
				String oid = JaxrIdentity.getInstance().getPrefixUID(FncConstants.COMMUNITY_PRE);
				
				org.setLid(oid);
				org.getKey().setId(oid);

				/* 
				 * Set community information
				 */
				org = setCommunity(org, jForm);

				/*
				 * Save organisation (without versioning)
				 */
				transaction.addObjectToSave(org);
				saveObjects(transaction.getObjectsToSave(), false, false);

				/*
				 * Index community
				 */				
				ReactorParams reactorParams = new ReactorParams(org, FncConstants.FNC_ID_Community, RAction.C_INDEX);
				ReactorImpl.onSubmit(reactorParams);

				/*
				 * Update response message
				 */
				message = SUCCESSFULLY_CREATED;

		} else {

			/*
			 * Retrieve existing organization
			 */
			RegistryObjectImpl ro = getRegistryObjectById(source);
			if (ro == null) {
				/*
				 * This should not happen
				 */
				throw new Exception("[GroupLCM] Organisation with id <" + source + "> does not exist.");
				
			} else {

				org = (OrganizationImpl)ro;
				
				/* 
				 * Name	(always replaced with a SET request)	
				 */
				String rimName = jForm.getString(RIM_NAME);
				org.setName(createInternationalString(rimName));

				/* 
				 * Update community information
				 */
				org = setCommunity(org, jForm);

				/*
				 * Save organisation (without versioning)
				 */
				transaction.addObjectToSave(org);
				saveObjects(transaction.getObjectsToSave(), false, false);

				/*
				 * Index community
				 */				
				ReactorParams reactorParams = new ReactorParams(org, FncConstants.FNC_ID_Community, RAction.C_INDEX);
				ReactorImpl.onSubmit(reactorParams);

				/*
				 * Update response message
				 */
				message = SUCCESSFULLY_CREATED;

			}

		}
		
		jResponse.put(JsonConstants.J_SUCCESS, true);
		jResponse.put(JsonConstants.J_MESSAGE, message);
		
		return jResponse.toString();
		
	}
	
	/**
	 * A helper method to set organization specific information
	 * from a JSON object
	 * 
	 * @param org
	 * @param jData
	 * @return
	 * @throws Exception
	 */
	private OrganizationImpl setCommunity(OrganizationImpl org, JSONObject jData) throws Exception {

		/* 
		 * Description
		 */
		
		String rimDescription = jData.has(RIM_DESC) ? jData.getString(RIM_DESC) : "No description available.";
		org.setDescription(createInternationalString(rimDescription));
		
		/* 
		 * Home url
		 */

		String rimHome = jaxrHandle.getEndpoint().replace("/soap", "");
		org.setHome(rimHome);

        /* 
         * Postal address
         * 
         * Each organization instance MAY have an addresses attribute that is a 
         * Set of PostalAddress instances. Each PostalAddress provides a postal 
         * address for that organization. An Organization SHOULD have at least 
         * one PostalAddress.
         */
        
        org.removeAllPostalAddresses();

        PostalAddressImpl postalAddress = createPostalAddress(jData);
        org.addPostalAddress(postalAddress);

        /* 
         * Email address
         * 
         * Each organization instance MAY have an attribute emailAddresses that 
         * is a Set of EmailAddress instances. Each EmailAddress provides an email 
         * address for that Organization. An Organization SHOULD have at least one 
         * EmailAddress.
         */
       
        
        String email = jData.has(RIM_EMAIL) ? jData.getString(RIM_EMAIL) : "";
      
        org.removeAllEmailAddresses();

        EmailAddressImpl emailAddress = createEmailAddress(email);
        org.addEmailAddress(emailAddress);

        /* 
         * Telefone number
         * 
         * Each organization instance MUST have a telephoneNumbers attribute that 
         * contains the Set of TelephoneNumber instances defined for that organization. 
         * An Organization SHOULD have at least one telephone number.
         */

        org.removeAllTelephoneNumbers();
        
        TelephoneNumberImpl telephoneNumber = createTelephoneNumber(jData);
        org.addTelephoneNumber(telephoneNumber);

        /* 
         * Symbol
         * 
         * A reference to a (military) symbol is represented by a certain
         * predefined slot
         */

        String symbol = jData.has(RIM_SYMBOL) ? jData.getString(RIM_SYMBOL) : null;
        if (symbol != null) {
        	
    		SlotImpl slot = createSlot(JaxrConstants.SLOT_SYMBOL, symbol, JaxrConstants.SLOT_TYPE);
    		org.addSlot(slot);
    	
        }
        
        return org;
	
	}

	/**
	 * A helper method to create a postal address from a JSON object
	 * 
	 * @param jPostalAddress
	 * @return
	 * @throws Exception
	 */
	private PostalAddressImpl createPostalAddress(JSONObject jPostalAddress) throws Exception  {

		String country         = jPostalAddress.has(RIM_COUNTRY) ? jPostalAddress.getString(RIM_COUNTRY) : "";
        String stateOrProvince = jPostalAddress.has(RIM_STATE_OR_PROVINCE) ? jPostalAddress.getString(RIM_STATE_OR_PROVINCE) : "";

        String postalCode = jPostalAddress.has(RIM_POSTAL_CODE) ? jPostalAddress.getString(RIM_POSTAL_CODE) : "";
        String city	      = jPostalAddress.has(RIM_CITY) ? jPostalAddress.getString(RIM_CITY) : "";

        String street		= jPostalAddress.has(RIM_STREET) ? jPostalAddress.getString(RIM_STREET) : "";
        String streetNumber = jPostalAddress.has(RIM_STREET_NUMBER) ? jPostalAddress.getString(RIM_STREET_NUMBER) : "";
       
        return this.createPostalAddress(streetNumber, street, city, stateOrProvince, country, postalCode);

	}

	/**
	 * A helper method to create a telephone number from a JSON object
	 * 
	 * @param jObject
	 * @return
	 * @throws Exception
	 */
	private TelephoneNumberImpl createTelephoneNumber(JSONObject jObject) throws Exception {

		TelephoneNumberImpl telephoneNumber = createTelephoneNumber();
        
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
