package de.kp.ames.web.test.product;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.test.product
 *  Module: ProductTestImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #product #test #web
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
import de.kp.ames.web.function.product.ProductServiceImpl;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;
import de.kp.ames.web.test.JaxrTestImpl;
import de.kp.ames.web.test.TestData;
import de.kp.ames.web.test.transform.TransformTestImpl;

public class ProductTestImpl extends JaxrTestImpl {

	/*
	 * static rimId to span different test-case instantiations
	 */
	private static String rimId;
	
	public ProductTestImpl() {
		super();
	}

	public ProductTestImpl(JaxrHandle jaxrHandle, String methodName) {
		super(jaxrHandle, methodName);
	}
	
	/*
	 * Generate a transformator for submit-test case
	 * 
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
        if (!this.getName().equals("testDoSubmitRequest")) return;

        System.out.println("====> ProductTestImpl.setUp ");
        
        /*
         * prepare a transformator
         */
    	new TransformTestImpl(jaxrHandle, "testDoSubmitRequest").run();

    }
	
	/*
	 * Cleanup transformator from submit-test case
	 * 
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
    protected void tearDown() throws Exception {
        if (!this.getName().equals("testDoSubmitRequest")) return;

        System.out.println("====> ProductformTestImpl.tearDown ");
        
        /*
         * teardown the transformator
         */
    	new TransformTestImpl(jaxrHandle, "testDoSubmitRequest").run();


	}	
	/*
	 * Explicit test suite definition.
	 * 
	 * (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#suite(de.kp.ames.web.core.regrep.JaxrHandle, java.lang.String)
	 */
    @Override
	public Test suite(JaxrHandle jaxrHandle, String clazzName) throws Exception {

		System.out.println("====> ProductTestImpl.suite: " + clazzName);
		
		TestSuite suite = new TestSuite();


        /*
         * create, get and delete
         */

        suite.addTest(new ProductTestImpl(jaxrHandle, "testDoSubmitRequest"));
        suite.addTest(new ProductTestImpl(jaxrHandle, "testDoGetRequest"));
        suite.addTest(new ProductTestImpl(jaxrHandle, "testDoDeleteRequest"));

		return suite;
	}


	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getService()
	 */
	public Service getService() {
		return new ProductServiceImpl();
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kp.ames.web.test.JaxrTestImpl#testDoSubmitRequest()
	 */
	@Override
	public void testDoSubmitRequest() throws Exception {

		System.out.println("====> ProductTestImpl.testDoSubmitRequest");

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
			ProductTestImpl.rimId = jObj.getString("id");

		}
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#createJsonSubmitData()
	 */
	public JSONObject createJsonSubmitData() throws Exception {
		
		System.out.println("======> ProductTestImpl.createJsonSubmitData: " + TestData.getInstance().getProductSubmitData().toString(2));
		return TestData.getInstance().getProductSubmitData();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getDeleteAttributes()
	 */
	public HashMap<String,String> getDeleteAttributes() {

		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Productor);

		/*
		 * delete Productor which was created from first test
		 */
		System.out.println("====> ProductTestImpl.getDeleteAttributes rimId: " + ProductTestImpl.rimId);
		attributes.put(MethodConstants.ATTR_ITEM, ProductTestImpl.rimId);

		return attributes;
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getGetAttributes()
	 */
	public HashMap<String,String> getGetAttributes() {

		/*
		 * This test case retrieves a single existing Productor
		 */
		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Productor);

		/*
		 * Object Type
		 */
		attributes.put(MethodConstants.ATTR_FORMAT, FormatConstants.FNC_FORMAT_ID_Object);

		/*
		 * get Productor which was created from first test
		 */
		attributes.put(MethodConstants.ATTR_ITEM, ProductTestImpl.rimId);

		return attributes;
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getSubmitAttributes()
	 */
	public HashMap<String,String> getSubmitAttributes() {
		
		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Productor);
		
		return attributes;
		
	}
	
}
