package de.kp.ames.web.test.user;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.test.user
 *  Module: UserTestImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #test #user #web
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
import de.kp.ames.web.function.user.UserServiceImpl;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;
import de.kp.ames.web.test.JaxrTestImpl;
import de.kp.ames.web.test.TestData;

public class UserFormatGridTestImpl extends JaxrTestImpl {

	/*
	 * The User Test supports get & submit (edit) 
	 * requests for registry user
	 */
	
	public UserFormatGridTestImpl() {
		super();
		
	}

	public UserFormatGridTestImpl(JaxrHandle jaxrHandle, String methodName) {
		super(jaxrHandle, methodName);
	}

	@Override
	public Test suite(JaxrHandle jaxrHandle, String clazzName) throws Exception {

		System.out.println("====> UserFormGridTestImpl.suite: " + clazzName);

		TestSuite suite = new TestSuite();
		
        suite.addTest(new UserFormatGridTestImpl(jaxrHandle, "testDoGetRequest"));

		return suite;
	}

	@Override
	public void testDoGetRequest() throws Exception {
		
		System.out.println("====> UserFormGridTestImpl.testDoGetRequest");
		int iterations = 5;
		for (int i = 0; i < iterations; i++) {
			RequestContext ctx = createDoGetMockContext();

			super.doGetRequest(ctx);
			
			MockHttpServletResponse response = (MockHttpServletResponse) ctx.getResponse();
			
			if (response.getStatus() != HttpServletResponse.SC_OK) {
				System.out.println("======> UserFormGridTestImpl.testDoGetRequest: run #" + i + " status: " +  response.getStatus() + "\n\n Response: " + response.getContentAsString());
			} else {
				JSONObject jObj = new JSONObject(response.getContentAsString());
				int totalRows = jObj.getJSONObject("response").getInt("totalRows");
				System.out.println("======> UserFormGridTestImpl.testDoGetRequest:  run #" + i + " result totalRows: " +  totalRows);

				assertTrue(totalRows > 0);

			}
			
		}
	}

	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getService()
	 */
	public Service getService() {
		return new UserServiceImpl();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#createJsonSubmitData()
	 */
	public JSONObject createJsonSubmitData() throws Exception {
		return TestData.getInstance().getUserSubmitData();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getGetAttributes()
	 */
	public HashMap<String,String> getGetAttributes() {

		/*
		 * This test case retrieves a single existing user
		 */
		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_FORMAT, FormatConstants.FNC_FORMAT_ID_Grid);

		return attributes;
		
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getSubmitAttributes()
	 */
	public HashMap<String,String> getSubmitAttributes() {
		
		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_User);
		
		return attributes;
		
	}

}
