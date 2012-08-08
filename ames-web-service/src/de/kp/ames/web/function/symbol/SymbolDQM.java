package de.kp.ames.web.function.symbol;
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

import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.json.StringCollector;

public class SymbolDQM {

	public SymbolDQM() {
	}
	
	/**
	 * Get control information for Icon-based symbols
	 * 
	 * @param parent
	 * @return
	 * @throws Exception
	 */
	public JSONArray getIconKeys(String parent) throws Exception {

		IconProcessor processor = IconProcessor.getInstance();
		
		/*
		 * Sort icon keys by name
		 */
		StringCollector collector = new StringCollector();
		
		/*
		 * Determine parent
		 */
		if (parent == null) parent = processor.getRootKey();
		
		/*
		 * Retrieve key information
		 */
		JSONArray jArray = processor.getChildren(parent);					
		for (int i=0; i < jArray.length(); i++) {
			
			String key = jArray.getString(i);
			
			JSONObject jSymbol = processor.getSymbol(key);
			if (jSymbol != null) collector.put(jSymbol.getString(SymbolProcessor.J_NAME), jSymbol);
		
		}
		
		return new JSONArray(collector.values());
	}
	
	/**
	 * Get control information for APP6-B symbols
	 * 
	 * @param parent
	 * @return
	 * @throws Exception
	 */
	public JSONArray getAPP6bKeys(String parent) throws Exception {

		APP6bProcessor processor = APP6bProcessor.getInstance();
		
		/*
		 * Sort symbols keys by name
		 */
		StringCollector collector = new StringCollector();
		
		/*
		 * Determine parent
		 */
		String pkey = parent.equals("null") ? pkey = processor.getRootKey() : parent;
		
		/*
		 * Retrieve key information
		 */
		JSONArray jArray = processor.getChildren(pkey);					
		for (int i=0; i < jArray.length(); i++) {
			
			String key = jArray.getString(i);
			
			/*
			 * Clone JSON object
			 */
			JSONObject jSymbol = new JSONObject(processor.getSymbol(key).toString());
			if (jSymbol != null) collector.put(jSymbol.getString(SymbolProcessor.J_NAME), jSymbol);
		
		}
		
		return new JSONArray(collector.values());

	}

	/**
	 * Get Icon-based symbols
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public JSONArray getIconSymbols(String item) throws Exception {
		
		IconProcessor processor = IconProcessor.getInstance();
		
		JSONObject jSymbol = null;
		
		/*
		 * Sort symbols keys by name
		 */
		StringCollector collector = new StringCollector();

		/*
		 * Get symbol for requested item
		 */
		jSymbol = processor.getSymbol(item);
		if (jSymbol == null) return new JSONArray();
		
		if (jSymbol.has(SymbolProcessor.J_SYMBOL)) {			
			/*
			 * Build thumbnail
			 */
			JSONObject jThumb = getIconThumb(jSymbol);
			collector.put(jThumb.getString(SymbolProcessor.J_NAME), jThumb);
		}

		/*
		 * Get children of requested item
		 */
		JSONArray jArray = processor.getChildren(item);
		for (int i=0; i < jArray.length(); i++) {
			
			String key = jArray.getString(i);

			jSymbol = processor.getSymbol(key);
			if (jSymbol == null) continue;
			
			if (jSymbol.has(SymbolProcessor.J_SYMBOL)) {
				/*
				 * Build thumbnail
				 */
				JSONObject jThumb = getIconThumb(jSymbol);
				collector.put(jThumb.getString(SymbolProcessor.J_NAME), jThumb);
			}
		}

