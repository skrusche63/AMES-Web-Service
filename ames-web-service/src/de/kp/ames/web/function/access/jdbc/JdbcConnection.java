package de.kp.ames.web.function.access.jdbc;
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

import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcConnection {

	/*
	 * Reference to Database Connection
	 */
	private Connection connection;
	
	public JdbcConnection() {
	}

	/**
	 * Connect to JDBC Database
	 * @param name
	 * @param type
	 * @param url
	 * @param alias
	 * @param keypass
	 */
	public void connect(String name, String type, String url, String alias, String keypass) throws Exception {			
		/* 
		 * Load & Register provided driver
		 */
		String driver = DriverRegistry.getDriver(type);
		if (driver == null) throw new Exception("[JdbcConnection] The Database driver <" + type +"> is not registered.");
			
		/*
		 * url = <host>:<port>
		 */
		Class.forName(driver).newInstance();	
		String endpoint = getEndpoint(name, driver, url);
		
		/* 
		 * Instantiate Database connection
		 */
		connection = DriverManager.getConnection(endpoint, alias, keypass);
		
	}
	
	/**
	 * Retrieve established database connection
	 * 
	 * @return
	 */
	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * Close database connection
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception {
			
		if ( (connection != null) && (connection.isClosed() == false)) {
			
			connection.close();
			connection = null;
			
		}
		
	}
	
	/**
	 * @param name
	 * @param driver
	 * @param url
	 * @return
	 */
	private String getEndpoint(String name, String driver, String url) {
		
		String endpoint = "jdbc:";
		
		if (driver.equals(DriverRegistry.DB2_DRIVER_IMPL)) {
			/*
			 * DB2 endpoint
			 */
			endpoint = endpoint + "db2:" + name;
			
		} else if (driver.equals(DriverRegistry.DERBY_DRIVER_IMPL)) {			
			/*
			 * Derby endpoint
			 */
			endpoint = endpoint + "derby:" + name;
			
		}  else if (driver.equals(DriverRegistry.HSQLDB_DRIVER_IMPL)) {
			/*
			 * HSQLDB endpoint
			 */
			endpoint = endpoint + "hsqldb:file:" + url + "/" + name;
			
		}  else if (driver.equals(DriverRegistry.MYSQL_DRIVER_IMPL)) {
			/*
			 * MySQL endpoint
			 */
			endpoint = endpoint + "mysql://" + url + "/" + name;
			
		}  else if (driver.equals(DriverRegistry.SQL_SERVER_DRIVER_IMPL)) {
			/*
			 * SqlServer endpoint
			 */
			endpoint = endpoint + "microsoft:sqlserver://" + url + ";databaseName=" + name;
			
		}  else if (driver.equals(DriverRegistry.ORACLE_DRIVER_IMPL)) {
			/*
			 * Oracle endpoint
			 * 
			 * Syntax jdbc:oracle:thin:@//[HOST][:PORT]/SERVICE
			 */
			endpoint = endpoint + "oracle:thin@//" + url + "/" + name;
			
		}  else if (driver.equals(DriverRegistry.POSTGRES_DRIVER_IMPL)) {
			/*
			 * PostgreSQL endpoint
			 */
			endpoint = endpoint + "postgresql://" + url + "/" + name;
			
		}  else if (driver.equals(DriverRegistry.SYBASE_DRIVER_IMPL)) {
			/*
			 * Synbase endpoint
			 */
			endpoint = endpoint + "sybase:Tds:" + url + "?ServiceName=" + name;
			
		}

		return endpoint.toString();
		
	}

}
