package de.kp.ames.web.function.transform;

import org.json.JSONArray;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;

public class TransformDQM extends JaxrDQM {

	public TransformDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}
	
	public JSONArray getTransformators() throws Exception {
		return null;
	}
}
