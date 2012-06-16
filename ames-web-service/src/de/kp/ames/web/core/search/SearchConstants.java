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

import de.kp.ames.web.Bundle;
import de.kp.ames.web.GlobalConstants;

public class SearchConstants {

	/*
	 * JSON parameters
	 */
	public static String J_CARD   = "card";
	public static String J_COUNT  = "count";
	public static String J_DESC   = "desc";
	public static String J_FACET  = "facet";
	public static String J_FIELD  = "field";
	public static String J_ID     = "id";
	public static String J_NAME   = "name";
	public static String J_SOURCE = "source";
	public static String J_TERM   = "term";
	public static String J_VALUE  = "value";
	
	/*
	 * Apache Solr parameters
	 */
	public static String S_DESC   = "description";
	public static String S_FACET  = "facet";
	public static String S_ID     = "id";
	public static String S_NAME   = "name";
		
	/*
	 * Term suggest parameters
	 */
	public static String TERMS_FIELD;
	public static String TERMS_LIMIT;
	
	/*
	 * Search methods
	 */
	public static String METH_FACET   = "facet";
	public static String METH_SEARCH  = "search";
	public static String METH_SUGGEST = "suggest";
	
	/*
	 * Search method attributes
	 */
	public static String ATTR_LIMIT;
	public static String ATTR_QUERY;
	public static String ATTR_START;
	public static String ATTR_TYPE;

	/*
	 * initialize constants
	 */
	static {
		
		Bundle bundle = Bundle.getInstance();
		
		/*
		 * Search method attributes
		 */
		ATTR_LIMIT   = bundle.getString(GlobalConstants.ATTR_LIMIT);
		ATTR_QUERY   = bundle.getString(GlobalConstants.ATTR_QUERY);
		ATTR_START   = bundle.getString(GlobalConstants.ATTR_START);
		ATTR_TYPE    = bundle.getString(GlobalConstants.ATTR_TYPE);
	
		/*
		 * Search parameters
		 */
		TERMS_FIELD = bundle.getString(GlobalConstants.TERMS_FIELD);
		TERMS_LIMIT = bundle.getString(GlobalConstants.TERMS_LIMIT);
		
	}

}
