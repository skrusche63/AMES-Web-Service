package de.kp.ames.web.core.json;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.json
 *  Module: DateCollector
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #collector #core #date #json #web
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
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONObject;

/**
 * A utility class to sort and collect JSON objects by Date;
 * this class is mainly used to provide sorted results to be 
 * returned as a service response
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 */

public class DateCollector {

	private Map<Date, ArrayList<JSONObject>> collector;

	public DateCollector() {

		collector = new TreeMap<Date, ArrayList<JSONObject>>(new Comparator<Date>(){
			public int compare(Date date1, Date date2) {				
				// this is a descending sorting of two dates
				return (-1) * date1.compareTo(date2);				
			}
		});
	
	}
	
	/**
	 * A helper method to add a JSON object and its 
	 * assigned date to this sorter and collector
	 * 
	 * @param date
	 * @param jObject
	 */
	public void put(Date date, JSONObject jObject) {
		
		if (!collector.containsKey(date)) collector.put(date, new ArrayList<JSONObject>());
		collector.get(date).add(jObject);
		
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
