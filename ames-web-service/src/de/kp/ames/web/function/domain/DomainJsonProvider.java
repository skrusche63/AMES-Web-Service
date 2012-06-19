package de.kp.ames.web.function.domain;

import java.util.List;

import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceImpl;
import org.freebxml.omar.common.CanonicalSchemes;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.format.json.JsonProvider;
import de.kp.ames.web.core.format.json.StringCollector;
import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;

public class DomainJsonProvider extends JsonProvider {

	private static String EXTRINSIC_OBJECT = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_ExtrinsicObject;
	private static String SERVICE          = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_Service;

	public static JSONObject getAccessor(JaxrHandle jaxrHandle, ServiceImpl service) throws Exception {
		// TODO
		return null;
	}

	/**
	 * A helper method to convert a list of accessors
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param accessors
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getAccessors(JaxrHandle jaxrHandle, List<RegistryObjectImpl> accessors) throws Exception {

		JaxrDQM dqm = new JaxrDQM(jaxrHandle);

		/*
		 * Sort result by name of accessor
		 */
		StringCollector collector = new StringCollector();

		for (RegistryObjectImpl accessor:accessors) {
			
			String objectType = dqm.getObjectType(accessor);
			if (objectType.equals(SERVICE) == false) continue;
			
			ServiceImpl service = (ServiceImpl)accessor;
			
			JSONObject jAccessor = getAccessor(jaxrHandle, service);	
			collector.put(jAccessor.getString(JaxrConstants.RIM_NAME), jAccessor);

			
		}

		return new JSONArray(collector.values());
		
	}

	
	public static JSONObject getEvaluation(JaxrHandle jaxrHandle, ExtrinsicObjectImpl extrinsicObject) throws Exception {
		// TODO
		return null;
	}

	/**
	 * A helper method to convert a list of evaluations
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param evaluations
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getEvaluations(JaxrHandle jaxrHandle, List<RegistryObjectImpl> evaluations) throws Exception {

		JaxrDQM dqm = new JaxrDQM(jaxrHandle);

		/*
		 * Sort result by name of evaluation
		 */
		StringCollector collector = new StringCollector();

		for (RegistryObjectImpl evaluation:evaluations) {
			
			String objectType = dqm.getObjectType(evaluation);
			if (objectType.equals(EXTRINSIC_OBJECT) == false) continue;
			
			ExtrinsicObjectImpl extrinsicObject = (ExtrinsicObjectImpl)evaluation;
			
			JSONObject jEvaluation = getEvaluation(jaxrHandle, extrinsicObject);	
			collector.put(jEvaluation.getString(JaxrConstants.RIM_NAME), jEvaluation);

			
		}

		return new JSONArray(collector.values());

	}

	public static JSONObject getProduct(JaxrHandle jaxrHandle, ExtrinsicObjectImpl extrinsicObject) throws Exception {
		// TODO
		return null;
	}
	
	/**
	 * A helper method to convert a list of products
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param products
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getProducts(JaxrHandle jaxrHandle, List<RegistryObjectImpl> products) throws Exception {

		JaxrDQM dqm = new JaxrDQM(jaxrHandle);

		/*
		 * Sort result by name of product
		 */
		StringCollector collector = new StringCollector();

		for (RegistryObjectImpl product:products) {
			
			String objectType = dqm.getObjectType(product);
			if (objectType.equals(EXTRINSIC_OBJECT) == false) continue;
			
			ExtrinsicObjectImpl extrinsicObject = (ExtrinsicObjectImpl)product;
			
			JSONObject jProduct = getProduct(jaxrHandle, extrinsicObject);	
			collector.put(jProduct.getString(JaxrConstants.RIM_NAME), jProduct);

			
		}

		return new JSONArray(collector.values());

	}
	
	public static JSONObject getProductor(JaxrHandle jaxrHandle, ServiceImpl service) throws Exception {
		// TODO
		return null;
	}

	/**
	 * A helper method to convert a list of productors
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param productors
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getProductors(JaxrHandle jaxrHandle, List<RegistryObjectImpl> productors) throws Exception {

		JaxrDQM dqm = new JaxrDQM(jaxrHandle);

		/*
		 * Sort result by name of productor
		 */
		StringCollector collector = new StringCollector();

		for (RegistryObjectImpl product:productors) {
			
			String objectType = dqm.getObjectType(product);
			if (objectType.equals(SERVICE) == false) continue;
			
			ServiceImpl service = (ServiceImpl)product;
			
			JSONObject jProductor = getProductor(jaxrHandle, service);	
			collector.put(jProductor.getString(JaxrConstants.RIM_NAME), jProductor);

			
		}

		return new JSONArray(collector.values());

	}
	
	public static JSONObject getReasoner(JaxrHandle jaxrHandle, ServiceImpl service) throws Exception {
		// TODO
		return null;
	}
	
	/**
	 * A helper method to convert a list of reasoners
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param reasoners
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getReasoners(JaxrHandle jaxrHandle, List<RegistryObjectImpl> reasoners) throws Exception {

		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		/*
		 * Sort result by name of reasoner
		 */
		StringCollector collector = new StringCollector();
		
		for (RegistryObjectImpl reasoner:reasoners) {
			
			String objectType = dqm.getObjectType(reasoner);
			if (objectType.equals(SERVICE) == false) continue;
			
			ServiceImpl service = (ServiceImpl)reasoner;
			
			JSONObject jService = getReasoner(jaxrHandle, service);	
			collector.put(jService.getString(JaxrConstants.RIM_NAME), jService);

			
		}

		return new JSONArray(collector.values());
		
	}

}
