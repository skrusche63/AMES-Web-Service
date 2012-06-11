package de.kp.ames.web.core.transform;
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

import java.io.InputStream;
import java.util.ArrayList;

import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.core.service.ServiceImpl;
import de.kp.ames.web.core.util.BaseParam;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.http.RequestContext;

public class TransformImpl extends ServiceImpl {

	public TransformImpl() {		
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.core.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(FncConstants.METH_APPLY)) {
			/*
			 * Call apply method
			 */
			String service = this.method.getAttribute(FncConstants.ATTR_SERVICE);			

			String source = this.method.getAttribute(FncConstants.ATTR_SOURCE);
			String target = this.method.getAttribute(FncConstants.ATTR_TARGET);

			if ((source == null) || (target == null) || (service == null)) {
				this.sendNotImplemented(ctx);
				
			} else {

				try {
					/*
					 * In case of a successful transformation
					 */
					String content = apply(source, target, service);
					sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			}
			
		}
		
	}
	
	/**
	 * @param source
	 * @param target
	 * @param service
	 * @return
	 * @throws Exception 
	 */
	private String apply(String source, String target, String service) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

		/*
		 * Retrieve transformed stream from source and respective
		 * service; actually no params are necessary
		 */
		ArrayList<BaseParam> xslParams = null;
		
		XslProcessor xslProcessor = new XslProcessor(jaxrHandle);
		InputStream stream = xslProcessor.execute(source, service, xslParams);
		
		if (stream == null) {
			/*
			 * Logoff
			 */
			JaxrClient.getInstance().logoff(jaxrHandle);
			throw new Exception("[TransformImpl] XSL Transformation of " + source + " failed.");
			
		} else {
			
			/*
			 * Add transformation result as repository item to
			 * the referenced target object
			 */
			XslConsumer xslConsumer = new XslConsumer(jaxrHandle);
			content = xslConsumer.setRepositoryItem(target, stream);
			
		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
	}
	
}
