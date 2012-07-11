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
	 * Method constants
	 */
	public static String ATTR_LIMIT;
	public static String ATTR_START;

	/*
	 * initialize constants
	 */
	static {
		
		Bundle bundle = Bundle.getInstance();
		
		/*
		 * Search method attributes
		 */
		ATTR_LIMIT   = bundle.getString(GlobalConstants.ATTR_LIMIT);
		ATTR_START   = bundle.getString(GlobalConstants.ATTR_START);
	
		/*
		 * Search parameters
		 */
		TERMS_FIELD = bundle.getString(GlobalConstants.TERMS_FIELD);
		TERMS_LIMIT = bundle.getString(GlobalConstants.TERMS_LIMIT);
		
	}

}
