package de.kp.ames.web.http;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;

import org.opensaml.saml2.core.Assertion;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.kp.ames.web.Bundle;
import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.core.util.BaseParam;
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
			 * Evaluate method
			 */
			
			if (service.getMethod() == null) {
				
				String errorMessage = "[" + service.getClass().getName() + "] Method not available.";
				int errorStatus = HttpServletResponse.SC_NOT_IMPLEMENTED;
				
				try {
					service.sendErrorResponse(errorMessage, errorStatus, ctx.getResponse());

				} catch (IOException e) {
					// do nothing
				}

				return;
				
			}

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
			 * Evaluate method
			 */
			
			if (service.getMethod() == null) {
				
				String errorMessage = "[" + service.getClass().getName() + "] Method not available.";
				int errorStatus = HttpServletResponse.SC_NOT_IMPLEMENTED;
				
				try {
					service.sendErrorResponse(errorMessage, errorStatus, ctx.getResponse());

				} catch (IOException e) {
					// do nothing
				}

				return;
				
			}

			/* 
			 * Process POST request
			 */
			service.processRequest(ctx);
			
		}
		
	}
		
	/**
	 * The main method to register new services, i.e. additional
	 * core and business functionality 
	 */
	private void initializeServices() {

		if (initialized == true) return;

		/*
		 * Temporary cache for all registered services
		 */
		registeredServices = new HashMap<String, Service>();

		/*
		 * Retrieve actual service configuration
		 * and initialize respective services
		 */
		ArrayList<BaseParam> serviceConfig = getServiceConfig();
		for (BaseParam param:serviceConfig) {
			
			String key = param.getKey();
			String val = param.getValue();
			
			Service service = createServiceForName(val);
			if (service == null) continue;
			
			registeredServices.put(key, service);

		}
	
	}

	/**
	 * This method is used to inform the caller's user that
	 * the current request is not authorized to be performed
	 * 
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
		
		String path = request.getRequestURI();

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
		
			method = new RequestMethod(request);
			service.setMethod(method);

		} catch (Exception e) {

			e.printStackTrace();
			service = null;
		
		}
		
		return service;
		
	}

	/**
	 * Helper method to create a certain service from a given class name
	 * 
	 * @param serviceName
	 * @return
	 */
	private Service createServiceForName(String serviceName) {

		try {

			Class<?> clazz = Class.forName(serviceName);
			Constructor<?> constructor = clazz.getConstructor();
			
			Object instance = constructor.newInstance();
			return (Service)instance;

		} catch (Exception e) {
			e.printStackTrace();
		
		} finally {}
		
		return null;
		
	}
	
	private ArrayList<BaseParam> getServiceConfig() {
		
		ArrayList<BaseParam> services = new ArrayList<BaseParam>();

		/*
		 * Bulletin Service to support a posting between
		 * different communities of interest and their
		 * associated members
		 */
		services.add(new BaseParam("bulletin", "de.kp.ames.web.function.bulletin.BulletinServiceImpl"));
		
		/*
		 * Chat & Mail Service to support the registration (submit)
		 * of chat and mail message via a selected client
		 */
		services.add(new BaseParam("comm", "de.kp.ames.web.function.comm.CommServiceImpl"));
		
		/*
		 * Disclaimer Service to represent a disclaimer
		 * to the caller's user; a disclaimer is returned
		 * after successful login
		 */
		services.add(new BaseParam("disclaimer", "de.kp.ames.web.function.security.DisclaimerImpl"));

		/*
		 * Core Group Service is used to manage community
		 * of interest specific information objects including
		 * organizations
		 */
		services.add(new BaseParam("group", "de.kp.ames.web.core.group.GroupServiceImpl"));

		/*
		 * Map Service is used to retrieve map specific
		 * information such as layers
		 */
		services.add(new BaseParam("map", "de.kp.ames.web.function.map.MapServiceImpl"));

		/*
		 * Role service is used to retrieve role & rights
		 * specific information
		 */
		services.add(new BaseParam("role", "de.kp.ames.web.function.role.RoleServiceImpl"));

		/*
		 * RSS Service is used to provide actually registered
		 * and temporarily cached registry objects as an RSS feed
		 */
		services.add(new BaseParam("rss", "de.kp.ames.web.core.rss.RssServiceImpl"));

		/*
		 * Security Service is used to register additional
		 * user credentials (i.e. alias, keypass) that
		 * are used to access external chat & mail server
		 */
		services.add(new BaseParam("security", "de.kp.ames.web.function.security.SecurityServiceImpl"));

		/*
		 * Core Search Service that supports access to
		 * the Enterprise Search Server Solr
		 */
		services.add(new BaseParam("search", "de.kp.ames.web.core.search.SearchServiceImpl"));
		
		/*
		 * Transform Service that supports the XSL
		 * transformation of registry objects
		 */
		services.add(new BaseParam("transform", "de.kp.ames.web.function.transform.TransformServiceImpl"));

		/*
		 * Upload service that supports the upload and
		 * malware scan of external files
		 */
		services.add(new BaseParam("upload", "de.kp.ames.web.function.upload.UploadServiceImpl"));

		/*
		 * User service is used to retrieve user
		 * specific information
		 */
		services.add(new BaseParam("user", "de.kp.ames.web.function.user.UserServiceImpl"));

		/*
		 * Core Vocabulary Service is used to retrieve all
		 * semantic information to classify other objects
		 */
		services.add(new BaseParam("vocab", "de.kp.ames.web.core.vocab.VocabServiceImpl"));

		return services;
		
	}
	
	/**
	 * Retrieve the actually registered services from the server's file system
	 * 
	 * @param ctx
	 * @return
	 */
	@SuppressWarnings("unused")
	private ArrayList<BaseParam> getServices(RequestContext ctx) {

		/*
		 * Retrieve services declaration from the file system
		 */
		ServletContext context = ctx.getContext();
		
		String filename = "/WEB-INF/resources/services.xml";		  
		InputStream is = context.getResourceAsStream(filename);

		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);

		ArrayList<BaseParam> params = new ArrayList<BaseParam>();
		
		try {
			
			Document xmlDoc = factory.newDocumentBuilder().parse(is);
			NodeList services = xmlDoc.getElementsByTagName("service");

			for (int i=0; i < services.getLength(); i++) {
				
				Element eService = (Element)services.item(i);
				BaseParam param = getServiceParams(eService);
				
				if (param == null) continue;
				
				params.add(param);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}

		return params;

	}
	
	private BaseParam getServiceParams(Element eService) {

		String id    = "";
		String clazz = "";
		
		NodeList properties = eService.getChildNodes();
		for (int i=0; i < properties.getLength(); i++) {
			
			Element property = (Element)properties.item(i);

			if (property.getTagName().equals("service-id")) {
				id = property.getTextContent().trim();
			}

			if (property.getTagName().equals("service-class")) {
				clazz = property.getTextContent().trim();
			}

		}

		if (id.equals("")  || clazz.equals("")) return null;
		return new BaseParam(id, clazz);
		
	}

}
