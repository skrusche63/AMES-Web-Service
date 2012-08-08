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

import org.json.JSONArray;

import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.MethodConstants;

public class RoleServiceImpl extends BusinessImpl {

	public RoleServiceImpl() {
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

			if (type.equals(ClassificationConstants.FNC_ID_Responsibility)) {
				/* 
				 * A responsibility is actually equal to a certain namespace,
				 * as a specific community or user be responsible (source) for
				 */
				String source = this.method.getAttribute(MethodConstants.ATTR_SOURCE);
				if (source == null) {
					this.sendNotImplemented(ctx);
					
				} else {

					String start = this.method.getAttribute(FncConstants.ATTR_START);			
					String limit = this.method.getAttribute(FncConstants.ATTR_LIMIT);			
			
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
			
			} else if (type.equals(ClassificationConstants.FNC_ID_Role)) {
				/* 
				 * Retrieves all roles that are actually registered 
				 * to classify a certain community or affiliate
				 */

				String affiliate = this.method.getAttribute(MethodConstants.ATTR_SOURCE);
				String community = this.method.getAttribute(MethodConstants.ATTR_TARGET);
				
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
		
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doSubmitRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doSubmitRequest(RequestContext ctx) {

		String type = this.method.getAttribute(MethodConstants.ATTR_TYPE);			

		String source = this.method.getAttribute(MethodConstants.ATTR_SOURCE);			
		String target = this.method.getAttribute(MethodConstants.ATTR_TARGET);			

		if ((type == null) || (source == null)) {
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
					String content = submit(type, source, target, data);
					sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

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

	/**
	 * A helper method to either delete a responsibility or a role
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
		
		if (type.equals(ClassificationConstants.FNC_ID_Responsibility)) {

			RoleLCM lcm = new RoleLCM(jaxrHandle);
			content = lcm.deleteResponsibility(item);
			
		} else if (type.equals(ClassificationConstants.FNC_ID_Role)) {

			RoleLCM lcm = new RoleLCM(jaxrHandle);
			content = lcm.deleteRole(item);

		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

	private String submit(String type, String source, String target, String data) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		
		
		if (type.equals(ClassificationConstants.FNC_ID_Responsibility)) {

			/* 
			 * This request either modifies or sets the responsibility 
			 * of a certain community of interest or a user; actually, 
			 * only namespaces contribute to responsibilities
			 */
			RoleLCM lcm = new RoleLCM(jaxrHandle);
			content = lcm.submitResponsibility(source, data);

		} else if (type.equals(ClassificationConstants.FNC_ID_Role)) {
			
			/* 
			 * this request either modifies or sets the roles
			 * assigned to a certain affiliation
			 */
			RoleLCM lcm = new RoleLCM(jaxrHandle);
			content = lcm.submitRoles(source, target, data);

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
