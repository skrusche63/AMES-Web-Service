package de.kp.ames.web.test.dms;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.test.dms
 *  Module: DmsTestImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #dms #test #web
 * </SemanticAssist>
 *
 */


import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.json.JSONObject;
import org.springframework.mock.web.MockHttpServletResponse;

import de.kp.ames.web.core.cache.CacheManager;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.function.dms.DmsServiceImpl;
import de.kp.ames.web.function.upload.UploadFactory;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;
import de.kp.ames.web.test.JaxrTestImpl;
import de.kp.ames.web.test.TestData;

public class DmsTestImpl extends JaxrTestImpl {

	/*
	 * static rimId to span different test-case instantiations
	 */
	private static String rimId;

	
	
	public DmsTestImpl() {
		super();
	}

	public DmsTestImpl(JaxrHandle jaxrHandle, String methodName) {
		super(jaxrHandle, methodName);
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getService()
	 */
	public Service getService() {
		return new DmsServiceImpl();
	}
	
	/*
	 * Populate document cache for test cases
	 * 
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
        System.out.println("====> DmsTestImpl.setUp ");
        
		UploadFactory factory = new UploadFactory();
		
		String cacheType = ClassificationConstants.FNC_ID_Document;
		CacheManager manager = factory.getCacheManager(cacheType);

		/*
		 * Mock upload document
		 */
		String item = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Document);
		String fileName = "TestUpload.txt";
		String mimeType = "text/plain";
		byte[] bytes = "This is a test document.".getBytes();
		
		/*
		 * Set to cache
		 */
		manager.setToCache(item, fileName, mimeType, bytes);

    }
	
	/*
	 * Cleanup document cache for test cases
	 * 
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
    protected void tearDown() throws Exception {
        System.out.println("====> DmsTestImpl.tearDown ");
        
		UploadFactory factory = new UploadFactory();
		
		String cacheType = ClassificationConstants.FNC_ID_Document;
		CacheManager manager = factory.getCacheManager(cacheType);

		/*
		 * Remove from cache
		 */
		String item = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Document);
		if (manager.getFromCache(item) != null)
			manager.removeFromCache(item);

	}

	/*
	 * Explicit test suite definition.
	 * 
	 * (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#suite(de.kp.ames.web.core.regrep.JaxrHandle, java.lang.String)
	 */
    @Override
	public Test suite(JaxrHandle jaxrHandle, String clazzName) throws Exception {

		System.out.println("====> DmsTestImpl.suite: " + clazzName);
		
		TestSuite suite = new TestSuite();
		
        /*
         * submit, get and delete
         */
        suite.addTest(new DmsTestImpl(jaxrHandle, "testDoSubmitRequest"));
        suite.addTest(new DmsTestImpl(jaxrHandle, "testDoGetRequest"));
        suite.addTest(new DmsTestImpl(jaxrHandle, "testDoDeleteRequest"));

		return suite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kp.ames.web.test.JaxrTestImpl#testDoSubmitRequest()
	 */
	@Override
	public void testDoSubmitRequest() throws Exception {

		System.out.println("====> DmsTestImpl.testDoSubmitRequest");

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
			DmsTestImpl.rimId = jObj.getString("id");

		}
	}

    /* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#createJsonSubmitData()
	 */
	public JSONObject createJsonSubmitData() throws Exception {
		return TestData.getInstance().getDmsSubmitData();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getDeleteAttributes()
	 */
	public HashMap<String,String> getDeleteAttributes() {

		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Document);

		/*
		 * delete document which was created from first test
		 */
		attributes.put(MethodConstants.ATTR_ITEM, DmsTestImpl.rimId);

		return attributes;
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getGetAttributes()
	 */
	public HashMap<String,String> getGetAttributes() {

		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Document);

		attributes.put(MethodConstants.ATTR_FORMAT, FormatConstants.FNC_FORMAT_ID_File);

		/*
		 * get document which was created from first test
		 */
		attributes.put(MethodConstants.ATTR_ITEM, DmsTestImpl.rimId);

		return attributes;
		
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getSubmitAttributes()
	 */
	public HashMap<String,String> getSubmitAttributes() {
		
		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Document);
		
		return attributes;
		
	}

}
