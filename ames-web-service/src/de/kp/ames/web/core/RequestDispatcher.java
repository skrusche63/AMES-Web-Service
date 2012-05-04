package de.kp.ames.web.core;
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

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensaml.saml2.core.Assertion;

import de.kp.ames.web.Bundle;
import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.method.RequestMethod;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.core.util.SamlUtil;

/**
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */

public class RequestDispatcher extends HttpServlet {

	/*
	 * generated serial number
	 */
	private static final long serialVersionUID = -7927476960641284338L;
	
	/*
	 * indicates whether registered services are initialized
	 */
	private boolean initialized = false;
	
	/*
	 * registered services
	 */
	private HashMap<String, Service> registeredServices;

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		initializeServices();
		
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*
		 * Retrieve JaxrHandle
		 */
		JaxrHandle jaxrHandle = getJaxrHandle(request);
		if (jaxrHandle == null) {
			
			/*
			 * A JaxrHandle is a prerequisite to access any of the
			 * web services registered with this dispatcher
			 */
			String message = "You are not authorized to access this web service.";
			sendUnauthorizedResponse(message, "text/plain", response);
			
		} else {
			/*
			 * Retrieve service from the unique service identifier
			 * provided with the actual request url
			 */
			Service service = getService(request);
			service.setJaxrHandle(jaxrHandle);
			
			RequestContext ctx = new RequestContext(request, response);
			ctx.setContext(getServletContext());

			/* 
			 * Process GET request
			 */
			service.processRequest(ctx);
			
		}
				
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*
		 * Retrieve JaxrHandle
		 */
		JaxrHandle jaxrHandle = getJaxrHandle(request);
		if (jaxrHandle == null) {
			
			/*
			 * A JaxrHandle is a prerequisite to access any of the
			 * web services registered with this dispatcher
			 */
			String message = "You are not authorized to access this web service.";
			sendUnauthorizedResponse(message, "text/plain", response);
			
		} else {
			/*
			 * Retrieve service from the unique service identifier
			 * provided with the actual request url
			 */
			Service service = getService(request);
			service.setJaxrHandle(jaxrHandle);
			
			RequestContext ctx = new RequestContext(request, response);
			ctx.setContext(getServletContext());

			/* 
			 * Process POST request
			 */
			service.processRequest(ctx);
			
		}
		
	}
		
	private void initializeServices() {

		if (initialized == true) return;
		registeredServices = new HashMap<String, Service>();
		
		// TODO
		
	}

	/**
	 * This method is used to inform the caller's user that the
	 * current request is not authorized to be performed
	 * @param content
	 * @param mimetype
	 * @param response
	 * @throws IOException
	 */
	private void sendUnauthorizedResponse(String content, String mimetype, HttpServletResponse response) throws IOException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setCharacterEncoding(GlobalConstants.UTF_8);
		
		response.setContentType(mimetype);
		
		byte[] bytes = content.getBytes(GlobalConstants.UTF_8);
		response.setContentLength(bytes.length);

		OutputStream os = response.getOutputStream();

		os.write(bytes);
		os.close();

	}

	/**
	 * This method retrieves the overall JaxrHandle, that is
	 * required to access the OASIS ebXML RegRep
	 * 
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	private JaxrHandle getJaxrHandle(HttpServletRequest request) {
		
		/*
		 * Retrieve endpoint of OASIS ebXML RegRep
		 */
		String endpoint = Bundle.getInstance().getString(GlobalConstants.REGREP_ENDPOINT);
		
		JaxrHandle jaxrHandle = new JaxrHandle(endpoint);
		
		/*
		 * Retrieve SAML v2.0 assertion assigned to the
		 * caller's user by the federated SAML IdP
		 */
		try {
			Assertion assertion = SamlUtil.getSamlAssertion(request);
			jaxrHandle.setCredential(assertion);
			
		} catch (Exception e) {
			// reset jaxrHandle in case of any exception
			jaxrHandle = null;
		}
		
		return jaxrHandle;
		
	}
	
	/**
	 * Common method to retrieve a certain functional service and
	 * initiate it with the request parameters provided by the 
	 * actual request
	 * 
	 * @param request
	 * @return
	 */
	private Service getService(HttpServletRequest request) {
		
		String path  = request.getRequestURI();
		String query = request.getQueryString(); 

		// determine service identifier from request URI
		int pos = path.lastIndexOf("/") + 1;
		String sid = path.substring(pos);

		Service service = null;

		Set<String> keys = registeredServices.keySet();
		Iterator<String> iter = keys.iterator();

		while (iter.hasNext()) {

			String key = iter.next();			
			if (sid.equals(key)) {
				service =  registeredServices.get(key);
				break;
			}
		
		}
		
		if (service == null) return null;
		
		// invoke request method from request uri
		RequestMethod method;
		try {
		
			method = new RequestMethod(query);
			service.setMethod(method);

		} catch (Exception e) {

			e.printStackTrace();
			service = null;
		
		}
		
		return service;
		
	}

}
