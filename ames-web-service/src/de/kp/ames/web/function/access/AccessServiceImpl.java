package de.kp.ames.web.function.access;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.access
 *  Module: AccessServiceImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #access #function #service #web
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

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.function.access.dav.DavConsumer;
import de.kp.ames.web.function.access.imap.ImapConsumer;
import de.kp.ames.web.function.access.jdbc.JdbcConsumer;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;

public class AccessServiceImpl extends BusinessImpl {

	public AccessServiceImpl() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(MethodConstants.METH_DELETE)) {
			/*
			 * Call delete method
			 */
			doDeleteRequest(ctx);

		} else if (methodName.equals(MethodConstants.METH_GET)) {
			/*
			 * Call get method
			 */
			doGetRequest(ctx);
	
		} else if (methodName.equals(MethodConstants.METH_SUBMIT)) {			
			/*
			 * Call submit method
			 */
			doSubmitRequest(ctx);

		}
		
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doDeleteRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doDeleteRequest(RequestContext ctx) {

		String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);
		String type = this.method.getAttribute(MethodConstants.ATTR_TYPE);	

		if ((item == null) || (type == null)) {
			this.sendNotImplemented(ctx);
			
		} else {

			try {
				/*
				 * JSON response
				 */
				String content = delete(type, item);
				sendJSONResponse(content, ctx.getResponse());

			} catch (Exception e) {
				this.sendBadRequest(ctx, e);

			}
			
		}

	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doGetRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doGetRequest(RequestContext ctx) {

		String format = this.method.getAttribute(MethodConstants.ATTR_FORMAT);	
		String type   = this.method.getAttribute(MethodConstants.ATTR_TYPE);	
		
		if ((format == null) || (type == null)) {
			this.sendNotImplemented(ctx);
			
		} else {

			/*
			 * This is an optional parameter that determines 
			 * a certain registry object
			 */
			String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);

			/*
			 * Evaluate the format parameter to determine the 
			 * format for the http response 
			 */
			if (format.startsWith(FormatConstants.FNC_FORMAT_ID_File)) {

				String source = this.method.getAttribute(MethodConstants.ATTR_SOURCE);
				if (source == null) {
					this.sendNotImplemented(ctx);
				
				} else {
					
					try {
						FileUtil file = getFileResponse(type, item, source);
						sendFileResponse(file, ctx.getResponse());
	
					} catch (Exception e) {
						this.sendBadRequest(ctx, e);
	
					}
					
				}

			} else if (format.startsWith(FormatConstants.FNC_FORMAT_ID_Html)) {

				String source = this.method.getAttribute(MethodConstants.ATTR_SOURCE);
				if (source == null) {
					this.sendNotImplemented(ctx);
				
				} else {
					
					try {
						String html = getHtmlResponse(type, item, source);
						sendHTMLResponse(html, ctx.getResponse());
	
					} catch (Exception e) {
						this.sendBadRequest(ctx, e);
	
					}
					
				}

			} else if (format.startsWith(FormatConstants.FNC_FORMAT_ID_Image)) {
			
				String source = this.method.getAttribute(MethodConstants.ATTR_SOURCE);
				if (source == null) {
					this.sendNotImplemented(ctx);
				
				} else {
					
					try {
						BufferedImage image = getImageResponse(type, item, source);
						sendImageResponse(image, ctx.getResponse());
	
					} catch (Exception e) {
						this.sendBadRequest(ctx, e);
	
					}
					
				}
				
			} else if (format.startsWith(FormatConstants.FNC_FORMAT_ID_Json)) {
				/*
				 * Optional parameters that may be used to describe
				 * a Grid-oriented response
				 */
				String start = this.method.getAttribute(FncConstants.ATTR_START);
				String limit = this.method.getAttribute(FncConstants.ATTR_LIMIT);

				try {
					String content = getJSONResponse(type, item, start, limit, format);
					sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}

			}
			
		}

	}	
	
	/**
	 * Retrieve a file from an external data source
	 * 
	 * @param type
	 * @param item
	 * @return
	 */
	private FileUtil getFileResponse(String type, String item, String source) throws Exception {

		FileUtil file = null;
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		

		AccessDQM dqm = new AccessDQM(jaxrHandle);
		JSONObject jAccessor = dqm.getAccessor(item);
		
		if (type.equals(ClassificationConstants.FNC_ID_Mail)) {
			
			/*
			 * Retrieve a certain mail attachment
			 * from an external IMAP server
			 */
			ImapConsumer consumer = new ImapConsumer(jAccessor);
			file = consumer.getAttachment(Integer.parseInt(source));
			
		} else if (type.equals(ClassificationConstants.FNC_ID_WebDav)) {
			
			/*
			 * Retrieve a certain file from a WebDAV server
			 */
			DavConsumer consumer = new DavConsumer(jAccessor);
			file = consumer.getFile();
			
		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return file;

	}

	/**
	 * Retrieve a html artefact
	 * 
	 * @param type
	 * @param item
	 * @return
	 */
	private String getHtmlResponse(String type, String item, String source) throws Exception {

		String html = null;
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		

		AccessDQM dqm = new AccessDQM(jaxrHandle);
		JSONObject jAccessor = dqm.getAccessor(item);
		
		if (type.equals(ClassificationConstants.FNC_ID_Mail)) {
			
			/*
			 * Retrieve a certain mail attachment
			 * from an external IMAP server
			 */
			ImapConsumer consumer = new ImapConsumer(jAccessor);
			html = consumer.getHTMLMessage(Integer.parseInt(source));
			
		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return html;

	}

	/**
	 * Retrieve an image from an external data source
	 * 
	 * @param type
	 * @param item
	 * @return
	 */
	private BufferedImage getImageResponse(String type, String item, String source) throws Exception {

		FileUtil file = null;
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		

		AccessDQM dqm = new AccessDQM(jaxrHandle);
		JSONObject jAccessor = dqm.getAccessor(item);
		
		if (type.equals(ClassificationConstants.FNC_ID_Mail)) {
			
			/*
			 * Retrieve a certain mail attachment
			 * from an external IMAP server
			 */
			ImapConsumer consumer = new ImapConsumer(jAccessor);
			file = consumer.getAttachment(Integer.parseInt(source));
			
		} else if (type.equals(ClassificationConstants.FNC_ID_WebDav)) {
			
			/*
			 * Retrieve a certain file from a WebDAV server
			 */
			DavConsumer consumer = new DavConsumer(jAccessor);
			file = consumer.getFile();
			
		}

		/*
		 * Convert most popular image formats into 'png'
		 * when sending the respective image (see sendImageResponse)
		 */
		BufferedImage image = ImageIO.read(file.getInputStream());
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return image;

	}

	/**
	 * Retrieve access specific information objects either
	 * as a single object or a set of objects
	 * 
	 * @param type
	 * @param item
	 * @param start
	 * @param limit
	 * @param format
	 * @return
	 * @throws Exception
	 */
	private String getJSONResponse(String type, String item, String start, String limit, String format) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		

		if (type.equals(ClassificationConstants.FNC_ID_Accessor)) {

			/*
			 * This is a predefined request to retrieve
			 * all registered accessors (in this case
			 * the response format is always JSON)
			 */
			AccessDQM dqm = new AccessDQM(jaxrHandle);
			JSONArray jArray = dqm.getAccessors(item);
			
			/*
			 * Render result
			 */
			if ((start == null) || (limit == null)) {
				content = render(jArray, format);
	
			} else {
				content = render(jArray, start, limit, format);
			}

		} else if (type.equals(ClassificationConstants.FNC_ID_Database)) {

			AccessDQM dqm = new AccessDQM(jaxrHandle);
			JSONObject jAccessor = dqm.getAccessor(item);

			/*
			 * Retrieve a certain database context
			 * from an external database server
			 */
			JdbcConsumer consumer = new JdbcConsumer(jAccessor);
			JSONObject jResult = consumer.getJRecords();
			
			content = jResult.toString();
			consumer.close();			

		} else if (type.equals(ClassificationConstants.FNC_ID_Mail)) {

			AccessDQM dqm = new AccessDQM(jaxrHandle);
			JSONObject jAccessor = dqm.getAccessor(item);

			/*
			 * Retrieve a list of mails from a 
			 * certain IMAP server
			 */
			ImapConsumer consumer = new ImapConsumer(jAccessor);
			JSONArray jArray = consumer.getJMessages();
			
			/*
			 * Render result
			 */
			if ((start == null) || (limit == null)) {
				content = render(jArray, format);
	
			} else {
				content = render(jArray, start, limit, format);
			}
			
		} else if (type.equals(ClassificationConstants.FNC_ID_WebDav)) {

			AccessDQM dqm = new AccessDQM(jaxrHandle);
			JSONObject jAccessor = dqm.getAccessor(item);

			/*
			 * Retrieve resources (folder descriptions) from
			 * a certain WebDAV server
			 */
			DavConsumer consumer = new DavConsumer(jAccessor);
			JSONArray jArray = consumer.getResources();
			
			/*
			 * Render result
			 */
			if ((start == null) || (limit == null)) {
				content = render(jArray, format);
	
			} else {
				content = render(jArray, start, limit, format);
			}
			
		} else {
			throw new Exception("[AccessServiceImpl] Information type <" + type + "> is not supported");
			
		}

		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doSubmitRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doSubmitRequest(RequestContext ctx) {

		String data = this.getRequestData(ctx);
		if (data == null) {
			this.sendNotImplemented(ctx);
			
		} else {

			try {
				/*
				 * JSON response
				 */
				String content = submit(data);
				sendJSONResponse(content, ctx.getResponse());

			} catch (Exception e) {
				this.sendBadRequest(ctx, e);

			}
			
		}

	}

	/**
	 * A helper method to delete an accessor
	 * 
	 * @param type
	 * @param item
	 * @return
	 * @throws Exception
	 */
	private String delete(String type, String item) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		

		AccessLCM lcm = new AccessLCM(jaxrHandle);
		content = lcm.deleteAccessor(item);
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

	/**
	 * Submit accessor
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private String submit(String data) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

		AccessLCM lcm = new AccessLCM(jaxrHandle);
		content = lcm.submitAccessor(data);
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

}
