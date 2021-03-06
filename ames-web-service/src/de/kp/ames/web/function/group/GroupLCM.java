package de.kp.ames.web.function.group;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.group
 *  Module: GroupLCM
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #function #group #lcm #web
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

import java.util.ArrayList;
import java.util.List;

import org.freebxml.omar.client.xml.registry.infomodel.AssociationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.OrganizationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.UserImpl;
import org.json.JSONObject;

import de.kp.ames.web.core.json.JsonUtil;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.regrep.JaxrTransaction;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.regrep.lcm.PartyLCM;
import de.kp.ames.web.core.regrep.sql.JaxrSQL;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.function.FncMessages;
import de.kp.ames.web.function.domain.model.CommunityObject;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.JaxrConstants;

public class GroupLCM extends PartyLCM {

	/*
	 * Response messages
	 */	
	private static String AFFILIATION_CREATED  = FncMessages.AFFILIATION_CREATED;
	private static String AFFILIATION_DELETED  = FncMessages.AFFILIATION_DELETED;
	private static String AFFILIATION_UPDATED  = FncMessages.AFFILIATION_UPDATED;
	private static String CATEGORY_CREATED     = FncMessages.CATEGORY_CREATED;
	private static String CONTACT_CREATED      = FncMessages.CONTACT_CREATED;


	/*
	 * Predefined name for a system generated affiliation
	 */
	private static String AFFIL_NAME = "User Affiliation";
	
	/*
	 * Predefined name for a system generated affiliation
	 */
	private static String AFFIL_DESC = "This association describes the affiliation of a registry user with a certain organization.";

	/*
	 * Registry object
	 */
	private static String RIM_CATE   = JaxrConstants.RIM_CATE;
	private static String RIM_CLAS   = JaxrConstants.RIM_CLAS;
	private static String RIM_SOURCE = JaxrConstants.RIM_SOURCE;
	private static String RIM_TARGET = JaxrConstants.RIM_TARGET;

	/*
	 * Primary contact
	 */
	private static String RIM_CONTACT = JaxrConstants.RIM_CONTACT;
	
	public GroupLCM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Delete a certain community of interest
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public String deleteCommunity(String uid) throws Exception {

		/*
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();
		
		/*
		 * Delete object
		 */
		deleteRegistryObject(uid, transaction);
		
		/*
		 * Retrieve response
		 */
		JSONObject jResponse = transaction.getJResponse(uid, FncMessages.ORGANIZATION_DELETED);
		return jResponse.toString();
			
	}
	
	/**
	 * Delete all affiliations between a certain user and
	 * a specific organization
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String deleteAffiliation(String data) throws Exception {
		
		/*
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();

		/*
		 * Initialize data
		 */
		JSONObject jForm = new JSONObject(data);

		if ((jForm.has(RIM_SOURCE) == false) || (jForm.has(RIM_TARGET) == false)) return transaction.getJResponse().toString();
		
		/*
		 * Determine source object (user)
		 */
		String sid = jForm.getString(RIM_SOURCE);	
		RegistryObjectImpl sourceObject = getRegistryObjectById(sid);

		if (sourceObject == null) throw new Exception("[GroupLCM] Source object with id <" + sid + "> not found.");

		/*
		 * Determine target object (organization)
		 */
		String tid = jForm.getString(RIM_TARGET);				
		RegistryObjectImpl targetObject = getRegistryObjectById(tid);

		if (targetObject == null) throw new Exception("[GroupLCM] Target object with id <" + tid + "> not found.");

		/*
		 * Determine affiliations
		 */
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		String user  = jForm.getString(RIM_SOURCE);	
		String group = jForm.getString(RIM_TARGET);	

		String sqlString = JaxrSQL.getSQLAssociations_AffiliatedWith(user, group);
		List<AssociationImpl> affiliations = dqm.getAssociationsByQuery(sqlString);
		
		for (AssociationImpl affiliation:affiliations) {
			deleteRegistryObject(affiliation, transaction);
		} 

		/*
		 * Delete objects
		 */
		deleteObjects(transaction.getKeysToDelete());

