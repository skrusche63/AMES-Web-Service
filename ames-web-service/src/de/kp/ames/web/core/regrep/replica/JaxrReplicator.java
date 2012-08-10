package de.kp.ames.web.core.regrep.replica;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.regrep.replica
 *  Module: JaxrReplicator
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #jaxr #regrep #replica #replicator #web
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
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ConceptImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExternalLinkImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceBindingImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceImpl;
import org.freebxml.omar.client.xml.registry.infomodel.SlotImpl;
import org.freebxml.omar.client.xml.registry.infomodel.SpecificationLinkImpl;
import org.freebxml.omar.common.CanonicalSchemes;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.regrep.JaxrTransaction;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.shared.constants.JaxrConstants;

/**
 * JaxrReplicator creates a new registry object
 * using an existing one as a template
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 * 
 */
public class JaxrReplicator {

	/*
	 * Classification nodes that represent the object types 
	 * that are supported by the replication functionality
	 */
	private static String EXTERNAL_LINK    = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_ExternalLink;
	private static String EXTRINSIC_OBJECT = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_ExtrinsicObject;
	private static String REGISTRY_PACKAGE = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_RegistryPackage;
	private static String SERVICE          = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_Service;

	/*
	 * Reference to caller's JaxrHandle
	 */
	@SuppressWarnings("unused")
	private JaxrHandle jaxrHandle;

	/*
	 * Reference to Lifecycle Manager
	 */
	private JaxrLCM jaxrLCM;
	
	/**
	 * Constructor requires JaxrHandle
	 * 
	 * @param jaxrHandle
	 */
	public JaxrReplicator(JaxrHandle jaxrHandle) {
		/*
		 * Register JaxrHandle of caller's user 
		 */
		this.jaxrHandle = jaxrHandle;
		
		/*
		 * Build access to lifecycle manager
		 * functionality
		 */
		this.jaxrLCM = new JaxrLCM(jaxrHandle);

	}
	
	/**
	 * Replicate the original registry object and optionally
	 * add a reference to this object if required
	 * 
	 * @param origin
	 * @param parent
	 * @param reference
	 * @return
	 * @throws Exception
	 */
	public RegistryObjectImpl replicate(RegistryObjectImpl origin, RegistryPackageImpl parent, boolean reference) throws Exception {

		JaxrTransaction transaction = new JaxrTransaction();

		RegistryObjectImpl replica = null;
		String objectType = jaxrLCM.getObjectType(origin);				
		
		if (objectType.startsWith(EXTERNAL_LINK))				
			replica = replicateExternalLink(origin, transaction);
					
		else if (objectType.startsWith(EXTRINSIC_OBJECT))
			replica = replicateExtrinsicObject(origin, transaction);
					
		else if (objectType.startsWith(REGISTRY_PACKAGE))
			replica = replicateRegistryPackage(origin, transaction); 

		else if (objectType.startsWith(SERVICE))
			replica = replicateService(origin, transaction); 

		/* 
		 * A replica object may have a reference to its source or origin
		 */
		if (reference == true) {
			
			SlotImpl slot = jaxrLCM.createSlot("Reference", origin.getId(), JaxrConstants.SLOT_TYPE);
			replica.addSlot(slot);
			
		}
		
		transaction.addObjectToSave(replica);
		if (parent != null) {

			parent.addRegistryObject(replica);		
			transaction.addObjectToSave(parent);
		
		}
		
		/*
		 * Replication supports no versioning 
		 */
		boolean versionMetadata = false;
		boolean versionContent  = false;
		
		jaxrLCM.saveObjects(transaction.getObjectsToSave(), versionMetadata, versionContent);
		return replica;

	}
		
	/**
	 * A helper method to replicate an instance of an ExternalLink
	 * 
	 * @param ro
	 * @param transaction
	 * @return
	 * @throws JAXRException
	 */
	private ExternalLinkImpl replicateExternalLink(RegistryObjectImpl ro, JaxrTransaction transaction) throws JAXRException {
		
		ExternalLinkImpl origin = (ExternalLinkImpl)ro;

		ExternalLinkImpl replica = jaxrLCM.createExternalLink(origin.getExternalURI());		
		replica= (ExternalLinkImpl)replicateProperties(origin, replica, transaction);

		/*
		 * Replicate external uri
		 */
		replica.setExternalURI(origin.getExternalURI());
		
		return replica;
		
	}

