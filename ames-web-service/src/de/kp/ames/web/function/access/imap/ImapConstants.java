package de.kp.ames.web.function.access.imap;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.access.imap
 *  Module: ImapConstants
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #access #constants #function #imap #web
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

public class ImapConstants {

	public static String IMAP_HOST     = "mail.imap.host";
	public static String IMAP_PORT     = "mail.imap.port";
	public static String IMAP_PROTOCOL = "mail.store.protocol";
	
	public static String DEFAULT_PORT_VALUE     = "143";
	public static String DEFAULT_PROTOCOL_VALUE = "imap";

	public static String INBOX = "INBOX";
	
	/*
	 * JSON parameters
	 */
	public static String J_ATTACHMENT = "attachment";
	public static String J_DATE 	  = "date";
	public static String J_FROM       = "from";
	public static String J_ID      	  = "id";
	public static String J_KEY 	      = "key";		    	
    public static String J_SUBJECT    = "subject";		      
	
}
