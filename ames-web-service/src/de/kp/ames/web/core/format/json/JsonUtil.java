package de.kp.ames.web.core.format.json;
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

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	
	/**
	 * A helper method to retrieve Array-based attribute 
	 * values from the provided JSONObject
	 * 
	 * @param key
	 * @param jObject
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<String> getAttributeAsArray(String key, JSONObject jObject) throws JSONException {

		ArrayList<String> values = null;
		
		if (jObject.has(key)) {
			
			/* 
			 * Retrieve json array from in more fault tolerant manner
			 */
			JSONArray jArray = getJArray(key, jObject);
			if (jArray.length() > 0) {
				
				values = new ArrayList<String>();
				for (int i=0; i < jArray.length(); i++) {
					values.add(jArray.getString(i));
				}
			}

		}
		
		return values;
		
	}

	/**
	 * @param key
	 * @param jObject
	 * @return
	 */
	public static JSONArray getJArray(String key, JSONObject jObject) {
		
		JSONArray jArray = new JSONArray();
		
		if (!jObject.has(key)) return jArray;

		try {
			
			/* 
			 * First, try to retrieve the expected JSONArray
			 * by regular means
			 */
			return jObject.getJSONArray(key);
			
		} catch(JSONException e) {
			/* 
			 * Second, try to reconstruct the respective 
			 * JSONArray from a string
			 */
			try {
				return new JSONArray(jObject.getString(key));

			} catch (JSONException ex) {}
			
		} finally {
			// do nothing
		}
		
		return jArray;
		
	}

}
