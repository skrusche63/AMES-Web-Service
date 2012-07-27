package de.kp.ames.web.function.transform;
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
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;

public class TransformServiceImpl extends BusinessImpl {

	/**
	 * Constructor
	 */
	public TransformServiceImpl() {		
		super();
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.core.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(MethodConstants.METH_APPLY)) {
			/*
			 * Call apply method
			 */
			doApplyRequest(ctx);

		} else if (methodName.equals(MethodConstants.METH_DELETE)) {
			/*
			 * Call get method
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
	 * @see de.kp.ames.web.core.service.ServiceImpl#doApplyRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doApplyRequest(RequestContext ctx) {

		String service = this.method.getAttribute(MethodConstants.ATTR_SERVICE);			

		String source = this.method.getAttribute(MethodConstants.ATTR_SOURCE);
		String target = this.method.getAttribute(MethodConstants.ATTR_TARGET);

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
			 * This is an optional parameter that determines 
			 * a certain registry object
			 */
			String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);

			if (format.startsWith(FormatConstants.FNC_FORMAT_ID_File)) {

				try {
					/*
					 * HTML Response
					 */
					FileUtil content = getFileResponse(type, item);					
					String xml = content.toString();
					
					sendHTMLResponse(xmlToHtml(xml), ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			} else if (format.startsWith(FormatConstants.FNC_FORMAT_ID_Json)) {
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
		/*
		 * An XSL transformation that is already uploaded to
		 * the server (and managed in a temporary cache) is
		 * registered in an OASIS ebXML RegRep
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
			throw new Exception("[TransformServiceImpl] XSL Transformation of " + source + " failed.");
			
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

	/**
	 * A helper method to delete a transformator instance
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
		TransformLCM lcm = new TransformLCM(jaxrHandle);
		content = lcm.deleteTransformator(item);
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

	/**
	 * Retrieve Xsl transformator in a FileUtil representation
	 * 
	 * @param type
	 * @param item
	 * @return
	 * @throws Exception
	 */
	private FileUtil getFileResponse(String type, String item) throws Exception {

		FileUtil file = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		
		
		/*
		 * Retrieve Xsl transformator in a FileUtil representation
		 */
		TransformDQM dqm = new TransformDQM(jaxrHandle);
		file = dqm.getTransformator(item);
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return file;
	
	}

	/**
	 * Get transformation specific information objects
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
			
		if (type.equals(ClassificationConstants.FNC_ID_Transformator)) {

			TransformDQM dqm = new TransformDQM(jaxrHandle);
			JSONArray jArray = dqm.getTransformators(item);
			
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
	 * Submit an XML transformation
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

		TransformLCM lcm = new TransformLCM(jaxrHandle);
		content = lcm.submitTransformator(data);
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}
	
	/**
	 * A helper method to convert an XML artefact into a Html document
	 * 
	 * @param xml
	 * @return
	 */
	private String xmlToHtml(String xml) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0.1 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"");
		sb.append("<html><body><style=\"background:#f2f2f4;padding:8px;font-family:tahoma,arial,helvetica,sans-serif;color:#566d99;font-size:11px;\"><pre>");
		
		sb.append(encodeHtml(xml));
		sb.append("</pre></body></html>");
		
		return sb.toString();
	
	}
	
}
