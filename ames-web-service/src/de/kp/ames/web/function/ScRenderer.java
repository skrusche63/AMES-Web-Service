package de.kp.ames.web.function;
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
	 * @see de.kp.ames.web.function.GuiRenderer#createGrid(org.json.JSONArray, java.lang.String, java.lang.String)
	 */
	public String createGrid(JSONArray jArray, String start, String limit) {
		
		JSONObject jResponse = new JSONObject();
		int card = jResponse.length();
		try {
			
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

		} catch(Exception e) {
			e.printStackTrace();
			
		} finally {}

		
		return jResponse.toString();
		
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
