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

import java.util.List;

import org.freebxml.omar.client.xml.registry.infomodel.UserImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.format.json.JsonProvider;
import de.kp.ames.web.core.format.json.StringCollector;
import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.regrep.sql.JaxrSQL;

public class UserDQM extends JaxrDQM {

	public UserDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}
	
	/**
	 * Retrieve affiliated users of a certain community
	 * as a sorted list in a JSON representation
	 * 
	 * @param community
	 * @return
	 * @throws Exception
	 */
	public JSONArray getUsers(String community) throws Exception {

		/*
		 * Sort result by name of user
		 */
		StringCollector collector = new StringCollector();

		/*
		 * Determine SQL statement
		 */
		String sqlString = (community == null) ? JaxrSQL.getSQLUsers_All() : JaxrSQL.getSQLUsers_AffiliatedWith(community);
		List<UserImpl> users = getUsersByQuery(sqlString);
		
		/*
		 * Build sorted list
		 */
		for (UserImpl user:users) {

			JSONObject jUser = JsonProvider.getUser(jaxrHandle, user);	
			collector.put(jUser.getString(JaxrConstants.RIM_NAME), jUser);

		}
			
		return new JSONArray(collector.values());
		
	}

}