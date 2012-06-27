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

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;

import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.ClassificationConstants;
import de.kp.ames.web.shared.FormatConstants;
import de.kp.ames.web.shared.MethodConstants;

public class BulletinImpl extends BusinessImpl {

	public BulletinImpl() {
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

			String recipient = this.method.getAttribute("recipient");
			if (recipient == null) {
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
						String content = get(recipient, start, limit);
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
					
					if (type.equals(ClassificationConstants.FNC_ID_Posting)) {
						
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
	 * A helper method to retrieve all postings registered
	 * for a certain recipient
	 * 
	 * @param recipient
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	private String get(String recipient, String start, String limit) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);
		
		PostingDQM dqm = new PostingDQM(jaxrHandle);
		JSONArray jArray = dqm.getPostings(recipient);
		
		/*
		 * Render result
		 */
		String format = FormatConstants.FNC_FORMAT_ID_Grid;
		content = render(jArray, start, limit, format);
		
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
