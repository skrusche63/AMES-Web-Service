package de.kp.ames.web.test;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.test
 *  Module: JaxrTestImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #jaxr #test #web
 * </SemanticAssist>
 *
 */


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.mock.web.MockHttpServletResponse;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.http.RequestMethod;
import de.kp.ames.web.shared.constants.MethodConstants;

public class JaxrTestImpl extends TestCase implements JaxrTest {

	protected JaxrHandle jaxrHandle;

	/*
	 * Reference to additional request data
	 */
	protected String type;	
	protected String data;

	public JaxrTestImpl() {}

	public JaxrTestImpl(JaxrHandle jaxrHandle, String methodName) {
		super(methodName);
		
		this.jaxrHandle = jaxrHandle;
	
	}

	public JaxrTestImpl(JaxrHandle jaxrHandle, String methodName, String type, String data) {
		super(methodName);
		
		this.jaxrHandle = jaxrHandle;
		
		this.type = type;
		this.data = data;
	
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTest#suite(de.kp.ames.web.core.regrep.JaxrHandle, java.lang.String)
	 */
	public Test suite(JaxrHandle jaxrHandle, String clazzName) throws Exception {
		
		/*
		 * Register JaxrHandle
		 * 
		 */
		this.jaxrHandle = jaxrHandle;
		
		TestSuite suite = new TestSuite();

		Class<?> clazz = Class.forName(clazzName);
		Method[] methods = clazz.getMethods();
		
		Class<?>[] paramTypes = { JaxrHandle.class, String.class };
		
		for (Method method : methods) {
			
			if (method.getName().startsWith("test")) {

				Constructor<?> constructor = clazz.getConstructor(paramTypes);

				Object[] params = {jaxrHandle, method.getName()};
				suite.addTest((Test) constructor.newInstance(params));

			}
		}

		return suite;

	}

	public Test suite(JaxrHandle jaxrHandle, String clazzName, String type, String data) throws Exception {
		
		/*
		 * Register JaxrHandle
		 * 
		 */
		this.jaxrHandle = jaxrHandle;
		
		TestSuite suite = new TestSuite();

		Class<?> clazz = Class.forName(clazzName);
		Method[] methods = clazz.getMethods();
		
		Class<?>[] paramTypes = { JaxrHandle.class, String.class, String.class, String.class };
		
		for (Method method : methods) {
			
			/*
			 * Focus POST requests on SUBMIT tests
			 */
			if (method.getName().startsWith("testDoSubmit")) {

				Constructor<?> constructor = clazz.getConstructor(paramTypes);

				Object[] params = {jaxrHandle, method.getName(), type, data};
				suite.addTest((Test) constructor.newInstance(params));

			}
		}

		return suite;
	}
	
	private RequestMethod createTestMethod(String name, HashMap<String,String> attributes) {

		RequestMethod method = mock(RequestMethod.class);
	
		/*
		 * Specify method name
		 */
		when(method.getName()).thenReturn(name);
		
		/*
		 * Specify method attributes
		 */
		Set<String> keys = attributes.keySet();
		for (String key:keys) {
			when(method.getAttribute(key)).thenReturn(attributes.get(key));
		}
		
		return method;
	
	}

	/** DELETE     DELETE     DELETE     DELETE     DELETE     DELETE **/
	
	public RequestContext createDoDeleteMockContext() {

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
		return new RequestContext(request, response);
		
	}
	
	public void doDeleteRequest(RequestContext ctx) {

		RequestMethod method = createTestMethod(MethodConstants.METH_GET, getDeleteAttributes());
	
		/*
		 * Invoke specific test case
		 */
		doDelete(jaxrHandle, method, ctx);
		assertEquals(HttpServletResponse.SC_OK, ((MockHttpServletResponse)ctx.getResponse()).getStatus());

	}
	
	public void testDoDeleteRequest() throws Exception {
		doDeleteRequest(createDoDeleteMockContext());
	}

	/** GET     GET     GET     GET     GET     GET **/

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
		return new RequestContext(request, response);

	}

	public void doGetRequest(RequestContext ctx) throws Exception {

		RequestMethod method = createTestMethod(MethodConstants.METH_GET, getGetAttributes());

		/*
		 * Invoke specific test case
		 */
		doGet(jaxrHandle, method, ctx);
		assertEquals(HttpServletResponse.SC_OK, ((MockHttpServletResponse)ctx.getResponse()).getStatus());

	}

