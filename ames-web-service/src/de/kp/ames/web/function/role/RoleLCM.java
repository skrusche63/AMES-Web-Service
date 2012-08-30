package de.kp.ames.web.function.role;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.role
 *  Module: RoleLCM
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #function #lcm #role #web
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
import java.util.Locale;

import javax.xml.registry.infomodel.Key;

import org.freebxml.omar.client.xml.registry.infomodel.AssociationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.json.JsonUtil;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrTransaction;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.core.regrep.sql.JaxrSQL;
import de.kp.ames.web.function.FncMessages;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.JaxrConstants;
import de.kp.ames.web.shared.constants.JsonConstants;

public class RoleLCM extends JaxrLCM {
	/*
	 * Response messages
	 */
	private static String MISSING_PARAMETERS = "Please provide valid parameters.";

	private static String RIM_NAMESPACE = JaxrConstants.RIM_NAMESPACE;
	private static String RIM_ROLES     = JaxrConstants.RIM_ROLE;
	
	public RoleLCM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Delete responsibility
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public String deleteResponsibility(String uid) throws Exception {

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
		JSONObject jResponse = transaction.getJResponse(uid, FncMessages.RESPONSIBILITY_DELETED);
		return jResponse.toString();
		
	}

	/**
	 * Delete role
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public String deleteRole(String uid) throws Exception {

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
		JSONObject jResponse = transaction.getJResponse(uid, FncMessages.ROLE_DELETED);
		return jResponse.toString();
		
	}

	/**
	 * Build responsilities (association instances) for
	 * a set for referenced namespaces
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String submitResponsibility(String source, String data) throws Exception {
	
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

		/*
		 * Retrieve responsible
		 */
		RegistryObjectImpl responsible = getRegistryObjectById(source);		
		if (responsible == null) new Exception("[RoleLCM] Responsible with id <" + source + "> not found.");

		/*
		 * Responsibility is registered within namespace container
		 */
		RegistryPackageImpl container = null;		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		List<RegistryPackageImpl> list = dqm.getRegistryPackage_ByClasNode(ClassificationConstants.FNC_ID_Namespace);
		if (list.size() == 0) new Exception("[RoleLCM] Namespace container does not exist.");

		container = list.get(0);

		/*
		 * Build responsibilities
		 */
		JSONArray jNamespaces = new JSONArray(jForm.getString(RIM_NAMESPACE));

		List<AssociationImpl> responsibilities = setResponsibility(responsible, jNamespaces);
		for (AssociationImpl responsibility:responsibilities) {
			/* 
			 * Confirm association
			 */
			confirmAssociation(responsibility);				

			/*
			 * Add association to container 
			 */
			container.addRegistryObject(responsibility);
			
		}
			
		transaction.addObjectToSave(responsible);
		transaction.addObjectToSave(container);
		
		/*
		 * Save objects (without versioning)
		 */
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Update response message
		 */
		
		jResponse.put(JsonConstants.J_SUCCESS, true);
		jResponse.put(JsonConstants.J_MESSAGE, FncMessages.RESPONSIBILITY_CREATED);

		return jResponse.toString();

	}
	
	/**
	 * Create roles for a certain affiliation
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String submitRoles(String user, String group, String data) throws Exception {
		
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

		/*
		 * Retrieve affiliation
		 */
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
			
		String sqlString = JaxrSQL.getSQLAssociations_AffiliatedWith(user, group);
		List<AssociationImpl> affiliations = dqm.getAssociationsByQuery(sqlString);
		
		if (affiliations.size() == 0) return jResponse.toString();

		AssociationImpl affiliation = affiliations.get(0);
		
		/*
		 * Create roles (note, they must NOT be added
		 * to the transaction, as these are registered
		 * implicitly, see 'addClassification')
		 */
		JSONArray jRoles = new JSONArray(jForm.getString(RIM_ROLES));
		
		List<ClassificationImpl> classifications = setRoles(affiliation, jRoles);
		affiliation.addClassifications(classifications);
		
		/*
		 * Save objects (without versioning)
		 */
		transaction.addObjectToSave(affiliation);			
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Update response message
		 */
		
		jResponse.put(JsonConstants.J_SUCCESS, true);
		jResponse.put(JsonConstants.J_MESSAGE, FncMessages.ROLES_CREATED);

		return jResponse.toString();
		
	}

	/**
	 * A helper method to create a set of associations classified
	 * by ResponsibleFor
	 * 
	 * @param ro
	 * @param jNamespaces
	 * @return
	 * @throws Exception
	 */
	private List<AssociationImpl> setResponsibility(RegistryObjectImpl ro, JSONArray jNamespaces) throws Exception {
		
		/*
		 * Retrieve existing responsibilities (associations)
		 */
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		String sqlString = JaxrSQL.getSQLAssociations_ResponsibleFor(ro.getId());
		List<AssociationImpl> associations = dqm.getAssociationsByQuery(sqlString);

		/*
		 * Retrieve list of references to namespaces provides
		 */
		ArrayList<String> folders = new ArrayList<String>();
		for (int i=0; i < jNamespaces.length(); i++) {
			folders.add(jNamespaces.getString(i));
		}

		if ((associations.size() == 0) && (folders.size() == 0)) return new ArrayList<AssociationImpl>();
		
		if (associations.size() > 0) {			
			/* 
			 * Delete all responsibilities registered for this responsible
			 */			
			ArrayList<Key> objectsToDelete = new ArrayList<Key>();
			for (AssociationImpl association:associations) {					
				objectsToDelete.add(association.getKey());
			}

			deleteObjects(objectsToDelete);
			
		}

		/*
		 * Create associations
		 */		
		List<AssociationImpl>responsibilities = createAssociations_ResponsibleFor(folders);
		ro.addAssociations(responsibilities);

		return responsibilities;
		
	}

	/**
	 * A helper method to remove all classifications from a
	 * certain registry object and recreate from a list of 
	 * classification nodes
	 * 
	 * @param ro
	 * @param jRoles
	 * @return
	 * @throws Exception
	 */
	private List<ClassificationImpl> setRoles(RegistryObjectImpl ro, JSONArray jRoles) throws Exception  {

		/*
		 * Delete all classifications for registry object
		 */
		deleteClassifications_All(ro);

		/*
		 * Convert JSON roles into String representation
		 */
		ArrayList<String> conceptTypes = JsonUtil.getStringArray(jRoles);
		
		/*
		 * Create classifications
		 */
		return createClassifications(conceptTypes);

	}

	/**
	 * A helper method to create a set of associations
	 * 
	 * @param targets
	 * @return
	 * @throws Exception
	 */
	private List<AssociationImpl> createAssociations_ResponsibleFor(ArrayList<String> targets) throws Exception {

		ArrayList<AssociationImpl> associations = new ArrayList<AssociationImpl>();

		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		for (String target:targets) {
			/*
			 * Determine target object
			 */
			RegistryObjectImpl targetObject = dqm.getRegistryObjectById(target);
			if (targetObject == null) throw new Exception("[RoleLCM] Namespace for id <" + target + "> not found.");
			
			AssociationImpl association = createAssociation_ResponsibleFor(targetObject);
			/*
			 * Name
			 */
			association.setName(createInternationalString(Locale.US, "Responsibility"));
			/*
			 * Description
			 */
			association.setDescription(createInternationalString(Locale.US, "This is a directed association between a namespace and the respective responsible."));			
			associations.add(association);
			
		}
		
		return associations;
		
	}

}
