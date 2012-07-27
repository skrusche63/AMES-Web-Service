package de.kp.ames.web.core.render;
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
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A helper class to render results for a SmartGwt 3.0 GUI
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */
public class ScRenderer implements GuiRenderer {

	public ScRenderer() {
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.function.GuiRenderer#createGrid(org.json.JSONArray)
	 */
	public String createGrid(JSONArray jArray) throws Exception {

		JSONObject jResponse = new JSONObject();
		int card = jArray.length();
		
		jResponse.put(ScConstants.SC_STATUS, 0);	
		jResponse.put(ScConstants.SC_TOTALROWS, card);

		jResponse.put(ScConstants.SC_DATA, jArray);		
		return jResponse.toString();
		

	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.render.GuiRenderer#createGrid(org.json.JSONArray, long)
	 */
	public String createGrid(JSONArray jArray, long total) throws Exception {

		JSONObject jResponse = new JSONObject();
		
		jResponse.put(ScConstants.SC_STATUS, 0);	
		jResponse.put(ScConstants.SC_TOTALROWS, total);

		jResponse.put(ScConstants.SC_DATA, jArray);		
		return new JSONObject().put(ScConstants.SC_RESPONSE, jResponse).toString();
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.function.GuiRenderer#createGrid(org.json.JSONArray, java.lang.String, java.lang.String)
	 */
	public String createGrid(JSONArray jArray, String start, String limit) throws Exception {
		
		JSONObject jResponse = new JSONObject();
		int card = jArray.length();
			
		jResponse.put(ScConstants.SC_STATUS, 0);			
		jResponse.put(ScConstants.SC_STARTROW, start);
		
		jResponse.put(ScConstants.SC_ENDROW, limit);
		jResponse.put(ScConstants.SC_TOTALROWS, card);

		/* 
		 * Number of sorted object is sliced with the provided 
		 * start and limit parameters
		 */
		
		int begin = this.toIntegerValue(start);
		int end   = begin + this.toIntegerValue(limit);
		
		end = (card < end) ? card : end;
		
		Collection<JSONObject> list = new ArrayList<JSONObject>();
		for (int i=begin; i < end; i++) {
			list.add(jArray.getJSONObject(i));
		}
		
		jResponse.put(ScConstants.SC_DATA, new JSONArray(list));		
		return new JSONObject().put(ScConstants.SC_RESPONSE, jResponse).toString();
		
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.function.GuiRenderer#createObject(org.json.JSONArray)
	 */
	public String createObject(JSONArray jArray) throws Exception {
		// TODO
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.function.GuiRenderer#createTree(org.json.JSONArray)
	 */
	public String createTree(JSONArray jArray) throws Exception {
		// TODO
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.function.GuiRenderer#getStartParam()
	 */
	public String getStartParam() {
		return ScConstants.SC_START;
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.function.GuiRenderer#getLimitParam()
	 */
	public String getLimitParam() {
		return ScConstants.SC_LIMIT;
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.function.GuiRenderer#getIconParam()
	 */
	public String getIconParam() {
		return ScConstants.SC_ICON;
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.function.GuiRenderer#getParentParam()
	 */
	public String getParentParam() {
		return ScConstants.SC_PARENT;
	}
	
	/**
	 * @param value
	 * @return
	 */
	private int toIntegerValue(String value) {

		try {
			return Float.valueOf(value).intValue();
		
		} catch (NumberFormatException e) {
			// do nothing

		} finally {
			// do nothing
		}
		
		return 0;

	}

}
