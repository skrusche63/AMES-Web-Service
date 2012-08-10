package de.kp.ames.web.core.rss;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.rss
 *  Module: RssCacheManager
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #cache #core #manager #rss #web
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
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;

public class RssCacheManager {

	private static RssCacheManager instance = new RssCacheManager();
	private RssCache cache;
	
	private RssCacheManager() {
		cache = new RssCache();
	}
		
	public static RssCacheManager getInstance() {
		if (instance == null) instance = new RssCacheManager();
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
	
	/**
	 * Remove syndication entry identified
	 * by its uri
	 * 
	 * @param uri
	 */
	public void removeEntry(String uri) {
		cache.remove(uri);
	}
}
