package de.kp.ames.web.function.role;
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
import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.function.FncConstants;

public class RoleImpl extends BusinessImpl {

	public RoleImpl() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.core.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(FncConstants.METH_GET)) {
			/*
			 * Call get method
			 */
			String format = this.method.getAttribute(FncConstants.ATTR_FORMAT);	
			String type   = this.method.getAttribute(FncConstants.ATTR_TYPE);			

			if ((format == null) || (type == null)) {
				this.sendNotImplemented(ctx);
				
			} else {
				
				if (type.equals(FncConstants.FNC_ID_Responsibility)) {
					/* 
					 * A responsibility is actually equal to a certain namespace,
					 * as a specific community or user be responsible (source) for
					 */
					String source = this.method.getAttribute(FncConstants.ATTR_SOURCE);
					if (source == null) {
						this.sendNotImplemented(ctx);
						
					} else {

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
	
							try {
								/*
								 * JSON response
								 */
								String content = responsibilities(source, start, limit, format);
								sendJSONResponse(content, ctx.getResponse());

							} catch (Exception e) {
								this.sendBadRequest(ctx, e);
	
							}
						
						}
					
					}
				
				} else if (type.equals(FncConstants.FNC_ID_Role)) {
					/* 
					 * Retrieves all roles that are actually registered 
					 * to classify a certain community or affiliate
					 */

					String affiliate = this.method.getAttribute(FncConstants.ATTR_AFFILIATE);
					String community = this.method.getAttribute(FncConstants.ATTR_COMMUNITY);
					
					if ((affiliate == null) || (community == null)) {
						this.sendNotImplemented(ctx);
						
					} else {

						try {
							/*
							 * JSON response
							 */
							String content = roles(affiliate, community, format);
							sendJSONResponse(content, ctx.getResponse());

						} catch (Exception e) {
							this.sendBadRequest(ctx, e);

						}
						
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

					try {
						/*
						 * JSON response
						 */
						String content = submit(type, data);
						sendJSONResponse(content, ctx.getResponse());

					} catch (Exception e) {
						this.sendBadRequest(ctx, e);

					}

				}
				
			}

		}
		
	}

	/**
	 * Retrieve registered responsibilities
	 * (registry packages)
	 * 
	 * @param source
	 * @param start
	 * @param limit
	 * @param format
	 * @return
	 * @throws Exception
	 */
	private String responsibilities(String source, String start, String limit, String format) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

		RoleDQM dqm = new RoleDQM(jaxrHandle);
		JSONArray jArray = dqm.getResponsibilities(source);
		
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
	 * Retrieve all registered roles in the context 
	 * of a certain affiliation, i.e. attached roles
	 * are marked 'checked' and others not
	 * 
	 * @param affiliate
	 * @param community
	 * @param format
	 * @return
	 * @throws Exception
	 */
	private String roles(String user, String community, String format) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

		RoleDQM dqm = new RoleDQM(jaxrHandle);
		JSONArray jArray = dqm.getRoles(user, community);
		
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

	private String submit(String type, String data) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		
		
		if (type.equals(FncConstants.FNC_ID_Responsibility)) {

			/* 
			 * This request either modifies or sets the responsibility 
			 * of a certain community of interest or a user; actually, 
			 * only namespaces contribute to responsibilities
			 */
			RoleLCM lcm = new RoleLCM(jaxrHandle);
			content = lcm.submitResponsibility(data);

		} else if (type.equals(FncConstants.FNC_ID_Role)) {
			
			/* 
			 * this request either modifies or sets the roles
			 * assigned to a certain affiliation
			 */
			RoleLCM lcm = new RoleLCM(jaxrHandle);
			content = lcm.submitRoles(data);

		} else {
			throw new Exception("[RoleImpl] Information type <" + type + "> is not supported");
		
		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
		
	}

}
