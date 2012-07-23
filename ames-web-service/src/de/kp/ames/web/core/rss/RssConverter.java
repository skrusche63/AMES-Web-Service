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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;

import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.shared.constants.JaxrConstants;

public class RssConverter {

	private static String NAME = JaxrConstants.RIM_NAME;
	private static String DESC = JaxrConstants.RIM_DESC;
	private static String URI  = JaxrConstants.RIM_URI;

	/**
	 * Returns RSS feed as JSON array; 
	 * note, that feed entries match to 
	 * external links
	 * 
	 * @param feed
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getJFeed(SyndFeed feed) throws Exception {

		JSONObject jFeed = new JSONObject();
		
		jFeed.put("total", 0);
		jFeed.put("list", new JSONArray());
		
		JSONArray jList = new JSONArray();

		List<?> entries = feed.getEntries();
		for (int i=0; i < entries.size(); i++) {

			SyndEntry entry = (SyndEntry)entries.get(i);

	    	JSONObject jEntry = new JSONObject();

	    	jEntry.put(NAME, entry.getTitle());
	    	jEntry.put(DESC, entry.getDescription().getValue());
	    	
	    	jEntry.put(URI,  entry.getLink());

	    	jList.put(i, jEntry);

		}
		
		jFeed.put("total", jList.length());
		jFeed.put("list", jList);

		return jFeed;
	  
	}

	/**
	 * Returns a list of syndication entries as JSON object
	 * 
	 * @param entries
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getJFeed(List<SyndEntry> entries) throws Exception {
		
		JSONObject jFeed = new JSONObject();
		
		jFeed.put("total", 0);
		jFeed.put("list", new JSONArray());
		
		JSONArray jList = new JSONArray();
		
		for (SyndEntry entry:entries) {

			Date date = entry.getUpdatedDate();
			if (date == null) continue;
			
			JSONObject jEntry = new JSONObject();
			
			// uri
			jEntry.put("uri", entry.getUri());
			
			// title
			jEntry.put("title", entry.getTitle());
						
			// description	
			SyndContent desc = entry.getDescription();	 
			jEntry.put("desc", desc.getValue());
			
	        // author
			jEntry.put("author", entry.getAuthor());
	        
	        // published date
			jEntry.put("date", entry.getUpdatedDate().toString());
	 
	        // category
			List<?> categories = entry.getCategories();		
			
			JSONArray jCategories = new JSONArray();
			for (int i=0; i < categories.size(); i++) {

				SyndCategory category = (SyndCategory)categories.get(i);
				jCategories.put(category.getName());
			
			}
	    
			jEntry.put("categories", jCategories.toString());

			// home
			jEntry.put("home", entry.getLink());
			jList.put(jEntry);
			
		}

		jFeed.put("total", jList.length());
		jFeed.put("list", jList);

		return jFeed;
		
	}
	
	/**
	 * A public method to convert a certain registry object 
	 * as a new syndication entry
	 * 
	 * @param ro
	 * @param jaxrHandle
	 * @param domain
	 * @return
	 */
	public static SyndEntry convertRegistryObject(RegistryObjectImpl ro, JaxrHandle jaxrHandle, String domain) {
		
		SyndEntry entry = null;
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		try {

			entry = new SyndEntryImpl();
			
			/* 
			 * The identifier of the respective registry 
			 * object is set to the uri 
			 */
			
			entry.setUri(ro.getId());

			/* 
			 * Entry title
			 */

			String eventType = dqm.getLastEventType(ro);
			if (eventType == null) eventType = "created";
			
			String title = ro.getDisplayName() + " (" + eventType + ")";
			entry.setTitle(title);
			
			/* 
			 * Description using default locale
			 */
			SyndContent desc = new SyndContentImpl();			 
			desc.setType(GlobalConstants.MT_HTML);
			
			desc.setValue(dqm.getDescription(ro));	        
	        entry.setDescription(desc);
			
	        /* 
	         * Author name
	         */
	        String author = dqm.getAuthor(ro);
	        entry.setAuthor(author);
	        
	        /* 
	         * Published date
	         */
	        Date date = dqm.getLastModified(ro);
	        entry.setUpdatedDate(date);
	 
	        /* 
	         * categories = domain, object type
	         */
			List<SyndCategory> categories = new ArrayList<SyndCategory>();			
			SyndCategory category = null;

			// domain
			category = new SyndCategoryImpl();
			
			category.setName(domain); 
			categories.add(category);
			
			// object type
			category = new SyndCategoryImpl();

			category.setName(ro.getObjectType().getKey().getId());    
			categories.add(category);
	    
			entry.setCategories(categories);

			/*
			 * Link parameter
			 * 
			 * the link parameter is actually set to the home address 
			 * of the respective registry; in a federated environment, 
			 * these parameters determine a certain registry object uniquely
			 */
			
			String home = dqm.getHome(ro);
			entry.setLink(home);
			
	        
		} catch (JAXRException e) {
			e.printStackTrace();
			
		} finally {}
		
		return entry;
		
	}

}
