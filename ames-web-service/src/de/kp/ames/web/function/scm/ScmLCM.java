package de.kp.ames.web.function.scm;

/**
 * Copyright 2012 Dr. Krusche & Partner PartG. All rights reserved
 *
 * This file is part of the AMES-Semantic Project.
 *   
 */

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.solr.common.SolrInputDocument;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.kp.ames.web.core.search.SolrProxy;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.scm.data.ScmVocabLoader;
import de.kp.ames.web.function.scm.solr.SolrConstants;

public class ScmLCM {

	protected static JSONArray jVocab;
	protected static Boolean initialized = false;

	/**
	 * @throws Exception
	 */
	public static void buildSCMVocabIndex() throws Exception {

		try {

			/*
			 * Load control information
			 */
			InputStream fis = ScmVocabLoader.load();

			byte[] bytes = FileUtil.getByteArrayFromInputStream(fis);
			jVocab = new JSONArray(new String(bytes));

			initialized = true;

			System.out.println("buildSCMVocabIndex loaded: " + jVocab.length());

			buildSCMVocabIndex(jVocab);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * This method creates a SCM Vocabulary based search index entry, that is
	 * based on the WSF Vocabulary provided as a JSON file
	 * 
	 * @param synonyms
	 */
	private static void buildSCMVocabIndex(JSONArray jVocab) throws Exception {

		// purge previous SCM Vocabulary
		SolrProxy.getInstance().delete(SolrConstants.CATEGORY_FIELD + ":" + SolrConstants.CATEGORY_SUGGEST_SCM_VALUE);

		Collection<SolrInputDocument> documents = new ArrayList<SolrInputDocument>();

		for (int i = 0; i < jVocab.length(); i++) {
			JSONObject jTerm = jVocab.getJSONObject(i);

			SolrInputDocument document = new SolrInputDocument();

			/*
			 * category
			 */
			// SUGGEST SCM
			document.addField(SolrConstants.CATEGORY_FIELD, SolrConstants.CATEGORY_SUGGEST_SCM_VALUE);

			/*
			 * Unique Identifier build of POS and SYNSET-ID PartOfSpeach [1-4] :
			 * Synset-id [0-9]{8}: Number of word [0-9]+
			 */
			document.addField("id", jTerm.get("id"));

			/*
			 * Word
			 */
			// document.addField(SolrConstants.WORD_FIELD,
			// beautify(word.getLemma()));
			document.addField("textquery", jTerm.get("term"));

			/*
			 * Description
			 */
			document.addField(SolrConstants.DESC_FIELD, "");

			/*
			 * Synonyms / related terms
			 */
			document.addField(SolrConstants.SYNONYM_FIELD, joinRelatedTerms(jTerm.getJSONArray("related"), ", "));

			System.out.println("related: <" + joinRelatedTerms(jTerm.getJSONArray("related"), ", ") + ">");

			//if (i == 5) #debug
			//	break;

			/*
			 * Hypernym / broader term
			 */
			document.addField(SolrConstants.HYPERNYM_FIELD, jTerm.get("broader"));

			documents.add(document);

		}

		System.out.println("\ndoc count: " + documents.size());

		SolrProxy.getInstance().createIndexEntries(documents);

	}

	private static String joinRelatedTerms(JSONArray jRelated, String separator) {
		StringBuilder builder = new StringBuilder();
		try {

			for (int i = 0; i < jRelated.length(); i++) {
				if (i + 1 < jRelated.length())
					builder.append(jRelated.getString(i)).append(separator);
				else
					builder.append(jRelated.getString(i));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return builder.toString();

	}
}
