package de.kp.ames.web.test;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.test
 *  Module: TestDispatcher
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #dispatcher #test #web
 * </SemanticAssist>
 *
 */


import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensaml.saml2.core.Assertion;

import de.kp.ames.web.Bundle;
import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.util.SamlUtil;


public class TestDispatcher extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private boolean initialized = false;
	private Map<String, TestService> services = new HashMap<String, TestService>();
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		initializeServices();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("====> TestDispatcher.doGet");

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

			String uri = request.getRequestURI();
	
			int pos = uri.lastIndexOf("/") + 1;
			String sid = uri.substring(pos);
			
			ServletContext context = getServletContext();
			
			TestService service = getService(sid);
			service.setJaxrHandle(jaxrHandle);
			
			service.execute(request, response, context);

		}
		
		return;			
				
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		System.out.println("====> TestDispatcher.doPost");

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

			String uri = request.getRequestURI();
	
			int pos = uri.lastIndexOf("/") + 1;
			String sid = uri.substring(pos);
			
			ServletContext context = getServletContext();
			

			TestService service = getService(sid);
			System.out.println("====> TestDispatcher.doPost service: " + service.getClass().getSimpleName());
			
			service.setJaxrHandle(jaxrHandle);
			
			service.execute(request, response, context);

		}
		
		return;			
		
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
	
	private void initializeServices() {

		if (initialized == true) return;

		services.put("unit", new TestServiceImpl());
		initialized = true;
		
	}

	private TestService getService(String sid) {

		TestService service = null;

		Set<String> keys = services.keySet();
		Iterator<String> iter = keys.iterator();
		while (iter.hasNext()) {

			String key = iter.next();			
			if (sid.equals(key)) {
				service =  services.get(key);
			}
		
		}
		
		return service;
		
	}
	
}
