package de.kp.ames.web.function.product;
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

import org.json.JSONArray;

import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.core.util.BaseParam;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.function.office.OfficeBuilder;
import de.kp.ames.web.function.office.OfficeConverter;
import de.kp.ames.web.function.office.OfficeFactory;
import de.kp.ames.web.function.transform.XslProcessor;
import de.kp.ames.web.http.RequestContext;

public class ProductServiceImpl extends BusinessImpl {

	public ProductServiceImpl() {
		super();
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
			String source  = this.method.getAttribute(FncConstants.ATTR_SOURCE);
			String service = this.method.getAttribute(FncConstants.ATTR_SERVICE);			

			if ((source == null) || (service == null)) {
				this.sendNotImplemented(ctx);
				
			} else {
				
				String data = this.getRequestData(ctx);
				if (data == null) {
					this.sendNotImplemented(ctx);
					
				} else {

					try {
						/*
						 * JSON response
						 */
						String content = apply(source, service, data);
						sendJSONResponse(content, ctx.getResponse());

					} catch (Exception e) {
						this.sendBadRequest(ctx, e);

					}
					
				}

			}

		} else if (methodName.equals(FncConstants.METH_FILE)) {

			/*
			 * Call file method
			 */
			String item = this.method.getAttribute(FncConstants.ATTR_ITEM);
			if (item == null) {
				this.sendNotImplemented(ctx);
				
			} else {
				
				try {
					/*
					 * FileUtil response
					 */
					FileUtil content = file(item);
					sendFileResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			}

		} else if (methodName.equals(FncConstants.METH_GET)) {

			/*
			 * Call get method
			 */
			String format = this.method.getAttribute(FncConstants.ATTR_FORMAT);	
			String type   = this.method.getAttribute(FncConstants.ATTR_TYPE);	
			
			if ((format == null) || (type == null)) {
				this.sendNotImplemented(ctx);
				
			} else {
				/*
				 * Reference to single object (Format: Object)
				 */
				String item = this.method.getAttribute(FncConstants.ATTR_ITEM);

				/*
				 * Format: Grid
				 */
				String start = this.method.getAttribute(FncConstants.ATTR_START);
				String limit = this.method.getAttribute(FncConstants.ATTR_LIMIT);

				try {
					/*
					 * JSON response
					 */
					String content = get(type, item, start, limit, format);
					sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			}
			
		} else if (methodName.equals(FncConstants.METH_SUBMIT)) {
			
			/*
			 * Call submit method
			 */
			String data = this.getRequestData(ctx);
			if (data == null) {
				this.sendNotImplemented(ctx);
				
			} else {

				try {
					/*
					 * JSON response
					 */
					String content = submit(data);
					sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			}

		}
		
	}

	/**
	 * Apply productor (service) to a certain registry object (source)
	 * and register result in an OASIS ebXML RegRep
	 * 
	 * @param source
	 * @param service
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private String apply(String source, String service, String data) throws Exception {
		
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
			throw new Exception("[ProductServiceImpl] XSL Transformation of " + source + " failed.");
			
		} else {

			/*
			 * Create product
			 */
			ProductLCM lcm = new ProductLCM(jaxrHandle);
			content = lcm.createProduct(data, stream);

		}

		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

	/**
	 * Retrieve FileUtil representation of a certain product
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	private FileUtil file(String item) throws Exception {

		FileUtil content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

		ProductDQM dqm = new ProductDQM(jaxrHandle);
		content = dqm.getFile(item);
		
		/*
		 * Retrieve office representation 
		 * of the specific product
		 */
		OfficeFactory factory = new OfficeFactory(content);
		OfficeBuilder builder = factory.getOfficeBuilder();
		
		content = builder.build();
		
		/*
		 * Convert office representation
		 * in Web enabled format
		 */
		OfficeConverter converter = factory.getOfficeConverter();
		content = converter.convert();

		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
	
	}

	/**
	 * Get product specific information objects
	 * 
	 * @param type
	 * @param item
	 * @param start
	 * @param limit
	 * @param format
	 * @return
	 * @throws Exception
	 */
	private String get(String type, String item, String start, String limit, String format) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		

		if (type.equals(FncConstants.FNC_ID_Product)) {
			
			ProductDQM dqm = new ProductDQM(jaxrHandle);
			JSONArray jArray = dqm.getProducts(item);
			
			/*
			 * Render result
			 */
			if ((start == null) || (limit == null)) {
				content = render(jArray, format);

			} else {
				content = render(jArray, start, limit, format);
			}
			
		} else if (type.equals(FncConstants.FNC_ID_Productor)) {

			ProductDQM dqm = new ProductDQM(jaxrHandle);
			JSONArray jArray = dqm.getProductors(item);
			
			/*
			 * Render result
			 */
			if ((start == null) || (limit == null)) {
				content = render(jArray, format);

			} else {
				content = render(jArray, start, limit, format);
			}
			
		} else {
			throw new Exception("[ProductServiceImpl] Information type <" + type + "> is not supported");
			
		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
		
	}

	/**
	 * Submit productor
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private String submit(String data) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

		ProductLCM lcm = new ProductLCM(jaxrHandle);
		content = lcm.submitProductor(data);
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

}