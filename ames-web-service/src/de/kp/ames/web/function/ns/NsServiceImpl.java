package de.kp.ames.web.function.ns;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.ns
 *  Module: NsServiceImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #function #ns #service #web
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
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;

public class NsServiceImpl extends BusinessImpl {

	public NsServiceImpl() {
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
		if (format == null) {
			this.sendNotImplemented(ctx);
			
		} else {
			/*
			 * This is an optional parameter that determines 
			 * a certain registry object
			 */
			String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);

			/*
			 * This is an optional parameter that determines 
			 * a certain registry package as parent
			 */
			String parent = this.method.getAttribute(MethodConstants.ATTR_PARENT);
			
			parent = (parent == null || parent.equals("null")) ? null : parent;
			
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
				String content = getJSONResponse(item, parent, start, limit, format);
				sendJSONResponse(content, ctx.getResponse());

			} catch (Exception e) {
				this.sendBadRequest(ctx, e);

			}

		}

	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doSubmitRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doSubmitRequest(RequestContext ctx) {
			
		String data = this.getRequestData(ctx);
		
		if (data == null) {
			this.sendNotImplemented(ctx);
			
		} else {

			try {
				/*
				 * JSON response
				 */
				
				/*
				 * This is an optional parameter that determines 
				 * a certain registry package as parent
				 */
				String parent = this.method.getAttribute(MethodConstants.ATTR_PARENT);
				
				parent = (parent == null || parent.equals("null")) ? null : parent;

				/*
				 * route request node
				 */
				String content = submit(data, parent);
				sendJSONResponse(content, ctx.getResponse());

			} catch (Exception e) {
				this.sendBadRequest(ctx, e);

			}

		}
		
	}

	/**
	 * @param item
	 * @param parent
	 * @param start
	 * @param limit
	 * @param format
	 * @return
	 * @throws Exception
	 */
	private String getJSONResponse(String item, String parent, String start, String limit, String format) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		
			
		NsDQM dqm = new NsDQM(jaxrHandle);
		JSONArray jArray = dqm.getNamespaces(item, parent);
		
		/*
		 * Render result
		 */
		if (format.equals(FormatConstants.FNC_FORMAT_ID_Tree)) {
			
			content = render(jArray, parent, format);
			
		} else if (format.equals(FormatConstants.FNC_FORMAT_ID_Object)) {
			
			content = render(jArray, format);
			
		} else if (format.equals(FormatConstants.FNC_FORMAT_ID_Grid)) {
		
			if ((start == null) || (limit == null)) {
				content = render(jArray, format);
	
			} else {
				content = render(jArray, start, limit, format);
			}

		} else {
			throw new Exception("[NsServiceImpl] Format <" + format + "> not supported.");
		}


		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
		
	}

	/**
	 * A helper method to delete a namespace
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

		NsLCM lcm = new NsLCM(jaxrHandle);
		content = lcm.deleteNamepace(item);
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

	/**
	 * @param data
	 * @param jRequestObject 
	 * @return
	 * @throws Exception
	 */
	private String submit(String data, String parent) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		

		NsLCM lcm = new NsLCM(jaxrHandle);
		content = lcm.submitNamespace(data, parent);
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
		
	}

}
