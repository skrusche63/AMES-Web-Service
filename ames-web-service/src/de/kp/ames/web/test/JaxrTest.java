package de.kp.ames.web.test;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.test
 *  Module: JaxrTest
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #jaxr #test #web
 * </SemanticAssist>
 *
 */


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
	 * A helper method to specify attributes for a delete request
	 * @return
	 */
	public HashMap<String,String> getDeleteAttributes();

	/**
	 * A helper method to specify attributes for a get request
	 * @return
	 */
	public HashMap<String,String> getGetAttributes();

	/**
	 * A helper method to specify attributes for a set request
	 * @return
	 */
	public HashMap<String,String> getSetAttributes();

	/**
	 * A helper method to specify attributes for a submit request
	 * @return
	 */
	public HashMap<String,String> getSubmitAttributes();

	/**
	 * Describes Delete Test Case
	 * 
	 * @throws Exception
	 */
	public void doDelete(JaxrHandle jaxrHandle, RequestMethod method, RequestContext ctx);

	/**
	 * Describes Get Test Case
	 * 
	 * @throws Exception
	 */
	public void doGet(JaxrHandle jaxrHandle, RequestMethod method, RequestContext ctx);

	/**
	 * Describes Set Test Case
	 * 
	 * @throws Exception
	 */
	public void doSet(JaxrHandle jaxrHandle, RequestMethod method, RequestContext ctx);

	/**
	 * Describes Submit Test Case
	 * 
	 * @throws Exception
	 */
	public void doSubmit(JaxrHandle jaxrHandle, RequestMethod method, RequestContext ctx);

}