		/*
		 * Retrieve response message
		 */
		JSONObject jResponse = transaction.getJResponse(AFFILIATION_DELETED);
		return jResponse.toString();

	}

	/**
	 * Create or update a certain affiliation
	 * 
	 * @param source
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String submitAffiliation(String source, String data) throws Exception {

		/*
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();
	
		/*
		 * Initialize data
		 */
		JSONObject jForm = new JSONObject(data);
						
		if (source == null) {

			/* 
			 * A new affiliation must be created
			 */
			
			RegistryObjectImpl sourceObject = null;
			RegistryObjectImpl targetObject = null;

			if ((jForm.has(RIM_SOURCE) == false) || (jForm.has(RIM_TARGET) == false)) return transaction.getJResponse().toString();

			/*
			 * Determine source object (user)
			 */
			String sid = jForm.getString(RIM_SOURCE);	
			sourceObject = getRegistryObjectById(sid);

			if (sourceObject == null) throw new Exception("[GroupLCM] Source object with id <" + sid + "> not found.");
			
			/*
			 * Determine target object (organization)
			 */
			String tid = jForm.getString(RIM_TARGET);				
			targetObject = getRegistryObjectById(tid);

			if (targetObject == null) throw new Exception("[GroupLCM] Target object with id <" + tid + "> not found.");

			/*
			 * Create affiliation (association)
			 */
			AssociationImpl affiliation = createAssociation_AffiliatedWith(targetObject);
			if (affiliation == null) throw new Exception("[GroupLCM] Creation of association failed.");

			affiliation.setSourceObject(sourceObject);

			/* 
			 * Unique identifier
			 */
			String aid = JaxrIdentity.getInstance().getPrefixUID(FncConstants.AFFILIATION_PRE);
			
			affiliation.setLid(aid);
			affiliation.getKey().setId(aid);

			/* 
			 * Name & description
			 */
			affiliation.setName(createInternationalString(AFFIL_NAME));
			affiliation.setDescription(createInternationalString(AFFIL_DESC));
				
			/* 
			 * Home url
			 */
			String rimHome = jaxrHandle.getEndpoint().replace("/saml", "");
			affiliation.setHome(rimHome);

			/*
			 * Save association (without versioning)
			 */
			transaction.addObjectToSave(affiliation);
			saveObjects(transaction.getObjectsToSave(), false, false);

			/*
			 * Retrieve response message
			 */
			JSONObject jResponse = transaction.getJResponse(affiliation.getId(), AFFILIATION_CREATED);
			return jResponse.toString();

			
		} else {

			/*
			 * Determine association
			 */
			RegistryObjectImpl ro = getRegistryObjectById(source);
			if (ro == null) throw new Exception("[GroupLCM] Affiliation with id <" + source + "> does not exist.");

			AssociationImpl affiliation = (AssociationImpl)ro;
			
			/*
			 * Determine classifications
			 */
			ArrayList<String> conceptTypes = JsonUtil.getAttributeAsArray(RIM_CLAS, jForm);
			if (conceptTypes != null) updateClassifications(ro, conceptTypes);
			
			/*
			 * Save association (without versioning)
			 */
			transaction.addObjectToSave(affiliation);
			saveObjects(transaction.getObjectsToSave(), false, false);

			/*
			 * Retrieve response message
			 */
			JSONObject jResponse = transaction.getJResponse(affiliation.getId(), AFFILIATION_UPDATED);
			return jResponse.toString();
			
		}

	}

	/**
	 * Create a new category for a certain community
	 * of interest
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String submitCategory(String group, String data) throws Exception {
	
		/*
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();
	
		/*
		 * Initialize data
		 */
		JSONObject jForm = new JSONObject(data);
				
		/*
		 * Determine organization
		 */
		RegistryObjectImpl ro = getRegistryObjectById(group);
		if (ro == null) throw new Exception("[GroupLCM] Organization with id <" + group + "> does not exist.");

		String category = jForm.getString(RIM_CATE);
			
		/* 
		 * A community of interest has a single category (or community type) assigned;
		 * this classification node is used to determine whether a certain community
		 * is an adhoc group without any responsibility for a certain namespace or
		 * a namespace board or namespace group
		 */

		String prefix = ClassificationConstants.FNC_ID_Community;
		deleteClassifications_Prefix(ro, prefix);

		/*
		 * Create classification (note, that it must
		 * NOT be added to the transaction, as it is
		 * registered implicitly)
		 */
		ClassificationImpl clas = createClassification(category);
		ro.addClassification(clas);
			
		/*
		 * Save organization
		 */
		transaction.addObjectToSave(ro);
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Retrieve response message
		 */
		JSONObject jResponse = transaction.getJResponse(CATEGORY_CREATED);
		return jResponse.toString();
		
	}

	/**
	 * Add an existing user as a primary contact
	 * to a selected organization
	 * 
	 * @param source
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String submitContact(String source, String data) throws Exception {
	
		/*
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();
	
		/*
		 * Initialize data
		 */
		JSONObject jForm = new JSONObject(data);
				
		/*
		 * Determine organization
		 */
		RegistryObjectImpl ro = getRegistryObjectById(source);
		if (ro == null) throw new Exception("[GroupLCM] Organization with id <" + source + "> does not exist.");

		OrganizationImpl org = (OrganizationImpl)ro;				

		/*
		 * Determine user
		 */
		String primaryContact = jForm.getString(RIM_CONTACT);

		UserImpl contact = (UserImpl)getRegistryObjectById(primaryContact);
		if (contact == null) throw new Exception("[GroupLCM] User with id <" + primaryContact + "> does not exist.");
			
		/*
		 * Set primary contact
		 */
		org.setPrimaryContact(contact);

		/*
		 * Save organization
		 */
		transaction.addObjectToSave(org);
		saveObjects(transaction.getObjectsToSave(), false, false);
			
		/*
		 * Retrieve response message
		 */
		JSONObject jResponse = transaction.getJResponse(CONTACT_CREATED);
		return jResponse.toString();
		
	}

	/**
	 * Create or update a certain organization
	 * 
	 * @param data
	 * @return
	 */
	public String submitCommunity(String data) throws Exception {

		/*
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();
		
		/*
		 * Create UserObject
		 */
		CommunityObject communityObject = new CommunityObject(jaxrHandle, this);
		RegistryObjectImpl ro = communityObject.submit(data);

		/*
		 * Save objects (without versioning)
		 */
		transaction.addObjectToSave(ro);
		saveObjects(transaction.getObjectsToSave(), false, false);
	
		/*
		 * Retrieve response message
		 */
		JSONObject jResponse = transaction.getJResponse(ro.getId(), communityObject.isCreated() ? FncMessages.ORGANIZATION_CREATED : FncMessages.ORGANIZATION_UPDATED);
		return jResponse.toString();
		
	}
}
