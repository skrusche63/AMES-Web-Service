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

import de.kp.ames.web.core.RequestContext;
import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.core.service.ServiceImpl;
import de.kp.ames.web.function.FncConstants;

public class GroupImpl extends ServiceImpl {

	public GroupImpl() {		
	}

	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(FncConstants.METH_DELETE)) {
			/*
			 * Call delete method
			 */
		} else if (methodName.equals(FncConstants.METH_GET)) {
			/*
			 * Call get method
			 */
			String format = this.method.getAttribute(FncConstants.ATTR_FORMAT);	
			String type   = this.method.getAttribute(FncConstants.ATTR_TYPE);			

			if ((format == null) || (type == null)) {
				this.sendNotImplemented(ctx);
				
			} else {
				
				if (type.equals(FncConstants.FNC_ID_Community)) {
					
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
		content = dqm.getCommunities(affiliate, format);
		
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
	 * @param data
	 * @return
	 */
	private String submit(String type, String data) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		
		if (type.equals(FncConstants.FNC_ID_Community)) {
			
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
	 * - post as grid
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
