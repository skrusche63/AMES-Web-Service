package de.kp.ames.web.function;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function
 *  Module: FncParams
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #fnc #function #params #web
 * </SemanticAssist>
 *
 */


import java.util.HashMap;

public class FncParams extends HashMap<String,String> {

	/**
	 * Generated serial number
	 */
	private static final long serialVersionUID = 5530687163550720531L;
	/*
	 * Predefined keys
	 */
	public static String K_CLAS = "clas";
	public static String K_DESC = "desc";
	public static String K_ID   = "id";
	public static String K_NAME = "name";
	public static String K_PRE  = "prefix";
		
	public FncParams() {
	}

}
