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

import java.util.List;

import org.freebxml.omar.client.xml.registry.infomodel.OrganizationImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.domain.JsonCoreProvider;
import de.kp.ames.web.core.json.StringCollector;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.regrep.sql.JaxrSQL;
import de.kp.ames.web.core.vocab.VocabDQM;
import de.kp.ames.web.shared.ClassificationConstants;
import de.kp.ames.web.shared.JaxrConstants;

public class GroupDQM extends JaxrDQM {
	
	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public GroupDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Retrieve all registered categories as 
	 * a sort list in a JSON representation
	 * 
	 * @return
	 * @throws Exception
	 */
	public JSONArray getCategories() throws Exception {

		VocabDQM vocab = new VocabDQM(jaxrHandle);
		return vocab.getConceptsByParent(ClassificationConstants.FNC_ID_Community);
	
	}

	/**
	 * Get registered communities as a sorted list in 
	 * a JSON representation
	 * 
	 * @param affiliate
	 * @return
	 * @throws Exception
	 */
	public JSONArray getCommunities(String affiliate) throws Exception {
		
		/*
		 * Sort result by name of community
		 */
		StringCollector collector = new StringCollector();

		/*
		 * Determine SQL statement
		 */
		String sqlString = (affiliate == null) ? JaxrSQL.getSQLOrganizations_All() : JaxrSQL.getSQLOrganisations_AffiliatedWith(affiliate);
		List<OrganizationImpl> organizations = getOrganizationsByQuery(sqlString);
		
		/*
		 * Build sorted list
		 */
		for (OrganizationImpl organization:organizations) {

			JSONObject jOrganization = JsonCoreProvider.getOrganization(jaxrHandle, organization);	
			collector.put(jOrganization.getString(JaxrConstants.RIM_NAME), jOrganization);

		}
			
		return new JSONArray(collector.values());
		
	}

}
