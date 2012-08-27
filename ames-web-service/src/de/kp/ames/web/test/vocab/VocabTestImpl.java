package de.kp.ames.web.test.vocab;

import java.util.HashMap;

import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.core.vocab.VocabServiceImpl;
import de.kp.ames.web.shared.constants.MethodConstants;
import de.kp.ames.web.test.JaxrTestImpl;

public class VocabTestImpl extends JaxrTestImpl {
		
	public VocabTestImpl() {
		super();
	}
	
	public VocabTestImpl(JaxrHandle jaxrHandle, String methodName, String type, String data) {
		super(jaxrHandle, methodName, type, data);
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getService()
	 */
	public Service getService() {
		return new VocabServiceImpl();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#createJsonSubmitData()
	 */
	public JSONObject createJsonSubmitData() throws Exception {

		if (this.data != null) return new JSONObject(this.data);
		return null;
	
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getSubmitAttributes()
	 */
	public HashMap<String,String> getSubmitAttributes() {
		
		HashMap<String,String> attributes = new HashMap<String,String>();
		if (this.type != null) attributes.put(MethodConstants.ATTR_TYPE,this.type);
		
		return attributes;
		
	}

}
