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


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import de.kp.ames.web.core.util.DateUtil;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.JaxrConstants;
import de.kp.ames.web.shared.constants.JsonConstants;
import de.kp.ames.web.test.transform.TransformTestImpl;

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
		
		// significant test timestamp
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
		// not needed for comm mail submit
		return new JSONObject();
	}

	/*
	 * Dms-Layer
	 */
	public JSONObject getDmsSubmitData() throws Exception {
		
		// {"rimName":"TestDocument", "rimDescription":"test", "rimClassification":"[\"urn:oasis:names:tc:ebxml-regrep:FNC:Document\"]", "rimSlot":"{\"(Property name)\":\"(Property value)\"}", "key":"urn:uid:de:kp:samltest:54562DCE-C6C0-4DD0-B388-1ECDE19146CD"}
		JSONObject jForm = new JSONObject();
		jForm.put(JaxrConstants.RIM_NAME, "TestDMS");
		jForm.put(JaxrConstants.RIM_DESC, "Test desc");
		jForm.put(JaxrConstants.RIM_CLAS, "[\"urn:oasis:names:tc:ebxml-regrep:FNC:Document\"]");
		jForm.put(JaxrConstants.RIM_SLOT, "{\"Alias\":\"tdms\"}");
		jForm.put(JsonConstants.J_KEY, TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Document));
		
		return jForm;
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
		
		return "http://localhost:9090/geoserver/wms";
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
		// Productor submit
		// {"rimName":"TestProductor", "rimDescription":"Processing Filter transformator", "rimClassification":"[\"urn:oasis:names:tc:ebxml-regrep:FNC:Productor\"]", "rimSlot":"{\"(Property name)\":\"(Property value)\"}", "rimSpecification":"[{\"rimSeqNo\":\"1\", \"rimId\":\"urn:de:kp:ames:transformator:46c9222e-5433-4405-9e9e-f80ecc021669\"}]"}
		
		JSONObject jForm = new JSONObject();
		jForm.put(JaxrConstants.RIM_NAME, "TestProductor");
		jForm.put(JaxrConstants.RIM_DESC, "Test desc");
		jForm.put(JaxrConstants.RIM_CLAS, "[\"urn:oasis:names:tc:ebxml-regrep:FNC:Productor\"]");
		jForm.put(JaxrConstants.RIM_SLOT, "{\"Alias\":\"tptor\"}");
		// depends that TransformTestImpl submit test had run before
		jForm.put(JaxrConstants.RIM_SPEC, "[{\"rimSeqNo\":\"1\", \"rimId\":\"" + TransformTestImpl.rimId + "\"}]");
		
		return jForm;
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
		// {"key":"urn:uid:de:kp:samltest:794A4A01-5D04-4B27-A2B6-9FD3B70B7A66", "rimName":"TestFilter", "rimDescription":"Filter artist", "rimClassification":"[\"urn:oasis:names:tc:ebxml-regrep:FNC:Transformator\"]"}
		JSONObject jForm = new JSONObject();
		jForm.put(JaxrConstants.RIM_NAME, "TestTransformator");
		jForm.put(JaxrConstants.RIM_DESC, "Test desc");
		jForm.put(JaxrConstants.RIM_CLAS, "[\"urn:oasis:names:tc:ebxml-regrep:FNC:Transformator\"]");
		jForm.put(JaxrConstants.RIM_SLOT, "{\"Alias\":\"txsl\"}");
		jForm.put(JsonConstants.J_KEY, TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Transformator));
		
		return jForm;
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
		 * Comm Test
		 */
		// 	{"rimTimestamp":"2012-09-25 15:18:36", "rimFrom":"\"Dr. Stefan Krusche\" <krusche@app6.org>", "rimSubject":"Test Mail Workshop", "rimMessage":"<p style=\"font-family: monospace; font-size: 10pt;\">regards<br />SK<br /></p>", "rimMessageId":"urn:uid:arwanitis@app6.org:5"}

		JSONObject jMail = new JSONObject();
		String encodedJMail = null;
		try {
			// significant test timestamp
			String timestamp = DateUtil.createTimeStamp("yyyyMMdd-HHmm");

			jMail.put(JaxrConstants.RIM_TIMESTAMP, "2012-09-25 15:18:36");
			jMail.put(JaxrConstants.RIM_FROM, "\"Dr. Stefan Krusche\" <krusche@app6.org>");
			jMail.put(JaxrConstants.RIM_SUBJECT, "[" + timestamp + "] generated server Test-Case mail");
			jMail.put(JaxrConstants.RIM_MESSAGE, "<p style=\"font-family: monospace; font-size: 10pt;\">regards<br />SK<br /></p>");
			jMail.put(JaxrConstants.RIM_MESSAGE_ID, "urn:uid:arwanitis@app6.org:5");

			encodedJMail  = URLEncoder.encode(jMail.toString(), "UTF-8");

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		uidMap.put(ClassificationConstants.FNC_ID_Mail, encodedJMail);
		
   		/*
		 * Dms Test
		 */
		uidMap.put(ClassificationConstants.FNC_ID_Document, "urn:uid:de:kp:testdata:upload.doc");
		
		/*
		 * Symbol Test
		 */
		uidMap.put(ClassificationConstants.FNC_SYMBOL_ID_APP6B, "1.X.3");
		
   		/*
		 * Transform Test
		 */
		uidMap.put(ClassificationConstants.FNC_ID_Transformator, "urn:uid:de:kp:testdata:upload.xsl");
		
   		/*
		 * User Test
		 */
		uidMap.put(ClassificationConstants.FNC_ID_User, "urn:freebxml:registry:predefinedusers:farrukh");

		
	}
	
}
