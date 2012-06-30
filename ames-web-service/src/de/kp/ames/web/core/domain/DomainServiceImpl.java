package de.kp.ames.web.core.domain;
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

import org.freebxml.omar.common.CanonicalSchemes;
import org.json.JSONArray;

import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.MethodConstants;

public class DomainServiceImpl extends BusinessImpl {

	public DomainServiceImpl() {
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
			
			/*
			 * Optional reference to existing registry object
			 */
			String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);
			
			/*
			 * Reference to registry package that manages registry objects
			 * of a certain type
			 */
			String parent = this.method.getAttribute(MethodConstants.ATTR_PARENT);	

			try {
				/*
				 * JSON response
				 */
				String content = getJSONResponse(type, item, parent, format);
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
			/*
			 * Reference to registry package that manages registry objects
			 * of a certain type
			 */
			String parent = this.method.getAttribute(MethodConstants.ATTR_PARENT);	
			String type   = this.method.getAttribute(MethodConstants.ATTR_TYPE);	
			
			if ((parent == null) || (type == null)) {
				this.sendNotImplemented(ctx);
				
			} else {
				
				try {
					/*
					 * JSON response
					 */
					String content = submit(type, parent, data);
					sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			}

		}
		
	}
	
	/**
	 * A helper method to retrieve RegistryObject instances
	 * of a certain type and in a specific format
	 * 
	 * @param type
	 * @param item
	 * @param parent
	 * @param format
	 * @return
	 * @throws Exception
	 */
	private String getJSONResponse(String type, String item, String parent, String format) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		

		if (type.equals(CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_ExternalLink)) {

			/*
			 * Retrieve external links
			 */
			DomainDQM dqm = new DomainDQM(jaxrHandle);
			JSONArray jArray = dqm.getExternalLinks(parent, item);
			
			/*
			 * Render result
			 */
			content = render(jArray, format);

		} else if (type.equals(CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_RegistryObject)) {

			/*
			 * Retrieve registry objects
			 */
			DomainDQM dqm = new DomainDQM(jaxrHandle);
			JSONArray jArray = dqm.getRegistryObjects(parent, item);
			
			/*
			 * Render result
			 */
			content = render(jArray, format);
			
		} else {
			throw new Exception("[DomainServiceImpl] Information type <" + type + "> is not supported");
			
		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

	/**
	 * A helper method to delete a RegistryObject instance
	 * of a certain type
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
		
		/*
		 * The type parameter is a MUST for the interface,
		 * but actually not used 
		 */
		DomainLCM lcm = new DomainLCM(jaxrHandle);
		content = lcm.deleteRegistryObject(item);
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}
		
	/**
	 * A helper method to submit a RegistryObject instance
	 * of a certain type
	 * 
	 * @param type
	 * @param parent
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private String submit(String type, String parent, String data) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		

		if (type.equals(CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_ExternalLink)) {

			/*
			 * Submit external link
			 */
			DomainLCM lcm = new DomainLCM(jaxrHandle);
			content = lcm.submitExternalLink(parent, data);

		} else if (type.equals(CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_RegistryPackage)) {

			/*
			 * Submit registry package
			 */
			DomainLCM lcm = new DomainLCM(jaxrHandle);
			content = lcm.submitRegistryPackage(parent, data);
			
		} else {
			throw new Exception("[DomainServiceImpl] Information type <" + type + "> is not supported");
			
		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

}
