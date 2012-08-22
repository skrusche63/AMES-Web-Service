package de.kp.ames.web.function;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function
 *  Module: FncConstants
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #constants #fnc #function #web
 * </SemanticAssist>
 *
 */

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
	 * Identity Prefixes
	 */	
	public static String ACCESSOR_PRE;
	public static String AFFILIATION_PRE;
	public static String ATTACHMENT_PRE;
	public static String CHAT_PRE;
	public static String COMMENT_PRE;
	public static String COMMUNITY_PRE;
	public static String CORE_PRE;
	public static String DOCUMENT_PRE;
	public static String EVALUATION_PRE;
	public static String FOLDER_PRE;
	public static String IMAGE_PRE;
	public static String MAIL_PRE;
	public static String NAMESPACE_PRE;
	public static String POSTING_PRE;
	public static String PRODUCT_PRE;
	public static String PRODUCTOR_PRE;
	public static String REASONER_PRE;
    public static String SECURITY_PRE;
    public static String TRANSFORMATOR_PRE;
	
	/*
	 * Method attributes
	 */
	public static String ATTR_ALIAS;
	public static String ATTR_AFFILIATE;
	public static String ATTR_COMMUNITY;
	public static String ATTR_KEYPASS;
	public static String ATTR_LIMIT;
	public static String ATTR_START;
	public static String ATTR_URI;

	public static String ATTR_AFFILIATION;
	public static String ATTR_ECHELON;
	
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
		
		ACCESSOR_PRE      = basePrefix + ":accessor";
		AFFILIATION_PRE   = basePrefix + ":affiliation";
		ATTACHMENT_PRE    = basePrefix + ":attachment";
		CHAT_PRE          = basePrefix + ":chat";
		COMMENT_PRE       = basePrefix + ":comment";
		COMMUNITY_PRE     = basePrefix + ":community";
		CORE_PRE          = basePrefix + ":core";
		DOCUMENT_PRE      = basePrefix + ":document";
		EVALUATION_PRE    = basePrefix + ":evaluation";
		FOLDER_PRE        = basePrefix + ":folder";
		IMAGE_PRE         = basePrefix + ":image";
		MAIL_PRE          = basePrefix + ":mail";
		NAMESPACE_PRE     = basePrefix + ":namespace";
		POSTING_PRE       = basePrefix + ":posting";
		PRODUCT_PRE       = basePrefix + ":product";
		PRODUCTOR_PRE     = basePrefix + ":productor";
		REASONER_PRE      = basePrefix + ":reasoner";
		SECURITY_PRE      = basePrefix + ":security";
		TRANSFORMATOR_PRE = basePrefix + ":transformator";
		
		/*
		 * Method attributes
		 */
		ATTR_ALIAS     = bundle.getString(GlobalConstants.ATTR_ALIAS);
		ATTR_AFFILIATE = bundle.getString(GlobalConstants.ATTR_AFFILIATE);
		ATTR_COMMUNITY = bundle.getString(GlobalConstants.ATTR_COMMUNITY);
		ATTR_KEYPASS   = bundle.getString(GlobalConstants.ATTR_KEYPASS);
		ATTR_LIMIT     = bundle.getString(GlobalConstants.ATTR_LIMIT);		
		ATTR_START     = bundle.getString(GlobalConstants.ATTR_START);
		ATTR_URI       = bundle.getString(GlobalConstants.ATTR_URI);

		ATTR_AFFILIATION = bundle.getString(GlobalConstants.ATTR_AFFILIATION);
		ATTR_ECHELON     = bundle.getString(GlobalConstants.ATTR_ECHELON);

		/*
		 * SCM Repository
		 */		
		GIT_HOME = bundle.getString(GlobalConstants.GIT_HOME);
		
	}

}
