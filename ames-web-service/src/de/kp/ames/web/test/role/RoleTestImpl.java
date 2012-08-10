package de.kp.ames.web.test.role;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.test.role
 *  Module: RoleTestImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #role #test #web
 * </SemanticAssist>
 *
 */


import java.util.HashMap;

import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.function.role.RoleServiceImpl;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;
import de.kp.ames.web.test.JaxrTestImpl;
import de.kp.ames.web.test.TestData;

public class RoleTestImpl extends JaxrTestImpl {

	public RoleTestImpl() {
		super();
	}

	public RoleTestImpl(JaxrHandle jaxrHandle, String methodName) {
		super(jaxrHandle, methodName);
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getService()
	 */
	public Service getService() {
		return new RoleServiceImpl();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#createJsonSubmitData()
	 */
	public JSONObject createJsonSubmitData() throws Exception {
		return TestData.getInstance().getRoleSubmitData();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getDeleteAttributes()
	 */
	public HashMap<String,String> getDeleteAttributes() {

		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Role);

		String item = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Role);
		attributes.put(MethodConstants.ATTR_ITEM, item);

		return attributes;
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getGetAttributes()
	 */
	public HashMap<String,String> getGetAttributes() {

		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Role);

		attributes.put(MethodConstants.ATTR_FORMAT, FormatConstants.FNC_FORMAT_ID_Grid);

		String source = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_User);
		attributes.put(MethodConstants.ATTR_SOURCE, source);

		String target = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Community);
		attributes.put(MethodConstants.ATTR_TARGET, target);

		return attributes;
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getSubmitAttributes()
	 */
	public HashMap<String,String> getSubmitAttributes() {
		
		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Role);

		String source = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_User);
		attributes.put(MethodConstants.ATTR_SOURCE, source);

		String target = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Community);
		attributes.put(MethodConstants.ATTR_TARGET, target);
	
		return attributes;
		
	}

}
