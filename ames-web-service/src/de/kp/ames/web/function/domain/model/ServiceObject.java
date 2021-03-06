package de.kp.ames.web.function.domain.model;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.domain.model
 *  Module: ServiceObject
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #domain #function #model #object #service #web
 * </SemanticAssist>
 *
 */

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceBindingImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceImpl;
import org.freebxml.omar.client.xml.registry.infomodel.SlotImpl;
import org.freebxml.omar.client.xml.registry.infomodel.SpecificationLinkImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.shared.constants.JaxrConstants;

public class ServiceObject extends BusinessObject {

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 * @param jaxrLCM
	 */
	public ServiceObject(JaxrHandle jaxrHandle, JaxrLCM jaxrLCM) {
		super(jaxrHandle, jaxrLCM);
	}
	
	/**
	 * Create ServiceObject
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public RegistryObjectImpl create(String data) throws Exception {

		/*
		 * Initialize data
		 */
		JSONObject jForm = new JSONObject(data);

		/*
		 * Create ServiceObject
		 */
		return create(jForm);

	}

	/**
	 * Create ServiceObject from JSON representation
	 * 
	 * @param jForm
	 * @return
	 * @throws Exception
	 */
	public RegistryObjectImpl create(JSONObject jForm) throws Exception {

		/*
		 * Create SpecificationLink instances
		 */
		JSONArray jSpecifications = new JSONArray(jForm.getString(RIM_SPEC));

		ArrayList<SpecificationLinkImpl> specificationLinks = new ArrayList<SpecificationLinkImpl>();			
		for (int i = 0; i < jSpecifications.length(); i++) {
			
			JSONObject jSpecification = jSpecifications.getJSONObject(i);
			
			String uid   = jSpecification.getString(RIM_ID);
			String seqNo = jSpecification.getString(RIM_SEQNO);

			RegistryObjectImpl ro = jaxrLCM.getRegistryObjectById(uid);
			if (ro == null) throw new Exception("[ServiceObject] RegistryObject with id <" + uid + "> does not exist.");
			
			/* 
			 * Create a new specification link
			 */
			SpecificationLinkImpl specificationLink = jaxrLCM.createSpecificationLink();
			if (specificationLink == null) throw new Exception("[ServiceObject] Creation of SpecificationLink failed.");

			SlotImpl slot = jaxrLCM.createSlot(JaxrConstants.SLOT_SEQNO, seqNo, JaxrConstants.SLOT_TYPE);
			specificationLink.addSlot(slot);
			
			specificationLink.setSpecificationObject(ro);			
			specificationLinks.add(specificationLink);

		}
	
		/*
		 * Create ServiceBinding instance
		 */		
		ServiceBindingImpl serviceBinding = null;
		if (specificationLinks.isEmpty() == false) {

			serviceBinding = jaxrLCM.createServiceBinding();	
			serviceBinding.addSpecificationLinks(specificationLinks);
		
		}
		
		/* 
		 * Create Service instance
		 */		
		ServiceImpl service = jaxrLCM.createService(jForm.getString(RIM_NAME));
		
		if (jForm.has(RIM_DESC)) service.setDescription(jaxrLCM.createInternationalString(jForm.getString(RIM_DESC)));				
		service.setHome(jaxrHandle.getEndpoint().replace("/saml", ""));

		if (serviceBinding != null) service.addServiceBinding(serviceBinding);

		/*
		 * Create classifications
		 */
		JSONArray jClases = jForm.has(RIM_CLAS) ? new JSONArray(jForm.getString(RIM_CLAS)) : null;
		if (jClases != null) {
			
			List<ClassificationImpl> classifications = createClassifications(jClases);			
			/*
			 * Set composed object
			 */
			service.addClassifications(classifications);
			
		}
		
		/* 
		 * Create slots
		 */
		JSONObject jSlots = jForm.has(RIM_SLOT) ? new JSONObject(jForm.getString(RIM_SLOT)) : null;
		if (jSlots != null) {

			List<SlotImpl> slots = createSlots(jSlots);
			/*
			 * Set composed object
			 */
			service.addSlots(slots);
		
		}
			
		/*
		 * Indicate as created
		 */
		this.created = true;
		
		return service;

	}

	/**
	 * Update ServiceObject
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public RegistryObjectImpl update(String data) throws Exception {

		/*
		 * Initialize data
		 */
		JSONObject jForm = new JSONObject(data);

		/*
		 * Update ServiceObject
		 */
		return update(jForm);

	}

	/**
	 * Update ServiceObject from JSON representation
	 * 
	 * @param jForm
	 * @return
	 * @throws Exception
	 */
	public RegistryObjectImpl update(JSONObject jForm) throws Exception {

		/* 
		 * Determine service from unique identifier
		 */
		String sid = jForm.getString(RIM_ID);
		
		ServiceImpl service = (ServiceImpl)jaxrLCM.getRegistryObjectById(sid);
		if (service == null) throw new Exception("[ServiceObject] RegistryObject with id <" + sid + "> does not exist.");

		/* 
		 * Remove service binding and associated specification links
		 */
		Collection<?> bindings = service.getServiceBindings();
		if (bindings.isEmpty() == false) service.removeServiceBindings(bindings);
		
		/*
		 * SpecificationLink instances
		 */
		JSONArray jSpecifications = new JSONArray(jForm.getString(RIM_SPEC));

		ArrayList<SpecificationLinkImpl> specificationLinks = new ArrayList<SpecificationLinkImpl>();			
		for (int i = 0; i < jSpecifications.length(); i++) {
			
			JSONObject jSpecification = jSpecifications.getJSONObject(i);
			
			String uid   = jSpecification.getString(RIM_ID);
			String seqNo = jSpecification.getString(RIM_SEQNO);

			RegistryObjectImpl ro = jaxrLCM.getRegistryObjectById(uid);
			if (ro == null) throw new Exception("[ServiceObject] RegistryObject with id <" + uid + "> does not exist.");
			
			/* 
			 * Create a new specification link
			 */
			SpecificationLinkImpl specificationLink = jaxrLCM.createSpecificationLink();
			if (specificationLink == null) throw new Exception("[ServiceObject] Creation of SpecificationLink failed.");

			SlotImpl slot = jaxrLCM.createSlot(JaxrConstants.SLOT_SEQNO, seqNo, JaxrConstants.SLOT_TYPE);
			specificationLink.addSlot(slot);
			
			specificationLink.setSpecificationObject(ro);			
			specificationLinks.add(specificationLink);

		}
	
		/*
		 * Create ServiceBinding instance
		 */		
		ServiceBindingImpl serviceBinding = null;
		if (specificationLinks.isEmpty() == false) {

			serviceBinding = jaxrLCM.createServiceBinding();	
			serviceBinding.addSpecificationLinks(specificationLinks);
		
		}
		
		if (serviceBinding != null) service.addServiceBinding(serviceBinding);

		/*
		 * Update core metadata
		 */
		updateMetadata(service, jForm);

		
//		/* 
//		 * Name & description
//		 */
//		if (jForm.has(RIM_NAME)) service.setName(jaxrLCM.createInternationalString(jForm.getString(RIM_NAME)));
//		if (jForm.has(RIM_DESC)) service.setDescription(jaxrLCM.createInternationalString(jForm.getString(RIM_DESC)));				
//
//		/* 
//		 * Update slots
//		 */
//		JSONObject jSlots = jForm.has(RIM_SLOT) ? new JSONObject(jForm.getString(RIM_SLOT)) : null;
//		if (jSlots != null) {
//
//			List<SlotImpl> slots = updateSlots(service, jSlots);
//			/*
//			 * Set composed object
//			 */
//			service.addSlots(slots);
//		
//		}

		/*
		 * Indicate as updated
		 */
		this.created = false;

		return service;

	}

}
