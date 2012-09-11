package de.kp.ames.web.core.search;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.search
 *  Module: SolrProxy
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #proxy #search #solr #web
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

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import de.kp.ames.web.Bundle;
import de.kp.ames.web.GlobalConstants;

@SuppressWarnings("deprecation")
public class SolrProxy {

	/*
	 * Reference to the resource bundle (settings)
	 * associated with the AMES web service
	 */
	private static Bundle bundle = Bundle.getInstance();
	
	/*
	 * Reference to the Solr Search Server
	 */
	private SolrServer server;
	private static SolrProxy instance = new SolrProxy();
	
	/**
	 * Constructor for Solr Search Client
	 */
	private SolrProxy() {

		/*
		 * Connect to Apache Solr
		 */
		try {

			String endpoint = bundle.getString(GlobalConstants.SEARCH_ENDPOINT);
			server = new CommonsHttpSolrServer(endpoint);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			
		}
		
	}

	/**
	 * @return
	 */
	public static SolrProxy getInstance() {
		if (instance == null) instance = new SolrProxy();
		return instance;
	}
	
	/**
	 * This method deletes an existing Apache Solr index
	 * as a whole
	 */
	public void deleteIndex() {
		
		try {
			server.deleteByQuery( "*:*" );
		
		} catch (Exception e) {
			e.printStackTrace();
		
		} finally {}
		
	}

	/**
	 * Remove a selected entry from an Apache Solr Index;
	 * the entry is identified by its unique 'id'
	 * 
	 * @param id
	 */
	public void removeIndexEntry(String id) {
		
		try {		

			server.deleteById(id);

		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {}

	}

	/**
	 * Base method to create an index entry for an Apache Solr
	 * search index from an input document
	 * 
	 * @param document
	 * @throws Exception
	 */
	public void createIndexEntry(SolrInputDocument document) throws Exception {

		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		docs.add(document);

		server.add(docs);
		server.commit();

	}
	
	/**
	 * Retrieve query response from Apache Solr
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public QueryResponse executeQuery(SolrQuery query) throws Exception {
		return server.query(query);
	}

}
