package de.kp.ames.web.core.service;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.service
 *  Module: ServiceImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #service #web
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

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLEncoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.http.RequestMethod;

/**
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */

public class ServiceImpl implements Service {

	/*
	 * RequestMethod describes the method to be
	 * invoked by the actual request
	 */
	protected RequestMethod method;
	
	/*
	 * JaxrHandle is a temporary data structure
	 * that holds all connection specific information
	 */
	protected JaxrHandle jaxrHandle;
	
	/**
	 * Constructor
	 */
	public ServiceImpl() {
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#doApplyRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doApplyRequest(RequestContext ctx) {		
		// This method must be overridden by derived
		// Service classes
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#doDeleteRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doDeleteRequest(RequestContext ctx) {
		// This method must be overridden by derived
		// Service classes		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#doDownloadRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doDownloadRequest(RequestContext ctx) {
		// This method must be overridden by derived
		// Service classes		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#doExtractRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doExtractRequest(RequestContext ctx) {
		// This method must be overridden by derived
		// Service classes		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#doGetRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doGetRequest(RequestContext ctx) {		
		// This method must be overridden by derived
		// Service classes
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#doSetRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doSetRequest(RequestContext ctx) {
		// This method must be overridden by derived
		// Service classes		
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#doSubmitRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doSubmitRequest(RequestContext ctx) {
		// This method must be overridden by derived
		// Service classes
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#setJaxrHandle(de.kp.ames.web.core.regrep.JaxrHandle)
	 */
	public void setJaxrHandle(JaxrHandle jaxrHandle) {
		this.jaxrHandle = jaxrHandle;
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#getJaxrHandle()
	 */
	public JaxrHandle getJaxrHandle() {
		return this.jaxrHandle;
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#setMethod(de.kp.ames.web.core.method.RequestMethod)
	 */
	public void setMethod(RequestMethod method) {
		this.method = method;		
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#getMethod()
	 */
	public RequestMethod getMethod() {
		return this.method;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#processRequest(de.kp.ames.web.core.RequestContext)
	 */
	public void processRequest(RequestContext requestContext) {		
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#sendHTMLResponse(java.lang.String, javax.servlet.http.HttpServletResponse)
	 */
	public void sendHTMLResponse(String content, HttpServletResponse response) throws IOException {
		if (content == null) return;
		sendResponse(content, GlobalConstants.MT_HTML, response);
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#sendJSONResponse(java.lang.String, javax.servlet.http.HttpServletResponse)
	 */
	public void sendJSONResponse(String content, HttpServletResponse response) throws IOException {
		
		System.out.println("======> sendJSONResponse: " + content);
		
		if (content == null) return;
		sendResponse(content, GlobalConstants.MT_JSON, response);		
	}

	public void sendRSSResponse(String content, HttpServletResponse response) throws IOException {
		if (content == null) return;
		sendResponse(content, GlobalConstants.MT_RSS, response);		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#sendTextResponse(java.lang.String, javax.servlet.http.HttpServletResponse)
	 */
	public void sendTextResponse(String content, HttpServletResponse response) throws IOException {
		if (content == null) return;
		sendResponse(content, GlobalConstants.MT_TEXT, response);
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#sendXMLResponse(java.lang.String, javax.servlet.http.HttpServletResponse)
	 */
	public void sendXMLResponse(String content, HttpServletResponse response) throws IOException {
		if (content == null) return;
		sendResponse(content, GlobalConstants.MT_XML, response);
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#sendFileResponse(de.kp.ames.web.core.util.FileUtil, javax.servlet.http.HttpServletResponse)
	 */
	public void sendFileResponse(FileUtil file, HttpServletResponse response) throws IOException {

		if (file == null) return;

		// finally set http status
		response.setStatus( HttpServletResponse.SC_OK );
	
		response.setContentType(file.getMimetype());
		response.setContentLength(file.getLength());
	
		OutputStream os = response.getOutputStream();
	
		os.write(file.getFile());				
		os.close();
		
	}

	/**
	 * A specific method to enable file download even in a secure (SSL) environment
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void sendFileDownloadResponse(FileUtil file, HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (file == null) return;

		/*
		 * Distinguish between secure and non-secure download requests
		 */
		if (request.isSecure()) {

			response.addHeader("Cache-Control", "no-cache");
	        response.addHeader("Pragma", "no-cache");
            
	        response.addHeader("Expires", "-1");
    
        } else {
          
        	response.addHeader("Cache-Control", "private");
            response.addHeader("Pragma", "public");
    
        }

		/*
		 * Determine user agent
		 */
		String ua = request.getHeader("User-Agent").toLowerCase();        
		boolean isIE = ((ua.indexOf("msie 6.0") != -1) || (ua.indexOf("msie 7.0") != -1)) ? true : false;

		/*
		 * Encode file name
		 */		
        String encFileName = URLEncoder.encode(file.getFilename(), "UTF-8");

        if (isIE) {
                    
        	response.addHeader("Content-Disposition", "attachment;  filename=\"" + encFileName + "\"" );
            response.addHeader("Connection", "close");

            response.setContentType("application/force-download;  name=\"" + encFileName + "\"" );
            
        } else {
                    
        	response.addHeader("Content-Disposition", "attachment; filename=\"" + encFileName + "\"" );
                   
        	response.setContentType("application/octet-stream; name=\"" + encFileName + "\"" );
            response.setContentLength(file.getLength());

        }
		
		// finally set http status
		response.setStatus( HttpServletResponse.SC_OK );
	
		OutputStream os = response.getOutputStream();
	
		os.write(file.getFile());				
		os.close();
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#sendImageResponse(java.awt.image.BufferedImage, javax.servlet.http.HttpServletResponse)
	 */
	public void sendImageResponse(BufferedImage image, HttpServletResponse response) throws IOException {

		response.setStatus( HttpServletResponse.SC_OK );			    		
	    response.setContentType(GlobalConstants.MT_PNG);					

		OutputStream os = response.getOutputStream();

		ImageIO.write(image, "png", os);
		os.close();

	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#sendResponse(java.lang.String, java.lang.String, javax.servlet.http.HttpServletResponse)
	 */
	public void sendResponse(String content, String mimetype, HttpServletResponse response) throws IOException {

		response.setStatus( HttpServletResponse.SC_OK );
		response.setCharacterEncoding(GlobalConstants.UTF_8);
		
		response.setContentType(mimetype);
		
		byte[] bytes = content.getBytes(GlobalConstants.UTF_8);
		response.setContentLength(bytes.length);

		OutputStream os = response.getOutputStream();

		os.write(bytes);
		os.close();

	}

	/**
	 * A helper method to retrieve the request data (POST) 
	 * in terms of a String representation
	 * 
	 * @param ctx
	 * @return
	 */
	protected String getRequestData(RequestContext ctx) {
		
		StringBuffer buffer = null;

		try {
			BufferedReader reader = ctx.getRequest().getReader();
			buffer = new StringBuffer();
			
			String line;
			while ( (line = reader.readLine()) != null) {
				buffer.append(line);
			}

			/*
			 * Check if data is a JSONObject
			 * Then check if it has a 'data' key to unpack
			 */
			JSONObject jObject = new JSONObject(buffer.toString());
			if (jObject.has("data"))
				return jObject.getJSONObject("data").toString();
				
		} catch (Exception e) {
			// do nothing
		}

		return (buffer == null) ? null : buffer.toString();
		
	}
	
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#sendBadResponse(java.lang.String, int, javax.servlet.http.HttpServletResponse)
	 */
	public void sendErrorResponse(String content, int errorStatus, HttpServletResponse response) throws IOException {

		response.setStatus(errorStatus);
		response.setCharacterEncoding(GlobalConstants.UTF_8);
		
		response.setContentType(GlobalConstants.MT_TEXT);
		
		byte[] bytes = content.getBytes(GlobalConstants.UTF_8);
		response.setContentLength(bytes.length);

		OutputStream os = response.getOutputStream();

		os.write(bytes);
		os.close();
		
	}

	/**
	 * Send Bad Request response
	 * 
	 * @param ctx
	 * @param e
	 */
	protected void sendBadRequest(RequestContext ctx, Throwable e) {

		// TODO: xxx pa debug stacktrace
	    Writer result = new StringWriter();
	    PrintWriter printWriter = new PrintWriter(result);
	    e.printStackTrace(printWriter);

		String errorMessage = "[" + this.getClass().getName() + "] " + result.toString();
//		String errorMessage = "[" + this.getClass().getName() + "] " + e.getMessage();
		int errorStatus = HttpServletResponse.SC_BAD_REQUEST;
		
		try {
			sendErrorResponse(errorMessage, errorStatus, ctx.getResponse());

		} catch (IOException e1) {
			// do nothing
		}

	}

	/**
	 * Send Not Implemented response
	 * 
	 * @param ctx
	 */
	protected void sendNotImplemented(RequestContext ctx) {

		String errorMessage = "[" + this.getClass().getName() + "] Required parameters not provided.";
		int errorStatus = HttpServletResponse.SC_NOT_IMPLEMENTED;
		
		try {
			sendErrorResponse(errorMessage, errorStatus, ctx.getResponse());

		} catch (IOException e1) {
			// do nothing
		}

	}

	/**
	 * A helper method to encode a certain String
	 * 
	 * @param s
	 * @return
	 */
	protected String encodeHtml(String s) {
		
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < s.length(); i++) {
			char c = s.charAt(i);
			
			if ( c > 127 || c=='"' || c=='<' || c == '>') {
				sb.append("&#" + (int)c + ";");
			
			} else {
				sb.append(c);
				
			}
		}
		
		return sb.toString();
		
	}
	
}
