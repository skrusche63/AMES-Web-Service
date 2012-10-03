package de.kp.ames.web.function.domain.model;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.domain.model
 *  Module: UserObject
 *  @author spex66@gmx.net
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #domain #function #model #object #user #web
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
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.TelephoneNumberImpl;
import org.freebxml.omar.client.xml.registry.infomodel.UserImpl;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;

public class UserObject extends PartyObject {
	
	private static String NO_DESCRIPTION = "No description available.";

	public UserObject(JaxrHandle jaxrHandle, JaxrLCM jaxrLCM) {
		super(jaxrHandle, jaxrLCM);
	}

	
	/**
	 * Update ProductObject from JSON representation
	 * 
	 * @param jForm
	 * @return
	 * @throws Exception
	 */
	public RegistryObjectImpl update(JSONObject jForm) throws Exception {

		/* 
		 * Determine document from unique identifier
		 */
		String uid = jForm.getString(RIM_ID);
		
		RegistryObjectImpl ro = (RegistryObjectImpl)jaxrLCM.getRegistryObjectById(uid);
		if (ro == null) throw new Exception("[UserObject] RegistryObject with id <" + uid + "> does not exist.");
	
		UserImpl user = (UserImpl)ro;

	    /* 
	     * Person name              
	     */
        PersonNameImpl personName = createPersonName(jForm);
        user.setPersonName(personName);
 
		/* 
		 * Description
		 */
		String rimDescription = jForm.has(RIM_DESC) ? jForm.getString(RIM_DESC) : NO_DESCRIPTION;
		user.setDescription(jaxrLCM.createInternationalString(rimDescription));
		
		/* 
		 * Home url
		 */
		String rimHome = jaxrHandle.getEndpoint().replace("/saml", "");
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
         * Telephone number
         * 
         * Each user instance MUST have a telephoneNumbers attribute that 
         * contains the Set of TelephoneNumber instances defined for that user. 
         * A user SHOULD have at least one telephone number.
         */
        user.removeAllTelephoneNumbers();
        
        TelephoneNumberImpl telephoneNumber = createTelephoneNumber(jForm);
        user.addTelephoneNumber(telephoneNumber);

		/*
		 * Indicate as updated
		 */
		this.created = false;
		
		return user;
		
	}

	
}
