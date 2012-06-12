package de.kp.ames.web.function.domain;
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

import org.freebxml.omar.client.xml.registry.infomodel.ExternalLinkImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.format.json.JsonProvider;
import de.kp.ames.web.core.format.json.StringCollector;
import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.regrep.sql.JaxrSQL;

public class DomainDQM extends JaxrDQM {

	public DomainDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}
	
	/**
	 * Retrieve registered external links either all or
	 * those attached to a certain registry package (parent)
	 * 
	 * @param parent
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public JSONArray getExternalLinks(String parent, String item) throws Exception {

		/*
		 * Sort result by name of external link
		 */
		StringCollector collector = new StringCollector();
		
		/*
		 * Determine SQL statement
		 */
		List<ExternalLinkImpl> links = null;
		
		if (item == null) {
			/*
			 * No specific external link instance is requested
			 */			
			String sqlString = (parent == null) ? JaxrSQL.getSQLExternalLinks_All() : JaxrSQL.getSQLPackageMembers(parent);
			links = getExternalLinksByQuery(sqlString);
		
			if (links.size() == 0) return new JSONArray();

			
		} else {
			/*
			 * A specific external link is requested
			 */
			RegistryObjectImpl ro = getRegistryObjectById(item);
			if (ro == null) return new JSONArray();
			
			links = new ArrayList<ExternalLinkImpl>();
			ExternalLinkImpl link = (ExternalLinkImpl)ro;
			
			links.add(link);		
		
		}
		
		/*
		 * Build sorted list
		 */
		for (ExternalLinkImpl link:links) {

			JSONObject jLink = JsonProvider.getExternalLink(jaxrHandle, link);	
			collector.put(jLink.getString(JaxrConstants.RIM_NAME), jLink);

		}
			
		return new JSONArray(collector.values());

	}
	
	/**
	 * Retrieve registry objects either all or
	 * those attached to a certain registry 
	 * package (parent)
	 * 
	 * @param parent
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public JSONArray getRegistryObjects(String parent, String item) throws Exception {

		/*
		 * Sort result by name of registry object
		 */
		StringCollector collector = new StringCollector();
		
		/*
		 * Determine SQL statement
		 */
		List<RegistryObjectImpl> objects = null;
		
		if (item == null) {
			/*
			 * No specific registry object instance is requested
			 */			
			String sqlString = (parent == null) ? JaxrSQL.getSQLRegistryObjects_All() : JaxrSQL.getSQLPackageMembers(parent);
			objects = getRegistryObjectsByQuery(sqlString);
		
			if (objects.size() == 0) return new JSONArray();

			
		} else {
			/*
			 * A specific registry object is requested
			 */
			RegistryObjectImpl ro = getRegistryObjectById(item);
			if (ro == null) return new JSONArray();
			
			objects = new ArrayList<RegistryObjectImpl>();
			objects.add(ro);		
		
		}
		
		/*
		 * Build sorted list
		 */
		for (RegistryObjectImpl object:objects) {

			JSONObject jObject = JsonProvider.getRegistryObject(jaxrHandle, object);	
			collector.put(jObject.getString(JaxrConstants.RIM_NAME), jObject);

		}
			
		return new JSONArray(collector.values());

	}
}
