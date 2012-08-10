package de.kp.ames.web.core.json;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.json
 *  Module: StringCollector
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #collector #core #json #string #web
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONObject;

/**
 * A utility class to sort and collect JSON objects by String,
 * i.e. especially by name; this class is mainly used to provide
 * sorted results to be returned as a service response
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 */

public class StringCollector {

	private Map<String, ArrayList<JSONObject>> collector;

	public StringCollector() {

		collector = new TreeMap<String, ArrayList<JSONObject>>(new Comparator<String>(){
			public int compare(String name1, String name2) {
				return name1.compareTo(name2);
			}
		});
	
	}
	
	/**
	 * A helper method to add a JSON object and its 
	 * assigned name to this sorter and collector
	 * 
	 * @param name
	 * @param jObject
	 */
	public void put(String name, JSONObject jObject) {
		
		if (!collector.containsKey(name)) collector.put(name, new ArrayList<JSONObject>());
		collector.get(name).add(jObject);
		
	}
	
	/**
	 * A method to return a sorted list of JSON objects
	 * 
	 * @return
	 */
	public ArrayList<JSONObject> values() {
		
		ArrayList<JSONObject> values = new ArrayList<JSONObject>();
		
		Iterator<ArrayList<JSONObject>> iterator = collector.values().iterator();
		while (iterator.hasNext()) {
			values.addAll(iterator.next());
		}
		
		return values;

	}
	
}
