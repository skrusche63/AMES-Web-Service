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

public class UserTestImpl extends JaxrTestImpl {

	/*
	 * The User Test supports get & submit (edit) 
	 * requests for registry user
	 */
	
	public UserTestImpl() {
		super();
	}

	public UserTestImpl(JaxrHandle jaxrHandle, String methodName) {
		super(jaxrHandle, methodName);
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getService()
	 */
	public Service getService() {
		return new UserServiceImpl();
	}

	/*
	 * Explicit test suite definition.
	 * 
	 * (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#suite(de.kp.ames.web.core.regrep.JaxrHandle, java.lang.String)
	 */
    @Override
	public Test suite(JaxrHandle jaxrHandle, String clazzName) throws Exception {

		System.out.println("====> UserTestImpl.suite: " + clazzName);

		TestSuite suite = new TestSuite();
		
        /*
         * There is no create and delete user use-case
         */
        suite.addTest(new UserTestImpl(jaxrHandle, "testDoGetRequest"));
        suite.addTest(new UserTestImpl(jaxrHandle, "testDoSubmitRequest"));

		return suite;
	}

    /*
     * (non-Javadoc)
     * @see de.kp.ames.web.test.JaxrTestImpl#testDoGetRequest()
     */
	@Override
	public void testDoGetRequest() throws Exception {
		
		System.out.println("====> UserTestImpl.testDoGetRequest");
	
		RequestContext ctx = createDoGetMockContext();

		super.doGetRequest(ctx);
		
		MockHttpServletResponse response = (MockHttpServletResponse) ctx.getResponse();
		System.out.println("====> testDoGetRequest: status: " +  response.getStatus() + "\n\n Response: " + response.getContentAsString());
		
		/*
		  {"rimObjectType":"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Person:User","rimCountryCode":"1","rim
			Name":"Najmi, Farrukh Salahudin","rimStateOrProvince":"MA","rimVersion":"1.0","rimLid":"urn:freebxml:registry:predefinedusers
			:farrukh","rimId":"urn:freebxml:registry:predefinedusers:farrukh","rimCountry":"USA","rimAreaCode":"781","rimIcon":"user","ri
			mPostalCode":"","rimMiddleName":"Salahudin","rimPhoneNumber":"442-9017","rimAuthor":"Registry  Operator","rimCity":"Burlingto
			n","rimOwner":"urn:freebxml:registry:predefinedusers:farrukh","rimLastName":"Najmi","rimClassification":"[\"urn:freebxml:regi
			stry:demoDB:SubjectRole:ProjectLead\"]","rimFirstName":"Farrukh","rimHome":"http://localhost:6480/omar/registry","rimEmail":"
			Farrukh.Najmi@Sun.COM","rimStreet":"Network Dr.","rimStatus":"Submitted","rimTimestamp":"Thu Feb 02 14:51:33 CET 2012","rimSt
			reeNumber":"1","rimSlot":"{}","rimEvent":"updated","rimPhoneExtension":""}
		 */
		
		if (response.getStatus() == HttpServletResponse.SC_OK) {
			JSONObject jObj = new JSONObject(response.getContentAsString());
			assertTrue(jObj.getString("rimFirstName").equals("Farrukh"));

		}
	}

    
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kp.ames.web.test.JaxrTestImpl#testDoSubmitRequest()
	 */
	@Override
	public void testDoSubmitRequest() throws Exception {

		System.out.println("====> UserTestImpl.testDoSubmitRequest");

		RequestContext ctx = createDoSubmitMockContext();

		super.doSubmitRequest(ctx);

		MockHttpServletResponse response = (MockHttpServletResponse) ctx.getResponse();
		System.out.println("====> testDoSubmitRequest: status: " + response.getStatus() + "\n\n Response: "
				+ response.getContentAsString());

		if (response.getStatus() == HttpServletResponse.SC_OK) {
			/*
			 * {"id":"urn:uid:de:kp:samltest","message":"User successfully updated."
			 * ,"success":true}
			 */
			JSONObject jObj = new JSONObject(response.getContentAsString());
			assertTrue(jObj.getBoolean("success"));

		}
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
		attributes.put(MethodConstants.ATTR_FORMAT, FormatConstants.FNC_FORMAT_ID_Object);

		/*
		 * Test User
		 */
		String item = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_User);
		attributes.put(MethodConstants.ATTR_ITEM, item);

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
