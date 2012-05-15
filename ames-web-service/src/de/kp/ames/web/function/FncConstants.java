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
	 * Security Classification Nodes
	 */
	public static String FNC_ID_Security = FNC_ID + ":Security";
	
	public static String FNC_SECURITY_ID_Safe  = FNC_ID_Security + ":Safe";

	/*
	 * Identity Prefixes
	 */	
    public static String SECURITY_PRE;

	/*
	 * Security methods
	 */
    public static String METH_EXPLORE   = "explore";
    public static String METH_GET_CREDS = "get-creds";
    public static String METH_MODULE    = "module";
	public static String METH_SET_CREDS = "set-creds";
	public static String METH_REGISTER  = "register";

	/*
	 * Method attributes
	 */
	public static String ATTR_ALIAS;
	public static String ATTR_KEYPASS;
	public static String ATTR_SERVICE;
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
		SECURITY_PRE = basePrefix + ":security";
		
		/*
		 * Method attributes
		 */
		ATTR_ALIAS   = bundle.getString(GlobalConstants.ATTR_ALIAS);
		ATTR_KEYPASS = bundle.getString(GlobalConstants.ATTR_KEYPASS);
		ATTR_SERVICE = bundle.getString(GlobalConstants.ATTR_SERVICE);
		ATTR_URI     = bundle.getString(GlobalConstants.ATTR_URI);
		
		/*
		 * SCM Repository
		 */
		
		GIT_HOME = bundle.getString(GlobalConstants.GIT_HOME);
		
	}

}
