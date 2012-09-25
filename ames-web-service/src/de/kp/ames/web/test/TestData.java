package de.kp.ames.web.test;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.test
 *  Module: TestData
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #data #test #web
 * </SemanticAssist>
 *
 */


import java.util.HashMap;

import org.json.JSONObject;

import de.kp.ames.web.core.util.DateUtil;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.JaxrConstants;

public class TestData {

	private static TestData instance = new TestData();
	
	private HashMap<String,String> uidMap;
	
	private TestData() {
		initialize();
	}
	
	public static TestData getInstance() {
		if (instance == null) instance = new TestData();
		return instance;
	}
	
	/*
	 * Access-Layer
	 */
	public JSONObject getAccessSubmitData() throws Exception {
		JSONObject jForm = new JSONObject();
		jForm.put(JaxrConstants.RIM_NAME, "Test IMAP Accessor");
		jForm.put(JaxrConstants.RIM_DESC, "Test desc");
		jForm.put(JaxrConstants.RIM_SLOT, "{\"Port\":\"143\",\"Alias\":\"arwanitis@app6.org\",\"Keypass\":\"xregistry\",\"Endpoint\":\"localhost\"}");
		jForm.put(JaxrConstants.RIM_SPEC, "[]");
		
		return jForm;
	}

	/*
	 * Bulletin-Layer
	 */
	public JSONObject getBulletinSubmitData() throws Exception {
		
		// {"rimSubject":"Test progress presentation", "rimMessage":"regards<br>PA<br>", "rimName":"freebXMLRegistry"}
		
		String timestamp = DateUtil.createTimeStamp("yyyyMMdd-HHmm");
		
		JSONObject jForm = new JSONObject();
		jForm.put(JaxrConstants.RIM_SUBJECT, "[" + timestamp  + "] Test progress presentation");
		jForm.put(JaxrConstants.RIM_MESSAGE, "regards<br>PA<br>");
		jForm.put(JaxrConstants.RIM_NAME, "freebXMLRegistry");
		
		return jForm;
	}
	
	/*
	 * Comm-Layer
	 */
	public JSONObject getCommSubmitData() throws Exception {
		// TODO
		return null;
	}

	/*
	 * Dms-Layer
	 */
	public JSONObject getDmsSubmitData() throws Exception {
		// TODO
		return null;
	}

	/*
	 * Group-Layer
	 */
	public JSONObject getGroupSubmitData() throws Exception {
		
		String data = "{\"rimName\":\"TestGroup\", " +
				"\"rimDescription\":\"Test desc\", " +
				"\"rimEmail\":\"test@test.org\", " +
				"\"rimCountry\":\"Germany\", " +
				"\"rimStateOrProvince\":\"\", " +
				"\"rimPostalCode\":\"\", " +
				"\"rimCity\":\"Munich\", " +
				"\"rimStreet\":\"\", " +
				"\"rimStreeNumber\":\"\", " +
				"\"rimCountryCode\":\"49\", " +
				"\"rimAreaCode\":\"89\", " +
				"\"rimPhoneNumber\":\"8989\", " +
				"\"rimPhoneExtension\":\"0\"}";

		return new JSONObject(data);

	}
	
	/*
	 * Map-Layer
	 */
	public String getWmsEndpoint() {
		// TODO
		return null;
	}
	
	/*
	 * Ns-Layer
	 */
	public JSONObject getNsSubmitData() throws Exception {

		JSONObject jForm = new JSONObject();
		jForm.put(JaxrConstants.RIM_NAME, "TestNS");
		jForm.put(JaxrConstants.RIM_DESC, "Test desc");
		jForm.put(JaxrConstants.RIM_SLOT, "{\"Alias\":\"tns\"}");
		
		return jForm;
	}
	
	/*
	 * Product-Layer
	 */
	public JSONObject getProductSubmitData() throws Exception {
		// TODO
		return null;
	}
	
	/*
	 * Role-Layer
	 */
	public JSONObject getRoleSubmitData() throws Exception {
		// TODO
		return null;
	}

	/*
	 * Rule-Layer
	 */
	public JSONObject getRuleSubmitData() throws Exception {
		// TODO
		return null;
	}
	
	/*
	 * Transform-Layer
	 */
	public JSONObject getTransformSubmitData() throws Exception {
		// TODO
		return null;
	}
	
	/*
	 * User-Layer
	 */
	public JSONObject getUserSubmitData() throws Exception {
		String data = "{\"rimFirstName\":\"\", " +
				"\"rimMiddleName\":\"\", " +
				"\"rimLastName\":\"Arwanitis (PKCS#12)\", " +
				"\"rimEmail\":\"arwanitis@dr-kruscheundpartner.de\", " +
				"\"rimCountry\":\"Germany\", " +
				"\"rimStateOrProvince\":\"\", " +
				"\"rimPostalCode\":\"89898\", " +
				"\"rimCity\":\"Munich-Test\", " +
				"\"rimStreet\":\"\", " +
				"\"rimStreeNumber\":\"\", " +
				"\"rimCountryCode\":\"49\", " +
				"\"rimAreaCode\":\"89\", " +
				"\"rimPhoneNumber\":\"8989\", " +
				"\"rimPhoneExtension\":\"8\", " +
				"\"rimId\":\"urn:uid:de:kp:samltest\"}";
		
		return new JSONObject(data);
	}
	
	public String getIdentifier(String type) {
		return uidMap.get(type);
	}
	
	private void initialize() {

		uidMap = new HashMap<String,String>();
		
		/*
		 * Bulletin Test
		 */
		uidMap.put(ClassificationConstants.FNC_ID_Community, "urn:freebxml:registry:Organization:freebXMLRegistry");

		/*
		 * Symbol Test
		 */
		uidMap.put(ClassificationConstants.FNC_SYMBOL_ID_APP6B, "1.X.3");
		
   		/*
		 * User Test
		 */
		uidMap.put(ClassificationConstants.FNC_ID_User, "urn:freebxml:registry:predefinedusers:farrukh");

	}
	
}