		return new JSONArray(collector.values());

	}

	/**
	 * Get APP6-B based symbols
	 * 
	 * @param item
	 * @param affiliation
	 * @param echelon
	 * @return
	 * @throws Exception
	 */
	public JSONArray getAPP6bSymbols(String item, String affiliation, String echelon) throws Exception {

		APP6bProcessor processor = APP6bProcessor.getInstance();
		
		JSONObject jSymbol = null;
		
		/*
		 * Sort symbols keys by name
		 */
		StringCollector collector = new StringCollector();

		/*
		 * Get symbol for requested item
		 */
		jSymbol = processor.getSymbol(item);
		if (jSymbol == null) return new JSONArray();
		
		if (jSymbol.has(SymbolProcessor.J_SYMBOL)) {			
			/*
			 * Build thumbnail
			 */
			JSONObject jThumb = getAPP6bThumb(jSymbol, affiliation, echelon);
			collector.put(jThumb.getString(SymbolProcessor.J_NAME), jThumb);
		}

		/*
		 * Get children of requested item
		 */
		JSONArray jArray = processor.getChildren(item);
		for (int i=0; i < jArray.length(); i++) {
			
			String key = jArray.getString(i);

			jSymbol = processor.getSymbol(key);
			if (jSymbol == null) continue;
			
			if (jSymbol.has(SymbolProcessor.J_SYMBOL)) {
				/*
				 * Build thumbnail
				 */
				JSONObject jThumb = getAPP6bThumb(jSymbol, affiliation, echelon);
				collector.put(jThumb.getString(SymbolProcessor.J_NAME), jThumb);
			}
		}

		return new JSONArray(collector.values());
		
	}

	/**
	 * A helper method to retrieve a thumb representation
	 * of an APP6-B symbol
	 * 
	 * @param jSymbol
	 * @param affiliation
	 * @param echelon
	 * @return
	 * @throws Exception
	 */
	private JSONObject getAPP6bThumb(JSONObject jSymbol, String affiliation, String echelon) throws Exception {

		APP6bProcessor processor = APP6bProcessor.getInstance();

		String name = jSymbol.getString(SymbolProcessor.J_NAME);
		String key  = jSymbol.getString(SymbolProcessor.J_SYMBOL);

		/* 
		 * Extend key
		 */
		if (affiliation != null) key = setAffiliation(key, affiliation);
		if (echelon != null)     key = setEchelon(key, echelon);

		/*
		 * Get image url
		 */
		String url = processor.getSymbolUri(key);
		
		/*
		 * Build thumbnail
		 */
		JSONObject jThumb = new JSONObject();
		
		jThumb.put(SymbolProcessor.J_NAME, name);
		jThumb.put(SymbolProcessor.J_URL, url);

		return jThumb;
		
	}

	/**
	 * A helper method to retrieve a thumb representation
	 * of an Icon-based symbol
	 * 
	 * @param jSymbol
	 * @return
	 * @throws Exception
	 */
	private JSONObject getIconThumb(JSONObject jSymbol) throws Exception {

		IconProcessor processor = IconProcessor.getInstance();

		String name = jSymbol.getString(SymbolProcessor.J_NAME);
		String key  = jSymbol.getString(SymbolProcessor.J_SYMBOL);

		/*
		 * Get image url
		 */
		String url = processor.getSymbolUri(key);
		
		/*
		 * Build thumbnail
		 */
		JSONObject jThumb = new JSONObject();
		
		jThumb.put(SymbolProcessor.J_NAME, name);
		jThumb.put(SymbolProcessor.J_URL, url);

		return jThumb;
		
	}

	/**
	 * @param symbol
	 * @param echelon
	 * @return
	 */
	private String setEchelon(String symbol, String echelon) {

		char[] cArray = symbol.toCharArray();
		
		char selector = 'S';
		
		if (cArray[0] == selector)
			cArray[11] = echelon.toCharArray()[0];

		return new String(cArray);
		
	}
	
	/**
	 * @param symbol
	 * @param affiliation
	 * @return
	 */
	public String setAffiliation(String symbol, String affiliation) {

		char[] cArray = symbol.toCharArray();
		
		char selector = 'S';
		
		if (cArray[0] == selector)
			cArray[1] = affiliation.toCharArray()[0];

		return new String(cArray);

	}

}
