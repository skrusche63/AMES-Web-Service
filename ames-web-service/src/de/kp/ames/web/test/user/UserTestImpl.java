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

import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.function.user.UserServiceImpl;
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
