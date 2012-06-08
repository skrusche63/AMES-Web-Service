package de.kp.ames.web.function;
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

public class FncConstants {

	/* 
	 * Common Classification Node
	 */
	public static String FNC_ID = "urn:oasis:names:tc:ebxml-regrep:FNC";

	/*
	 * Bulletin Classification Nodes
	 */
	public static String FNC_ID_Posting = FNC_ID + ":Posting";
	
	/*
	 * Communication Classification Nodes
	 */
	public static String FNC_ID_Chat = FNC_ID + ":Chat";
		
	/*
	 * Community Classification Nodes
	 */
	public static String FNC_ID_Affiliation = FNC_ID + ":Affiliation";
	public static String FNC_ID_Category    = FNC_ID + ":Category";
	public static String FNC_ID_Contact     = FNC_ID + ":Contact";	
	public static String FNC_ID_Community   = FNC_ID + ":Community";
	
	/*
	 * Format Classification Nodes
	 */
	public static String FNC_ID_Format = FNC_ID + ":Format";
	
	public static String FNC_FORMAT_ID_Grid = FNC_ID_Format + ":Grid";
	public static String FNC_FORMAT_ID_Json = FNC_ID_Format + ":Json";
	public static String FNC_FORMAT_ID_Rss  = FNC_ID_Format + ":Rss";
	
	/*
	 * Security Classification Nodes
	 */
	public static String FNC_ID_Security = FNC_ID + ":Security";	
	public static String FNC_SECURITY_ID_Safe  = FNC_ID_Security + ":Safe";

	/*
	 * Transformation Classification Nodes
	 */
	public static String FNC_ID_Transformator =  FNC_ID + ":Transformator";
	
	/*
	 * Identity Prefixes
	 */	
	public static String AFFILIATION_PRE;
	public static String CHAT_PRE;
	public static String COMMUNITY_PRE;
	public static String POSTING_PRE;
    public static String SECURITY_PRE;

	/*
	 * Methods
	 */
    
    // appy a certain set of xsl stylesheets to a specific
    // registry object
    public static String METH_APPLY     = "apply";
    public static String METH_DELETE    = "delete";
    public static String METH_EXPLORE   = "explore";
    public static String METH_GET       = "get";
    
    // retrieve a kml representation of a certain registry 
    // package and all of its content
    public static String METH_EDGES   	= "edges";
    public static String METH_NODES   	= "nodes";
    
    // retrieve all registered wms layers from the respective
    // geo server
    public static String METH_LAYERS    = "layers";
    public static String METH_MODULE    = "module";
	public static String METH_REGISTER  = "register";
	public static String METH_SET       = "set";
	public static String METH_SUBMIT    = "submit";
	
	/*
	 * Method attributes
	 */
	public static String ATTR_ALIAS;
	public static String ATTR_AFFILIATE;
	public static String ATTR_ENDPOINT;
	public static String ATTR_FORMAT;
	public static String ATTR_ITEM;
	public static String ATTR_KEYPASS;
	public static String ATTR_RECIPIENT;
	public static String ATTR_SERVICE;
	public static String ATTR_SOURCE;
	public static String ATTR_TARGET;
	public static String ATTR_TYPE;
	public static String ATTR_URI;

	/*
	 * SCM Repository
	 */
	public static String GIT_HOME;
	
	/*
	 * initialize constants
	 */
	static {
		
		Bundle bundle = Bundle.getInstance();
		
		/*
		 * Identity prefixes
		 */		
		String basePrefix = bundle.getString(GlobalConstants.BASE_PRE);
		
		AFFILIATION_PRE = basePrefix + ":affiliation";
		CHAT_PRE        = basePrefix + ":chat";
		COMMUNITY_PRE   = basePrefix + ":community";
		POSTING_PRE     = basePrefix + ":posting";
		SECURITY_PRE    = basePrefix + ":security";
		
		/*
		 * Method attributes
		 */
		ATTR_ALIAS     = bundle.getString(GlobalConstants.ATTR_ALIAS);
		ATTR_AFFILIATE = bundle.getString(GlobalConstants.ATTR_AFFILIATE);
		ATTR_ENDPOINT  = bundle.getString(GlobalConstants.ATTR_ENDPOINT);
		ATTR_FORMAT    = bundle.getString(GlobalConstants.ATTR_FORMAT);
		ATTR_ITEM      = bundle.getString(GlobalConstants.ATTR_ITEM);
		ATTR_KEYPASS   = bundle.getString(GlobalConstants.ATTR_KEYPASS);
		ATTR_RECIPIENT = bundle.getString(GlobalConstants.ATTR_RECIPIENT);
		ATTR_SERVICE   = bundle.getString(GlobalConstants.ATTR_SERVICE);
		ATTR_SOURCE    = bundle.getString(GlobalConstants.ATTR_SOURCE);
		ATTR_TARGET    = bundle.getString(GlobalConstants.ATTR_TARGET);
		ATTR_TYPE      = bundle.getString(GlobalConstants.ATTR_TYPE);
		ATTR_URI       = bundle.getString(GlobalConstants.ATTR_URI);
		
		/*
		 * SCM Repository
		 */		
		GIT_HOME = bundle.getString(GlobalConstants.GIT_HOME);
		
	}

}
