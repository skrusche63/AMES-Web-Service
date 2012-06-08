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

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;

import de.kp.ames.web.core.RequestContext;
import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.core.service.ServiceImpl;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.function.GuiFactory;
import de.kp.ames.web.function.GuiRenderer;

public class GroupImpl extends ServiceImpl {

	/*
	 * Reference to the registered renderer
	 */
	private GuiRenderer renderer;

	public GroupImpl() {		
		renderer = GuiFactory.getInstance().getRenderer();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.core.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(FncConstants.METH_DELETE)) {
			/*
			 * Call delete method
			 */
			String type = this.method.getAttribute(FncConstants.ATTR_TYPE);			
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

		} else if (methodName.equals(FncConstants.METH_GET)) {
			/*
			 * Call get method
			 */
			String format = this.method.getAttribute(FncConstants.ATTR_FORMAT);	
			String type   = this.method.getAttribute(FncConstants.ATTR_TYPE);			

			if ((format == null) || (type == null)) {
				this.sendNotImplemented(ctx);
				
			} else {

				if (type.equals(FncConstants.FNC_ID_Category)) {
					/*
					 * Additional request parameters are directly provided
					 * by a (e.g.) SmartGwt 3.0 widget (Grid) and must be 
					 * retrieved from the respective Http Request
					 */
					HttpServletRequest request = ctx.getRequest();
					
					String startParam = renderer.getStartParam();
					String start = request.getParameter(startParam);
					
					String limitParam = renderer.getLimitParam();
					String limit = request.getParameter(limitParam);
			
					if ((start == null) || (limit == null)) {
						this.sendNotImplemented(ctx);
						
					} else {

						/* 
						 * Retrieve all categories that are actually registered 
						 * to classify a certain community of interest
						 */

						try {
							String content = categories(start, limit);
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

		} else if (methodName.equals(FncConstants.METH_SUBMIT)) {
			/*
			 * Call submit method
			 */
			String type = this.method.getAttribute(FncConstants.ATTR_TYPE);			
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
	private String categories(String start, String limit) throws Exception {

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
		content = renderer.createGrid(jArray);
		
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
		if (format.equals(FncConstants.FNC_FORMAT_ID_Grid)) {
			
			GuiRenderer renderer = GuiFactory.getInstance().getRenderer();
			content = renderer.createGrid(jArray);
			
		} else {
			throw new Exception("[GroupImpl] Format <" + format + "> not supported.");

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
			throw new Exception("[GroupImpl] Information type <" + type + "> is not supported");
		
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
			throw new Exception("[GroupImpl] Information type <" + type + "> is not supported");
		
		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
		
	}

	/*
	 * APP-6 Maker
	 * 
	 * - get
	 * 
	 * - html
	 * 
	 * - load
	 * 
	 * Sense Maker
	 * 
	 * - category as grid
	 * 
	 * - get namespaces
	 * 
	 * - html
	 * 
	 * - responsibility as grid
	 * 
	 * - role as grid
	 * 
	 * - load
	 * 
	 * - user as grid
	 * 
	 * 
	 */
}
