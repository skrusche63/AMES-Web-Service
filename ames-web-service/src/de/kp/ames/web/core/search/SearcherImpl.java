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

import java.util.Iterator;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.TermsParams;
import org.apache.solr.common.util.NamedList;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.format.json.StringCollector;
import de.kp.ames.web.core.render.GuiFactory;
import de.kp.ames.web.core.render.GuiRenderer;
import de.kp.ames.web.function.FncMessages;

public class SearcherImpl implements Searcher {

	/*
	 * Reference to SolrProxy
	 */
	private SolrProxy solrProxy;
	
	/*
	 * Reference to GUI renderer
	 */
	private GuiRenderer renderer;
	
	public SearcherImpl() {
		solrProxy = SolrProxy.getInstance();
		/*
		 * initialize renderer
		 */
		renderer = GuiFactory.getInstance().getRenderer();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.search.Searcher#facet()
	 */
	public String facet() throws Exception {
		
		/*
		 * Create query
		 */		
		SolrQuery query = new SolrQuery();
		query.setRows(0);
		
		/*
		 * A single facet field is supported
		 */
		query.addFacetField(SearchConstants.J_FACET);					
		query.setQuery("*");

		/*
		 * Retrieve facets from Apache Solr
		 */
		QueryResponse response = solrProxy.executeQuery(query);
		FacetField facet = response.getFacetField(SearchConstants.J_FACET);

		/*
		 * Evaluate response
		 */
		if (facet == null) return new JSONArray().toString();
		
		/*
		 * Sort search result
		 */
		StringCollector collector = new StringCollector();

		List<Count> values = facet.getValues();
		if (values == null) return new JSONArray().toString();

		for (int i=0; i < values.size(); i++) {
			
			Count count = values.get(i);
			String name = facet.getName();
			
			JSONObject jCount = new JSONObject();				

			jCount.put(SearchConstants.J_COUNT, count.getCount());
			
			jCount.put(SearchConstants.J_FIELD, facet.getName());
			jCount.put(SearchConstants.J_VALUE, count.getName());
			
			collector.put(name, jCount);
			
		}
		
		JSONArray jArray = new JSONArray(collector.values());
		return jArray.toString();

	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.search.Searcher#search(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String search(String request, String start, String limit) throws Exception {
		
		/*
		 * Build Apache Solr query
		 */
		JSONObject jQuery = new JSONObject(request);
		String term = jQuery.has(SearchConstants.J_TERM) ? jQuery.getString(SearchConstants.J_TERM) : "*";
		
		String fp = setFacets(jQuery);		
		SolrQuery query = new SolrQuery();
			
		if (fp != null) query.addFilterQuery(fp);		
		
		/* 
		 * Paging support from Apache Solr
		 */
		int s = Integer.valueOf(start);
		int r = Integer.valueOf(limit);
		
		query.setStart(s);
		query.setRows(r);
		
		String qs = term + " OR " + SearchConstants.TERMS_FIELD + ":" + term;
		query.setQuery(qs);
		
		QueryResponse response = solrProxy.executeQuery(query);
		SolrDocumentList docs = response.getResults();
		
		long total = docs.getNumFound();	
		
		/*
		 * Sort search result
		 */
		StringCollector collector = new StringCollector();

		Iterator<SolrDocument> iter = docs.iterator();
		while (iter.hasNext()) {
			
			int pos = -1;
			
			SolrDocument doc = iter.next();
			JSONObject jDoc = new JSONObject();
			
			/* 
			 * Identifier
			 */
			String id  = (String)doc.getFieldValue(SearchConstants.S_ID);
			jDoc.put(SearchConstants.J_ID, id);
			
			/* 
			 * Name
			 */
			String name  = (String)doc.getFieldValue(SearchConstants.S_NAME);
			jDoc.put(SearchConstants.J_NAME, name);

			/* 
			 * Source
			 */
			String source = (String)doc.getFieldValue(SearchConstants.S_FACET);
			pos = source.lastIndexOf(":");
			
			jDoc.put(SearchConstants.J_FACET, source.substring(pos+1));
			
			/* 
			 * Description
			 */
			String desc  = (String)doc.getFieldValue(SearchConstants.S_DESC);
			desc = (desc == null) ? FncMessages.NO_DESCRIPTION_DESC : desc;

			jDoc.put(SearchConstants.J_DESC, desc);
			collector.put(name, jDoc);

		}

		/*
		 * Render result
		 */
		JSONArray jArray = new JSONArray(collector.values());
		return renderer.createGrid(jArray, total);
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.search.Searcher#suggest(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String suggest(String request, String start, String limit) throws Exception {

		/*
		 * Retrieve terms
		 */
		SolrQuery query = new SolrQuery();

		query.setParam(CommonParams.QT, "/terms");
	    query.setParam(TermsParams.TERMS, true);

	    query.setParam(TermsParams.TERMS_LIMIT, SearchConstants.TERMS_LIMIT);
	    
	    query.setParam(TermsParams.TERMS_FIELD, SearchConstants.TERMS_FIELD);
	    query.setParam(TermsParams.TERMS_PREFIX_STR, request);
		
		QueryResponse response = solrProxy.executeQuery(query);
		NamedList<Object> terms = getTerms(response);
		
		JSONArray jTerms = getTermValues(SearchConstants.TERMS_FIELD, terms);
		
		/*
		 * Render result
		 */
		return renderer.createGrid(jTerms);

	}
	/**
	 * A helper method to retrieve requested
	 * facets from a JSON-based query object
	 * 
	 * @param jQuery
	 * @return
	 * @throws Exception
	 */
	protected String setFacets(JSONObject jQuery) throws Exception {
		
		String fp = null;
		if (jQuery.has(SearchConstants.J_FACET)) {
			
			/* 
			 * Externally selected facets are mapped
			 * onto a filter query 
			 */
			
			fp = "";
			
			JSONArray jFacets = jQuery.getJSONArray(SearchConstants.J_FACET);
			for (int i=0; i < jFacets.length(); i++) {
				
				JSONObject jFacet = jFacets.getJSONObject(i);
				
				String field = jFacet.getString(SearchConstants.J_FIELD);
				String value = jFacet.getString(SearchConstants.J_VALUE);
				
				fp += "+" + field + ":\"" + value + "\"";
				
			}

		}

		return fp;
	}
	
	
	/**
	 * A helper method to support the term suggest
	 * mechanism of the AMES search functionality
	 * 
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected NamedList<Object> getTerms(QueryResponse response) {
		
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
	protected JSONArray getTermValues(String field, NamedList<Object> terms) throws Exception {
		
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
			
			jTerm.put(SearchConstants.J_NAME, name);
			jTerm.put(SearchConstants.J_CARD, card);
			
			jTerms.put(jTerm);
			
		}
		
		return jTerms;
	}

}
