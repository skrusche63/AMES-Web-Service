package de.kp.ames.web.core.reactor;

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
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

import de.kp.ames.web.core.reactor.ReactorParams.RAction;

/**
 * ReactorImpl supports additional functionality
 * with respect to Read/Write requests
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */
public class ReactorImpl implements Reactor {

	/* 
	 * The reactor actually supports to reaction right after a certain registry
	 * object has been created or updated: this is a provisioning for (a) the
	 * search index, and (b) the rss processor
	 */
	
	public static void execute(ReactorParams params) throws Exception {

		// retrieve registry object & referenced domain
		String domain = params.getDomain();
		RegistryObjectImpl ro = params.getRO();
		
		// retrieve action to distinguish further processing
		RAction action = params.getAction();
		
		if (action.equals(RAction.C_INDEX) || action.equals(RAction.C_INDEX_RSS)) {
		
			// TODO
			// index reaction
			//JSONObject jEntry = getJEntry(handle, ro, domain);
			//new FProxy().index(jEntry.toString());

		}

		if (action.equals(RAction.D_INDEX)) {
			
			/*
			JSONArray jEntries = new JSONArray();
			jEntries.put(ro.getId());
			
			new FProxy().remove(jEntries.toString());
			*/
		}
		
		if (action.equals(RAction.C_RSS) || action.equals(RAction.C_INDEX_RSS)) {

			/*
			// rss reaction:: CAVE service
			XRSSProcessor.getInstance().addRegistryObject(domain, ro);
			*/
			
		}
		
	}

}
