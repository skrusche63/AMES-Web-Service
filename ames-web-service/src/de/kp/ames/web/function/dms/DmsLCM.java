package de.kp.ames.web.function.dms;
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
import de.kp.ames.web.function.domain.DomainLCM;
import de.kp.ames.web.function.domain.model.DocumentObject;
import de.kp.ames.web.function.domain.model.ImageObject;

public class DmsLCM extends JaxrLCM {

	public DmsLCM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	public String submitDocument(String data) throws Exception {
		/*
		 * Create or retrieve registry package that is 
		 * responsible for managing documents
		 */
		RegistryPackageImpl container = null;		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		List<RegistryPackageImpl> list = dqm.getRegistryPackage_ByClasNode(FncConstants.FNC_ID_Document);
		if (list.size() == 0) {
			/*
			 * Create container
			 */
			container = createDocumentPackage();
			
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
		 * Create DocumentObject
		 */
		DocumentObject documentObject = new DocumentObject(jaxrHandle, this);
		RegistryObjectImpl ro = documentObject.create(data);

		transaction.addObjectToSave(ro);		
		container.addRegistryObject(ro);

		/*
		 * Save objects
		 */
		transaction.addObjectToSave(container);
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Supply reactor
		 */
		ReactorParams reactorParams = new ReactorParams(jaxrHandle, ro, FncConstants.FNC_ID_Document, RAction.C_INDEX_RSS);
		ReactorImpl.onSubmit(reactorParams);

		/*
		 * Retrieve response
		 */
		JSONObject jResponse = transaction.getJResponse(ro.getId(), FncMessages.DOCUMENT_CREATED);
		return jResponse.toString();

	}
	
	public String submitImage(String data) throws Exception {
		/*
		 * Create or retrieve registry package that is 
		 * responsible for managing images
		 */
		RegistryPackageImpl container = null;		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		List<RegistryPackageImpl> list = dqm.getRegistryPackage_ByClasNode(FncConstants.FNC_ID_Image);
		if (list.size() == 0) {
			/*
			 * Create container
			 */
			container = createImagePackage();
			
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
		 * Create ImageObject
		 */
		ImageObject imageObject = new ImageObject(jaxrHandle, this);
		RegistryObjectImpl ro = imageObject.create(data);

		transaction.addObjectToSave(ro);		
		container.addRegistryObject(ro);

		/*
		 * Save objects
		 */
		transaction.addObjectToSave(container);
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Supply reactor
		 */
		ReactorParams reactorParams = new ReactorParams(jaxrHandle, ro, FncConstants.FNC_ID_Image, RAction.C_INDEX_RSS);
		ReactorImpl.onSubmit(reactorParams);

		/*
		 * Retrieve response
		 */
		JSONObject jResponse = transaction.getJResponse(ro.getId(), FncMessages.IMAGE_CREATED);
		return jResponse.toString();

	}

	/**
	 * A helper method to create a new document container
	 * 
	 * @return
	 * @throws JAXRException
	 */
	private RegistryPackageImpl createDocumentPackage() throws JAXRException  {

		FncParams params = new FncParams();
		
		/*
		 * Name & description
		 */
		params.put(FncParams.K_NAME, "Documents");
		params.put(FncParams.K_DESC, FncMessages.DOCUMENT_DESC);
		
		/*
		 * Prefix
		 */
		params.put(FncParams.K_PRE, FncConstants.DOCUMENT_PRE);
		
		/*
		 * Classification
		 */
		params.put(FncParams.K_CLAS, FncConstants.FNC_ID_Document);
		
		/*
		 * Create package
		 */
		DomainLCM lcm = new DomainLCM(jaxrHandle);
		return lcm.createBusinessPackage(params);
	
	}
	
	/**
	 * A helper method to create a new image container
	 * 
	 * @return
	 * @throws JAXRException
	 */
	private RegistryPackageImpl createImagePackage() throws JAXRException  {

		FncParams params = new FncParams();
		
		/*
		 * Name & description
		 */
		params.put(FncParams.K_NAME, "Images");
		params.put(FncParams.K_DESC, FncMessages.IMAGE_DESC);
		
		/*
		 * Prefix
		 */
		params.put(FncParams.K_PRE, FncConstants.IMAGE_PRE);
		
		/*
		 * Classification
		 */
		params.put(FncParams.K_CLAS, FncConstants.FNC_ID_Image);
		
		/*
		 * Create package
		 */
		DomainLCM lcm = new DomainLCM(jaxrHandle);
		return lcm.createBusinessPackage(params);
	
	}

}
