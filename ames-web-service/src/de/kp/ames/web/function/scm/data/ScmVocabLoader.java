package de.kp.ames.web.function.scm.data;
/**
 * Copyright 2012 Dr. Krusche & Partner PartG. All rights reserved
 *
 * This file is part of the AMES-Semantic Project.
 *   
 */

import java.io.InputStream;

public class ScmVocabLoader {

	private static String SCM_VOCAB_FILE = "ames_vocab.json";
	
	public static InputStream load() {
		
		Class<?> loader = ScmVocabLoader.class;
		InputStream is = loader.getResourceAsStream(SCM_VOCAB_FILE);
		
		return is;
		
	}
	
}
