package de.kp.ames.web.function.scm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrDocument;

import de.kp.ames.web.function.scm.solr.SolrConstants;

public class SuggestObject {

	private SolrDocument doc;
	private Map<String, Map<String, List<String>>> highlighting;

	public SuggestObject(SolrDocument doc) {
		this(doc, null);
	}

	
	public SuggestObject(SolrDocument doc, Map<String, Map<String, List<String>>> highlighting) {
		this.doc = doc;
		this.highlighting = highlighting;
	}

	
	
	public String getId() {
		return (String) doc.getFieldValue("id");
	}

	public String getName() {
		return (String) doc.getFieldValue(SolrConstants.NAME_FIELD);
	}

	/*
	 * title is a multivalue field, only one value expected
	 */
	@SuppressWarnings("unchecked")
	public String getTitle() {
		return ((ArrayList<String>) doc.getFieldValue(SolrConstants.TITLE_FIELD)).get(0);
	}

	public String getDescription() {
		return (String) doc.getFieldValue("description");
	}

	public String getTextSuggest() {
		return (String) doc.getFieldValue("textsuggest");
	}

	public String getSynonyms() {
		return (String) doc.getFieldValue(SolrConstants.SYNONYM_FIELD);
	}
	public String getHypernym() {
		return (String) doc.getFieldValue(SolrConstants.HYPERNYM_FIELD);
	}
	
	
	public String getHighlightTextSuggest() {
		String highlighted = null;
		if (highlighting.get(getId()).containsKey("textsuggest")) {
			highlighted = highlighting.get(getId()).get("textsuggest").get(0);
		} else {
			highlighted = getTextSuggest();
		}
		return highlighted;
	}

	/*
	 * Multiple teaser support with elipses
	 */
	public String getHighlightDescription() {
		String highlighted = "";
		if (highlighting.get(getId()).containsKey("description")) {
			ArrayList<String> hls = (ArrayList<String>) highlighting.get(getId()).get("description");
			for (String hl : hls) {
				highlighted += " ... " + hl;
			}
			highlighted += " ...";
		} else {
			highlighted = getDescription();
		}
		
		return highlighted;
	}

}
