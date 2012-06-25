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
	public static String FNC_ID_Mail = FNC_ID + ":Mail";
		
	/*
	 * Business Classification Nodes
	 */
	public static String FNC_ID_Accessor       = FNC_ID + ":Accessor";
	public static String FNC_ID_Affiliation    = FNC_ID + ":Affiliation";
	public static String FNC_ID_Category       = FNC_ID + ":Category";
	public static String FNC_ID_Contact        = FNC_ID + ":Contact";	
	public static String FNC_ID_Community      = FNC_ID + ":Community";
	public static String FNC_ID_Database       = FNC_ID + ":Database";
	public static String FNC_ID_Document       = FNC_ID + ":Document";
	public static String FNC_ID_Evaluation     = FNC_ID + ":Evaluation";
	public static String FNC_ID_Image          = FNC_ID + ":Image";
	public static String FNC_ID_Link           = FNC_ID + ":Link";
	public static String FNC_ID_Namespace      = FNC_ID + ":Namespace";	
	public static String FNC_ID_Product        = FNC_ID + ":Product";
	public static String FNC_ID_Productor      = FNC_ID + ":Productor";
	public static String FNC_ID_Reasoner       = FNC_ID + ":Reasoner";
	public static String FNC_ID_Remote         = FNC_ID + ":Remote";
	public static String FNC_ID_Responsibility = FNC_ID + ":Responsibility";
	public static String FNC_ID_Role           = FNC_ID + ":Role";
	public static String FNC_ID_User           = FNC_ID + ":User";
	
	/*
	 * Symbol Classification Nodes
	 */
	public static String FNC_ID_Symbol = FNC_ID + ":Symbol";
	
	public static String FNC_SYMBOL_ID_APP6B = FNC_ID_Symbol + ":APP6-B";
	public static String FNC_SYMBOL_ID_Icon  = FNC_ID_Symbol + ":Icon";
	
	/*
	 * Transformation Classification Nodes
	 */
	public static String FNC_ID_Transformator =  FNC_ID + ":Transformator";
	
	/*
	 * Identity Prefixes
	 */	
	public static String ACCESSOR_PRE;
	public static String AFFILIATION_PRE;
	public static String CHAT_PRE;
	public static String COMMUNITY_PRE;
	public static String DOCUMENT_PRE;
	public static String EVALUATION_PRE;
	public static String IMAGE_PRE;
	public static String LINK_PRE;
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
	public static String ATTR_ENDPOINT;
	public static String ATTR_KEYPASS;
	public static String ATTR_LIMIT;
	public static String ATTR_PARENT;
	public static String ATTR_RECIPIENT;
	public static String ATTR_SERVICE;
	public static String ATTR_START;
	public static String ATTR_TARGET;
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
		CHAT_PRE          = basePrefix + ":chat";
		COMMUNITY_PRE     = basePrefix + ":community";
		DOCUMENT_PRE      = basePrefix + ":document";
		EVALUATION_PRE    = basePrefix + ":evaluation";
		IMAGE_PRE         = basePrefix + ":image";
		LINK_PRE          = basePrefix + ":link";
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
		ATTR_ENDPOINT  = bundle.getString(GlobalConstants.ATTR_ENDPOINT);
		ATTR_KEYPASS   = bundle.getString(GlobalConstants.ATTR_KEYPASS);
		ATTR_LIMIT     = bundle.getString(GlobalConstants.ATTR_LIMIT);		
		ATTR_PARENT    = bundle.getString(GlobalConstants.ATTR_PARENT);
		ATTR_RECIPIENT = bundle.getString(GlobalConstants.ATTR_RECIPIENT);
		ATTR_SERVICE   = bundle.getString(GlobalConstants.ATTR_SERVICE);
		ATTR_START     = bundle.getString(GlobalConstants.ATTR_START);
		ATTR_TARGET    = bundle.getString(GlobalConstants.ATTR_TARGET);
		ATTR_URI       = bundle.getString(GlobalConstants.ATTR_URI);

		ATTR_AFFILIATION = bundle.getString(GlobalConstants.ATTR_AFFILIATION);
		ATTR_ECHELON     = bundle.getString(GlobalConstants.ATTR_ECHELON);

		/*
		 * SCM Repository
		 */		
		GIT_HOME = bundle.getString(GlobalConstants.GIT_HOME);
		
	}

}
