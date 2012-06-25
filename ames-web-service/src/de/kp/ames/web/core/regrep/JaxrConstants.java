package de.kp.ames.web.core.regrep;
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

public class JaxrConstants {

	/*
	 * Registry guest
	 */
	public static String REGISTRY_GUEST = "urn:oasis:names:tc:ebxml-regrep:RegistryGuest";

	/*
	 * ebRIM prefix
	 */
	public static String RIM_PRE = "rim";
	
	/*
	 * Basic ebRIM parameters
	 */
	public static String RIM_AUTHOR    = "rimAuthor";
	public static String RIM_CODE      = "rimCode";
	public static String RIM_DATE      = "rimDatetime";
	public static String RIM_DESC 	   = "rimDescription";	 
	public static String RIM_EVENT     = "rimEvent";
	public static String RIM_HOME 	   = "rimHome";
	public static String RIM_ID 	   = "rimId";	
	public static String RIM_LID 	   = "rimLid";	
	public static String RIM_MIME      = "rimMimeType";
	public static String RIM_NAME 	   = "rimName";
	public static String RIM_OWNER     = "rimOwner";
	public static String RIM_SOURCE    = "rimSourceObject";
	public static String RIM_STATUS    = "rimStatus";
	public static String RIM_TARGET    = "rimTargetObject";
	public static String RIM_TIMESTAMP = "rimTimestamp";
    public static String RIM_TYPE  	   = "rimObjectType";
    public static String RIM_URI       = "rimExternalURI";
    public static String RIM_VERSION   = "rimVersion";
   
    /*
     * Semantic description
     */
	public static String RIM_CATE  = "rimCategory";
	public static String RIM_CLAS  = "rimClassification";
	public static String RIM_ROLE  = "rimRole";

	/* 
	 * Person name (User)
	 */
    public static String RIM_FIRST_NAME  = "rimFirstName";
    public static String RIM_MIDDLE_NAME = "rimMiddleName";
    public static String RIM_LAST_NAME   = "rimLastName";
    public static String RIM_USER_NAME   = "rimUserName";

	/* 
	 * Postal address (Organization & User)
	 */
    public static String RIM_ADDRESS           = "rimAddress";
    
    public static String RIM_CITY              = "rimCity";
    public static String RIM_COUNTRY           = "rimCountry";
    public static String RIM_POSTAL_CODE       = "rimPostalCode";
    public static String RIM_STATE_OR_PROVINCE = "rimStateOrProvince";
    public static String RIM_STREET            = "rimStreet";
    public static String RIM_STREET_NUMBER     = "rimStreeNumber";

	/* 
	 * Telefone number (Organization & User)
	 */
    public static String RIM_PHONE           = "rimPhone";
    
    public static String RIM_AREA_CODE       = "rimAreaCode";
    public static String RIM_COUNTRY_CODE    = "rimCountryCode";
    public static String RIM_PHONE_NUMBER    = "rimPhoneNumber";
    public static String RIM_PHONE_EXTENSION = "rimPhoneExtension";

	/* 
	 * Email address (Organization & User)
	 */
    public static String RIM_EMAIL = "rimEmail";

	/* 
	 * Primary contact (Organization)
	 */
    public static String RIM_CONTACT	   = "rimContact";
    
	public static String RIM_CONTACT_NAME  = "rimContactName";
	public static String RIM_CONTACT_EMAIL = "rimContactEmail";
	public static String RIM_CONTACT_PHONE = "rimContactPhone";

	/*
	 * Namespace
	 */
	public static String RIM_NAMESPACE = "rimNamespace";

	/*
	 * Service
	 */
	public static String RIM_SEQNO = "rimSeqNo";
	public static String RIM_SPEC  = "rimSpecification";

	/*
	 * Evaluation
	 */
	public static String RIM_BASE     = "rimBase";
	public static String RIM_REASONER = "rimReasoner";

	/*
	 * Slot
	 */
	public static String RIM_SLOT = "rimSlot";

	/*
	 * Symbol representation
	 */
	public static String RIM_SYMBOL = "rimSymbol";
	
	/*
	 * Default Slot Type
	 */
	public static String SLOT_TYPE = "XRegistry";

	/*
	 * Slot Names
	 */
	public static String SLOT_ALIAS     = "Alias";
	public static String SLOT_COLOR     = "Color";
	public static String SLOT_DATABASE  = "Database";
	public static String SLOT_DRIVER    = "Driver";
	public static String SLOT_ENDPOINT  = "Endpoint";
	public static String SLOT_FILE      = "File";
	public static String SLOT_LATITUDE  = "Latitude";
	public static String SLOT_LONGITUDE = "Longitude";
	public static String SLOT_KEYPASS   = "Keypass";
	public static String SLOT_MARKUP    = "Markup";
	public static String SLOT_PORT      = "Port";
	public static String SLOT_PROTOCOL  = "Protocol";
	public static String SLOT_RULESET   = "Ruleset";
	public static String SLOT_SEQNO     = "SeqNo";
	public static String SLOT_SOURCE    = "Source";
	public static String SLOT_SYMBOL    = "Symbol";
	public static String SLOT_SQL       = "SQL";
	public static String SLOT_TRACK     = "Track";
	// Cartesic coordinates
	public static String SLOT_X         = "X";
	public static String SLOT_Y         = "Y";
	
}
