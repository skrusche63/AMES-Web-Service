package de.kp.ames.web.function.scm.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrDocument;

import de.kp.ames.web.function.scm.solr.SolrConstants;

public class ResultObject {
	private SolrDocument doc;
	private Map<String, Map<String, List<String>>> highlighting;

	public ResultObject(SolrDocument doc) {
		this(doc, null);
	}

	public ResultObject(SolrDocument doc, Map<String, Map<String, List<String>>> highlighting) {
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

	/*
	 * methodname is a multivalue field, only one value expected
	 */
	@SuppressWarnings("unchecked")
	public List<String> getMethodNames() {
		
		List<String> methodNames = (ArrayList<String>) doc.getFieldValue("meth_kpg");
		List<String> methodNamesFiltered = new ArrayList<String>();
		
		for (String methodName : methodNames) {
			if (!(methodName.equals(getName())))
				methodNamesFiltered.add(methodName);
		}
		Collections.sort(methodNamesFiltered);
		
		return methodNamesFiltered;
	}

	public String getDescription() {
		return (String) doc.getFieldValue("description");
	}

	@SuppressWarnings("unchecked")
	public String getSource() {
		return ((ArrayList<String>) doc.getFieldValue(SolrConstants.SOURCE_FIELD)).get(0);
	}

	public String getHighlightTitle() {
		String highlighted = null;
		if (!(highlighting==null) && highlighting.get(getId()).containsKey(SolrConstants.TITLE_FIELD)) {
			highlighted = highlighting.get(getId()).get(SolrConstants.TITLE_FIELD).get(0);
		} else {
			highlighted = getTitle();
		}
		return highlighted;
	}

	/*
	 * Multiple teaser support with elipses
	 */
	public String getHighlightDescription() {
		String highlighted = "";
		if (!(highlighting==null) && highlighting.get(getId()).containsKey("description")) {
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

	/*
	 * construct package from FQDN title and substract name
	 */
	public String getPackage() {
		String title = getTitle();
		return title.substring(0, title.length() - getName().length() - 1);
	}

	@SuppressWarnings("unchecked")
	public String getMetricBacklinks() {
		return ((ArrayList<String>) doc.getFieldValue("mback_kps")).get(0);
	}

	@SuppressWarnings("unchecked")
	public String getMetricMethodCount() {
		return ((ArrayList<String>) doc.getFieldValue("mmthd_kps")).get(0);
	}

	@SuppressWarnings("unchecked")
	public String getMetricLOC() {
		return ((ArrayList<String>) doc.getFieldValue("mloc_kps")).get(0);
	}

	@SuppressWarnings("unchecked")
	public String getAnnotations() {
		String result = StringUtils.join((ArrayList<String>)doc.getFieldValue("tags_kpg"), ", ");
		if (result.startsWith(", ")) result = result.substring(2);
		return result;
	}

	
}
