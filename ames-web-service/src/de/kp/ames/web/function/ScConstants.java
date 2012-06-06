package de.kp.ames.web.function;

/**
 * This class comprises all smart client specific
 * parameters, defined by SmartGwt 3.0, that are
 * used within HttpRequests
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */
public class ScConstants {

	/*
	 * Paging request parameters
	 */
	public static String SC_START = "_startRow";
	public static String SC_LIMIT = "_endRow";

	/*
	 * Grid response parameters
	 */
	public static String SC_DATA        = "data";
	public static String SC_STATUS 	 	= "status";
	public static String SC_STARTROW 	= "startRow";
	public static String SC_ENDROW   	= "endRow";
	public static String SC_TOTALROWS 	= "totalRows";

}
