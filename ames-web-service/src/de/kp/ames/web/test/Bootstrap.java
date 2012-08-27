package de.kp.ames.web.test;

import org.json.JSONObject;

import de.kp.ames.http.HttpClient;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.JaxrConstants;
import junit.framework.TestCase;

public class Bootstrap extends TestCase {

	private static String BASE_URL   = "https://localhost:8443/ames/test/unit?name=";
	private static String CLAZZ_NAME = 	"de.kp.ames.web.test.vocab.VocabTestImpl";

	private String[] mainConcepts = {
			
			ClassificationConstants.FNC_ID_Accessor,
			ClassificationConstants.FNC_ID_Affiliation,
			ClassificationConstants.FNC_ID_Attachment,
			ClassificationConstants.FNC_ID_Category,
			ClassificationConstants.FNC_ID_Chat,
			ClassificationConstants.FNC_ID_Comment,
			ClassificationConstants.FNC_ID_Contact,
			ClassificationConstants.FNC_ID_Community,
			ClassificationConstants.FNC_ID_Concept,
			ClassificationConstants.FNC_ID_Database,
			ClassificationConstants.FNC_ID_Document,
			ClassificationConstants.FNC_ID_Edge,
			ClassificationConstants.FNC_ID_Evaluation,
			ClassificationConstants.FNC_ID_Folder,
			ClassificationConstants.FNC_ID_Image,
			ClassificationConstants.FNC_ID_Layer,
			ClassificationConstants.FNC_ID_Link,
			ClassificationConstants.FNC_ID_Mail,
			ClassificationConstants.FNC_ID_Namespace,
			ClassificationConstants.FNC_ID_Node,
			ClassificationConstants.FNC_ID_Package,
			ClassificationConstants.FNC_ID_Posting,
			ClassificationConstants.FNC_ID_Product,
			ClassificationConstants.FNC_ID_Productor,
			ClassificationConstants.FNC_ID_Reasoner,
			ClassificationConstants.FNC_ID_Remote,
			ClassificationConstants.FNC_ID_Responsibility,
			ClassificationConstants.FNC_ID_Role,
			ClassificationConstants.FNC_ID_Rule,
			ClassificationConstants.FNC_ID_Scheme,
			ClassificationConstants.FNC_ID_Security,
			ClassificationConstants.FNC_ID_Specification,
			ClassificationConstants.FNC_ID_Symbol,
			ClassificationConstants.FNC_ID_Transformator,
			ClassificationConstants.FNC_ID_User,
			ClassificationConstants.FNC_ID_WebDav

	};

	private String[] securityConcepts = {

			ClassificationConstants.FNC_ID_Security,
			ClassificationConstants.FNC_SECURITY_ID_Safe
			
	};
	
	private String[] symbolConcepts = {
			
			ClassificationConstants.FNC_SYMBOL_ID_APP6B,
			ClassificationConstants.FNC_SYMBOL_ID_Icon
			
	};
	
	public Bootstrap() {
	}
	
	public void test() throws Exception {

		/*
		 * Create ClassificationScheme
		 */
		createClassificationScheme();
		
		/*
		 * Create main concepts
		 */
		for (String concept:mainConcepts) {
			
			String pid = ClassificationConstants.FNC_ID;
			createConcept(pid, concept);
			
		}

		/*
		 * Create security concepts
		 */
		for (String concept:securityConcepts) {
			
			String pid = ClassificationConstants.FNC_ID_Security;
			createConcept(pid, concept);
			
		}

		/*
		 * Create symbol concepts
		 */
		for (String concept:symbolConcepts) {
			
			String pid = ClassificationConstants.FNC_ID_Symbol;
			createConcept(pid, concept);
			
		}

	}
	
	private void createClassificationScheme() throws Exception {
		
		String type = ClassificationConstants.FNC_ID_Scheme;
		JSONObject jScheme = new JSONObject();
		
		/*
		 * Identifier
		 */
		String id = ClassificationConstants.FNC_ID;
		jScheme.put(JaxrConstants.RIM_ID, id);
		
		/*
		 * Name & Description
		 */
		int pos = id.lastIndexOf(":");

		String name = id.substring(pos+1);
		jScheme.put(JaxrConstants.RIM_NAME, name);
		
		String desc = "";
		jScheme.put(JaxrConstants.RIM_DESC, desc);
	
		/*
		 * Send request
		 */
		sendRequest(type, jScheme.toString());
		
	}

	private void createConcept(String pid, String id) throws Exception {
		
		String type = ClassificationConstants.FNC_ID_Concept;
		JSONObject jConcept = new JSONObject();

		/*
		 * Identifier
		 */
		jConcept.put(JaxrConstants.RIM_ID, id);

		/*
		 * Name & code
		 */
		int pos = id.lastIndexOf(":");
		String name = id.substring(pos+1);
		
		jConcept.put(JaxrConstants.RIM_NAME, name);
		jConcept.put(JaxrConstants.RIM_CODE, name);

		/*
		 * Parent
		 */
		jConcept.put(JaxrConstants.RIM_PARENT, pid);

		/*
		 * Send request
		 */
		sendRequest(type, jConcept.toString());

	}

	private void sendRequest(String type, String data) throws Exception {

		String url  = BASE_URL + CLAZZ_NAME + "&type=" + type;

		/* 
		 * Invoke HttpsClient
		 */		
		HttpClient client = new HttpClient();
		client.doPost(url, data);

	}
}
