package de.kp.ames.web.test;

import java.io.BufferedReader;
import java.util.HashMap;

import org.json.JSONObject;

import junit.framework.Test;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.http.RequestMethod;

public interface JaxrTest {

	public Test suite(JaxrHandle jaxrHandle, String className) throws Exception;

	/**
	 * A helper method to retrieve the service that is responsible
	 * for processing the test cases
	 * 
	 * @return
	 */
	public Service getService();
	
	/**
	 * A helper method to create data for a submit (POST) request
	 * 
	 * @return
	 * @throws Exception
	 */
	public BufferedReader createSubmitData() throws Exception;

	public JSONObject createJsonSubmitData() throws Exception;

	/**
	 * A helper method to specify attribuest for a certain request
	 * @return
	 */
	public HashMap<String,String> getSubmitAttributes();

	/**
	 * Describes Submit Test Case
	 * 
	 * @throws Exception
	 */
	public void doSubmit(JaxrHandle jaxrHandle, RequestMethod method, RequestContext ctx);

}
