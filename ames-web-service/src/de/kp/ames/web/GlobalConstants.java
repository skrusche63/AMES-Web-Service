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
	 * Buffer size
	 */	
	public static final int BUFFER_SIZE = 1024;

	/*
	 * Default Gui Renderer
	 */
	public static String GUI_RENDERER = "ames.gui.renderer";
	
	/*
	 * Endpoints
	 */
	public static String FEDREP_ENDPOINT = "ames.fedrep.endpoint";
	public static String REGREP_ENDPOINT = "ames.regrep.endpoint";

	public static String RSS_ENDPOINT    = "ames.rss.endpoint";
	public static String SEARCH_ENDPOINT = "ames.search.endpoint";
	
	/*
	 * RSS Feed parameters
	 */
	public static String FEED_TITLE = "AMES Concepts";
	public static String FEED_DESC  = "Continuous AMES results";

	/* 
	 * HTTP response parameters
	 */
	public static String UTF_8 = "UTF-8";

	/*
	 * Mimetypes
	 */
	public static String MT_JSON      = "application/json";
	public static String MT_HTML      = "text/html";
	public static String MT_MULTIPART = "multipart/*";
	public static String MT_PDF       = "application/pdf";
	public static String MT_PNG       = "image/png";
	public static String MT_RSS       = "application/rss+xml";
	public static String MT_TEXT      = "text/plain";
	public static String MT_WMS       = "application/vnd.ogc.wms_xml";
	public static String MT_XML       = "text/xml";
	
	/*
	 * Method attributes
	 */
	public static String ATTR_AFFILIATE = "ames.attr.affiliate";
	public static String ATTR_ALIAS   	= "ames.attr.alias";
	public static String ATTR_COMMUNITY = "ames.attr.community";
	public static String ATTR_ENDPOINT  = "ames.attr.endpoint";
	public static String ATTR_FORMAT    = "ames.attr.format";
	public static String ATTR_IMAGE     = "ames.attr.image";
	public static String ATTR_KEYPASS 	= "ames.attr.keypass";
	public static String ATTR_LIMIT   	= "ames.attr.limit";
	public static String ATTR_QUERY     = "ames.attr.query";
	public static String ATTR_PARENT    = "ames.attr.parent";
	public static String ATTR_RECIPIENT = "ames.attr.recipient";
	public static String ATTR_REQUEST 	= "ames.attr.request";
	public static String ATTR_SERVICE 	= "ames.attr.service";
	public static String ATTR_SOURCE 	= "ames.attr.source";
	public static String ATTR_START   	= "ames.attr.start";
	public static String ATTR_TARGET    = "ames.attr.target";
	public static String ATTR_URI     	= "ames.attr.uri";

	public static String ATTR_AFFILIATION = "ames.attr.affiliation";
	public static String ATTR_ECHELON     = "ames.attr.echelon";

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
	
	/*
	 * URL Part
	 */	
	public static String WMS_PATH = "/wms";
	
	/*
	 * Malware scan
	 */
	public static String MALWARE_SCAN = "ames.malware.scan";
	
	public static String MALWARE_DAEMON_HOST    = "ames.malware.daemon.host";
	public static String MALWARE_DAEMON_PORT    = "ames.malware.daemon.port";
	public static String MALWARE_DAEMON_TIMEOUT = "ames.malware.daemon.timeout";
	
	/*
	 * OpenOffice.org support
	 */
	public static String OFFICE_PATH = "ames.office.path";
	public static String OFFICE_HOST = "ames.office.host";
	public static String OFFICE_PORT = "ames.office.port";
	
	/*
	 * Reasoning support
	 */
	public static String FACTBASE_BUILDER = "ames.rule.factbase.builder";
	public static String RULEBASE_BUILDER = "ames.rule.rulebase.builder";

	public static String RESULT_CONVERTER = "ames.rule.result.converter";

	
	/*
	 * Symbol support
	 */
	
	public static String SYMBOL_APP6B_FILE = "ames.app6b.file";
	public static String SYMBOL_APP6B_URI  = "ames.app6b.uri";
	
	public static String SYMBOL_ICON_FILE  = "ames.icon.file";
	public static String SYMBOL_ICON_URI   = "ames.icon.uri";

}
