package de.kp.ames.web.function.group;
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
import de.kp.ames.web.shared.MethodConstants;

public class GroupServiceImpl extends BusinessImpl {

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
			String type = this.method.getAttribute(MethodConstants.ATTR_TYPE);			
			if (type == null) {
				this.sendNotImplemented(ctx);
				
			} else {
				
				/*
				 * Delete request requires data
				 */
				String data = this.getRequestData(ctx);
				if (data == null) {
					this.sendNotImplemented(ctx);
					
				} else {
					
					try {
						/*
						 * JSON response
						 */
						String content = delete(type, data);
						sendJSONResponse(content, ctx.getResponse());

					} catch (Exception e) {
						this.sendBadRequest(ctx, e);

					}
					
				}
				
			}

		} else if (methodName.equals(MethodConstants.METH_GET)) {
			/*
			 * Call get method
			 */
			String format = this.method.getAttribute(FncConstants.ATTR_FORMAT);	
			String type   = this.method.getAttribute(MethodConstants.ATTR_TYPE);			

			if ((format == null) || (type == null)) {
				this.sendNotImplemented(ctx);
				
			} else {

				if (type.equals(FncConstants.FNC_ID_Category)) {

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
							String content = categories(start, limit, format);
							sendJSONResponse(content, ctx.getResponse());

						} catch (Exception e) {
							this.sendBadRequest(ctx, e);

						}
						
					}
					
				} else if (type.equals(FncConstants.FNC_ID_Community)) {
					
					/*
					 * Retrieve communities of interest, either
					 * all registered ones or those, that refer
					 * to a certain affiliate
					 */
					String affiliate = this.method.getAttribute(FncConstants.ATTR_AFFILIATE);
					
					try {
						String content = communities(affiliate, format);
						sendJSONResponse(content, ctx.getResponse());

					} catch (Exception e) {
						this.sendBadRequest(ctx, e);

					}

				}
				
			}

		} else if (methodName.equals(MethodConstants.METH_SUBMIT)) {
			/*
			 * Call submit method
			 */
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
					 * 'source' is an optional parameter that refers to an already
					 * existing registry object referenced by this submission
					 */
					String source = this.method.getAttribute(FncConstants.ATTR_SOURCE);	
					
					try {
						/*
						 * JSON response
						 */
						String content = submit(type, source, data);
						sendJSONResponse(content, ctx.getResponse());

					} catch (Exception e) {
						this.sendBadRequest(ctx, e);

					}
					
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
	private String categories(String start, String limit, String format) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

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
	private String communities(String affiliate, String format) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

		GroupDQM dqm = new GroupDQM(jaxrHandle);
		JSONArray jArray = dqm.getCommunities(affiliate);
		
		/*
		 * Render result
		 */
		content = render(jArray, format);
		
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
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private String delete(String type, String data) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		
		
		if (type.equals(FncConstants.FNC_ID_Affiliation)) {

			GroupLCM lcm = new GroupLCM(jaxrHandle);
			content = lcm.deleteAffiliation(data);
		
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
	 * @param source
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private String submit(String type, String source, String data) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		
		
		if (type.equals(FncConstants.FNC_ID_Affiliation)) {

			/* 
			 * An affiliation is a specific association between a certain user
			 * and a community of interest; with this request an affiliation is
			 * either created or modified; 'source' refers to an existing affiliation
			 */

			GroupLCM lcm = new GroupLCM(jaxrHandle);
			content = lcm.submitAffiliation(source, data);

		} else if (type.equals(FncConstants.FNC_ID_Category)) {

			/* 
			 * A 'category' is a specific classification for a community of
			 * interest; it is used to distinguish between groups with and
			 * without responsibility about namespaces
			 */

			GroupLCM lcm = new GroupLCM(jaxrHandle);
			content = lcm.submitCategory(data);

		} else if (type.equals(FncConstants.FNC_ID_Contact)) {

			/* 
			 * This is a request to assign a certain primary contact
			 * to a specific community of interest; source refers to
			 * the organization that is affected by this request 
			 */

			GroupLCM lcm = new GroupLCM(jaxrHandle);
			content = lcm.submitContact(source, data);

		} else if (type.equals(FncConstants.FNC_ID_Community)) {
			
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
