package de.kp.ames.web.core.method;
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

/**
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */

public class RequestMethod {

	private String query;

	private String name;
	private HashMap<String, String> attributes;
	
	private static String ATTRIBUTE_ERROR = "[RequestMethod] Attribute Retrieval Error";
	private static String METHOD_ERROR    = "[RequestMethod] Method Retrieval Error";
	
	/**
	 * @param query
	 * @throws Exception
	 */
	public RequestMethod(String query) throws Exception {

		this.query = query;
		
		String[] tokens = query.split("&");
		
		// the first token describes the method: method=name
		setName(tokens[0]);
		
		if (tokens.length > 1) {
			// retrieve attribute from query: key=value
			attributes = new HashMap<String, String>();
			for (int i=1; i < tokens.length; i++) {
				setAttribute(tokens[i]);
			}
		}
		 
	}
	
	/**
	 * @param token
	 * @throws Exception
	 */
	private void setAttribute(String token) throws Exception {

		String[] tokens = token.split("=");

		if (tokens.length != 2) throw new Exception(ATTRIBUTE_ERROR);
		
		String key = tokens[0];
		String val = tokens[1];
		
		attributes.put(key, val);
		
	}
	
	/**
	 * @param token
	 * @throws Exception
	 */
	private void setName(String token) throws Exception {
		
		String[] tokens = token.split("=");

		if (tokens.length != 2) throw new Exception(METHOD_ERROR);
		if (tokens[0].equals("method") == false) throw new Exception(METHOD_ERROR);
		
		this.name = tokens[1];
		
	}
	
	/**
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return
	 */
	public String getQuery() {
		return this.query;
	}
	
	/**
	 * @return
	 */
	public HashMap<String, String> getAttributes() {
		return this.attributes;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public String getAttribute(String key) {
		if (this.attributes.containsKey(key)) return this.attributes.get(key);
		return null;
	}
}
