package de.kp.ames.web.test;

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

	public JaxrTestImpl() {
	};

	public JaxrTestImpl(JaxrHandle jaxrHandle, String methodName) {
		this.jaxrHandle = jaxrHandle;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTest#suite(de.kp.ames.web.core.regrep.JaxrHandle, java.lang.String)
	 */
	public Test suite(JaxrHandle jaxrHandle, String clazzName) throws Exception {
		
		/*
		 * Register JaxrHandl) {
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

				Object[] params = { method.getName(), jaxrHandle };
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
	
	public void testDoSubmitRequest() throws Exception {
		
		/*
		 * Specify submit method for type document only;
		 * for testing other Dms entry are not important
		 */
		RequestMethod method = createTestMethod(MethodConstants.METH_SUBMIT, getSubmitAttributes());

		/*
		 * Create mock Http Request
		 */
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getReader()).thenReturn(createSubmitData());

		/*
		 * Create mock Http Response
		 */
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		/*
		 * Create request context
		 */
		RequestContext ctx = new RequestContext(request, response);

		/*
		 * Invoke speific test case
		 */
		doSubmit(jaxrHandle, method, ctx);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());

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

		JSONObject jData = createJsonSubmitData();
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(bufferedReader.readLine()).thenReturn(jData.toString(), (String[]) null);

		return bufferedReader;
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTest#getService()
	 */
	public Service getService() {
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
