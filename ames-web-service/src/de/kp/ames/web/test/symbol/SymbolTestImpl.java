package de.kp.ames.web.test.symbol;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.test.symbol
 *  Module: SymbolTestImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #symbol #test #web
 * </SemanticAssist>
 *
 */


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.springframework.mock.web.MockHttpServletResponse;

import de.kp.ames.web.Bundle;
import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.function.symbol.SymbolServiceImpl;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;
import de.kp.ames.web.test.JaxrTestImpl;
import de.kp.ames.web.test.TestData;

public class SymbolTestImpl extends JaxrTestImpl {

	public SymbolTestImpl() {
		super();
	}

	public SymbolTestImpl(JaxrHandle jaxrHandle, String methodName) {
		super(jaxrHandle, methodName);
	}

	/*
	 * Explicit test suite definition.
	 * 
	 * (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#suite(de.kp.ames.web.core.regrep.JaxrHandle, java.lang.String)
	 */
    @Override
	public Test suite(JaxrHandle jaxrHandle, String clazzName) throws Exception {

		System.out.println("====> SymbolTestImpl.suite: " + clazzName);
		
		TestSuite suite = new TestSuite();
		
        /*
         * get
         */
        suite.addTest(new SymbolTestImpl(jaxrHandle, "testDoGetRequest"));

		return suite;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getService()
	 */
	public Service getService() {
		return new SymbolServiceImpl();
	}
	
	
	public RequestContext createDoGetMockContext() {

		/*
		 * Create mock Http Request
		 */
		HttpServletRequest request = mock(HttpServletRequest.class);

		/*
		 * Create mock Http Response
		 */
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		/*
		 * Create request context
		 */
		//return new RequestContext(request, response);
		RequestContext ctx = mock(RequestContext.class);
		when(ctx.getRequest()).thenReturn(request);
		when(ctx.getResponse()).thenReturn(response);
		
		// prepare init of SymbolProcessor
		// when(ctx.getContext()).thenReturn(getServletContext()); // how to get context from TestServiceImpl here?
		String SYMBOL_PATH = Bundle.getInstance().getString(GlobalConstants.SYMBOL_APP6B_FILE);
		String CONTAINER_PATH = "C:/xvalve6/apache-tomcat-6.0.35__saml32_jdk6/webapps/ames";

		try {
			ServletContext servletCtx = mock(ServletContext.class);
			// step wise, 1st ServletContext
			when(ctx.getContext()).thenReturn(servletCtx);
			// 2nd resource
			when(servletCtx.getResourceAsStream(SYMBOL_PATH)).thenReturn(new FileInputStream(new File(CONTAINER_PATH + SYMBOL_PATH)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ctx;
		
	}

	/*
	 "{"response":{"status":0,"data":[{"id":"1.X.3.2","isFolder":true,"symbol":"S*G*E-----*****","name":"GROUND TRACK EQUIPMENT","parent":"1.X.3","symb":"S*G*E-----*****"},{"id":"1.X.3.3","isFolder":true,"symbol":"S*G*I-----H****","name":"INSTALLATION","parent":"1.X.3","symb":"S*G*I-----H****"},{"id":"1.X.3.4","isFolder":true,"symbol":"S*G*IR----H****","name":"SEA SURFACE INSTALLATION","parent":"1.X.3","symb":"S*G*IR----H****"},{"id":"1.X.3.1","isFolder":true,"symbol":"S*G*U-----*****","name":"UNIT","parent":"1.X.3","symb":"S*G*U-----*****"}]}}"
	 */
	
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getGetAttributes()
	 */
	public HashMap<String,String> getGetAttributes() {

		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_SYMBOL_ID_APP6B);

		attributes.put(MethodConstants.ATTR_FORMAT, FormatConstants.FNC_FORMAT_ID_Tree);

		String parent = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_SYMBOL_ID_APP6B);
		attributes.put(MethodConstants.ATTR_PARENT, parent);

		return attributes;
		
	}

}