	/**
	 * A helper method to replicate an instance of an ExtrinsicObject
	 * 
	 * @param ro
	 * @param transaction
	 * @return
	 * @throws JAXRException
	 */
	private ExtrinsicObjectImpl replicateExtrinsicObject(RegistryObjectImpl ro, JaxrTransaction transaction) throws JAXRException {

		ExtrinsicObjectImpl origin = (ExtrinsicObjectImpl)ro;

		ExtrinsicObjectImpl replica = jaxrLCM.createExtrinsicObject();
		replica = (ExtrinsicObjectImpl)replicateProperties(origin, replica, transaction);

		/*
		 *  Replicate mimetype
		 */
		replica.setMimeType(origin.getMimeType());

		/*
		 * Replicate repository item
		 */
		DataHandler handler = origin.getRepositoryItem();
		if (handler != null) replica.setRepositoryItem(handler);
		
		return replica;
		
	}
		
	/**
	 * A helper method to replicate an instance of a RegistryPackage
	 * 
	 * @param ro
	 * @param transaction
	 * @return
	 * @throws JAXRException
	 */
	private RegistryPackageImpl replicateRegistryPackage(RegistryObjectImpl ro, JaxrTransaction transaction) throws JAXRException {

		RegistryPackageImpl origin = (RegistryPackageImpl)ro;
		
		RegistryPackageImpl replica = jaxrLCM.createRegistryPackage(origin.getName());
		replica = (RegistryPackageImpl)replicateProperties(origin, replica, transaction);
		
		return replica;
		
	}

	/**
	 * A helper methd to replicate an instance of a Service
	 * 
	 * @param ro
	 * @param transaction
	 * @return
	 * @throws JAXRException
	 */
	private ServiceImpl replicateService(RegistryObjectImpl ro, JaxrTransaction transaction) throws JAXRException {
		
		ServiceImpl origin = (ServiceImpl)ro;
		
		ServiceImpl replica = jaxrLCM.createService(origin.getName());
		replica = (ServiceImpl)replicateProperties(origin, replica, transaction);

		/*
		 * Replicate service binding
		 */
		Collection<?> sourceBindings = origin.getServiceBindings();
		if ((sourceBindings == null) || (sourceBindings.size() == 0)) return null;

		/*
		 * The first binding of the service is taken into account
		 */
		ServiceBindingImpl sourceBinding = (ServiceBindingImpl) sourceBindings.toArray()[0];

		/* 
		 * The specification links of the source binding are determined
		 */
		Collection<?> sourceSpecifications = sourceBinding.getSpecificationLinks();
		if ((sourceSpecifications == null) || (sourceSpecifications.size() == 0)) return null;
		
		/* 
		 * Replicate specification links 
		 */
		Collection<SpecificationLinkImpl> replicaSpecifications = new ArrayList<SpecificationLinkImpl>();			
		
		Iterator<?> iterator = sourceSpecifications.iterator();
		while (iterator.hasNext()) {
			
			SpecificationLinkImpl originSpecification = (SpecificationLinkImpl) iterator.next();

			SpecificationLinkImpl replicaSpecification = replicateSpecificationLink(originSpecification, transaction);
			if (replicaSpecification == null) continue;
			
			replicaSpecifications.add(replicaSpecification);
			transaction.addObjectToSave(replicaSpecification);

		}
		
		ServiceBindingImpl replicaBinding = null;
		if (replicaSpecifications.isEmpty() == false) {

			replicaBinding = jaxrLCM.createServiceBinding();	
			replicaBinding.addSpecificationLinks(replicaSpecifications);
			
			transaction.addObjectToSave(replicaBinding);
		
		}

		if (replicaBinding != null) replica.addServiceBinding(replicaBinding);
		return replica;
		
	}
	
