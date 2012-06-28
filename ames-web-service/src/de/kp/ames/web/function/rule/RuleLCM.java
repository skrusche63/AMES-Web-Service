package de.kp.ames.web.function.rule;
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

import java.io.InputStream;
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
import de.kp.ames.web.function.domain.model.EvaluationObject;
import de.kp.ames.web.function.domain.model.ReasonerObject;
import de.kp.ames.web.shared.ClassificationConstants;

public class RuleLCM extends JaxrLCM {

	public RuleLCM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Create EvaluationObject
	 * 
	 * @param source
	 * @param reasoner
	 * @param data
	 * @param stream
	 * @return
	 * @throws Exception
	 */
	public String createEvaluation(String source, String reasoner, String data, InputStream stream) throws Exception {

		/*
		 * Create or retrieve registry package that is 
		 * responsible for managing evaluations
		 */
		RegistryPackageImpl container = null;		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		List<RegistryPackageImpl> list = dqm.getRegistryPackage_ByClasNode(ClassificationConstants.FNC_ID_Evaluation);
		if (list.size() == 0) {
			/*
			 * Create container
			 */
			container = createEvaluationPackage();
			
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
		 * Create EvaluationObject
		 */
		EvaluationObject evaluationObject = new EvaluationObject(jaxrHandle, this);
		RegistryObjectImpl ro = evaluationObject.create(source, reasoner, data, stream);

		transaction.addObjectToSave(ro);
		
		/*
		 * Add EvaluationObject and also its associations
		 * to the respective container; this is a requirement
		 * for visualizing evaluations as concept graphs
		 */
		container.addRegistryObject(ro);
		container.addRegistryObjects(ro.getAssociations());

		/*
		 * Save objects
		 */
		transaction.addObjectToSave(container);
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Supply reactor
		 */
		ReactorParams reactorParams = new ReactorParams(jaxrHandle, ro, ClassificationConstants.FNC_ID_Evaluation, RAction.C_INDEX_RSS);
		ReactorImpl.onSubmit(reactorParams);

		/*
		 * Retrieve response
		 */
		JSONObject jResponse = transaction.getJResponse(ro.getId(), FncMessages.EVALUATION_CREATED);
		return jResponse.toString();

	}

	/**
	 * Submit ReasonerObject
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String submitReasoner(String data) throws Exception {

		/*
		 * Create or retrieve registry package that is 
		 * responsible for managing productors
		 */
		RegistryPackageImpl container = null;		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		List<RegistryPackageImpl> list = dqm.getRegistryPackage_ByClasNode(ClassificationConstants.FNC_ID_Reasoner);
		if (list.size() == 0) {
			/*
			 * Create container
			 */
			container = createReasonerPackage();
			
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
		 * Submit ReasonerObject
		 */
		ReasonerObject reasonerObject = new ReasonerObject(jaxrHandle, this);
		RegistryObjectImpl ro = reasonerObject.submit(data);

		transaction.addObjectToSave(ro);
		if (reasonerObject.isCreated()) container.addRegistryObject(ro);

		/*
		 * Save objects
		 */
		transaction.addObjectToSave(container);
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Supply reactor
		 */
		ReactorParams reactorParams = new ReactorParams(jaxrHandle, ro, ClassificationConstants.FNC_ID_Productor, RAction.C_INDEX_RSS);
		ReactorImpl.onSubmit(reactorParams);

		/*
		 * Retrieve response
		 */
		JSONObject jResponse = transaction.getJResponse(ro.getId(), FncMessages.PRODUCTOR_CREATED);
		return jResponse.toString();
	}
	
	/**
	 * A helper method to create a new evaluation container
	 * 
	 * @return
	 * @throws JAXRException
	 */
	private RegistryPackageImpl createEvaluationPackage() throws JAXRException  {

		FncParams params = new FncParams();
		
		/*
		 * Name & description
		 */
		params.put(FncParams.K_NAME, "Evaluations");
		params.put(FncParams.K_DESC, FncMessages.EVALUATION_DESC);
		
		/*
		 * Prefix
		 */
		params.put(FncParams.K_PRE, FncConstants.EVALUATION_PRE);
		
		/*
		 * Classification
		 */
		params.put(FncParams.K_CLAS, ClassificationConstants.FNC_ID_Evaluation);
		
		/*
		 * Create package
		 */
		DomainLCM lcm = new DomainLCM(jaxrHandle);
		return lcm.createBusinessPackage(params);
	
	}

	/**
	 * A helper method to create a new reasoner container
	 * 
	 * @return
	 * @throws JAXRException
	 */
	private RegistryPackageImpl createReasonerPackage() throws JAXRException  {

		FncParams params = new FncParams();
		
		/*
		 * Name & description
		 */
		params.put(FncParams.K_NAME, "Reasoners");
		params.put(FncParams.K_DESC, FncMessages.REASONER_DESC);
		
		/*
		 * Prefix
		 */
		params.put(FncParams.K_PRE, FncConstants.REASONER_PRE);
		
		/*
		 * Classification
		 */
		params.put(FncParams.K_CLAS, ClassificationConstants.FNC_ID_Reasoner);
		
		/*
		 * Create package
		 */
		DomainLCM lcm = new DomainLCM(jaxrHandle);
		return lcm.createBusinessPackage(params);
	
	}

}
