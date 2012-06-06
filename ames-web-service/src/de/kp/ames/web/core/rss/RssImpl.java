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
import java.net.URL;

import de.kp.ames.web.core.RequestContext;
import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.core.service.ServiceImpl;
import de.kp.ames.web.function.FncConstants;

/**
 * RSSImpl supports the retrieval from either local or
 * remote RSS feeds in JSON or native RSS representation
 */

public class RssImpl extends ServiceImpl {

	private RssProvider provider;
	private RssConsumer consumer;
	
	private static String UNKNOWN_RSS_REQUEST = "[RssImpl] Unknown RSS Request.";
	
	/**
	 * Constructor
	 */
	public RssImpl() {	
		consumer = new RssConsumer();
		provider = new RssProvider();		
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.core.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(FncConstants.METH_GET)) {
			
			/*
			 * Call getXXFeed method
			 */
			String format = this.method.getAttribute(FncConstants.ATTR_FORMAT);
			String type   = this.method.getAttribute(FncConstants.ATTR_TYPE);
			
			if ((format == null) || (type == null)) {
				this.sendNotImplemented(ctx);
				
			} else {
				
				try {
					
					/*
					 * An optional parameter that reference an external RSS service
					 */
					String uri = this.method.getAttribute(FncConstants.ATTR_URI);
					
					/*
					 * Distinguish between two different formats
					 */
					if (format.equals(FncConstants.FNC_FORMAT_ID_Json)) {
						
						String content = getJFeed(type, uri);
						this.sendJSONResponse(content, ctx.getResponse());
						
					} else if (format.equals(FncConstants.FNC_FORMAT_ID_Rss)) {

						String content = getRssFeed(type, uri);
						this.sendRSSResponse(content, ctx.getResponse());

					}

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			}
			
		}
		
	}

	/**
	 * Get RSS feed in JSON representation
	 * 
	 * @param type
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	public String getJFeed(String type, String uri) throws Exception {		

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);
		if (type.equals("local")) {
			/*
			 * Retrieve OASIS ebXML RegRep RSS feed (local)
			 */
			content = provider.getJFeed();
		
		} else if (type.equals("remote")) {
			/*
			 * Retrieve RSS feed from a remote service
			 */
			if (uri == null) {
				/*
				 * Logoff
				 */
				JaxrClient.getInstance().logoff(jaxrHandle);
				/*
				 * Throw exception
				 */
				throw new Exception(UNKNOWN_RSS_REQUEST);
				
			} else {

				/* 
				 * Access a certain rss servic and retrieve all entries
				 * of a specific feed; the result is returned as a list
				 */

				/* 
				 * Prepare uri for feed consumer
				 */
				int pos = uri.lastIndexOf("/");
				
				String url    = uri.substring(0, pos);
				String method = uri.substring(pos+1);

				/*
				 * Initialize Feed Fetcher
				 */
				consumer.setup(new URL(url));
				/*
				 * Retrieve RSS feed in JSON representation
				 */
				content = consumer.getJFeed(method);
				
			}
			
		} else {
			/*
			 * Logoff
			 */
			JaxrClient.getInstance().logoff(jaxrHandle);
			/*
			 * Throw exception
			 */
			throw new Exception(UNKNOWN_RSS_REQUEST);
			
		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
		
	}
	
	/**
	 * Get RSS feed in native representation
	 * 
	 * @param type
	 * @param uri
	 * @return
	 * @throws Exception 
	 */
	public String getRssFeed(String type, String uri) throws Exception {		

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);
		if (type.equals("local")) {
			/*
			 * Retrieve OASIS ebXML RegRep RSS feed (local)
			 */
			content = provider.getFeed();	
			
		} else if (type.equals("remote")) {
			/*
			 * Retrieve RSS feed from a remote service
			 */
			if (uri == null) {
				/*
				 * Logoff
				 */
				JaxrClient.getInstance().logoff(jaxrHandle);
				/*
				 * Throw exception
				 */
				throw new Exception(UNKNOWN_RSS_REQUEST);
				
			} else {

				/* 
				 * Access a certain rss servic and retrieve all entries
				 * of a specific feed; the result is returned as a list
				 */

				/* 
				 * Prepare uri for feed consumer
				 */
				int pos = uri.lastIndexOf("/");
				
				String url    = uri.substring(0, pos);
				String method = uri.substring(pos+1);

				/*
				 * Initialize Feed Fetcher
				 */
				consumer.setup(new URL(url));
				/*
				 * Retrieve RSS feed in JSON representation
				 */
				content = consumer.getFeed(method);

			}
			
		} else {
			/*
			 * Logoff
			 */
			JaxrClient.getInstance().logoff(jaxrHandle);
			/*
			 * Throw exception
			 */
			throw new Exception(UNKNOWN_RSS_REQUEST);
			
		}

		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
		
	}

}
