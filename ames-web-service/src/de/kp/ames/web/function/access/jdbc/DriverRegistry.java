package de.kp.ames.web.function.access.jdbc;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.access.jdbc
 *  Module: DriverRegistry
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #access #driver #function #jdbc #registry #web
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

import java.util.HashMap;
import java.util.Map;

import de.kp.ames.web.shared.constants.ClassificationConstants;

public class DriverRegistry {

	/* 
	 * Drivers may be found from: http://industry.java.sun.com/products/jdbc/drivers
	 */
	
	/*
	 * Technical driver description that have to be mapped onto an external representation
	 * Note, that the respective jar must be in the build path (e.g. derby.jar)
	 */
	public static String DB2_DRIVER_IMPL        = "com.ibm.db2.jdbc.app.DB2Driver";
	public static String DERBY_DRIVER_IMPL      = "org.apache.derby.jdbc.EmbeddedDriver";
	public static String HSQLDB_DRIVER_IMPL     = "org.hsqldb.jdbcDriver";
	public static String MYSQL_DRIVER_IMPL      = "com.mysql.jdbc.Driver";
	public static String SQL_SERVER_DRIVER_IMPL = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	public static String ORACLE_DRIVER_IMPL     = "oracle.jdbc.driver.OracleDriver";
	public static String POSTGRES_DRIVER_IMPL   = "org.postgresql.Driver";
	public static String SYBASE_DRIVER_IMPL     = "com.sybase.jdbc2.jdbc.SybDriver";
	
	/* 
	 * An external representation of drivers
	 */
	public static String FNC_DRIVER = ClassificationConstants.FNC_ID_Database + ":Driver";

	public static String FNC_DB2        = FNC_DRIVER + ":DB2";
	public static String FNC_DERBY      = FNC_DRIVER + ":DERBY";
	public static String FNC_HSQLDB     = FNC_DRIVER + ":HSQLDB";
	public static String FNC_MYSQL      = FNC_DRIVER + ":MYSQL";
	public static String FNC_SQL_SERVER = FNC_DRIVER + ":SQL-SERVER";
	public static String FNC_ORACLE     = FNC_DRIVER + ":ORACLE";
	public static String FNC_POSTGRES   = FNC_DRIVER + ":POSTGRES";
	public static String FNC_SYBASE     = FNC_DRIVER + ":SYBASE";

	protected static final Map<String, String> REGISTRY;
	
	/* 
	 * Initialization of driver map
	 */
	static {
		
		REGISTRY = new HashMap<String, String>();

		REGISTRY.put(FNC_DB2,   	 DB2_DRIVER_IMPL);
		REGISTRY.put(FNC_DERBY, 	 DERBY_DRIVER_IMPL);
		REGISTRY.put(FNC_HSQLDB, 	 HSQLDB_DRIVER_IMPL);		
		REGISTRY.put(FNC_MYSQL, 	 MYSQL_DRIVER_IMPL);
		REGISTRY.put(FNC_SQL_SERVER, SQL_SERVER_DRIVER_IMPL);
		REGISTRY.put(FNC_ORACLE, 	 ORACLE_DRIVER_IMPL);		
		REGISTRY.put(FNC_POSTGRES,   POSTGRES_DRIVER_IMPL);
		REGISTRY.put(FNC_SYBASE, 	 SYBASE_DRIVER_IMPL);

	}

	/**
	 * @param key
	 * @return
	 */
	public static String getDriver(String key) {
		
		if (REGISTRY.containsKey(key)) 
			return REGISTRY.get(key);
		
		return null;
		
		
	}
 }
