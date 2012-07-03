package de.kp.ames.web.function.user;
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
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.TelephoneNumberImpl;
import org.freebxml.omar.client.xml.registry.infomodel.UserImpl;
import org.json.JSONObject;


import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrTransaction;
import de.kp.ames.web.core.regrep.lcm.PartyLCM;
import de.kp.ames.web.function.FncMessages;
import de.kp.ames.web.shared.JaxrConstants;

public class UserLCM extends PartyLCM {

	/*
	 * Response messages
	 */
	private static String USER_UPDATED = FncMessages.USER_UPDATED;

	private static String NO_DESCRIPTION = "No description available.";
	
	/*
	 * Registry object
	 */
	private static String RIM_DESC = JaxrConstants.RIM_DESC;
	private static String RIM_ID   = JaxrConstants.RIM_ID;

	public UserLCM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}
	
	public String submitUser(String data) throws Exception {
		
		/*
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();
	
		/*
		 * Initialize data
		 */
		JSONObject jForm = new JSONObject(data);

		/*
		 * Determine user
		 */
		String uid = jForm.has(RIM_ID) ? jForm.getString(RIM_ID) : null;			
		if (uid == null) return transaction.getJResponse().toString();
		
		RegistryObjectImpl ro = getRegistryObjectById(uid);
		if (ro == null) throw new Exception("[UserLCM] User with id <" + uid + "> not found.");
		
		UserImpl user = (UserImpl)ro;
		user = setUser(user, jForm);

		/*
		 * Save objects (without versioning)
		 */
		transaction.addObjectToSave(user);
		saveObjects(transaction.getObjectsToSave(), false, false);
	
		/*
		 * Retrieve response message
		 */
		JSONObject jResponse = transaction.getJResponse(uid, USER_UPDATED);
		return jResponse.toString();
		
	}

	/**
	 * Update user specific information of an already
	 * registered user
	 * 
	 * @param user
	 * @param jForm
	 * @return
	 * @throws Exception
	 */
	public UserImpl setUser(UserImpl user, JSONObject jForm) throws Exception {

	    /* 
	     * Person name              
	     */
        PersonNameImpl personName = createPersonName(jForm);
        user.setPersonName(personName);
 
		/* 
		 * Description
		 */
		String rimDescription = jForm.has(RIM_DESC) ? jForm.getString(RIM_DESC) : NO_DESCRIPTION;
		user.setDescription(createInternationalString(rimDescription));
		
		/* 
		 * Home url
		 */
		String rimHome = jaxrHandle.getEndpoint().replace("/soap", "");
		user.setHome(rimHome);

        /* 
         * Postal address
         * 
         * Each user instance MAY have an addresses attribute that is a 
         * Set of PostalAddress instances. Each PostalAddress provides a postal 
         * address for that user. A user SHOULD have at least one PostalAddress.
         */
		user.removeAllPostalAddresses();

        PostalAddressImpl postalAddress = createPostalAddress(jForm);
        user.addPostalAddress(postalAddress);

        /* 
         * Email
         * 
         * Each user instance MAY have an attribute emailAddresses that 
         * is a Set of EmailAddress instances. Each EmailAddress provides an email 
         * address for that user. A user SHOULD have at least one EmailAddress.
          */
        user.removeAllEmailAddresses();

        EmailAddressImpl emailAddress = createEmailAddress(jForm);
        user.addEmailAddress(emailAddress);

        /* 
         * Telefone number
         * 
         * Each user instance MUST have a telephoneNumbers attribute that 
         * contains the Set of TelephoneNumber instances defined for that user. 
         * A user SHOULD have at least one telephone number.
         */
        TelephoneNumberImpl telephoneNumber = createTelephoneNumber(jForm);
        user.addTelephoneNumber(telephoneNumber);

        return user;

	}

}
