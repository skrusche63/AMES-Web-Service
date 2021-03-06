package de.kp.ames.web.function;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function
 *  Module: BusinessImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #business #function #web
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

import org.json.JSONArray;

import de.kp.ames.web.core.render.GuiFactory;
import de.kp.ames.web.core.render.GuiRenderer;
import de.kp.ames.web.core.service.ServiceImpl;
import de.kp.ames.web.shared.constants.FormatConstants;

public class BusinessImpl extends ServiceImpl {

	/*
	 * Reference to the registered renderer
	 */
	protected GuiRenderer renderer;

	public BusinessImpl() {
		renderer = GuiFactory.getInstance().getRenderer();
	}
	
	/**
	 * A helper method to render a JSON array
	 * 
	 * @param jArray
	 * @param format
	 * @return
	 * @throws Exception
	 */
	protected String render(JSONArray jArray, String format) throws Exception {

		if (format.equals(FormatConstants.FNC_FORMAT_ID_Grid)) {
			/*
			 * Render JSONArray as a GUI
			 */
			return renderer.createGrid(jArray);

		} else if (format.equals(FormatConstants.FNC_FORMAT_ID_Object)) {
			/*
			 * Render JSONArray as a single JSON object
			 */
			return renderer.createObject(jArray);

		} else if (format.equals(FormatConstants.FNC_FORMAT_ID_Tree)) {
			/*
			 * Render JSONArray as a Tree
			 */
			return renderer.createTree(jArray);
			
		} else {
			throw new Exception("[BusinessImpl] Format <" + format + "> not supported.");

		}
		
	}
	
	/**
	 * A helper method to render a JSON array with
	 * respect to a certain parent
	 * 
	 * @param jArray
	 * @param parent
	 * @param format
	 * @return
	 * @throws Exception
	 */
	protected String render(JSONArray jArray, String parent, String format) throws Exception {

		if (format.equals(FormatConstants.FNC_FORMAT_ID_Tree)) {
			/*
			 * Render JSONArray as a Tree
			 */
			return renderer.createTree(jArray, parent);

		} else {
			throw new Exception("[BusinessImpl] Format <" + format + "> not supported.");
			
		}

	}
	
	/**
	 * A helper method to render a JSON array
	 * 
	 * @param jArray
	 * @param start
	 * @param limit
	 * @param format
	 * @return
	 * @throws Exception
	 */
	protected String render(JSONArray jArray, String start, String limit, String format) throws Exception {

		if (format.equals(FormatConstants.FNC_FORMAT_ID_Grid)) {
			return renderer.createGrid(jArray, start, limit);
			
		} else {
			throw new Exception("[BusinessImpl] Format <" + format + "> not supported.");

		}

	}

}
