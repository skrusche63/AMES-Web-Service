package de.kp.ames.web.function.product;
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
import de.kp.ames.web.function.domain.model.ProductObject;
import de.kp.ames.web.function.domain.model.ProductorObject;
import de.kp.ames.web.shared.constants.ClassificationConstants;

public class ProductLCM extends JaxrLCM {

	public ProductLCM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Delete product
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public String deleteProduct(String uid) throws Exception {

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
		JSONObject jResponse = transaction.getJResponse(uid, FncMessages.PRODUCT_DELETED);
		return jResponse.toString();
		
	}

	/**
	 * Delete productor
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public String deleteProductor(String uid) throws Exception {

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
		JSONObject jResponse = transaction.getJResponse(uid, FncMessages.PRODUCTOR_DELETED);
		return jResponse.toString();
		
	}

	/**
	 * Create ProductObject
	 * 
	 * @param data
	 * @param stream
	 * @return
	 * @throws Exception
	 */
	public String createProduct(String data, InputStream stream) throws Exception {

		/*
		 * Create or retrieve registry package that is 
		 * responsible for managing products
		 */
		RegistryPackageImpl container = null;		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		List<RegistryPackageImpl> list = dqm.getRegistryPackage_ByClasNode(ClassificationConstants.FNC_ID_Product);
		if (list.size() == 0) {
			/*
			 * Create container
			 */
			container = createProductPackage();
			
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
		 * Create ProductObject
		 */
		ProductObject productObject = new ProductObject(jaxrHandle, this);
		RegistryObjectImpl ro = productObject.create(data, stream);

		transaction.addObjectToSave(ro);
		
		/*
		 * Add ProductObject to the respective container
		 */
		container.addRegistryObject(ro);

		/*
		 * Save objects
		 */
		transaction.addObjectToSave(container);
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Supply reactor
		 */
		ReactorParams reactorParams = new ReactorParams(jaxrHandle, ro, ClassificationConstants.FNC_ID_Product, RAction.C_INDEX_RSS);
		ReactorImpl.onSubmit(reactorParams);

		/*
		 * Retrieve response
		 */
		JSONObject jResponse = transaction.getJResponse(ro.getId(), FncMessages.PRODUCT_CREATED);
		return jResponse.toString();
		
	}
	
	/**
	 * Submit Productor
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String submitProductor(String data) throws Exception {

		/*
		 * Create or retrieve registry package that is 
		 * responsible for managing productors
		 */
		RegistryPackageImpl container = null;		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		List<RegistryPackageImpl> list = dqm.getRegistryPackage_ByClasNode(ClassificationConstants.FNC_ID_Productor);
		if (list.size() == 0) {
			/*
			 * Create container
			 */
			container = createProductorPackage();
			
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
		 * Submit ProductorObject
		 */
		ProductorObject productorObject = new ProductorObject(jaxrHandle, this);
		RegistryObjectImpl ro = productorObject.submit(data);

		transaction.addObjectToSave(ro);
		if (productorObject.isCreated()) container.addRegistryObject(ro);

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
	 * A helper method to create a new product container
	 * 
	 * @return
	 * @throws JAXRException
	 */
	private RegistryPackageImpl createProductPackage() throws JAXRException  {

		FncParams params = new FncParams();
		
		/*
		 * Name & description
		 */
		params.put(FncParams.K_NAME, "Products");
		params.put(FncParams.K_DESC, FncMessages.PRODUCT_DESC);
		
		/*
		 * Prefix
		 */
		params.put(FncParams.K_PRE, FncConstants.PRODUCT_PRE);
		
		/*
		 * Classification
		 */
		params.put(FncParams.K_CLAS, ClassificationConstants.FNC_ID_Product);
		
		/*
		 * Create package
		 */
		DomainLCM lcm = new DomainLCM(jaxrHandle);
		return lcm.createBusinessPackage(params);
	
	}

	/**
	 * A helper method to create a new productor container
	 * 
	 * @return
	 * @throws JAXRException
	 */
	private RegistryPackageImpl createProductorPackage() throws JAXRException  {

		FncParams params = new FncParams();
		
		/*
		 * Name & description
		 */
		params.put(FncParams.K_NAME, "Productors");
		params.put(FncParams.K_DESC, FncMessages.PRODUCTOR_DESC);
		
		/*
		 * Prefix
		 */
		params.put(FncParams.K_PRE, FncConstants.PRODUCTOR_PRE);
		
		/*
		 * Classification
		 */
		params.put(FncParams.K_CLAS, ClassificationConstants.FNC_ID_Productor);
		
		/*
		 * Create package
		 */
		DomainLCM lcm = new DomainLCM(jaxrHandle);
		return lcm.createBusinessPackage(params);
	
	}

}
