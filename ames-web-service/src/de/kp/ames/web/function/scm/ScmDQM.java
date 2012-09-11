package de.kp.ames.web.function.scm;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.json.JSONArray;
import org.json.JSONObject;

import com.googlecode.jatl.Html;

import de.kp.ames.web.Bundle;
import de.kp.ames.web.core.render.ScConstants;
import de.kp.ames.web.core.search.SolrProxy;
import de.kp.ames.web.core.util.DateUtil;
import de.kp.ames.web.core.util.LRUCache;
import de.kp.ames.web.core.util.ZipUtil;
import de.kp.ames.web.function.scm.model.ResultObject;
import de.kp.ames.web.function.scm.model.SuggestObject;
import de.kp.ames.web.function.scm.renderer.ScmHtmlRenderer;
import de.kp.ames.web.function.scm.solr.SolrConstants;

public class ScmDQM {
	
	private static LRUCache<String, Integer> suggestionLRUCache = new LRUCache<String, Integer>(50, 100);
	/*
	 * Reference to SolrProxy
	 */
	private SolrProxy solrProxy;
	private Integer MAX_SIMILARITY_LEVEL = 3;
	private Integer MAX_SIMILARITY_LEAVES = 4;

	private static Bundle bundle = Bundle.getInstance();
	
	public ScmDQM() {
		solrProxy = SolrProxy.getInstance();

	}

	
	/**
	 * Process a Java-Module ZIP file representation of the cart
	 * 
	 * @param jCheckout
	 * @return
	 * @throws Exception 
	 * @throws Exception
	 */
	public byte[] download(JSONArray jCheckout) throws Exception {

    	System.out.println("====> SCMSearcher.download");

		// generate HTML representation as readme
		String semanticResearchReport = generateCheckoutHtml(jCheckout, true);
		
		List<String> ids = new ArrayList<String>();
		for (int i = 0; i < jCheckout.length(); i++) {
			JSONObject record = jCheckout.getJSONObject(i);
			ids.add(record.getString("id"));
		} 
		
		List<String> files = getAbsoluteFilenamesFromIds(ids);
		byte[] zip = ZipUtil.zipFiles(bundle.getString("ames.scm.root"), files, semanticResearchReport);

    	System.out.println("====> SCMSearcher.download.zipFiles packed");

		return zip;
	}

	@SuppressWarnings({ "unchecked" })
	private List<String> getAbsoluteFilenamesFromIds(List<String> ids) throws Exception {
		QueryResponse response = queryDownloadCase(ids);
		SolrDocumentList docs = response.getResults();
		
		Iterator<SolrDocument> iter = docs.iterator();
		List<String> files = new ArrayList<String>();
		while (iter.hasNext()) {

			SolrDocument doc = iter.next();
			files.add(((ArrayList<String>) doc.getFieldValue(SolrConstants.EXTURI_FIELD)).get(0));
		}
		
    	System.out.println("======> SCMSearcher.download.getAbsoluteFilenamesFormIds: " + files.size());

		return files;
	}

	/**
	 * Process a server-side HTML representation of the cart
	 * 
	 * @param jCheckout
	 * @return
	 * @throws Exception
	 */
	public String checkout(JSONArray jCheckout) throws Exception {
		
		System.out.println("====> SCMSearcher.checkout: count: " + jCheckout.length());

		String response = generateCheckoutHtml(jCheckout, false);
	
		return createCheckout(response);
	}


	private Map<String, ResultObject> getDocsFromIds(List<String> ids) throws Exception {
		Map<String, ResultObject>  resultDocMap = new HashMap<String, ResultObject>();
		
		QueryResponse response = queryDownloadCase(ids);
		SolrDocumentList docs = response.getResults();
		
		Iterator<SolrDocument> iter = docs.iterator();
		while (iter.hasNext()) {

			SolrDocument doc = iter.next();
			ResultObject scm = new ResultObject(doc);
			resultDocMap.put(scm.getId(), scm);
		}
		

		return resultDocMap;
	}
	