	/**
	 * A helper method to replicate an instance of a SpecificationLink
	 * 
	 * @param origin
	 * @param transaction
	 * @return
	 * @throws JAXRException
	 */
	/**
	 * @param origin
	 * @param transaction
	 * @return
	 * @throws JAXRException
	 */
	private SpecificationLinkImpl replicateSpecificationLink(SpecificationLinkImpl origin, JaxrTransaction transaction) throws JAXRException {

		SpecificationLinkImpl replica = jaxrLCM.createSpecificationLink();
		if (replica == null) return null;

		/*
		 * Replicate slots
		 */
		replicateSlots(origin, replica);

		/*
		 * Replicate specification object
		 */
		replica.setSpecificationObject((RegistryObjectImpl)origin.getSpecificationObject());

		return replica;

	}
	
	/**
	 * A helper method to replicate the slots of a certain
	 * registry object (origin)
	 * 
	 * @param origin
	 * @param replica
	 * @throws JAXRException
	 */
	private void replicateSlots(RegistryObjectImpl origin, RegistryObjectImpl replica) throws JAXRException {

		Collection<?> slots = origin.getSlots();
		
		Iterator<?> slotsIterator = slots.iterator();
		while (slotsIterator.hasNext()) {

			SlotImpl sourceSlot = (SlotImpl)slotsIterator.next();

			String name = sourceSlot.getName();
			String type = sourceSlot.getSlotType();
			
			Collection<?> values = sourceSlot.getValues();
			
			SlotImpl replicaSlot = jaxrLCM.createSlot(name, values, type);
			replica.addSlot(replicaSlot);

		}
		
	}
	
	/**
	 * A helper method to replicate the common properties
	 * of a certain registry object (origin); note, that
	 * actually associations, and external identifiers
	 * are not replicated
	 * 
	 * @param origin
	 * @param replica
	 * @param transaction
	 * @return
	 * @throws JAXRException
	 */
	private RegistryObjectImpl replicateProperties(RegistryObjectImpl origin, RegistryObjectImpl replica, JaxrTransaction transaction) throws JAXRException {

		/* 
		 * A replica has a new physical identifier to distinguish
		 * this registry object from the original registry object
		 * 
		 * The prefix, however, is replicated
		 */

		String sid = origin.getId();
		
		int pos = sid.lastIndexOf(":");
		String uid = sid.substring(0, pos) + ":" + JaxrIdentity.getInstance().getUID();

		replica.setLid(uid);
		replica.getKey().setId(uid);

		/* 
		 * Replicate name & description
		 */
		replica.setName(origin.getName());
		replica.setDescription(origin.getDescription());
			
		/* 
		 * Replicate home url
		 */
		replica.setHome(jaxrLCM.getHome(origin));
		
		/*
		 * Replicate slots
		 */
		replicateSlots(origin, replica);

		/*
		 * Make sure that the replica is processed right before any references 
		 * to this object are made (e.g. classifications, external links)
		 */
		transaction.addObjectToSave(replica);
		
		/*
		 * Replicate classifications
		 */
		replicateClassifications(origin, replica, transaction);

		/*
		 * Replicate external links
		 */
		replica.setExternalLinks(origin.getExternalLinks());
		
		return replica;
		
	}

	/**
	 * A helper method to replicate classifications
	 * 
	 * @param origin
	 * @param replica
	 * @param transaction
	 * @throws JAXRException
	 */
	private void replicateClassifications(RegistryObjectImpl origin, RegistryObjectImpl replica, JaxrTransaction transaction) throws JAXRException {
		
		Collection<?> classifications = origin.getClassifications();
		Iterator<?> iterator = classifications.iterator();
		
		while (iterator.hasNext()) {

			ClassificationImpl c = (ClassificationImpl) iterator.next();
			ConceptImpl cpt = (ConceptImpl) c.getConcept();
			
			ClassificationImpl rc = jaxrLCM.createClassification(cpt);
			transaction.addObjectToSave(rc);
			
			replica.addClassification(rc);		
		
		}
		
	}

}