	public void testDoGetRequest() throws Exception {		
		doGetRequest(createDoGetMockContext());
	}

	/** SUBMIT     SUBMIT     SUBMIT     SUBMIT     SUBMIT     SUBMIT **/

	public RequestContext createDoSubmitMockContext() throws Exception {

		/*
		 * Create mock submit data
		 */
		JSONObject jData = createJsonSubmitData();
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(bufferedReader.readLine()).thenReturn(jData.toString(), (String[]) null);

		/*
		 * Create mock Http Request
		 */
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getReader()).thenReturn(bufferedReader);

		/*
		 * Create mock Http Response
		 */
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		/*
		 * Create request context
		 */
		return new RequestContext(request, response);

	}
	
	public void doSubmitRequest(RequestContext ctx) throws Exception {
		
		RequestMethod method = createTestMethod(MethodConstants.METH_SUBMIT, getSubmitAttributes());

		/*
		 * Invoke speific test case
		 */
		doSubmit(jaxrHandle, method, ctx);
		assertEquals(HttpServletResponse.SC_OK, ((MockHttpServletResponse)ctx.getResponse()).getStatus());
		
	}
	
	public void testDoSubmitRequest() throws Exception {
		doSubmitRequest(createDoSubmitMockContext());
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTest#createJsonSubmitData()
	 */
	public JSONObject createJsonSubmitData() throws Exception  {
		/*
		 * Must be overridden
		 */
		return null;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTest#createSubmitData()
	 */
	public BufferedReader createSubmitData() throws Exception {
		/*
		 * This method is no longer used
		 */
		return null;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTest#getService()
	 */
	public Service getService() {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTest#getDeleteAttributes()
	 */
	public HashMap<String, String> getDeleteAttributes() {
		/*
		 * Must be overridden
		 */
		return null;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTest#getGetAttributes()
	 */
	public HashMap<String, String> getGetAttributes() {
		/*
		 * Must be overridden
		 */
		return null;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTest#getSetAttributes()
	 */
	public HashMap<String,String> getSetAttributes() {
		/*
		 * Must be overridden
		 */
		return null;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTest#getSubmitAttributes()
	 */
	public HashMap<String,String> getSubmitAttributes() {
		/*
		 * Must be overridden
		 */
		return null;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTest#doDelete(de.kp.ames.web.core.regrep.JaxrHandle, de.kp.ames.web.http.RequestMethod, de.kp.ames.web.http.RequestContext)
	 */
	public void doDelete(JaxrHandle jaxrHandle, RequestMethod method, RequestContext ctx) {
		/*
		 * Specify service
		 */
		Service service = getService();

		service.setJaxrHandle(jaxrHandle);
		service.setMethod(method);
		
		service.doDeleteRequest(ctx);
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTest#doGet(de.kp.ames.web.core.regrep.JaxrHandle, de.kp.ames.web.http.RequestMethod, de.kp.ames.web.http.RequestContext)
	 */
	public void doGet(JaxrHandle jaxrHandle, RequestMethod method, RequestContext ctx) {
		/*
		 * Specify service
		 */
		Service service = getService();

		service.setJaxrHandle(jaxrHandle);
		service.setMethod(method);
		
		service.doGetRequest(ctx);
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTest#doSet(de.kp.ames.web.core.regrep.JaxrHandle, de.kp.ames.web.http.RequestMethod, de.kp.ames.web.http.RequestContext)
	 */
	public void doSet(JaxrHandle jaxrHandle, RequestMethod method, RequestContext ctx) {
		/*
		 * Specify service
		 */
		Service service = getService();

		service.setJaxrHandle(jaxrHandle);
		service.setMethod(method);
		
		service.doSetRequest(ctx);
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTest#doSubmit(de.kp.ames.web.core.regrep.JaxrHandle, de.kp.ames.web.http.RequestMethod, de.kp.ames.web.http.RequestContext)
	 */
	public void doSubmit(JaxrHandle jaxrHandle, RequestMethod method, RequestContext ctx) {

		/*
		 * Specify service
		 */
		Service service = getService();

		service.setJaxrHandle(jaxrHandle);
		service.setMethod(method);
		
		service.doSubmitRequest(ctx);
		
	}
	
}
