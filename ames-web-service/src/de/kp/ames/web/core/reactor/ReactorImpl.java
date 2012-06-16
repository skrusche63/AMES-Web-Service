package de.kp.ames.web.core.reactor;

import java.util.ArrayList;

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
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.search.SolrProxy;
import de.kp.ames.web.core.search.data.SolrEntry;

/**
 * ReactorImpl supports additional functionality
 * with respect to Read/Write requests
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */
public class ReactorImpl implements Reactor {

	/* 
	 * The reactor actually supports action right after a certain registry
	 * object has been created or updated: this is a provisioning for 
	 * (a) the search index, and (b) the rss processor
	 */
	
	/**
	 * @param params
	 * @throws Exception
	 */
	public static void onSubmit(ReactorParams params) throws Exception {

		// retrieve registry object & referenced domain
		String domain = params.getDomain();
		
		RegistryObjectImpl ro = params.getRegistryObject();
		JaxrHandle jaxrHandle = params.getJaxrHandle();
		
		// retrieve action to distinguish further processing
		RAction action = params.getAction();
		
		if (action.equals(RAction.C_INDEX)) {
		
			/*
			 * Index registry object
			 */
			SolrEntry entry = new SolrEntry(jaxrHandle);
			
			entry.setDomain(domain);
			entry.setRegistryObject(ro);
			
			SolrProxy.getInstance().createIndexEntry(entry);

			return;
			
		}

		if (action.equals(RAction.D_INDEX)) {
			
			/*
			 * Remove a certain registry object from
			 * the Apache Solr search index
			 */
			SolrProxy.getInstance().removeIndexEntry(ro.getId());

			return;
			
		}
		
		if (action.equals(RAction.C_RSS)) {

			/*
			// rss reaction:: CAVE service
			XRSSProcessor.getInstance().addRegistryObject(domain, ro);
			*/
			
		}

		if (action.equals(RAction.C_INDEX_RSS)) {

			/*
			 * Index registry object
			 */
			SolrEntry entry = new SolrEntry(jaxrHandle);
			
			entry.setDomain(domain);
			entry.setRegistryObject(ro);
			
			SolrProxy.getInstance().createIndexEntry(entry);

			/*
			// rss reaction:: CAVE service
			XRSSProcessor.getInstance().addRegistryObject(domain, ro);
			*/
			
		}

	}
	
	/**
	 * @param ros
	 * @throws Exception
	 */
	public static void onDelete(ArrayList<RegistryObjectImpl> ros) throws Exception {
		
		/*
		 * Remove list of registry objects from
		 * the Apache Solr search index and RSS
		 */
		SolrProxy solrProxy = SolrProxy.getInstance();
		
		for (RegistryObjectImpl ro:ros) {
			/*
			 * Remove from search index
			 */
			solrProxy.removeIndexEntry(ro.getId());
			
			// TODO
			
		}

		return;
	}

}
