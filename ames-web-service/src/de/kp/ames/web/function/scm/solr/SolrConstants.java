package de.kp.ames.web.function.scm.solr;
/**
 * Copyright 2012 Dr. Krusche & Partner PartG. All rights reserved
 *
 * This file is part of the AMES-Semantic Project.
 *   
 */

public class SolrConstants {

	// Solr field definitions
	public static final String HYPERNYM_FIELD 	= "hypernym_kpf";
	public static final String SYNONYM_FIELD  	= "synonym_kpf";

	public static final String DESC_FIELD 		= "desc_kpf";
	public static final String WORD_FIELD 		= "word_kpg";
	public static final String TITLE_FIELD 		= "title";
	public static final String NAME_FIELD 		= "name";
	
	// SCM specific fields
	public static final String SOURCE_FIELD 	= "source_kps";
	public static final String EXTURI_FIELD 	= "exturi_kps";

	public static final String CATEGORY_FIELD 	= "cat";

	// value definitions
	public static final String CATEGORY_SUGGEST_WN_VALUE 	= "sgwn"; // suggest SCM
	public static final String CATEGORY_SUGGEST_SCM_VALUE 	= "sgscm"; // suggest wordnet
	public static final String CATEGORY_RESULT_WN_VALUE 	= "rwp";   // result wikipedia  
	public static final String CATEGORY_RESULT_SCM_VALUE 	= "scm";   // result SCM as indexed by ScmAnalyzer

}
