package de.kp.ames.web.core.rss;
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
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;

public class RssProcessor {

	private static RssProcessor instance = new RssProcessor();
	private RssCache cache;
	
	private RssProcessor() {
		cache = new RssCache();
	}
		
	public static RssProcessor getInstance() {
		if (instance == null) instance = new RssProcessor();
		return instance;
	}
	
	/**
	 * Add syndication entry
	 * 
	 * @param entry
	 */
	public void addEntry(SyndEntry entry) {
		cache.add(entry);
	}
		
	/**
	 * Get all syndication entries
	 * 
	 * @return
	 */
	public List<SyndEntry> getEntries() {
		return cache.getAll();
	}
	
}
