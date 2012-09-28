package de.kp.ames.web.test.transform;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.test.transform
 *  Module: TransformTestImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #test #transform #web
 * </SemanticAssist>
 *
 */


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.json.JSONObject;
import org.springframework.mock.web.MockHttpServletResponse;

import de.kp.ames.web.core.cache.CacheManager;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.function.transform.TransformServiceImpl;
import de.kp.ames.web.function.upload.UploadFactory;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;
import de.kp.ames.web.test.JaxrTestImpl;
import de.kp.ames.web.test.TestData;

public class TransformTestImpl extends JaxrTestImpl {

	/*
	 * static rimId to span different test-case instantiations
	 */
	public static String rimId;

	/*
	 * Populate document cache for test cases
	 * 
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
        if (!this.getName().equals("testDoSubmitRequest")) return;

        System.out.println("====> TransformTestImpl.setUp ");
        
		UploadFactory factory = new UploadFactory();
		
		String cacheType = ClassificationConstants.FNC_ID_Transformator;
		CacheManager manager = factory.getCacheManager(cacheType);

		/*
		 * Mock upload document
		 */
		String item = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Transformator);
		String fileName = "TransformTest.xsl";
		
		/*
		 * read test file as bytes
		 */
		Class<?> loader = TransformTestImpl.class;
		InputStream is = loader.getResourceAsStream(fileName);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bos.write(is.read());
		byte[] bytes = bos.toByteArray(); 
		
		String mimeType = "text/xml";
		
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
        if (!this.getName().equals("testDoSubmitRequest")) return;
        
        System.out.println("====> TransformTestImpl.tearDown ");
        
		UploadFactory factory = new UploadFactory();
		
		String cacheType = ClassificationConstants.FNC_ID_Transformator;
		CacheManager manager = factory.getCacheManager(cacheType);

		/*
		 * Remove from cache
		 */
		String item = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Transformator);
		if (manager.getFromCache(item) != null)
			manager.removeFromCache(item);

	}	
	
	public TransformTestImpl() {
		super();
	}

	public TransformTestImpl(JaxrHandle jaxrHandle, String methodName) {
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

		System.out.println("====> TransformTestImpl.suite: " + clazzName);
		
		TestSuite suite = new TestSuite();
		
        /*
         * submit, get and delete
         */
        suite.addTest(new TransformTestImpl(jaxrHandle, "testDoSubmitRequest"));
        suite.addTest(new TransformTestImpl(jaxrHandle, "testDoGetRequest"));
        suite.addTest(new TransformTestImpl(jaxrHandle, "testDoDeleteRequest"));

		return suite;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getService()
	 */
	public Service getService() {
		return new TransformServiceImpl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kp.ames.web.test.JaxrTestImpl#testDoSubmitRequest()
	 */
	@Override
	public void testDoSubmitRequest() throws Exception {

		System.out.println("====> TransformTestImpl.testDoSubmitRequest");

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
			TransformTestImpl.rimId = jObj.getString("id");

		}
	}
	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#createJsonSubmitData()
	 */
	public JSONObject createJsonSubmitData() throws Exception {
		return TestData.getInstance().getTransformSubmitData();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getDeleteAttributes()
	 */
	public HashMap<String,String> getDeleteAttributes() {
		
		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Transformator);

		/*
		 * delete Transformation which was created from first test
		 */
		attributes.put(MethodConstants.ATTR_ITEM, TransformTestImpl.rimId);

		return attributes;
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getGetAttributes()
	 */
	public HashMap<String,String> getGetAttributes() {
		
		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Transformator);
		
		attributes.put(MethodConstants.ATTR_FORMAT, FormatConstants.FNC_FORMAT_ID_File);

		/*
		 * get Transformation which was created from first test
		 */
		attributes.put(MethodConstants.ATTR_ITEM, TransformTestImpl.rimId);

		return attributes;
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getSubmitAttributes()
	 */
	public HashMap<String,String> getSubmitAttributes() {
		
		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Transformator);
		
		return attributes;
		
	}
	
}
