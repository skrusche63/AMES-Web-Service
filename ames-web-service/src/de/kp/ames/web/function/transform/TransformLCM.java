package de.kp.ames.web.function.transform;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.transform
 *  Module: TransformLCM
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #function #lcm #transform #web
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
import de.kp.ames.web.function.domain.model.TransformatorObject;
import de.kp.ames.web.shared.constants.ClassificationConstants;

public class TransformLCM extends JaxrLCM {
	
	public TransformLCM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Delete transformator
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public String deleteTransformator(String uid) throws Exception {

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
		JSONObject jResponse = transaction.getJResponse(uid, FncMessages.TRANSFORMATOR_DELETED);
		return jResponse.toString();
		
	}

	/**
	 * Submit XSL transformation
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String submitTransformator(String data) throws Exception {

		/*
		 * Create or retrieve registry package that is 
		 * responsible for managing transformators
		 */
		RegistryPackageImpl container = null;		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		List<RegistryPackageImpl> list = dqm.getRegistryPackage_ByClasNode(ClassificationConstants.FNC_ID_Transformator);
		if (list.size() == 0) {
			/*
			 * Create container
			 */
			container = createTransformatorPackage();
			
		} else {
			/*
			 * Retrieve container
			 */
			container = list.get(0);

		}
		
		/*
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();
		
		/*
		 * Create TransformatorObject
		 */
		TransformatorObject transformatorObject = new TransformatorObject(jaxrHandle, this);
		RegistryObjectImpl ro = transformatorObject.create(data);

		transaction.addObjectToSave(ro);
		if (transformatorObject.isCreated()) container.addRegistryObject(ro);

		/*
		 * Save objects
		 */
		transaction.addObjectToSave(container);
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Supply reactor
		 */
		ReactorParams reactorParams = new ReactorParams(jaxrHandle, ro, ClassificationConstants.FNC_ID_Transformator, RAction.C_INDEX_RSS);
		ReactorImpl.onSubmit(reactorParams);

		/*
		 * Retrieve response
		 */
		JSONObject jResponse = transaction.getJResponse(ro.getId(), FncMessages.TRANSFORMATOR_CREATED);
		return jResponse.toString();
		
	}

	/**
	 * A helper method to create a new transformator container
	 * 
	 * @return
	 * @throws JAXRException
	 */
	private RegistryPackageImpl createTransformatorPackage() throws JAXRException  {

		FncParams params = new FncParams();
		
		/*
		 * Name & description
		 */
		params.put(FncParams.K_NAME, "Transformators");
		params.put(FncParams.K_DESC, FncMessages.TRANSFORMATOR_DESC);
		
		/*
		 * Prefix
		 */
		params.put(FncParams.K_PRE, FncConstants.TRANSFORMATOR_PRE);
		
		/*
		 * Classification
		 */
		params.put(FncParams.K_CLAS, ClassificationConstants.FNC_ID_Transformator);
		
		/*
		 * Create package
		 */
		DomainLCM lcm = new DomainLCM(jaxrHandle);
		return lcm.createBusinessPackage(params);
	
	}

}
