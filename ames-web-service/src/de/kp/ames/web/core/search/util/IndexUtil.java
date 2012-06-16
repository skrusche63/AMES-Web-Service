package de.kp.ames.web.core.search.util;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;

import de.kp.ames.web.core.search.SearchConstants;
import de.kp.ames.web.core.search.data.SolrEntry;

public class IndexUtil {

	/**
	 * A helper method to retrieve terms from a 
	 * certain string
	 * 
	 * @param camelCase
	 * @return
	 */
	public static List<String> termAsKeywords(String camelCase) {

		int n = camelCase.trim().length();
		String separator = "-";
		
		StringBuilder sb = new StringBuilder(n * 2);
		for (int i = 0; i < n; i++) {
	    
			char c = camelCase.charAt(i);
			int x = (int) c;
	    
			// A=65, N=78, Z=90, a=97
			// See http://blossomassociates.net/ascii.html
			if (separator == null) separator = "";
	    
			if (x >= 65 && x <= 90) {
				sb.append(separator).append(c);
			} else
				sb.append(c);
		}
	 
		// Final string converted from camelCaseString
		String convertedString = sb.toString();
		if (convertedString.startsWith("-")) convertedString = convertedString.substring(1);

		convertedString = convertedString.toLowerCase();
		String[] keywords = convertedString.split(separator);

		return Arrays.asList(keywords);
		
	}
	
	/**
	 * Retrieve Solr Document from SolrEntry; this method
	 * implements a very basic search use case
	 * 
	 * @param entry
	 * @return
	 */
	public static SolrInputDocument getSolrDocument(SolrEntry entry) throws Exception {
		
		SolrInputDocument document = new SolrInputDocument();
		
		String id = entry.getId();
		document.addField(SearchConstants.S_ID, id);
		
		String name = entry.getName();
		document.addField(SearchConstants.S_NAME, name);
		
		String desc = entry.getDescription();
		document.addField(SearchConstants.S_DESC, desc);

		Collection<String> facets = entry.getFacets();
		document.addField(SearchConstants.S_FACET, facets);
		
		Collection<?> terms = entry.getTerms();
		if (terms.size() > 0) document.addField(SearchConstants.TERMS_FIELD, terms);

		return document;

	}
}
