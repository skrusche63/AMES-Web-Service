package de.kp.ames.web.function.dms;
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

public class DmsServiceImpl extends BusinessImpl {

	/**
	 * Constructor
	 */
	public DmsServiceImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.http.RequestContext)
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

		String format = this.method.getAttribute(FncConstants.ATTR_FORMAT);	
		String type   = this.method.getAttribute(MethodConstants.ATTR_TYPE);	
		
		if ((format == null) || (type == null)) {
			this.sendNotImplemented(ctx);
			
		} else {
			/*
			 * This is an optional parameter that determines 
			 * a certain registry object
			 */
			String item = this.method.getAttribute(FncConstants.ATTR_ITEM);

			if (format.startsWith(FncConstants.FNC_FORMAT_ID_Json)) {
				/*
				 * Optional parameters that may be used to describe
				 * a Grid-oriented response
				 */
				String start = this.method.getAttribute(FncConstants.ATTR_START);
				String limit = this.method.getAttribute(FncConstants.ATTR_LIMIT);

				try {
					/*
					 * JSON response
					 */
					String content = getJSONResponse(type, item, start, limit, format);
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

		String data = this.getRequestData(ctx);
		String type = this.method.getAttribute(MethodConstants.ATTR_TYPE);	
		
		if ((data == null) || (type == null)) {
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

	/**
	 * Get DMS specific information objects
	 * 
	 * @param type
	 * @param item
	 * @param start
	 * @param limit
	 * @param format
	 * @return
	 * @throws Exception
	 */
	private String getJSONResponse(String type, String item, String start, String limit, String format) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		
			
		if (type.equals(FncConstants.FNC_ID_Document)) {

			DmsDQM dqm = new DmsDQM(jaxrHandle);
			JSONArray jArray = dqm.getDocuments(item);
			
			/*
			 * Render result
			 */
			if ((start == null) || (limit == null)) {
				content = render(jArray, format);

			} else {
				content = render(jArray, start, limit, format);
			}
			
		} else if (type.equals(FncConstants.FNC_ID_Image)) {

			DmsDQM dqm = new DmsDQM(jaxrHandle);
			JSONArray jArray = dqm.getImages(item);
			
			/*
			 * Render result
			 */
			if ((start == null) || (limit == null)) {
				content = render(jArray, format);

			} else {
				content = render(jArray, start, limit, format);
			}
			
		} else {
			throw new Exception("[TransformServiceImpl] Information type <" + type + "> is not supported");
			
		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
		
	}

	/**
	 * A helper method to either submit a
	 * document or image
	 * 
	 * @param type
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private String submit(String type, String data) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

		if (type.equals(FncConstants.FNC_ID_Document)) {

			DmsLCM lcm = new DmsLCM(jaxrHandle);
			content = lcm.submitDocument(data);
			
		} else if (type.equals(FncConstants.FNC_ID_Image)) {

			DmsLCM lcm = new DmsLCM(jaxrHandle);
			content = lcm.submitImage(data);

		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

}
