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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONObject;

/**
 * A utility class to sort and collect JSON objects by Number;
 * this class is mainly used to provide sorted results to be 
 * returned as a service response
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 */
public class DoubleCollector {

	private Map<Double, ArrayList<JSONObject>> collector;

	public DoubleCollector() {

		collector = new TreeMap<Double, ArrayList<JSONObject>>(new Comparator<Double>(){
			public int compare(Double double1, Double double2) {
				return double1.compareTo(double2);
			}
		});

	}
		
	/**
	 * A helper method to add a JSON object and its 
	 * assigned number to this sorter and collector
	 * 
	 * @param number
	 * @param jObject
	 */
	public void put(Double number, JSONObject jObject) {
		
		if (!collector.containsKey(number)) collector.put(number, new ArrayList<JSONObject>());
		collector.get(number).add(jObject);
		
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
