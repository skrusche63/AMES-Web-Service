package de.kp.ames.web.test.access;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.test.access
 *  Module: AccessTestImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #access #test #web
 * </SemanticAssist>
 *
 */


import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.json.JSONObject;
import org.springframework.mock.web.MockHttpServletResponse;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.function.access.AccessServiceImpl;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;
import de.kp.ames.web.test.JaxrTestImpl;
import de.kp.ames.web.test.TestData;

public class AccessTestImpl extends JaxrTestImpl {

	/*
	 * static rimId to span different test-case instantiations
	 */
	private static String rimId;

	public AccessTestImpl() {
		super();
	}

	public AccessTestImpl(JaxrHandle jaxrHandle, String methodName) {
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

		System.out.println("====> AccessTestImpl.suite: " + clazzName);
		
		TestSuite suite = new TestSuite();
		
        /*
         * create, get and delete
         */
        suite.addTest(new AccessTestImpl(jaxrHandle, "testDoSubmitRequest"));
        suite.addTest(new AccessTestImpl(jaxrHandle, "testDoGetRequest"));
        suite.addTest(new AccessTestImpl(jaxrHandle, "testDoDeleteRequest"));

		return suite;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getService()
	 */
	public Service getService() {
		return new AccessServiceImpl();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kp.ames.web.test.JaxrTestImpl#testDoSubmitRequest()
	 */
	@Override
	public void testDoSubmitRequest() throws Exception {

		System.out.println("====> AccessTestImpl.testDoSubmitRequest");

		RequestContext ctx = createDoSubmitMockContext();

		super.doSubmitRequest(ctx);

		MockHttpServletResponse response = (MockHttpServletResponse) ctx.getResponse();
		System.out.println("====> testDoSubmitRequest: status: " + response.getStatus() + "\n\n Response: "
				+ response.getContentAsString());

		if (response.getStatus() == HttpServletResponse.SC_OK) {
			/*
			 * {"id":"urn:...","message":"... successfully updated."
			 * ,"success":true}
			 */
			JSONObject jObj = new JSONObject(response.getContentAsString());
			assertTrue(jObj.getBoolean("success"));
			
			// remember created rimId for further test cases
			AccessTestImpl.rimId = jObj.getString("id");

		}
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#createJsonSubmitData()
	 */
	public JSONObject createJsonSubmitData() throws Exception {
		return TestData.getInstance().getAccessSubmitData();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getDeleteAttributes()
	 */
	public HashMap<String,String> getDeleteAttributes() {

		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Accessor);

		/*
		 * delete Accessor which was created from first test
		 */
		System.out.println("====> AccessTestImpl.getDeleteAttributes rimId: " + AccessTestImpl.rimId);
		attributes.put(MethodConstants.ATTR_ITEM, AccessTestImpl.rimId);

		return attributes;
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getGetAttributes()
	 */
	public HashMap<String,String> getGetAttributes() {

		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Accessor);

		attributes.put(MethodConstants.ATTR_FORMAT, FormatConstants.FNC_FORMAT_ID_Object);

		/*
		 * get Accessor which was created from first test
		 */
		attributes.put(MethodConstants.ATTR_ITEM, AccessTestImpl.rimId);

		return attributes;
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getSubmitAttributes()
	 */
	public HashMap<String,String> getSubmitAttributes() {
		
		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Accessor);
		
		return attributes;
		
	}

}
