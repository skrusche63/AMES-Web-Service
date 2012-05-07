package de.kp.ames.web.core.json;
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

import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrTransaction;

/**
 * This class is part of the JSON layer on top of
 * the JAXR layer
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */

public class JsonTransaction extends JaxrTransaction {

	private JSONObject jObject;

	public JsonTransaction(JSONObject jObject) {
		this.jObject = jObject;
	}
		
	public JSONObject getjObject() {
		return jObject;
	}

	public void setjObject(JSONObject jObject) {
		this.jObject = jObject;
	}

}
