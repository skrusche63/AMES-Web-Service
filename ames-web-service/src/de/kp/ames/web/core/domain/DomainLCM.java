package de.kp.ames.web.core.domain;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.domain
 *  Module: DomainLCM
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #domain #lcm #web
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

import java.lang.reflect.Constructor;
import java.util.Locale;

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.json.JSONObject;

import de.kp.ames.web.core.domain.model.ICoreObject;
import de.kp.ames.web.core.reactor.ReactorImpl;
import de.kp.ames.web.core.reactor.ReactorParams;
import de.kp.ames.web.core.reactor.ReactorParams.RAction;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.regrep.JaxrTransaction;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.function.FncMessages;
import de.kp.ames.web.function.FncParams;
import de.kp.ames.web.shared.constants.ClassificationConstants;

public class DomainLCM extends JaxrLCM {
	
	public DomainLCM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}
	
	/**
	 * Delete registry object
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public String deleteRegistryObject(String uid) throws Exception {

		/*
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();
		
		/*
		 * Delete object
		 */
		deleteRegistryObject(uid, transaction);
		
		/*
		 * Retrieve response
		 */
		JSONObject jResponse = transaction.getJResponse(uid, FncMessages.CORE_OBJECT_DELETED);
		return jResponse.toString();
		
	}
	
	/**
	 * Submit an external link to the OASIS ebXML RegRep
	 * 
	 * @param parent
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String submitExternalLink(String parent, String data) throws Exception {
		
		/*
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();

		/*
		 * Submit ExternalLink
		 */
		RegistryObjectImpl ro = submitCoreObject(parent, data, "ExternalLink", transaction);

		/*
		 * Supply reactor
		 */
		ReactorParams reactorParams = new ReactorParams(jaxrHandle, ro, ClassificationConstants.FNC_ID_Link, RAction.C_INDEX_RSS);
		ReactorImpl.onSubmit(reactorParams);
		
		/*
		 * Get response
		 */
		JSONObject jResponse = transaction.getJResponse(ro.getId(), FncMessages.CORE_OBJECT_CREATED);
		return jResponse.toString();
		
	}

	/**
	 * Submit a registry package to an OASIS ebXML RegRep
	 * 
	 * @param parent
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String submitRegistryPackage(String parent, String data) throws Exception {
		
		/*
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();

		/*
		 * Submit ExternalLink
		 */
		RegistryObjectImpl ro = submitCoreObject(parent, data, "RegistryPackage", transaction);

		/*
		 * Supply reactor
		 */
		ReactorParams reactorParams = new ReactorParams(jaxrHandle, ro, ClassificationConstants.FNC_ID_Package, RAction.C_INDEX_RSS);
		ReactorImpl.onSubmit(reactorParams);
		
		/*
		 * Get response
		 */
		JSONObject jResponse = transaction.getJResponse(ro.getId(), FncMessages.CORE_OBJECT_CREATED);
		return jResponse.toString();

	}

	/**
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public RegistryPackageImpl getContainer(String uid) throws Exception {
		/*
		 * Retrieve parent package
		 */
		RegistryPackageImpl container = (RegistryPackageImpl)getRegistryObjectById(uid);
		if (container == null) throw new Exception("[DomainLCM] Containing registry package for id <" + uid + "> does not exist.");

		return container;
		
	}
	
	/**
	 * @param objectName
	 * @return
	 */
	private ICoreObject createObjectForName(String objectName) {

		try {

			Class<?> clazz = Class.forName(objectName);
			Constructor<?> constructor = clazz.getConstructor(JaxrHandle.class, JaxrLCM.class);
			
			Object instance = constructor.newInstance(jaxrHandle, this);
			return (ICoreObject)instance;

		} catch (Exception e) {
			e.printStackTrace();
		
		} finally {}
		
		return null;
		
	}
	
	/**
	 * A helper method to submit a selected CoreObject
	 * by object name
	 * 
	 * @param parent
	 * @param data
	 * @param objectName
	 * @return
	 * @throws Exception
	 */
	private RegistryObjectImpl submitCoreObject(String parent, String data, String objectName, JaxrTransaction transaction) throws Exception {

		/*
		 * Retrieve parent package
		 */
		RegistryPackageImpl container = getContainer(parent);

		/*
		 * Submit CoreObject
		 */
		ICoreObject coreObject = createObjectForName(objectName);
		RegistryObjectImpl ro = coreObject.submit(data);

		transaction.addObjectToSave(ro);
		if (coreObject.isCreated()) container.addRegistryObject(ro);

		/*
		 * Save objects
		 */
		transaction.addObjectToSave(container);
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Return registry object
		 */
		return ro;
		
	}

	/**
	 * A helper method to create a top registry package
	 * fro a set of parameters
	 * 
	 * @param params
	 * @return
	 * @throws JAXRException
	 */
	public RegistryPackageImpl createBusinessPackage(FncParams params) throws JAXRException {

		JaxrTransaction transaction = new JaxrTransaction();
		
		String name = params.get(FncParams.K_NAME);
		RegistryPackageImpl rp = this.createRegistryPackage(Locale.US, name);
	
		/* 
		 * Identifier
		 */
		String uid = JaxrIdentity.getInstance().getPrefixUID(params.get(FncParams.K_PRE));
	
		rp.setLid(uid);
		rp.getKey().setId(uid);
		
		/* 
		 * Description
		 */
		String desc = params.get(FncParams.K_DESC);
		rp.setDescription(createInternationalString(Locale.US, desc));
		
		/*
		 * home url
		 */
		String home = jaxrHandle.getEndpoint().replace("/saml", "");
		rp.setHome(home);
	
		/* 
		 * Make sure that the registry object is processed
		 * right before any references to this object are made
		 */
		transaction.addObjectToSave(rp);

		/*
		 * Create classification
		 */
		String conceptType = params.get(FncParams.K_CLAS);
		ClassificationImpl c = createClassification(conceptType);

		/* 
		 * Associate classification and mail container
		 */
		rp.addClassification(c);
		transaction.addObjectToSave(c);				

		/*
		 * Save objects
		 */
		saveObjects(transaction.getObjectsToSave(), false, false);
		return (RegistryPackageImpl)jaxrHandle.getDQM().getRegistryObject(uid);
		
	}
}
