package de.kp.ames.web.test.map;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.test.map
 *  Module: MapTestImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #map #test #web
 * </SemanticAssist>
 *
 */


import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.function.map.MapServiceImpl;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.MethodConstants;
import de.kp.ames.web.test.JaxrTestImpl;
import de.kp.ames.web.test.TestData;

public class MapTestImpl extends JaxrTestImpl {

	public MapTestImpl() {
		super();
	}

	public MapTestImpl(JaxrHandle jaxrHandle, String methodName) {
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

		System.out.println("====> MapTestImpl.suite: " + clazzName);
		
		TestSuite suite = new TestSuite();
		
        /*
         * get
         */
        suite.addTest(new MapTestImpl(jaxrHandle, "testDoGetRequest"));

		return suite;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getService()
	 */
	public Service getService() {
		return new MapServiceImpl();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getGetAttributes()
	 */
	public HashMap<String,String> getGetAttributes() {

		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Layer);
		attributes.put(FncConstants.ATTR_START, "0");
		attributes.put(FncConstants.ATTR_LIMIT, "25");

		String endpoint = TestData.getInstance().getWmsEndpoint();
		attributes.put(MethodConstants.ATTR_ENDPOINT, endpoint);

		return attributes;
		
	}

}
