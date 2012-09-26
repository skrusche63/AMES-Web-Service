package de.kp.ames.web.test.comm;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.test.comm
 *  Module: CommTestImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #comm #test #web
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
import de.kp.ames.web.function.comm.CommServiceImpl;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;
import de.kp.ames.web.test.JaxrTestImpl;
import de.kp.ames.web.test.TestData;

public class CommTestImpl extends JaxrTestImpl {

	public CommTestImpl() {
		super();
	}

	public CommTestImpl(JaxrHandle jaxrHandle, String methodName) {
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

		System.out.println("====> CommTestImpl.suite: " + clazzName);
		
		TestSuite suite = new TestSuite();
		
        /*
         * create, get and delete
         */
        suite.addTest(new CommTestImpl(jaxrHandle, "testDoSubmitRequest"));
        suite.addTest(new CommTestImpl(jaxrHandle, "testDoGetRequest"));

		return suite;
	}

    /* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getService()
	 */
	public Service getService() {
		return new CommServiceImpl();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#createJsonSubmitData()
	 */
	public JSONObject createJsonSubmitData() throws Exception {
		return TestData.getInstance().getCommSubmitData();
	}

	/*
	 * (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#testDoGetRequest()
	 */
	@Override
	public void testDoGetRequest() throws Exception {
		
		System.out.println("====> CommTestImpl.testDoGetRequest");
		RequestContext ctx = createDoGetMockContext();

		super.doGetRequest(ctx);
		
		MockHttpServletResponse response = (MockHttpServletResponse) ctx.getResponse();

		if (response.getStatus() == HttpServletResponse.SC_OK) {
			JSONObject jObj = new JSONObject(response.getContentAsString());
			int totalRows = jObj.getJSONObject("response").getInt("totalRows");
			System.out.println("======> CommTestImpl.testDoGetRequest:  result totalRows: " +  totalRows);

			assertTrue(totalRows > 0);

		}
		
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getGetAttributes()
	 */
	public HashMap<String,String> getGetAttributes() {

		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Mail);

		attributes.put(MethodConstants.ATTR_FORMAT, FormatConstants.FNC_FORMAT_ID_Grid);
		return attributes;
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getSubmitAttributes()
	 */
	public HashMap<String,String> getSubmitAttributes() {
		
		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Mail);
		attributes.put(MethodConstants.ATTR_MAIL, TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Mail));
		
		return attributes;
		
	}

}
