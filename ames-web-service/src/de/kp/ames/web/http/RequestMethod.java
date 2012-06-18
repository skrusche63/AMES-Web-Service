package de.kp.ames.web.http;
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

import javax.servlet.http.HttpServletRequest;

import de.kp.ames.web.core.render.GuiFactory;
import de.kp.ames.web.core.render.GuiRenderer;
import de.kp.ames.web.function.FncConstants;

/**
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */

public class RequestMethod {

	private HttpServletRequest request;
	
	private String query;

	private String name;
	private HashMap<String, String> attributes;
	
	private static String ATTRIBUTE_ERROR = "[RequestMethod] Attribute Retrieval Error";
	private static String METHOD_ERROR    = "[RequestMethod] Method Retrieval Error";

	/*
	 * Reference to the registered renderer
	 */
	protected GuiRenderer renderer;
	
	/**
	 * @param query
	 * @throws Exception
	 */
	public RequestMethod(HttpServletRequest request) throws Exception {

		/*
		 * Register request
		 */
		this.request =request;
		
		/*
		 * Derive query
		 */
		this.query = request.getQueryString(); 
		
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

		/*
		 * Initialize renderer
		 */
		renderer = GuiFactory.getInstance().getRenderer();

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
	public HttpServletRequest getRequest() {
		return this.request;
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
		
		if (this.attributes.containsKey(key)) {
			return this.attributes.get(key);
		}

		/*
		 * There are additional request attributes, that depend on widgets
		 * of the selected GUI framework
		 */
		if (key.equals(FncConstants.ATTR_LIMIT)) {

			String limitParam = renderer.getLimitParam();
			return (this.attributes.containsKey(limitParam)) ? this.attributes.get(limitParam) : null;

		} else if (key.equals(FncConstants.ATTR_PARENT)) {

			String parentParam = renderer.getParentParam();
			return (this.attributes.containsKey(parentParam)) ? this.attributes.get(parentParam) : null;
		
		} else if (key.equals(FncConstants.ATTR_START)) {

			String startParam = renderer.getStartParam();
			return (this.attributes.containsKey(startParam)) ? this.attributes.get(startParam) : null;
			
		}
		
		return null;
	
	}
}