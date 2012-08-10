package de.kp.ames.web.core.rss;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.rss
 *  Module: RssConsumer
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #consumer #core #rss #web
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
import java.net.URL;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpClientFeedFetcher;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * RSS Client. Based on the ROME FeedFetcher project.
 * Provides a single execute() method to point to the 
 * RSS based webservice.
 * 
 * The response is RSS 2.0 XML, which is converted into 
 * a SyndFeed object and returned to the caller to parse 
 * as needed.
 */

public class RssConsumer {
	  
	private URL serviceUrl;
	private HttpClientFeedFetcher fetcher = null;

	private static final boolean USE_CACHE = true;

	private static final int DEFAULT_CONN_TIMEOUT = 5000;
	private static final int DEFAULT_READ_TIMEOUT = 1000;

	public RssConsumer() {	
	}
	
	public RssConsumer(URL serviceUrl) {
		setup(serviceUrl, USE_CACHE, DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT);
	}
	
	public RssConsumer(URL serviceUrl, boolean useLocalCache, int connectTimeout, int readTimeout) {
		setup(serviceUrl, useLocalCache, connectTimeout, readTimeout);
	}
	
	/**
	 * Setup Feed Fetcher
	 * 
	 * @param serviceUrl
	 */
	public void setup(URL serviceUrl) {
		setup(serviceUrl, USE_CACHE, DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT);		
	}
	
	/**
	 * Setup Feed Fetcher
	 * 
	 * @param serviceUrl
	 * @param useLocalCache
	 * @param connectTimeout
	 * @param readTimeout
	 */
	public void setup(URL serviceUrl, boolean useLocalCache, int connectTimeout, int readTimeout) {

		this.serviceUrl = serviceUrl;

	    fetcher = new HttpClientFeedFetcher();
	    
	    fetcher.setUserAgent("SMClientFetcher-1.0");
	    fetcher.setConnectTimeout(connectTimeout);
	    fetcher.setReadTimeout(readTimeout);

	    if (useLocalCache) fetcher.setFeedInfoCache(HashMapFeedInfoCache.getInstance());
		
	}
 
	/**
	 * Executes a service request and returns 
	 * a ROME SyndFeed object
	 * 
	 * @param method
	 * @return
	 */
	public SyndFeed execute(String method) {
		
		Map<String,String> params = new HashMap<String,String>();
		return execute(method, params);
		
	}
	
	/**
	 * Executes a service request and returns 
	 * a ROME SyndFeed object
	 * 
	 * @param method
	 * @param params
	 * @return
	 */
	public SyndFeed execute(String method, Map<String,String> params) {

		URL feedUrl = buildUrl(method, params);
	    SyndFeed feed = null;
	    
	    try {

	    	feed = fetcher.retrieveFeed(feedUrl);
	      
	    } catch (FetcherException e) {
	    	throw new RuntimeException("Failed to fetch URL:[" + feedUrl.toExternalForm() + "]. HTTP Response code:[" + 
	        e.getResponseCode() + "]", e);
	    
	    } catch (FeedException e) {
	    	throw new RuntimeException("Failed to parse response for URL:[" + feedUrl.toString() + "]", e);
	    
	    } catch (IOException e) {
	    	throw new RuntimeException("IO Error fetching URL:[" + feedUrl.toString() + "]", e);

	    }
	    
	    return feed;
	}

	/**
	 * A convenience method to build up the request URL from the
	 * method name and the Map of query parameters
	 * 
	 * @param methodName
	 * @param params
	 * @return
	 */
	private URL buildUrl(String methodName, Map<String,String> params) {

		StringBuilder urlBuilder = new StringBuilder(serviceUrl.toString());
	    urlBuilder.append("/").append(methodName);
	    
	    int numParams = 0;
	    
	    for (String key : params.keySet()) {

	    	String value = params.get(key);
	    	if (StringUtils.isBlank(value)) continue;

	    	try {
	    		value = URLEncoder.encode(value, "UTF-8");
	    	
	    	} catch (UnsupportedEncodingException e) {
	    		/* 
	    		 * Will never happen, but just in case it does, 
	    		 * we throw the error up
	    		 */
	    		throw new RuntimeException(e);
	    	}
	    	
	    	urlBuilder.append(numParams == 0 ? "?" : "&");
	    	urlBuilder.append(key);
	    	
	    	urlBuilder.append("=");
	    	urlBuilder.append(value);

	    	numParams++;
	    }
	    
	    try {
	      return new URL(urlBuilder.toString());

	    } catch (MalformedURLException e) {
	      throw new RuntimeException("Malformed URL:[" + urlBuilder.toString() + "]", e);
	    
	    }
	  
	}

	/**
	 * Get RSS feed in native representation
	 * 
	 * @param method
	 * @return
	 */
	public String getFeed(String method) {
		
		try {
			SyndFeed feed = execute(method);

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
	public String getJFeed(String method) {
		
		try {
			SyndFeed feed = execute(method);
			JSONObject jFeed = RssConverter.getJFeed(feed);
			
			return jFeed.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {}
		
		return null;

	}
}

