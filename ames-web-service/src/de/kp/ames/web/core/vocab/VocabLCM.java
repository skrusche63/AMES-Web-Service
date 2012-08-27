package de.kp.ames.web.core.vocab;

import org.freebxml.omar.client.xml.registry.infomodel.ClassificationSchemeImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ConceptImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrTransaction;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.function.FncMessages;
import de.kp.ames.web.shared.constants.JaxrConstants;

public class VocabLCM extends JaxrLCM {

	public VocabLCM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * A helper method to create a ClassificationScheme instance
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String submitScheme(String data) throws Exception {
		
		/*
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();

		/*
		 * Build JSONObject
		 */
		JSONObject jScheme = new JSONObject(data);
		
		String name = jScheme.getString(JaxrConstants.RIM_NAME);
		String desc = jScheme.getString(JaxrConstants.RIM_DESC);
		
		ClassificationSchemeImpl cs = this.createClassificationScheme(name, desc);
		
		/*
		 * Identifier
		 */
		String cid = jScheme.getString(JaxrConstants.RIM_ID);

		cs.setLid(cid);
		cs.getKey().setId(cid);

		/* 
		 * Home url
		 */
		String home = jaxrHandle.getEndpoint().replace("/saml", "");
		cs.setHome(home);

		transaction.addObjectToSave(cs);
		
		/*
		 * Save objects
		 */
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Retrieve response
		 */
		JSONObject jResponse = transaction.getJResponse(cs.getId(), FncMessages.SCHEME_CREATED);
		return jResponse.toString();

	}
	
	/**
	 * A helper method to create a ClassificationNode instance
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String submitConcept(String data) throws Exception {

		/*
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();

		/*
		 * Build JSONObject
		 */
		JSONObject jConcept = new JSONObject(data);
		
		/*
		 * Retrieve request parameters
		 */
		String name = jConcept.getString(JaxrConstants.RIM_NAME);
		String code = jConcept.getString(JaxrConstants.RIM_CODE);

		String cid    = jConcept.getString(JaxrConstants.RIM_ID);
		String parent = jConcept.getString(JaxrConstants.RIM_PARENT);

		/*
		 * Retrieve ClassificationScheme or Classification Node instance
		 */
		RegistryObjectImpl ro = getRegistryObjectById(parent);
		if (ro == null) throw new Exception("[VocabLCM] Registry Object with id <" + parent + "> does not exist.");				

		/*
		 * Distinguish between ClassificationScheme and Concept as parent of
		 * the concept that has to be submitted
		 */
		if (ro instanceof ClassificationSchemeImpl) {

			ClassificationSchemeImpl cs = (ClassificationSchemeImpl)ro;

			/*
			 * Build Concept instance
			 */
			ConceptImpl c = (ConceptImpl)this.createConcept(cs, name, code);

			/*
			 * Identifier
			 */
			c.setLid(cid);
			c.getKey().setId(cid);

			/* 
			 * Home url
			 */
			String home = jaxrHandle.getEndpoint().replace("/saml", "");
			c.setHome(home);

			cs.addChildConcept(c);
			transaction.addObjectToSave(cs);
			
			/*
			 * Save objects
			 */
			saveObjects(transaction.getObjectsToSave(), false, false);

			/*
			 * Retrieve response
			 */
			JSONObject jResponse = transaction.getJResponse(cs.getId(), FncMessages.CONCEPT_CREATED);
			return jResponse.toString();

		} else if (ro instanceof ConceptImpl) {

			ConceptImpl p = (ConceptImpl)ro;

			/*
			 * Build Concept instance
			 */
			ConceptImpl c = (ConceptImpl)this.createConcept(p, name, code);

			/*
			 * Identifier
			 */
			c.setLid(cid);
			c.getKey().setId(cid);

			/* 
			 * Home url
			 */
			String home = jaxrHandle.getEndpoint().replace("/saml", "");
			c.setHome(home);

			p.addChildConcept(c);
			transaction.addObjectToSave(p);
			
			/*
			 * Save objects
			 */
			saveObjects(transaction.getObjectsToSave(), false, false);

			/*
			 * Retrieve response
			 */
			JSONObject jResponse = transaction.getJResponse(p.getId(), FncMessages.CONCEPT_CREATED);
			return jResponse.toString();

		} else {
			throw new Exception("[VocabLCM] ClassificationSchemeImpl or ConceptImpl expected.");	
		}
		
	}
	
}
