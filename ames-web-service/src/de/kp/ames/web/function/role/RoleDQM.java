package de.kp.ames.web.function.role;
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
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.freebxml.omar.client.xml.registry.infomodel.AssociationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.json.DateCollector;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.regrep.sql.JaxrSQL;
import de.kp.ames.web.core.vocab.VocabDQM;
import de.kp.ames.web.function.FncSQL;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.JaxrConstants;
import de.kp.ames.web.shared.constants.JsonConstants;

public class RoleDQM extends JaxrDQM {

	/*
	 * Registry object
	 */
	private static String RIM_ID        = JaxrConstants.RIM_ID;
	private static String RIM_NAME      = JaxrConstants.RIM_NAME;
	private static String RIM_TIMESTAMP = JaxrConstants.RIM_TIMESTAMP;

	public RoleDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}
	
	/**
	 * Get registered namespaces (registry packages) that 
	 * are assigned to either an organization or user
	 * 
	 * @param responsible
	 * @return
	 * @throws Exception
	 */
	public JSONArray getResponsibilities(String responsible) throws Exception {
	
		/*
		 * Sort result by datetime
		 */
		DateCollector collector = new DateCollector();

		/* 
		 * A responsible is either an organization or user that is associated
		 * with a certain package; actually these packages are restricted to
		 * namespaces
		 */
			
		String sqlString = FncSQL.getSQLResponsibilities_All(responsible);
		List<RegistryObjectImpl> objects = getRegistryObjectsByQuery(sqlString);

		for (RegistryObjectImpl object:objects) {
			
			RegistryPackageImpl rp = (RegistryPackageImpl)object;
			JSONObject jNamespace = new JSONObject();

			/*
			 * Name
			 */
			String name = getName(rp);
			/* 
			 * The top level container for all namespaces is 
			 * excluded from this request
			 */			
			if (name.equals("Namespaces")) continue;
			jNamespace.put(RIM_NAME, name);

			/*
			 * Unique identifier
			 */
			String uid = rp.getId();				
			jNamespace.put(RIM_ID, uid);
			
			/*
			 * Timestamp
			 */
			Date lastModified = getLastModified(rp);
			jNamespace.put(RIM_TIMESTAMP, lastModified);

			collector.put(lastModified, jNamespace);

		}

		return new JSONArray(collector.values());

	}
		
	/**
	 * Retrieve all registered roles in the context 
	 * of a certain affiliation, i.e. attached roles
	 * are marked 'checked' and others not
	 * 
	 * @param user
	 * @param community
	 * @return
	 * @throws Exception
	 */
	public JSONArray getRoles(String user, String community) throws Exception {

		/*
		 * Retrieve all roles actually registered
		 */
		VocabDQM vocab = new VocabDQM(jaxrHandle);
		JSONArray jConcepts = vocab.getConceptsByParent(ClassificationConstants.FNC_ID_Role);

		/*
		 * Retrieve affiliation that refers to affiliate & community
		 */
		String sqlString = JaxrSQL.getSQLAssociations_AffiliatedWith(user, community);
		List<AssociationImpl> affiliations = getAssociationsByQuery(sqlString);
		
		if (affiliations.size() == 0) return jConcepts;

		/* 
		 * There must only be one association available
		 */
		AssociationImpl affiliation = affiliations.get(0);

		/*
		 * Retrieve concept types for the selected affiliation
		 */
		ArrayList<String> conceptTypes = new ArrayList<String>();
			
		Collection<?> clases = affiliation.getClassifications();
		Iterator<?> iterator = clases.iterator();
			
		while (iterator.hasNext()) {
			
			ClassificationImpl clas = (ClassificationImpl)iterator.next();
			conceptTypes.add(clas.getConcept().getKey().getId());
			
		}

		if (conceptTypes.size() == 0) return jConcepts;
		
		/*
		 * Mark concepts (above) as checked, if they also
		 * refer to concept types attached to an affiliation
		 */

		for (int i=0; i < jConcepts.length(); i++) {
				
			JSONObject jConcept = jConcepts.getJSONObject(i);
				
			String conceptType = jConcept.getString(RIM_ID);
			
			jConcept.put(JsonConstants.J_CHECK, false);
			if (conceptTypes.contains(conceptType)) jConcept.put(JsonConstants.J_CHECK, true);
			
		} 
		
		return jConcepts;
		
	}
}
