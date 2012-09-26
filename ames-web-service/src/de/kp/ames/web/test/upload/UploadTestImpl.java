package de.kp.ames.web.test.upload;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.test.upload
 *  Module: UploadTestImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #test #upload #web
 * </SemanticAssist>
 *
 */


import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;
import de.kp.ames.web.core.cache.CacheManager;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.function.upload.UploadFactory;
import de.kp.ames.web.function.upload.UploadServiceImpl;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;
import de.kp.ames.web.test.JaxrTestImpl;
import de.kp.ames.web.test.TestData;

public class UploadTestImpl extends JaxrTestImpl {

	public UploadTestImpl() {
		super();
	}

	public UploadTestImpl(JaxrHandle jaxrHandle, String methodName) {
		super(jaxrHandle, methodName);
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getService()
	 */
	public Service getService() {
		return new UploadServiceImpl();
	}
	
	/*
	 * Populate document cache for test cases
	 * 
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
        System.out.println("====> UploadTestImpl.setUp ");
        
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
        System.out.println("====> UploadTestImpl.tearDown ");
        
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

		System.out.println("====> UploadTestImpl.suite: " + clazzName);
		
		TestSuite suite = new TestSuite();
		
        /*
         * get and delete
         */
        suite.addTest(new UploadTestImpl(jaxrHandle, "testDoGetRequest"));
        suite.addTest(new UploadTestImpl(jaxrHandle, "testDoDeleteRequest"));

		return suite;
	}

    
	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getDeleteAttributes()
	 */
	public HashMap<String,String> getDeleteAttributes() {

		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Document);

		/*
		 * Test Document
		 */
		String item = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Document);
		attributes.put(MethodConstants.ATTR_ITEM, item);

		return attributes;
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getGetAttributes()
	 */
	public HashMap<String,String> getGetAttributes() {

		/*
		 * This test case retrieves a single existing document
		 */
		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Document);

		/*
		 * Test Document
		 */
		attributes.put(MethodConstants.ATTR_FORMAT, FormatConstants.FNC_FORMAT_ID_File);

		String item = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Document);
		attributes.put(MethodConstants.ATTR_ITEM, item);

		return attributes;
		
	}

}