	public String generateCheckoutHtml(final JSONArray jCheckout, final boolean detailed) throws Exception {
		
		final Map<String, ArrayList<String>> suggestionResultMap = new HashMap<String, ArrayList<String>>();
		Set<String>  resultDocSet = new HashSet<String>();
		
		for (int i = 0; i < jCheckout.length(); i++) {
			
			JSONObject record = jCheckout.getJSONObject(i);
			
			String suggestion = record.getString("suggest");
			String resultId = record.getString("id");
			
			if (!suggestionResultMap.containsKey(suggestion))
				suggestionResultMap.put(suggestion, new ArrayList<String>());

			suggestionResultMap.get(suggestion).add(resultId);
			resultDocSet.add(resultId);
		}
		
		final Map<String, ResultObject>  resultDocLookupMap = getDocsFromIds(new ArrayList<String>(resultDocSet));
		
		StringWriter htmlWriter;
		htmlWriter = new StringWriter();
		new Html(htmlWriter) {{
			html();
				body();
					if (detailed) h1().text("Semantic Source Code Catalog").end();
					h2().text("Research Session Protocol").end();
					h4()
						.text("Generated session report from " + DateUtil.createTimeStamp("dd MMMMM yyyy HH:mm"))
					.end();
					
					p()
						.text("This report will list all your ADF modules of interest, " +
								"structured by the semantic context of the suggestion. " +
								"You can download this report and all its Java modules " +
								"to your disk by the yellow \"compress-icon\" button in the window headline.")
					.end();
					
					h3()
						.text("Summary")
					.end();
					p()
						.text("This report contains:")
					.end();
					int suggestionCount = suggestionResultMap.keySet().size();
					int moduleCount = resultDocLookupMap.keySet().size();
					ul()
						.li().text(suggestionCount + " suggestion" + (suggestionCount==1 ? "" : "s")).end()
						.li().text(moduleCount + " ADF module"  + (moduleCount==1 ? "" : "s")).end()
					.end();
					h3()
						.text("List of modules structured by semantic context")
					.end();
					makeModuleList();
			endAll();
			done();
		}
			Html makeModuleList() throws Exception {
	             ul().classAttr("sgc-suggest");
	             for (Entry<String, ArrayList<String>> suggestionEntry : suggestionResultMap.entrySet()) {
	            	 String suggestion = suggestionEntry.getKey();
	            	 ArrayList<String> resultIds = suggestionEntry.getValue();
	                 li()
	                 	.div().classAttr("sgc-suggest-item")
	                 		.raw(suggestion.replace("(", "(selected within context: "))
	                 	.end();
		                 ul().classAttr("sgc-result");
		                 	for (String resultId : resultIds) {
		                 		ResultObject scm = resultDocLookupMap.get(resultId);
								li()
									.div().classAttr("sgc-result-entry")
										.text(scm.getName() + " (" + scm.getPackage() + ")")
									.end()
									.raw(ScmHtmlRenderer.getResultHtmlDescription(scm, true))
								.end();
							}
		                 end();

				}
	             return end();
	         }
		};
		
		return htmlWriter.getBuffer().toString();
	}

	
	/**
	 * Query is uid from focused record
	 * This query processes a recursive query till max level is reached
	 * and responds with a JSONObject compatible with thejit HyperTree  
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public String similar(String uid, String rootName) throws Exception {

		return hypertree(uid, rootName);
	}

	public String result(String searchTerm, String start, String end) throws Exception {

		/*
		 * Paging support from Apache Solr
		 */
		int s = Integer.valueOf(start);
		int e = Integer.valueOf(end);
		int r = e - s;

		QueryResponse response = querySearchCase(searchTerm, s, r);
		
		SolrDocumentList docs = response.getResults();
		long total = docs.getNumFound();

		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

		/*
		 * Iterate search result and create JSON search result 
		 */
		JSONArray jArray = new JSONArray();

		Iterator<SolrDocument> iter = docs.iterator();
		while (iter.hasNext()) {

			SolrDocument doc = iter.next();
			ResultObject scm = new ResultObject(doc, highlighting);

			JSONObject jDoc = new JSONObject();

			/*
			 * Identifier
			 */
			
			String id = scm.getId();
			jDoc.put("id", id);

			/*
			 * Title (multivalue field)
			 */
			String title = scm.getName() + " (" + scm.getPackage() + ")";

			jDoc.put("title", title);
			jDoc.put("name", scm.getName());
			jDoc.put("source", scm.getSource());
			
			/*
			 * send back flag for LoC metric 
			 */
			String flag = ScmHtmlRenderer.getFlagForMetric("loc", scm.getMetricLOC()).getString("flag_name");
			jDoc.put("icon", flag);

			/*
			 * result field (includes teaser: highlightedDescription)
			 */
			
			jDoc.put("result", ScmHtmlRenderer.getResultHtml(scm));
			
			/*
			 * detail "desc" field
			 */
			jDoc.put("desc", ScmHtmlRenderer.getResultHtmlDescription(scm, false));

			jArray.put(jDoc);
		}

