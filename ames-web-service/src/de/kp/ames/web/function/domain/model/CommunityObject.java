package de.kp.ames.web.function.domain.model;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.domain.model
 *  Module: CommunityObject
 *  @author spex66@gmx.net
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #domain #function #model #object #community #organization #web
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

import java.io.InputStream;

import org.freebxml.omar.client.xml.registry.infomodel.EmailAddressImpl;
import org.freebxml.omar.client.xml.registry.infomodel.OrganizationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.PostalAddressImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.SlotImpl;
import org.freebxml.omar.client.xml.registry.infomodel.TelephoneNumberImpl;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.shared.constants.JaxrConstants;

public class CommunityObject extends PartyObject {
	
	/*
	 * Symbol
	 */
	private static String SLOT_SYMBOL = JaxrConstants.SLOT_SYMBOL;

	public CommunityObject(JaxrHandle jaxrHandle, JaxrLCM jaxrLCM) {
		super(jaxrHandle, jaxrLCM);
	}

	/**
	 * Create CommunityObject
	 * 
	 * @param data
	 * @param stream
	 * @return
	 * @throws Exception
	 */
	public RegistryObjectImpl create(String data, InputStream stream) throws Exception {
		
		/*
		 * Initialize data
		 */
		JSONObject jForm = new JSONObject(data);
		
		
		/* 
		 * Name				
		 */
		String rimName = jForm.getString(RIM_NAME);
		OrganizationImpl org = jaxrLCM.createOrganization(rimName);

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
		 * Indicate as created
		 */
		this.created = true;

		return org;
		
	}
	
	/**
	 * Update CommunityObject from JSON representation
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
	

		OrganizationImpl org = (OrganizationImpl)ro;
		
		/* 
		 * Name	(always replaced with a SET request)	
		 */
		String rimName = jForm.getString(RIM_NAME);
		org.setName(jaxrLCM.createInternationalString(rimName));

		/* 
		 * Update community information
		 */
		org = setCommunity(org, jForm);

		/*
		 * Indicate as updated
		 */
		this.created = false;
		
		return org;
		
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
		org.setDescription(jaxrLCM.createInternationalString(rimDescription));
		
		/* 
		 * Home url
		 */

		String rimHome = jaxrHandle.getEndpoint().replace("/saml", "");
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
      
        org.removeAllEmailAddresses();

        EmailAddressImpl emailAddress = createEmailAddress(jData);
        org.addEmailAddress(emailAddress);

        /* 
         * Telephone number
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

        String symbol = jData.has(SLOT_SYMBOL) ? jData.getString(SLOT_SYMBOL) : null;
        if (symbol != null) {
        	
    		SlotImpl slot = jaxrLCM.createSlot(JaxrConstants.SLOT_SYMBOL, symbol, JaxrConstants.SLOT_TYPE);
    		org.addSlot(slot);
    	
        }
        
        return org;
	
	}
	
}
