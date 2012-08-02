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
import org.json.JSONArray;

public interface GuiRenderer {

	/**
	 * Build a Grid representation from a JSON array
	 * 
	 * @param jArray
	 * @return
	 * @throws Exception
	 */
	public String createGrid(JSONArray jArray) throws Exception;

	/**
	 * Build a Grid representation from a JSON array
	 * where the total number is different from the
	 * length of the array
	 * 
	 * @param jArray
	 * @param total
	 * @return
	 * @throws Exception
	 */
	public String createGrid(JSONArray jArray, long total) throws Exception ;

	/**
	 * Build a paged GUI grid representation from a JSON array
	 * 
	 * @param jArray
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public String createGrid(JSONArray jArray, String start, String limit) throws Exception;
	
	/**
	 * Build a Object representation from a JSON Array
	 * 
	 * @param jArray
	 * @return
	 * @throws Exception
	 */
	public String createObject(JSONArray jArray) throws Exception;

	/**
	 * Build a Tree representation from a JSON Array
	 * 
	 * @param jArray
	 * @return
	 * @throws Exception
	 */
	public String createTree(JSONArray jArray) throws Exception;

	/**
	 * Build a Tree representation from a JSON Array
	 * with a reference to a parent node
	 * 
	 * @param jArray
	 * @param parent
	 * @return
	 * @throws Exception
	 */
	public String createTree(JSONArray jArray, String parent) throws Exception;

	/*
	 * Paging paramters depend on registered Gui
	 */
	public String getStartParam();
	public String getLimitParam();
	
	/**
	 * Icon parameter
	 * 
	 * @return
	 */
	public String getIconParam();

	/**
	 * Parent parameter
	 * 
	 * @return
	 */
	public String getParentParam();
	
}
