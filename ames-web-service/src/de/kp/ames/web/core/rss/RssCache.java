package de.kp.ames.web.core.rss;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.rss
 *  Module: RssCache
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #cache #core #rss #web
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
import java.util.Collections;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;

/**
 * A temporary cache for syndication entries; each entry
 * refers to a certain registry object that has been added
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */
public class RssCache {
	
	private List<SyndEntry> cache = Collections.synchronizedList(new ArrayList<SyndEntry>());
	private int MAX_CACHE_SIZE = 100;
	
	public RssCache() {		
	}

	/**
	 * Number of syndication entries
	 * 
	 * @return
	 */
	public int count() {
		return this.cache.size();
	}

	/**
	 * Get all syndication entries
	 * 
	 * @return
	 */
	public List<SyndEntry> getAll() {

		List<SyndEntry> list;
		synchronized (cache) {
			list = new ArrayList<SyndEntry>(this.cache);
		}
		
		Collections.reverse(list);
		return list;
		
	}

	/**
	 * Add syndication entry
	 * 
	 * @param entry
	 */
	public void add(SyndEntry entry) {

		synchronized (cache) {
			this.cache.add(entry);
		}

		if(cache.size() > MAX_CACHE_SIZE) {
			synchronized (cache) {
				cache.remove(0);
			}
		}

	}

	/**
	 * Clear syndication cache
	 */
	public void clear() {
		synchronized (this.cache) {
			this.cache.clear();
		}
	}

	/**
	 * Remove entry from cache referring
	 * to a specific uri
	 * 
	 * @param uri
	 */
	public void remove(String uri) {
		/*
		 * Determine all cache entries
		 */
		List<SyndEntry> list;
		synchronized (cache) {
			list = new ArrayList<SyndEntry>(this.cache);
		}

		/*
		 * Retrieve entry that matches uri
		 */
		SyndEntry entry = null;
		for (SyndEntry item:list) {
			if (uri.equals(item.getUri())) {
				entry = item;
				break;
			}
		}
		
		/*
		 * Remove entry from cache
		 */
		if (entry != null) {

			synchronized (cache) {
				this.cache.remove(entry);
			}

		}
		
	}
}
