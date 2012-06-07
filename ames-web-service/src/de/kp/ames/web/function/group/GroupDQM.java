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

import de.kp.ames.web.core.format.json.JsonProvider;
import de.kp.ames.web.core.format.json.StringCollector;
import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.regrep.sql.JaxrSQL;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.function.GuiFactory;
import de.kp.ames.web.function.GuiRenderer;

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
	 * Get registered communities as a sorted list in 
	 * a JSON representation
	 * 
	 * @param affiliate
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public String getCommunities(String affiliate, String format) throws Exception {
		
		String result = null;
		
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

			JSONObject jOrganization = JsonProvider.getOrganization(jaxrHandle, organization);	
			collector.put(jOrganization.getString(JaxrConstants.RIM_NAME), jOrganization);

		}
			
		JSONArray jArray = new JSONArray(collector.values());
		
		/*
		 * Render result
		 */
		if (format.equals(FncConstants.FNC_FORMAT_ID_Grid)) {
			
			GuiRenderer renderer = GuiFactory.getInstance().getRenderer();
			result = renderer.createGrid(jArray);
			
		} else {
			throw new Exception("[GroupDQM] Format <" + format + "> not supported.");

		}
		 
		return result;
		
	}
	
}