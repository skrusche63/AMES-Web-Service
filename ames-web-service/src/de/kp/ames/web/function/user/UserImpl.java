package de.kp.ames.web.function.user;
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

import de.kp.ames.web.core.RequestContext;
import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.function.FncConstants;

public class UserImpl extends BusinessImpl {

	public UserImpl() {
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
	
			if (format == null) {
				this.sendNotImplemented(ctx);
				
			} else {
				/* 
				 * Retrieve all users that are affiliated to a certain community (source)
				 */
				String source = this.method.getAttribute(FncConstants.ATTR_SOURCE);
				if (source == null) {
					this.sendNotImplemented(ctx);
					
				} else {

					try {
						String content = users(source, format);
						sendJSONResponse(content, ctx.getResponse());

					} catch (Exception e) {
						this.sendBadRequest(ctx, e);

					}
					
				}
				
			}
			
		}

	}

	/**
	 * Retrieve all users that are affiliated to 
	 * a certain community of interest
	 * 
	 * @param community
	 * @param format
	 * @return
	 * @throws Exception
	 */
	private String users(String community, String format) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

		UserDQM dqm = new UserDQM(jaxrHandle);
		JSONArray jArray = dqm.getUsers(community);
		
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

}
