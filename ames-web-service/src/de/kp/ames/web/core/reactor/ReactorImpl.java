package de.kp.ames.web.core.reactor;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.reactor
 *  Module: ReactorImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #reactor #web
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

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;

import com.sun.syndication.feed.synd.SyndEntry;
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
import de.kp.ames.web.core.rss.RssCacheManager;
import de.kp.ames.web.core.rss.RssConverter;
import de.kp.ames.web.core.search.IndexerImpl;
import de.kp.ames.web.core.search.SolrProxy;
import de.kp.ames.web.core.search.data.SolrEntryImpl;

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
			SolrEntryImpl entry = new SolrEntryImpl(jaxrHandle);
			
			entry.setDomain(domain);
			entry.setRegistryObject(ro);
			
			new IndexerImpl().createIndexEntry(entry);
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
			 * Convert registry object into syndication entry
			 * and add result to cache
			 */
			SyndEntry syndEntry = RssConverter.convertRegistryObject(ro, jaxrHandle, domain);
			RssCacheManager.getInstance().addEntry(syndEntry);
			
		}

		if (action.equals(RAction.C_INDEX_RSS)) {

			/*
			 * Index registry object
			 */
			SolrEntryImpl entry = new SolrEntryImpl(jaxrHandle);
			
			entry.setDomain(domain);
			entry.setRegistryObject(ro);
			
			new IndexerImpl().createIndexEntry(entry);

			/*
			 * Convert registry object into syndication entry
			 * and add result to cache
			 */
			SyndEntry syndEntry = RssConverter.convertRegistryObject(ro, jaxrHandle, domain);
			RssCacheManager.getInstance().addEntry(syndEntry);
			
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
		IndexerImpl indexer = new IndexerImpl();
		
		for (RegistryObjectImpl ro:ros) {
			/*
			 * Remove from search index
			 */
			indexer.removeFromIndex(ro.getId());
			
			/*
			 * Remove from rss cache
			 */
			RssCacheManager.getInstance().removeEntry(ro.getId());
			
		}

		return;
	}

}
