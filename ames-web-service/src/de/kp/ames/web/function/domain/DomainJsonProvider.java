package de.kp.ames.web.function.domain;
/**
 *	Copyright 2012 Dr. Krusche & Partner PartG
 *
 *	AMES-Web-Service is free software: you can redistribute it and/or 
 *	modify it under the terms of the GNU General Public License 
 *	as published by the Free Software Foundation, either version 3 of 
 *	the License, or (at your option) any later version.
 *
 *	AMES- Web-Service is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * 
 *  See the GNU General Public License for more details. 
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this software. If not, see <http://www.gnu.org/licenses/>.
 *
 */

import java.util.List;

import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceImpl;
import org.freebxml.omar.common.CanonicalSchemes;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.domain.JsonProvider;
import de.kp.ames.web.core.json.DateCollector;
import de.kp.ames.web.core.json.StringCollector;
import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.function.domain.model.JsonAccessor;
import de.kp.ames.web.function.domain.model.JsonChat;
import de.kp.ames.web.function.domain.model.JsonDocument;
import de.kp.ames.web.function.domain.model.JsonEvaluation;
import de.kp.ames.web.function.domain.model.JsonImage;
import de.kp.ames.web.function.domain.model.JsonMail;
import de.kp.ames.web.function.domain.model.JsonProduct;
import de.kp.ames.web.function.domain.model.JsonProductor;
import de.kp.ames.web.function.domain.model.JsonReasoner;
import de.kp.ames.web.function.domain.model.JsonTransformator;

public class DomainJsonProvider extends JsonProvider {

	private static String EXTRINSIC_OBJECT = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_ExtrinsicObject;
	private static String REGISTRY_PACKAGE = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_RegistryPackage;
	private static String SERVICE          = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_Service;

	/**
	 * A helper method to convert an AccessorObject
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param service
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getAccessor(JaxrHandle jaxrHandle, ServiceImpl service) throws Exception {

		JsonAccessor jAccessor = new JsonAccessor(jaxrHandle);
		jAccessor.set(service);

		return jAccessor.get();
		
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

	/**
	 * A helper method to convert a Chat Message
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param extrinsicObject
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getChatMessage(JaxrHandle jaxrHandle, ExtrinsicObjectImpl extrinsicObject) throws Exception {

		JsonChat jMessage = new JsonChat(jaxrHandle);
		jMessage.set(extrinsicObject);

		return jMessage.get();
		
	}

	/**
	 * A helper method to convert a list of chat
	 * messages into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param messages
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getChatMessages(JaxrHandle jaxrHandle, List<RegistryObjectImpl> messages) throws Exception {
		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);

		/*
		 * Sort result by datetime of message
		 */
		DateCollector collector = new DateCollector();

		for (RegistryObjectImpl message:messages) {

			String objectType = dqm.getObjectType(message);
			if (objectType.equals(EXTRINSIC_OBJECT) == false) continue;
			
			ExtrinsicObjectImpl extrinsicObject = (ExtrinsicObjectImpl)message;

			JSONObject jChat = getChatMessage(jaxrHandle, extrinsicObject);	
			collector.put(dqm.getLastModified(extrinsicObject), jChat);
		
		}
		
