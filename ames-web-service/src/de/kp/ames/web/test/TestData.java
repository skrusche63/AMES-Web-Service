package de.kp.ames.web.test;

import java.util.HashMap;

import org.json.JSONObject;

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
		// TODO
		return null;
	}

	/*
	 * Bulletin-Layer
	 */
	public JSONObject getBulletinSubmitData() throws Exception {
		// TODO
		return null;
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
		// TODO
		return null;
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
		// TODO
		return null;
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
		// TODO
		return null;
	}
	
	public String getIdentifier(String type) {
		return uidMap.get(type);
	}
	
	private void initialize() {
		// TODO
	}
	
}
