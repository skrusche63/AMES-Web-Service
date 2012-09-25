package de.kp.ames.web.test.bulletin;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.test.bulletin
 *  Module: BulletinTestImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #bulletin #test #web
 * </SemanticAssist>
 *
 */


import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.function.bulletin.BulletinServiceImpl;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;
import de.kp.ames.web.test.JaxrTestImpl;
import de.kp.ames.web.test.TestData;

public class BulletinTestImpl extends JaxrTestImpl {


	public BulletinTestImpl() {
		super();
	}

	public BulletinTestImpl(JaxrHandle jaxrHandle, String methodName) {
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

		System.out.println("====> BulletinTestImpl.suite: " + clazzName);
		
		TestSuite suite = new TestSuite();
		
        /*
         * create, get
         */
        suite.addTest(new BulletinTestImpl(jaxrHandle, "testDoSubmitRequest"));
        suite.addTest(new BulletinTestImpl(jaxrHandle, "testDoGetRequest"));

		return suite;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getService()
	 */
	public Service getService() {
		return new BulletinServiceImpl();
	}


	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#createJsonSubmitData()
	 */
	public JSONObject createJsonSubmitData() throws Exception {
		return TestData.getInstance().getBulletinSubmitData();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getGetAttributes()
	 */
	public HashMap<String,String> getGetAttributes() {

		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Posting);

		attributes.put(MethodConstants.ATTR_FORMAT, FormatConstants.FNC_FORMAT_ID_Grid);

		String target = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Community);
		attributes.put(MethodConstants.ATTR_TARGET, target);

		return attributes;
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getSubmitAttributes()
	 */
	public HashMap<String,String> getSubmitAttributes() {
		
		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Posting);
		
		String target = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Community);
		attributes.put(MethodConstants.ATTR_TARGET, target);
		
		return attributes;
		
	}

}