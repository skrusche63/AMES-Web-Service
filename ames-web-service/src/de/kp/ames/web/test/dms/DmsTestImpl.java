package de.kp.ames.web.test.dms;

import java.util.HashMap;

import org.json.JSONObject;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.function.dms.DmsServiceImpl;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.MethodConstants;
import de.kp.ames.web.test.JaxrTestImpl;

public class DmsTestImpl extends JaxrTestImpl {

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

		JSONObject jData = new JSONObject();
//		try {
//			// J_KEY helds the link to uploaded and cached document
//			object.put(JsonConstants.J_KEY, key);
//			
//			object.put(JaxrConstants.RIM_NAME, name);
//			object.put(JaxrConstants.RIM_DESC, description);
////			object.put(JaxrConstants.RIM_CLAS, new JSONArray().put(classification));
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}

		return jData;
		
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
