package de.kp.ames.web.test.dms;

import java.util.HashMap;

import org.json.JSONObject;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.function.dms.DmsServiceImpl;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;
import de.kp.ames.web.test.JaxrTestImpl;
import de.kp.ames.web.test.TestData;

public class DmsTestImpl extends JaxrTestImpl {

	// TODO: Download & Extract Test
	
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

		String item = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Document);
		attributes.put(MethodConstants.ATTR_ITEM, item);

		return attributes;
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getGetAttributes()
	 */
	public HashMap<String,String> getGetAttributes() {

		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Document);

		attributes.put(MethodConstants.ATTR_FORMAT, FormatConstants.FNC_FORMAT_ID_File);

		String item = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Document);
		attributes.put(MethodConstants.ATTR_ITEM, item);

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
