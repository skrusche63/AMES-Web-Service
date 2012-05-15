package de.kp.ames.web;
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

public class GlobalConstants {

	/*
	 * Endpoints
	 */
	public static String FEDREP_ENDPOINT = "ames.fedrep.endpoint";
	public static String REGREP_ENDPOINT = "ames.regrep.endpoint";

	public static String SEARCH_ENDPOINT = "ames.search.endpoint";
	
	/* 
	 * HTTP response parameters
	 */
	public static String UTF_8 = "UTF-8";

	/*
	 * Method attributes
	 */
	public static String ATTR_ALIAS   = "ames.attr.alias";
	public static String ATTR_KEYPASS = "ames.attr.keypass";
	public static String ATTR_REQUEST = "ames.attr.request";
	public static String ATTR_SERVICE = "ames.attr.service";
	public static String ATTR_START   = "ames.attr.start";
	public static String ATTR_LIMIT   = "ames.attr.limit";
	public static String ATTR_URI     = "ames.attr.uri";
	
	/*
	 * Prefixes
	 */
	public static String BASE_PRE = "ames.prefix";	

	/*
	 * Search parameters
	 */
	
	public static String TERMS_FIELD = "ames.search.terms.field";
	public static String TERMS_LIMIT = "ames.search.terms.limit";
	
	/*
	 * Source code discovery
	 */
	public static String GIT_HOME = "ames.git.home";
	
}
