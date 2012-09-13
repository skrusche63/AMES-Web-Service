package de.kp.ames.web.function.group;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.group
 *  Module: GroupServiceImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #function #group #service #web
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

public class GroupServiceImpl extends BusinessImpl {

	/**
	 * Constructor
	 */
	public GroupServiceImpl() {	
		super();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.core.RequestContext)
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
		
		String type = this.method.getAttribute(MethodConstants.ATTR_TYPE);	
		
		/*
		 * This parameter is optional in case of
		 * a community delete request
		 */
		String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);			

		if (type == null) {
			this.sendNotImplemented(ctx);
			
		} else {
			
			/*
			 * Delete request may have additional data
			 */
			String data = this.getRequestData(ctx);

			try {
				/*
				 * JSON response
				 */
				String content = delete(type, item, data);
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

			if (type.equals(ClassificationConstants.FNC_ID_Category)) {

				String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);			

				String start = this.method.getAttribute(FncConstants.ATTR_START);			
				String limit = this.method.getAttribute(FncConstants.ATTR_LIMIT);			
		
				if ((start == null) || (limit == null)) {
					this.sendNotImplemented(ctx);
					
				} else {

					/* 
					 * Retrieve all categories that are actually registered 
					 * to classify a certain community of interest
					 */

					try {
						String content = categories(format, item, start, limit);
						sendJSONResponse(content, ctx.getResponse());

					} catch (Exception e) {
						this.sendBadRequest(ctx, e);

					}
					
				}
				
			} else if (type.equals(ClassificationConstants.FNC_ID_Community)) {

				String start = this.method.getAttribute(FncConstants.ATTR_START);			
				String limit = this.method.getAttribute(FncConstants.ATTR_LIMIT);			

				/*
				 * A request for a single community of interest
				 */
				String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);

				/*
				 * Retrieve communities of interest, either
				 * all registered ones or those, that refer
				 * to a certain affiliate (source)
				 */
				String affiliate = this.method.getAttribute(MethodConstants.ATTR_SOURCE);
				
				try {
					String content = communities(format, item, affiliate, start, limit);
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
			
			/*
			 * Submit request requires data
			 */
			String data = this.getRequestData(ctx);
			if (data == null) {
				this.sendNotImplemented(ctx);
				
			} else {

				/* 
				 * 'item' is an optional parameter that refers to an already
				 * existing registry object referenced by this submission
				 */
				String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);	
				
				try {
					/*
					 * JSON response
					 */
					String content = submit(type, item, data);
					sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			}
			
		}		
	}

	/**
	 * Retrieve registered categories in a JSON paging
	 * Grid representation
	 * 
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	private String categories(String format, String item, String start, String limit) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

		// TODO
		GroupDQM dqm = new GroupDQM(jaxrHandle);
		JSONArray jArray = dqm.getCategories();

		/*
		 * Render result
		 */
		content = render(jArray, start, limit, format);
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
		
	}
	
	/**
	 * Retrieve registered communities 
	 * 
	 * @param affiliate
	 * @param format
	 * @return
	 * @throws Exception
	 */
	private String communities(String format, String item, String affiliate, String start, String limit) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

		GroupDQM dqm = new GroupDQM(jaxrHandle);
		JSONArray jArray = dqm.getCommunities(item, affiliate);
		
		/*
		 * Render result
		 */
		if ((start == null) || (limit == null)) {
			content = render(jArray, format);
			
		} else {
			content = render(jArray, start, limit, format);
			
		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
		
	}

	/**
	 * This method deletes community related information
	 * from an OASIS ebXML RegRep
	 * 
	 * @param type
	 * @param item
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private String delete(String type, String item, String data) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		
		
		if (type.equals(ClassificationConstants.FNC_ID_Affiliation)) {
			
			/*
			 * Deleting affiliation requires knowledge about
			 * the respective groups & users; that is why
			 * additional data are provided
			 */
			GroupLCM lcm = new GroupLCM(jaxrHandle);
			content = lcm.deleteAffiliation(data);

		} else if (type.equals(ClassificationConstants.FNC_ID_Community)) {

			GroupLCM lcm = new GroupLCM(jaxrHandle);
			content = lcm.deleteCommunity(item);
			
		} else {
			throw new Exception("[GroupServiceImpl] Information type <" + type + "> is not supported");
		
		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
		
	}

	/**
	 * This method submits community and community related
	 * information to the OASIS ebXML RegRep
	 * 
	 * @param type
	 * @param item
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private String submit(String type, String item, String data) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		
		
		if (type.equals(ClassificationConstants.FNC_ID_Affiliation)) {

			/* 
			 * An affiliation is a specific association between a certain user
			 * and a community of interest; with this request an affiliation is
			 * either created or modified; 'item' refers to an existing affiliation
			 */

			GroupLCM lcm = new GroupLCM(jaxrHandle);
			content = lcm.submitAffiliation(item, data);

		} else if (type.equals(ClassificationConstants.FNC_ID_Category)) {

			/* 
			 * A 'category' is a specific classification for a community of
			 * interest; it is used to distinguish between groups with and
			 * without responsibility about namespaces
			 */

			GroupLCM lcm = new GroupLCM(jaxrHandle);
			content = lcm.submitCategory(item, data);

		} else if (type.equals(ClassificationConstants.FNC_ID_Contact)) {

			/* 
			 * This is a request to assign a certain primary contact
			 * to a specific community of interest; source refers to
			 * the organization that is affected by this request 
			 */

			GroupLCM lcm = new GroupLCM(jaxrHandle);
			content = lcm.submitContact(item, data);

		} else if (type.equals(ClassificationConstants.FNC_ID_Community)) {
			
			GroupLCM lcm = new GroupLCM(jaxrHandle);
			content = lcm.submitCommunity(data);

		} else {
			throw new Exception("[GroupServiceImpl] Information type <" + type + "> is not supported");
		
		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
		
	}

}
