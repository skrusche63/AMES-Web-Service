package de.kp.ames.web.function.bulletin;
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

public class BulletinImpl extends ServiceImpl {
	
	public BulletinImpl() {
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.core.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(FncConstants.METH_SUBMIT)) {
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
					
					if (type.equals(FncConstants.FNC_ID_Posting)) {
						
						String recipient = this.method.getAttribute(FncConstants.ATTR_RECIPIENT);
						if (recipient == null) {
							this.sendNotImplemented(ctx);
						
						} else {
							
							try {
								String content = submit(recipient, data);
								sendJSONResponse(content, ctx.getResponse());

							} catch (Exception e) {
								this.sendBadRequest(ctx, e);

							}
						}
					}
				}
				
			}

		}
	}
	
	/**
	 * A helper method to submit a certain posting
	 * 
	 * @param recipient
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private String submit(String recipient, String data) throws Exception {
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
		content = lcm.submit(recipient, data);

		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

}
