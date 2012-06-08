package de.kp.ames.web.core.transform.cache;
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

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class XslCache {

	private Map<String, XslTransformator> cache = Collections.synchronizedMap(new LinkedHashMap<String, XslTransformator>());
	private int MAX_CACHE_SIZE;
	
	/**
	 * Constructor
	 */
	public XslCache() {
		
		/* 
		 * Obtain the memory cache size
		 */
		try {

			String size = System.getProperty("memcache", "500");
			MAX_CACHE_SIZE = Math.max(1, Integer.parseInt(size));

		} catch(Exception e) {
			MAX_CACHE_SIZE = 500;
		}

	}
	
	/**
	 * @return
	 */
	public int count() {
		return this.cache.size();
	}

	/**
	 * @param key
	 * @return
	 */
	public XslTransformator get(String key) {

		XslTransformator retval = null;
		
		synchronized (cache) {
			if (this.cache.containsKey(key)) {

				/* 
				 * Update position of the object in the LinkedHashMap
				 */

				retval = cache.remove(key);
				cache.put(key, retval);				
			}
		}
		
		return retval;
	}

	/**
	 * @return
	 */
	public List<XslTransformator> getAll() {

		List<XslTransformator> list;

		synchronized (cache) {
			list = new LinkedList<XslTransformator>(this.cache.values());
		}
		
		/* 
		 * Relative position of the objects remains the same since
		 * all objects are read. Therefore no need to update the positions
		 * in the linked hash map 
		 */
		
		return list;
	}

	/**
	 * @return
	 */
	public Set<String> getKeys() {
		
		Set<String> keys;
		synchronized (cache) {
			keys = this.cache.keySet();
		}
		
		return keys;
		
	}

	/**
	 * @param key
	 * @return
	 */
	public boolean hasKey(String key) {
		
		boolean retval = false;
		synchronized (cache) {
			retval = this.cache.containsKey(key);
		}
	
		return retval;
	
	}

	/**
	 * @param key
	 * @param value
	 */
	public void put(String key, XslTransformator value) {

		if (!hasKey(key)) {

			/* 
			 * Make room in the cache to add the new value
			 */
			if(cache.size() > MAX_CACHE_SIZE) {
				synchronized (cache) {

					Iterator<String> iter = cache.keySet().iterator();
					for (int i=0;i<cache.size()-MAX_CACHE_SIZE;i++) {
						/* 
						 * The linked hashmap provides an iterator s.t. the
						 * elements are sorted in the order of their insertion.
						 * The last inserted element is accessed last so
						 * we need to remove the elements at the start
						 */
						iter.next();
						iter.remove();
					}
				}
			}
			synchronized (cache) {
				this.cache.put(key, value);
			}
		}
		
	}

	/**
	 * @param key
	 */
	public void remove(String key) {
		synchronized (cache) {
			this.cache.remove(key);
		}
	}

	/**
	 * Clear cache
	 */
	public void removeAll() {
		synchronized (this.cache) {
			this.cache.clear();
		}
	}

}
