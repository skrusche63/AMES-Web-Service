package de.kp.ames.web.core.service;

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
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.RequestContext;
import de.kp.ames.web.core.method.RequestMethod;
import de.kp.ames.web.core.regrep.JaxrHandle;

/**
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */

public class ServiceImpl implements Service{

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
	
	private static String MT_HTML = "text/html";
	private static String MT_JSON = "application/json";
	private static String MT_PNG  = "image/png";
	private static String MT_TEXT = "text/plain";
	private static String MT_XML  = "text/xml";
	
	/**
	 * Constructor
	 */
	public ServiceImpl() {
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
		sendResponse(content, MT_HTML, response);
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#sendJSONResponse(java.lang.String, javax.servlet.http.HttpServletResponse)
	 */
	public void sendJSONResponse(String content, HttpServletResponse response) throws IOException {
		if (content == null) return;
		sendResponse(content, MT_JSON, response);		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#sendTextResponse(java.lang.String, javax.servlet.http.HttpServletResponse)
	 */
	public void sendTextResponse(String content, HttpServletResponse response) throws IOException {
		if (content == null) return;
		sendResponse(content, MT_TEXT, response);
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#sendXMLResponse(java.lang.String, javax.servlet.http.HttpServletResponse)
	 */
	public void sendXMLResponse(String content, HttpServletResponse response) throws IOException {
		if (content == null) return;
		sendResponse(content, MT_XML, response);
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.Service#sendImageResponse(java.awt.image.BufferedImage, javax.servlet.http.HttpServletResponse)
	 */
	public void sendImageResponse(BufferedImage image, HttpServletResponse response) throws IOException {

		response.setStatus( HttpServletResponse.SC_OK );			    		
	    response.setContentType(MT_PNG);					

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

}