		/*
		 * Render result
		 */
		// return jArray.toString();
		return createGrid(jArray, start, end, String.valueOf(total));

	}



	public String suggest(String prefix, String start, String end) throws Exception {


		/*
		 * Paging support from Apache Solr
		 */
		int s = Integer.valueOf(start);
		int e = Integer.valueOf(end);
		int r = e - s;

		QueryResponse response = querySuggestCase(prefix, s, r);

		SolrDocumentList docs = response.getResults();
		long total = docs.getNumFound();

		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

		/*
		 * Sort search result
		 */
		JSONArray jArray = new JSONArray();

		Iterator<SolrDocument> iter = docs.iterator();
		while (iter.hasNext()) {

			SolrDocument doc = iter.next();
			SuggestObject scm = new SuggestObject(doc, highlighting);

			JSONObject jDoc = new JSONObject();

			/*
			 * Identifier
			 */
			String id = scm.getId();
			jDoc.put("id", id);

			/*
			 * term field
			 */
			jDoc.put("term", scm.getTextSuggest());

			/*
			 * HTML rendered Result field
			 */
			String synonyms = scm.getSynonyms();
			jDoc.put("result", ScmHtmlRenderer.getSuggestHtmlResult(scm));
			
			//System.out.println("====> SCM.suggest:result HTML: " + jDoc.get("result") );

			/*
			 * Hypernym
			 */
			String hypernym = scm.getHypernym();
			jDoc.put("hypernym", hypernym);

			/*
			 * Query String for selection
			 */
			// String queryString = (String)
			// doc.getFieldValue(SolrConstants.WORD_FIELD);
			/*
			 * All terms are quoted as phrases and combined with an open OR
			 * query
			 * 
			 * Main term is mandatory and weighted 50 +queryString^20 Synonyms
			 * is optional and weighted 10 Hypernym is optional and weighted 5
			 */
			String synonymBoosts = "\"" + synonyms.replace(", ", "\"^10 OR \"") + "\"^10";
			
			String queryString = scm.getTextSuggest();
			jDoc.put("qs", "+\"" + queryString + "\"^100 OR " + "\"" + hypernym + "\"^5 OR " + synonymBoosts);
			// raw query string for TextWidget update
			jDoc.put("qsraw", queryString);

			jDoc.put("type", "suggest");

			jArray.put(jDoc);

		}

		/*
		 * group entries by hypernym
		 */
		ArrayList<String> groupHeaders = new ArrayList<String>();
		ArrayList<ArrayList<JSONObject>> groupedList = new ArrayList<ArrayList<JSONObject>>();
		for (int i = 0; i < jArray.length(); i++) {
			JSONObject jDoc = (JSONObject) jArray.get(i);
			String hypernym = jDoc.getString("hypernym");

			if (groupHeaders.contains(hypernym)) {
				// add doc to groupedList with index number as hypernym in
				// groupHeaders
				int index = groupHeaders.indexOf(hypernym);
				groupedList.get(index).add(jDoc);
			} else {
				// add new group header
				groupHeaders.add(hypernym);
				//System.out.println("====> SCM.suggest: new group: <" + hypernym+ ">");

				// add new empty list
				groupedList.add(new ArrayList<JSONObject>());
				// add doc to last new empty list
				ArrayList<JSONObject> lastList = groupedList.get(groupedList.size() - 1);
				JSONObject jGroupHeaderDoc = new JSONObject();
				// generate unique key from first doc with g: prefix
				jGroupHeaderDoc.put("id", "g:" + jDoc.getString("id"));
				
				jGroupHeaderDoc.put("result", ScmHtmlRenderer.getSuggestHtmlGroupHeader(jDoc));


				jGroupHeaderDoc.put("type", "group");
				jGroupHeaderDoc.put("enabled", false);
				jGroupHeaderDoc.put("qsraw", hypernym);
				// add pseudo group entry
				lastList.add(jGroupHeaderDoc);

				// add doc after pseudo group entry
				lastList.add(jDoc);
			}
		}

		/*
		 * flatten result list
		 */
		JSONArray jGroupedJArray = new JSONArray();
		// depth first
		for (ArrayList<JSONObject> groupList : groupedList) {

			JSONObject jGroupHeader = groupList.get(0);
			
			jGroupHeader.put(
					"result",
					jGroupHeader.getString("result")
					);

			
			for (JSONObject doc : groupList) {
				jGroupedJArray.put(doc);
			}
		}

		/*
		 * increase total count with additional group-headers count
		 */
		System.out.println("====> SCM.suggest: term: <" + prefix + "> s/e: " + s + "/" + e +" total: " + total + " groups: " + groupHeaders.size());
		if (suggestionLRUCache.containsKey(prefix)) {
//			if (groupHeaders.size() == 0) {
//				// no additional headers
//				total = suggestionLRUCache.get(prefix);
//			} else {
//				// paging search will add additional headers 
//				total = suggestionLRUCache.get(prefix) + groupHeaders.size();
//				suggestionLRUCache.put(prefix, (int) total);
//			}
			total = suggestionLRUCache.get(prefix);
				
		} else {
			total = total + groupHeaders.size();
			suggestionLRUCache.put(prefix, (int) total);
		}
		System.out.println("======> SCM.suggest: LRU total: " + total);

		/*
		 * Render result
		 */
		// return jGroupedJArray.toString();
		// with group headers
		return createGrid(jGroupedJArray, start, end, String.valueOf(total));

		// without group headers
		// return createGrid(jArray, start, end, String.valueOf(total));

	}





	public String createGrid(JSONArray jArray, String start, String end, String total) throws Exception {

		JSONObject jResponse = new JSONObject();

		try {

			jResponse.put(ScConstants.SC_STATUS, 0);
			jResponse.put(ScConstants.SC_TOTALROWS, total);
			// jResponse.put(ScConstants.SC_STARTROW, start);
			// jResponse.put(ScConstants.SC_ENDROW, end);

			jResponse.put(ScConstants.SC_DATA, jArray);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
		}

		return new JSONObject().put("response", jResponse).toString();

	}
	
	public String createCheckout(String checkoutPage) throws Exception {

		JSONObject jResponse = new JSONObject();

		try {
			jResponse.put(ScConstants.SC_DATA, checkoutPage);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
		}

		return jResponse.toString();

	}

	/************************************************************************
	 * 
	 * HYPERTREE HYPERTREE HYPERTREE HYPERTREE HYPERTREE
	 * @param rootName 
	 * @throws Exception 
	 * 
	 ***********************************************************************/

	public String hypertree(String rootId, String rootName) throws Exception {
		HashMap<String, Object> rootNode = new HashMap<String, Object>();
		rootNode.put("id", rootId);
		rootNode.put("node", construct(rootId, rootName, new JSONArray()));
		rootNode.put("parentChildren", null);
		rootNode.put("level", MAX_SIMILARITY_LEVEL);
		
		Set<String> cache = new HashSet<String>();
		cache.add((String) rootId);

		Queue<HashMap<String, Object>> q = new LinkedList<HashMap<String, Object>>();
		q.offer(rootNode);
		
		bfs(q, cache);
		
		return ((JSONObject)rootNode.get("node")).toString(2);
	}
	
	public void bfs(Queue<HashMap<String, Object>> q, Set<String> cache) throws Exception {
		if (q.isEmpty()) return;
		
		HashMap<String, Object> node = q.poll();
		String nodeId = (String) node.get("id");
		int level = (Integer) node.get("level");

    	//System.out.println("====> bfs: lv: " + level + " name: " + ((JSONObject)node.get("node")).get("name"));

		if (level == 0) return;
		int directChildrenCount = 0;

		for (HashMap<String, Object> child: expand(nodeId)) {

			if (directChildrenCount == MAX_SIMILARITY_LEAVES)
				// children count is satisfied, so we can skip the additional docs
				break;
			
			if (cache.contains(child.get("id"))) {
				//System.out.println("======> bfs: skip: " + ((JSONObject)child.get("node")).get("name"));
				continue;
			}
			// remember id
			cache.add((String) child.get("id"));

			// anchor children at parent
	    	//System.out.println("======> bfs: anchor " +
	    	//		"node: " + ((JSONObject)node.get("node")).get("name") + 
	    	//		" / child: " + ((JSONObject)child.get("node")).get("name") 
	    	//		);
			((JSONArray)((JSONObject)node.get("node")).get("children") ).put(child.get("node"));
			
			child.put("level", level-1);
			child.put("parentChildren", (JSONArray)((JSONObject)node.get("node")).get("children"));
			
			
			// put on queue
			q.offer(child);
			directChildrenCount++;

		}
		
		bfs(q, cache);
	}
	
	private JSONObject construct(String nodeId, String name, JSONArray data) throws Exception {
		JSONObject jDocument = new JSONObject();
		jDocument.put("id", nodeId);
		jDocument.put("name", name);
		jDocument.put("data", data);
		
		// this 'children' parameter is a MUST
		jDocument.put("children", new JSONArray());

		return jDocument;
	}


	private List<HashMap<String, Object>> expand(String nodeId) throws Exception {
		
		List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>(); 
		

		/*
		 * Paging support from Apache Solr
		 */
		int s = 0;
		int r = 2 * MAX_SIMILARITY_LEAVES; // ask for more to fill up the duplicates

		QueryResponse response = querySimilarCase(nodeId, s, r);
		SolrDocumentList docs = response.getResults();
		
		if (docs == null) return children;

		Iterator<SolrDocument> iter = docs.iterator();

		while (iter.hasNext()) {
			SolrDocument doc = iter.next();
			ResultObject scm = new ResultObject(doc);

			HashMap<String, Object> child = new HashMap<String, Object>();
			
			String id = scm.getId();
			child.put("id", id);
			child.put("node", construct(id, 
					scm.getName(), 
					new JSONArray()
			));

			children.add(child);
			
		}
		return children;
	}

	private QueryResponse queryDownloadCase(List<String> ids) throws Exception {
		/*
		 * Build Apache Solr query
		 */
		SolrQuery query = new SolrQuery();
		/*
		 *  concatenate quoted ids to searchTerm
		 *  
		 *  +id:("id1" "id2" "id3" "id4")
		 */
		
		String searchTerm = "+id:(\"" + StringUtils.join(ids, "\" \"")+ "\")";
		query.setQuery(searchTerm);
		//query.setFields("id", "exturi_kps");
		query.setRows(ids.size());

		QueryResponse response = solrProxy.executeQuery(query);
		return response;
	}

	private QueryResponse querySimilarCase(String nodeId, int s, int r) throws Exception {
		/*
		 * Build Apache Solr query
		 */
		SolrQuery query = new SolrQuery();

		query.setStart(s);
		query.setRows(r);
		
		// choose suggest requesthandler
		query.setQueryType("/mlt");

		/*
		 *  quoted query due to colon conflicts on solr, it cannot separate
		 *  id: from following uid
		 *  &q=id:urn:de:kp:ames:scm:ADF:af613363 
		 */
		
		String searchTerm = "id:\"" + nodeId + "\"";
		query.setQuery(searchTerm);
		
		// MLT more like this parameters
		query.setParam("mlt.fl", "tags_kpg");
		query.setParam("mlt.mintf", "1");
		query.setParam("mlt.mindf", "2");
		query.setParam("mlt.match.include", "false");
		// minimize result size
		query.setFields("id", "name", "title");
		
		QueryResponse response = solrProxy.executeQuery(query);
		return response;
	}


	private QueryResponse querySearchCase(String searchTerm, int s, int r) throws Exception {
		/*
		 * Build Apache Solr query
		 */
		SolrQuery query = new SolrQuery();

		query.setStart(s);
		query.setRows(r);

		// String qs = SolrConstants.WORD_FIELD + ":" + searchTerm;
		String qs = searchTerm; // default field
		// query term weights are prepared from suggest already
		query.setQuery(qs);

		// this query settings can be moved to an own RequestHandler too
		query.setParam("defType", "edismax");
		// query field weights
		query.setParam("qf", "tags_kpg^20.0 description^0.3");
//		query.setParam("qf", "title^20.0 description^0.3");
		query.setParam("q.op", "OR");

		/*
		 * set filter for a specific set of indexed documents within Solr by
		 * category "cat" field
		 */
		query.setFilterQueries(SolrConstants.CATEGORY_FIELD + ":" +
				SolrConstants.CATEGORY_RESULT_SCM_VALUE);

		query.addHighlightField(SolrConstants.TITLE_FIELD);
		query.addHighlightField("description");
		query.setHighlight(true);

		query.setHighlightSimplePre("<span class=\"sg-th\">");
		query.setHighlightSimplePost("</span>");
		query.setHighlightSnippets(3);

		QueryResponse response = solrProxy.executeQuery(query);
		return response;
	}

	private QueryResponse querySuggestCase(String prefix, int s, int r) throws Exception {
		/*
		 * Build Apache Solr query
		 */
		SolrQuery query = new SolrQuery();

		query.setStart(s);
		query.setRows(r);

		String qs = prefix;
		query.setQuery(qs);

		// choose suggest requesthandler
		query.setQueryType("/suggest");

		/*
		 * set filter for a specific set of indexed documents within Solr by
		 * category "cat" field
		 */
		query.setFilterQueries(SolrConstants.CATEGORY_FIELD + ":" + SolrConstants.CATEGORY_SUGGEST_SCM_VALUE);

		// query.addHighlightField(SolrConstants.WORD_FIELD);
		query.addHighlightField("textsuggest");
		query.setHighlight(true);

		query.setHighlightSimplePre("<span class=\"sg-th\">");
		query.setHighlightSimplePost("</span>");

		QueryResponse response = solrProxy.executeQuery(query);
		return response;
	}
	

}
