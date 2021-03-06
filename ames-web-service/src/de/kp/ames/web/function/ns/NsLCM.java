package de.kp.ames.web.function.ns;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.ns
 *  Module: NsLCM
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #function #lcm #ns #web
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

import java.util.List;

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.json.JSONObject;

import de.kp.ames.web.core.domain.DomainLCM;
import de.kp.ames.web.core.reactor.ReactorImpl;
import de.kp.ames.web.core.reactor.ReactorParams;
import de.kp.ames.web.core.reactor.ReactorParams.RAction;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrTransaction;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.function.FncMessages;
import de.kp.ames.web.function.FncParams;
import de.kp.ames.web.function.domain.model.NamespaceObject;
import de.kp.ames.web.shared.constants.ClassificationConstants;

public class NsLCM extends JaxrLCM {

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public NsLCM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Delete namespace
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public String deleteNamepace(String uid) throws Exception {

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
		JSONObject jResponse = transaction.getJResponse(uid, FncMessages.FOLDER_DELETED);
		return jResponse.toString();
		
	}

	/**
	 * Submit a certain namespace 
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String submitNamespace(String data) throws Exception {
		return submitNamespace(data, null);
	}
	
	/**
	 * Submit a certain namespace to an optional parent namespace 
	 * 
	 * @param data
	 * @param parent
	 * @return
	 * @throws Exception
	 */
	public String submitNamespace(String data, String parent) throws Exception {

		/*
		 * Create or retrieve registry package that is 
		 * responsible for managing this namespace
		 */
		RegistryPackageImpl container = null;		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		if (parent == null) {
			/*
			 * root namespace
			 */
			List<RegistryPackageImpl> list = dqm.getRegistryPackage_ByClasNode(ClassificationConstants.FNC_ID_Namespace);
			if (list.size() == 0) {
				/*
				 * Create container
				 */
				container = createNamespacePackage();
				
			} else {
				/*
				 * Retrieve container
				 */
				container = list.get(0);
	
			}
		} else {
			/*
			 * given namespace parent
			 */
			container = (RegistryPackageImpl)dqm.getRegistryObjectById(parent);
			if (container == null) throw new JAXRException("[NsLCM] RegistryPackage with id <" + parent + "> not found.");

		}
		
		/*
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();

		/*
		 * Submit NamespaceObject
		 */
		NamespaceObject namespaceObject = new NamespaceObject(jaxrHandle, this);
		RegistryObjectImpl ro = namespaceObject.submit(data);

		transaction.addObjectToSave(ro);
		if (namespaceObject.isCreated()) container.addRegistryObject(ro);

		/*
		 * Save objects
		 */
		transaction.addObjectToSave(container);
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Supply reactor
		 */
		ReactorParams reactorParams = new ReactorParams(jaxrHandle, ro, ClassificationConstants.FNC_ID_Folder, RAction.C_INDEX_RSS);
		ReactorImpl.onSubmit(reactorParams);

		/*
		 * Retrieve response
		 */
		JSONObject jResponse = transaction.getJResponse(ro.getId(), namespaceObject.isCreated() ? FncMessages.FOLDER_CREATED : FncMessages.FOLDER_UPDATED);
		return jResponse.toString();
		
	}

	/**
	 * A helper method to create a new namespace container
	 * 
	 * @return
	 * @throws JAXRException
	 */
	private RegistryPackageImpl createNamespacePackage() throws JAXRException  {

		FncParams params = new FncParams();
		
		/*
		 * Name & description
		 */
		params.put(FncParams.K_NAME, "Namespaces");
		params.put(FncParams.K_DESC, FncMessages.NAMESPACE_DESC);
		
		/*
		 * Prefix
		 */
		params.put(FncParams.K_PRE, FncConstants.NAMESPACE_PRE);
		
		/*
		 * Classification
		 */
		params.put(FncParams.K_CLAS, ClassificationConstants.FNC_ID_Namespace);
		
		/*
		 * Create package
		 */
		DomainLCM lcm = new DomainLCM(jaxrHandle);
		return lcm.createBusinessPackage(params);
	
	}

}