		return new JSONArray(collector.values());

	}

	/**
	 * A helper method to convert a DocumentObject
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param extrinsicObject
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getDocument(JaxrHandle jaxrHandle, ExtrinsicObjectImpl extrinsicObject) throws Exception {

		JsonDocument jDocument = new JsonDocument(jaxrHandle);
		jDocument.set(extrinsicObject);

		return jDocument.get();
		
	}

	/**
	 * A helper method to convert a list of documents
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param documents
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getDocuments(JaxrHandle jaxrHandle, List<RegistryObjectImpl> documents) throws Exception {
	
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);

		/*
		 * Sort result by datetime of document
		 */
		DateCollector collector = new DateCollector();

		for (RegistryObjectImpl document:documents) {

			String objectType = dqm.getObjectType(document);
			if (objectType.equals(EXTRINSIC_OBJECT) == false) continue;
			
			ExtrinsicObjectImpl extrinsicObject = (ExtrinsicObjectImpl)document;

			JSONObject jDocument = getDocument(jaxrHandle, extrinsicObject);	
			collector.put(dqm.getLastModified(extrinsicObject), jDocument);
		
		}
		
		return new JSONArray(collector.values());

	}

	/**
	 * A helper method to convert an EvaluationObject
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param extrinsicObject
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getEvaluation(JaxrHandle jaxrHandle, ExtrinsicObjectImpl extrinsicObject) throws Exception {

		JsonEvaluation jEvaluation = new JsonEvaluation(jaxrHandle);
		jEvaluation.set(extrinsicObject);

		return jEvaluation.get();
		
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

	/**
	 * A helper method to convert an ImageObject
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param extrinsicObject
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getImage(JaxrHandle jaxrHandle, ExtrinsicObjectImpl extrinsicObject) throws Exception {

		JsonImage jImage = new JsonImage(jaxrHandle);
		jImage.set(extrinsicObject);

		return jImage.get();
		
	}

	/**
	 * A helper method to convert a list of images
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param images
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getImages(JaxrHandle jaxrHandle, List<RegistryObjectImpl> images) throws Exception {
	
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);

		/*
		 * Sort result by datetime of image
		 */
		DateCollector collector = new DateCollector();

		for (RegistryObjectImpl image:images) {

			String objectType = dqm.getObjectType(image);
			if (objectType.equals(EXTRINSIC_OBJECT) == false) continue;
			
			ExtrinsicObjectImpl extrinsicObject = (ExtrinsicObjectImpl)image;

			JSONObject jImage = getImage(jaxrHandle, extrinsicObject);	
			collector.put(dqm.getLastModified(extrinsicObject), jImage);
		
		}
		
		return new JSONArray(collector.values());

	}

	/**
	 * A helper method to convert a Mail Message
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param extrinsicObject
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getMailMessage(JaxrHandle jaxrHandle, ExtrinsicObjectImpl extrinsicObject) throws Exception {

		JsonMail jMessage = new JsonMail(jaxrHandle);
		jMessage.set(extrinsicObject);

		return jMessage.get();
		
	}

	/**
	 * A helper method to convert a list of mail
	 * messages into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param messages
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getMailMessages(JaxrHandle jaxrHandle, List<RegistryObjectImpl> messages) throws Exception {
		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);

		/*
		 * Sort result by datetime of message
		 */
		DateCollector collector = new DateCollector();

		for (RegistryObjectImpl message:messages) {

			String objectType = dqm.getObjectType(message);
			if (objectType.equals(EXTRINSIC_OBJECT) == false) continue;
			
			ExtrinsicObjectImpl extrinsicObject = (ExtrinsicObjectImpl)message;

			JSONObject jMail = getMailMessage(jaxrHandle, extrinsicObject);	
			collector.put(dqm.getLastModified(extrinsicObject), jMail);
		
		}
		
		return new JSONArray(collector.values());

	}

	/**
	 * A helper method to convert a namespace
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param registryPackage
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getNamespace(JaxrHandle jaxrHandle, RegistryPackageImpl registryPackage) throws Exception {
		// TODO
		return null;
	}

	/**
	 * A helper method to convert a list of namespaces
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param namespaces
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getNamespaces(JaxrHandle jaxrHandle, List<RegistryObjectImpl> namespaces) throws Exception {

		JaxrDQM dqm = new JaxrDQM(jaxrHandle);

		/*
		 * Sort result by name of namespace
		 */
		StringCollector collector = new StringCollector();

		for (RegistryObjectImpl namespace:namespaces) {
			
			String objectType = dqm.getObjectType(namespace);
			if (objectType.equals(REGISTRY_PACKAGE) == false) continue;
			
			RegistryPackageImpl folder = (RegistryPackageImpl)namespace;
			
			JSONObject jFolder = getNamespace(jaxrHandle, folder);	
			collector.put(jFolder.getString(JaxrConstants.RIM_NAME), jFolder);
			
		}

		return new JSONArray(collector.values());
	}

	
	/**
	 * A helper method to convert a ProductObject
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param extrinsicObject
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getProduct(JaxrHandle jaxrHandle, ExtrinsicObjectImpl extrinsicObject) throws Exception {

		JsonProduct jProduct = new JsonProduct(jaxrHandle);
		jProduct.set(extrinsicObject);

		return jProduct.get();
		
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
	
	/**
	 * A helper method to convert a ProductorObject
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param service
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getProductor(JaxrHandle jaxrHandle, ServiceImpl service) throws Exception {

		JsonProductor jProductor = new JsonProductor(jaxrHandle);
		jProductor.set(service);

		return jProductor.get();
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
	
	/**
	 * A helper method to convert a ReasonerObject
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param service
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getReasoner(JaxrHandle jaxrHandle, ServiceImpl service) throws Exception {

		JsonReasoner jReasoner = new JsonReasoner(jaxrHandle);
		jReasoner.set(service);

		return jReasoner.get();

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

	/**
	 * A helper method to convert a TransformatorObject
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param eo
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getTransformator(JaxrHandle jaxrHandle, ExtrinsicObjectImpl extrinsicObject) throws Exception {

		JsonTransformator jTransformator = new JsonTransformator(jaxrHandle);
		jTransformator.set(extrinsicObject);

		return jTransformator.get();
	}

	/**
	 * A helper method to convert a list of transformators
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param transformators
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getTransformators(JaxrHandle jaxrHandle, List<RegistryObjectImpl> transformators) throws Exception {

		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		/*
		 * Sort result by name of transformator
		 */
		StringCollector collector = new StringCollector();
		
		for (RegistryObjectImpl transformator:transformators) {
			
			String objectType = dqm.getObjectType(transformator);
			if (objectType.equals(EXTRINSIC_OBJECT) == false) continue;
			
			ExtrinsicObjectImpl eo = (ExtrinsicObjectImpl)transformator;
			
			JSONObject jService = getTransformator(jaxrHandle, eo);	
			collector.put(jService.getString(JaxrConstants.RIM_NAME), jService);

			
		}

		return new JSONArray(collector.values());
		
	}

}
