package de.kp.ames.web.core.search;
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

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.TermsParams;
import org.apache.solr.common.util.NamedList;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.Bundle;
import de.kp.ames.web.GlobalConstants;

public class SearchClient {

	/*
	 * Reference to the resource bundle (settings)
	 * associated with the AMES web service
	 */
	private static Bundle bundle = Bundle.getInstance();
	
	/*
	 * Reference to the Solr Search Server
	 */
	private SolrServer server;
	private static SearchClient instance = new SearchClient();
	
	/**
	 * Constructor for Solr Search Client
	 */
	private SearchClient() {

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
	public static SearchClient getInstance() {
		if (instance == null) instance = new SearchClient();
		return instance;
	}
	
	/**
	 * External search method that supports term suggestion
	 * 
	 * @param request
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public String suggest(String request, String start, String limit) throws Exception {

		JSONObject jResponse = new JSONObject();
			
		jResponse.put("total", 0);
		jResponse.put("list", new JSONArray());
		
		SolrQuery query = new SolrQuery();

		query.setParam(CommonParams.QT, "/terms");
	    query.setParam(TermsParams.TERMS, true);

	    query.setParam(TermsParams.TERMS_LIMIT, SearchConstants.TERMS_LIMIT);
	    
	    query.setParam(TermsParams.TERMS_FIELD, SearchConstants.TERMS_FIELD);
	    query.setParam(TermsParams.TERMS_PREFIX_STR, request);
		
		QueryResponse response = server.query(query);
		NamedList<Object> terms = getTerms(response);
		
		JSONArray jTerms = getJTermValues(SearchConstants.TERMS_FIELD, terms);
		
		jResponse.put("total", jTerms.length());
		jResponse.put("list",  jTerms);
		
		return jResponse.toString();

	}

	/**
	 * A helper method to support the term suggest
	 * mechanism of the AMES search functionality
	 * 
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private NamedList<Object> getTerms(QueryResponse response) {
		
		NamedList<Object> terms = null;
		NamedList<Object> list = response.getResponse();
		
		for (int i=0; i < list.size(); i++) {
			
			String fieldName = list.getName(i);
			if (fieldName.equals("terms")) terms = (NamedList<Object>) list.getVal(i);
			
		}

		return terms;
		
	}

	/**
	 * This helper method supports the term suggest 
	 * mechanism of the AMES search functionality
	 * 
	 * @param field
	 * @param terms
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private JSONArray getJTermValues(String field, NamedList<Object> terms) throws Exception {
		
		JSONArray jTerms = new JSONArray();
		
		NamedList<Object> items = null;
		int i = 0;
		
		for (i=0; i < terms.size(); i++) {
			String fieldName = terms.getName(i);
			if (fieldName.equals(field)) items = (NamedList<Object>)terms.getVal(i);
		}
		
		if (items == null) return jTerms;

		for (i=0; i < items.size(); i++) {
			
			String name  = items.getName(i);
			Integer card = (Integer)items.getVal(i);
			
			JSONObject jTerm = new JSONObject();
			
			jTerm.put("name", name);
			jTerm.put("card", card);
			
			jTerms.put(jTerm);
			
		}
		
		return jTerms;
	}

}
