package de.kp.ames.web.function.symbol;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.symbol
 *  Module: SymbolProcessor
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #function #processor #symbol #web
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

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.JsonConstants;

public class SymbolProcessor {

	protected String SYMBOL_PATH;
	protected String SYMBOL_ROOT;
	protected String SYMBOL_URI;
	
	protected JSONObject jSymbols;
	
	protected Boolean initialized = false;
	protected double SCALE_128 = 6.4;

	public static String J_CHILDREN = JsonConstants.J_CHILDREN;
	public static String J_ID       = JsonConstants.J_ID;
	public static String J_NAME     = JsonConstants.J_NAME;
	public static String J_SYMB     = JsonConstants.J_SYMB;
	public static String J_SYMBOL   = JsonConstants.J_SYMBOL;
	public static String J_URL      = JsonConstants.J_URL;
	
	/**
	 * A helper method to initialize reference
	 * information to managed symbols
	 */
	public void init(RequestContext ctx) {

		/*
		 * Parameters to acess the configuration file must be present
		 */
		if ((SYMBOL_PATH == null) || (SYMBOL_ROOT == null)) return;
		if (initialized == true) return;
		
		try {
			
			/*
			 * Load control information
			 */
			InputStream fis = ctx.getContext().getResourceAsStream(SYMBOL_PATH);
	
			byte[] bytes = FileUtil.getByteArrayFromInputStream(fis);
			jSymbols = new JSONObject(new String(bytes));
			
			initialized = true;
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}

	}
	
	/**
	 * @return
	 */
	public String getRootKey() {
		return SYMBOL_ROOT;
	}
	
	/**
	 * Get root entry of symbol control file
	 * in a JSON representation
	 * 
	 * @return
	 * @throws Exception
	 */
	public JSONObject getRoot() throws Exception {
		
		if (initialized == false) throw new Exception("[SymbolProcessor] Control file not loaded.");
		return jSymbols.getJSONObject(SYMBOL_ROOT);
		
	}

	/**
	 * Get specific entry (key) of symbol control file
	 * in a JSON representation
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public JSONObject getSymbol(String key) throws Exception {		

		if (initialized == false) throw new Exception("[SymbolProcessor] Control file not loaded.");
		
		JSONObject jSymbol = jSymbols.has(key) ? jSymbols.getJSONObject(key) : null;
		if (jSymbol != null) {

			String id     = jSymbol.has(J_ID)   ? jSymbol.getString(J_ID) : null;			
			String symbol = jSymbol.has(J_SYMB) ? jSymbol.getString(J_SYMB) : null;
			
			if (symbol == null) symbol = id;
			if (symbol != null) jSymbol.put(J_SYMBOL, symbol);
		
		}
		
		return jSymbol; 	

	}

	/**
	 * Get subordinate entries of a specific entry of
	 * the symbol control file in a JSON representation
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public JSONArray getChildren(String key) throws Exception {

		if (initialized == false) throw new Exception("[SymbolProcessor] Control file not loaded.");

		JSONArray children = new JSONArray();
		if (!jSymbols.has(key)) return children;
		
		JSONObject jSymbol = jSymbols.getJSONObject(key);
		if (!jSymbol.has(J_CHILDREN)) return children;
		
		return jSymbol.getJSONArray(J_CHILDREN);

	}

	/**
	 * Build symbol uri from key
	 * 
	 * @param key
	 * @return
	 */
	public String getSymbolUri(String key) {
		return SYMBOL_URI + "?img=" + key + "&scale=" + SCALE_128;
	}

}
