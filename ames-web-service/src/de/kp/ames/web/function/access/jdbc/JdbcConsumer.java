package de.kp.ames.web.function.access.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrConstants;

public class JdbcConsumer {

	private String sqlString;
	
	/*
	 * Reference to database connection
	 */
	private JdbcConnection connection;
	
	/**
	 * Constructor
	 * 
	 * @param jAccessor
	 */
	public JdbcConsumer(JSONObject jAccessor) {

		try {

			/* 
			 * Database access parameters
			 */
			String name   = jAccessor.getString(JaxrConstants.SLOT_DATABASE);
			String driver = jAccessor.getString(JaxrConstants.SLOT_DRIVER);
			String url    = jAccessor.getString(JaxrConstants.SLOT_ENDPOINT);
			
			/* 
			 * User credentials
			 */
			String alias   = jAccessor.getString(JaxrConstants.SLOT_ALIAS);
			String keypass = jAccessor.getString(JaxrConstants.SLOT_KEYPASS);

			sqlString = jAccessor.getString(JaxrConstants.SLOT_SQL);

			connection = new JdbcConnection();
			connection.connect(name, driver, url, alias, keypass);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Constructor
	 * 
	 * @param name
	 * @param driver
	 * @param url
	 * @param alias
	 * @param keypass
	 */
	public JdbcConsumer(String name, String driver, String url, String alias, String keypass) {
		
		try {
			connection = new JdbcConnection();
			connection.connect(name, driver, url, alias, keypass);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
		
	/**
	 * Get database connection
	 * @return
	 */
	public Connection getConnection() {
		return connection.getConnection();
	}
	
	/**
	 * Close database connection
	 * 
	 * @return
	 */
	public boolean close() {

		try {
			Connection c = connection.getConnection();
			if (c == null) return false;
		
			connection.close();
			return true;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;

	}
	
	/**
	 * @return
	 */
	public JSONObject getJRecords() {
		return getJRecords(sqlString);
	}
	
	/**
	 * This method executes a certain SQL query statement against
	 * a remote database and transforms the results into a JSON
	 * representation, which combines data and metadata
	 * 
	 * @param sqlString
	 * @return
	 */
	public JSONObject getJRecords(String sqlString) {

		ResultSet resultSet = null;
		Statement statement = null;
		
		JSONObject jResult = new JSONObject();		
		JSONArray jRecords = new JSONArray();
		
		try {
			
			/*
			 * Retrieve connection
			 */
			Connection conn = connection.getConnection();
			if (conn == null) return null;
			
			/*
			 * Execute SQL statement
			 */
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sqlString);

			/* 
			 * Retrieve metadata from result set
			 */
			ResultSetMetaData metadata = resultSet.getMetaData();
			int columnCount = metadata.getColumnCount();

			/*
			 * Retrieve columns from metadata description
			 */
			JSONArray jColumns = new JSONArray();
			for (int col=1; col < columnCount; col++) {
				jColumns.put(metadata.getColumnName(col));
			}
			
			jResult.put(JdbcConstants.J_COLUMNS, jColumns);
			
			/*
			 * Retrieve rows from metadata description
			 */
			while (resultSet.next()) {
			
				// this is a row processing
				JSONObject jRecord = new JSONObject();
				
				for (int col=1; col < columnCount; col++) {
					
					/* 
					 * Table and data type are actually not supported; 
					 * this is done in a future version; also note, that
					 * the data type of the column values is restricted
					 * to Strings					 
					 */
					
					String colName = metadata.getColumnName(col);
					String colValu = resultSet.getString(col);
					
					colValu = (colValu==null) ? JdbcConstants.NO_DATA : colValu;
					jRecord.put(colName, colValu);					
					
				}
				
				jRecords.put(jRecords.length(), jRecord);
				
			}

			jResult.put(JdbcConstants.J_RECORDS, jRecords);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {

			/* 
			 * Release result set
			 */
			if (resultSet != null) releaseResultSet(resultSet);

			/* 
			 * Release statement
			 */
			if (statement != null) releaseStatement(statement);
			
		}

		return jResult;

	}
	
	/**
	 * @param resultSet
	 */
	private void releaseResultSet(ResultSet resultSet) {

		try {
			resultSet.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
			
		}

	}
	
	/**
	 * @param statement
	 */
	private void releaseStatement(Statement statement) {

		try {
			statement.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
			
		}

	}
}
