package de.kp.ames.web.function.scm.data;
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

import java.io.InputStream;

public class ScmVocabLoader {

	private static String SCM_VOCAB_FILE = "ames_vocab.json";
	
	public static InputStream load() {
		
		Class<?> loader = ScmVocabLoader.class;
		InputStream is = loader.getResourceAsStream(SCM_VOCAB_FILE);
		
		return is;
		
	}
	
}
