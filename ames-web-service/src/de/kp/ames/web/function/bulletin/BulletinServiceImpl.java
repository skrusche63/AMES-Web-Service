package de.kp.ames.web.function.bulletin;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.bulletin
 *  Module: BulletinServiceImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #bulletin #function #service #web
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

import org.json.JSONArray;

import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.MethodConstants;

public class BulletinServiceImpl extends BusinessImpl {

	public BulletinServiceImpl() {
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
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doGetRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doGetRequest(RequestContext ctx) {

		String format = this.method.getAttribute(MethodConstants.ATTR_FORMAT);
		String type   = this.method.getAttribute(MethodConstants.ATTR_TYPE);
		
		String target = this.method.getAttribute(MethodConstants.ATTR_TARGET);

		if ((format == null) || (type == null) || (target == null)) {
			this.sendNotImplemented(ctx);

		} else {

			String start = this.method.getAttribute(FncConstants.ATTR_START);
			String limit = this.method.getAttribute(FncConstants.ATTR_LIMIT);

			try {
				/*
				 * JSON response
				 */
				String content = getJSONResponse(type, target, start, limit, format);
				sendJSONResponse(content, ctx.getResponse());

			} catch (Exception e) {
				this.sendBadRequest(ctx, e);

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
			
			/*
			 * Submit request requires data
			 */
			String data = this.getRequestData(ctx);
			if (data == null) {
				this.sendNotImplemented(ctx);
				
			} else {

				if (type.equals(ClassificationConstants.FNC_ID_Comment)) {
					
					String posting = this.method.getAttribute(MethodConstants.ATTR_TARGET);
					if (posting == null) {
						this.sendNotImplemented(ctx);
					
					} else {
						
						try {
							String content = submitComment(posting, data);
							sendJSONResponse(content, ctx.getResponse());

						} catch (Exception e) {
							this.sendBadRequest(ctx, e);

						}
					}
				
				} else if (type.equals(ClassificationConstants.FNC_ID_Posting)) {
					
					String recipient = this.method.getAttribute(MethodConstants.ATTR_TARGET);
					if (recipient == null) {
						this.sendNotImplemented(ctx);
					
					} else {
						
						try {
							String content = submitPosting(recipient, data);
							sendJSONResponse(content, ctx.getResponse());

						} catch (Exception e) {
							this.sendBadRequest(ctx, e);

						}
					}
				}

			}
			
		}
		
	}

	/**
	 * @param recipient
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	private String getJSONResponse(String type, String target, String start, String limit, String format) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);
				
		if (type.equals(ClassificationConstants.FNC_ID_Comment)) {
			
			PostingDQM dqm = new PostingDQM(jaxrHandle);
			JSONArray jArray = dqm.getComments(target);
			
			/*
			 * Render result
			 */
			if ((start == null) || (limit == null)) {
				content = render(jArray, format);
				
			} else {
				content = render(jArray, start, limit, format);
				
			}
			
		} else if (type.equals(ClassificationConstants.FNC_ID_Posting)) {
		
			PostingDQM dqm = new PostingDQM(jaxrHandle);
			JSONArray jArray = dqm.getPostings(target);
			
			/*
			 * Render result
			 */
			if ((start == null) || (limit == null)) {
				content = render(jArray, format);
				
			} else {
				content = render(jArray, start, limit, format);
				
			}

		} else {
			throw new Exception("[BulletinServiceImpl] Information type <" + type + "> is not supported");

		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
		
	}

	/**
	 * A helper method to submit a certain comment
	 * for a specific posting
	 * 
	 * @param posting
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private String submitComment(String posting, String data) throws Exception {
		/*
		 * Access OASIS ebXML RegRep to register
		 * a certain comment for a specific posting
		 */

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

		PostingLCM lcm = new PostingLCM(jaxrHandle);
		content = lcm.submitComment(posting, data);

		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

	/**
	 * A helper method to submit a certain posting
	 * 
	 * @param recipient
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private String submitPosting(String recipient, String data) throws Exception {
		/*
		 * Access OASIS ebXML RegRep to register
		 * a certain posting for a specific recipient
		 */

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

		PostingLCM lcm = new PostingLCM(jaxrHandle);
		content = lcm.submitPosting(recipient, data);

		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

}
