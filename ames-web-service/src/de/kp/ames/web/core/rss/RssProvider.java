package de.kp.ames.web.core.rss;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.rss
 *  Module: RssProvider
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #provider #rss #web
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
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.json.JSONArray;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;

import de.kp.ames.web.Bundle;
import de.kp.ames.web.GlobalConstants;

public class RssProvider {
	
	private static String FEED_TYPE = "rss_2.0";
	private static String FEED_URI  = Bundle.getInstance().getString(GlobalConstants.RSS_ENDPOINT);
	
	private static String FEED_TITLE = GlobalConstants.FEED_TITLE;
	private static String FEED_DESC  = GlobalConstants.FEED_DESC;
	
	private static RssCacheManager processor;
	
	public RssProvider() {
		processor = RssCacheManager.getInstance();
	}
	
	public String getFeed() {
		
		try {
			
			SyndFeed feed = new SyndFeedImpl();
			
			/* 
			 * Describe feed
			 */
			feed.setTitle(FEED_TITLE);
			feed.setDescription(FEED_DESC);
			
			feed.setFeedType(FEED_TYPE);
			feed.setUri(FEED_URI);
			
			/* 
			 * Finally we get feeds from processor
			 */
			List<SyndEntry> entries = processor.getEntries();
			feed.setEntries(entries);
			
			Writer writer = new StringWriter();
		      
			SyndFeedOutput output = new SyndFeedOutput();
		    output.output(feed, writer);
		      
		    String content = writer.toString();
		    writer.close();

		    return content;
		    
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {}
		
		return null;
		
	}

	/**
	 * Get RSS feed in JSON representation
	 * 
	 * @return
	 */
	public JSONArray getJFeed() {
		
		try {
			
			List<SyndEntry> entries = processor.getEntries();
			return RssConverter.getJFeed(entries);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {}
		
		return null;

	}

}

