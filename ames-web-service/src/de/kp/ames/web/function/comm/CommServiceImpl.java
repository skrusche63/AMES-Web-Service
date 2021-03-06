package de.kp.ames.web.function.comm;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.comm
 *  Module: CommServiceImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #comm #function #service #web
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

import java.io.InputStream;
import java.net.URLDecoder;

import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.JaxrConstants;
import de.kp.ames.web.shared.constants.MethodConstants;

public class CommServiceImpl extends BusinessImpl {

	public CommServiceImpl() {	
		super();
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.core.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(MethodConstants.METH_GET)) {
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
				
			if (format.startsWith(FormatConstants.FNC_FORMAT_ID_Json)) {
				/*
				 * Optional parameters that may be used to describe
				 * a Grid-oriented response
				 */
				String start = this.method.getAttribute(FncConstants.ATTR_START);
				String limit = this.method.getAttribute(FncConstants.ATTR_LIMIT);

				try {
					/*
					 * JSON response
					 */
					String content = getJSONResponse(type, item, start, limit, format);
					sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			}

		}

	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doSubmitRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doSubmitRequest(RequestContext ctx) {
		
		String type = this.method.getAttribute(MethodConstants.ATTR_TYPE);
		if (type == null) {
			this.sendNotImplemented(ctx);
			
		} else {

			if (type.equals(ClassificationConstants.FNC_ID_Chat)) {
				
				/*
				 * Chat message processing
				 */
				String data = this.getRequestData(ctx);
		
				if (data == null) {
					this.sendNotImplemented(ctx);
					
				} else {
		
					try {
						/*
						 * JSON response
						 */
						String content = submitChat(data);
						sendJSONResponse(content, ctx.getResponse());
		
					} catch (Exception e) {
						this.sendBadRequest(ctx, e);
		
					}
		
				}
				
			} else {
				/*
				 * Mail message processing
				 */
				String mail = this.method.getAttribute(MethodConstants.ATTR_MAIL);
				if (mail == null) {
					this.sendNotImplemented(ctx);
					
				} else {
					
					try {

						JSONObject jMail = new JSONObject(URLDecoder.decode(mail, "UTF-8"));
						/*
						 * Process potential attachment
						 */
						String file = jMail.has(JaxrConstants.RIM_FILE) ? jMail.getString(JaxrConstants.RIM_FILE) : null;
						String mime = jMail.has(JaxrConstants.RIM_MIME) ? jMail.getString(JaxrConstants.RIM_MIME) : null;
						
						InputStream attachment = null;
						
						if ((file != null) && (mime != null)) attachment = ctx.getRequest().getInputStream();

						/*
						 * JSON response
						 */
						String content = submitMail(jMail.toString(), attachment, mime);
						sendJSONResponse(content, ctx.getResponse());

					} catch (Exception e) {
						this.sendBadRequest(ctx, e);
		
					}
				}

			}
			
		}
		
	}

	private String getJSONResponse(String type, String item, String start, String limit, String format) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		

		if (type.equals(ClassificationConstants.FNC_ID_Chat)) {

			CommDQM dqm = new CommDQM(jaxrHandle);
			JSONArray jArray = dqm.getChatMessages(item);
			
			/*
			 * Render result
			 */
			if ((start == null) || (limit == null)) {
				content = render(jArray, format);

			} else {
				content = render(jArray, start, limit, format);
			}
		
		} else if (type.equals(ClassificationConstants.FNC_ID_Mail)) {

			CommDQM dqm = new CommDQM(jaxrHandle);
			JSONArray jArray = dqm.getMailMessages(item);
			
			/*
			 * Render result
			 */
			if ((start == null) || (limit == null)) {
				content = render(jArray, format);

			} else {
				content = render(jArray, start, limit, format);
			}
			
		} else {
			throw new Exception("[CommServiceImpl] Information type <" + type + "> is not supported");
			
		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
		
	}

	/**
	 * A helper method to submit a chat
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private String submitChat(String data) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);
			
		CommLCM lcm = new CommLCM(jaxrHandle);
		content = lcm.submitChat(data);
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}
	
	/**
	 * @param mail
	 * @param attachment
	 * @return
	 * @throws Exception
	 */
	private String submitMail(String mail, InputStream attachment, String mime) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);
			
		CommLCM lcm = new CommLCM(jaxrHandle);
		content = lcm.submitMail(mail, attachment, mime);
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}
	
}
