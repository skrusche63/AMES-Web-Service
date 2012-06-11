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
import java.util.Locale;

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExternalLinkImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.json.JSONObject;

import de.kp.ames.web.core.format.json.JsonConstants;
import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.regrep.JaxrTransaction;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.function.FncMessages;
import de.kp.ames.web.function.FncParams;

public class DomainLCM extends JaxrLCM {

	/*
	 * Response messages
	 */
	private static String MISSING_PARAMETERS    = FncMessages.MISSING_PARAMETERS;
	private static String EXTERNAL_LINK_CREATED = FncMessages.EXTERNAL_LINK_CREATED;

	/*
	 * Registry object
	 */
	private static String RIM_ID = JaxrConstants.RIM_ID;
	
	public DomainLCM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
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
		
		JSONObject jResponse = new JSONObject();
		
		/*
		 * Prepare (pessimistic) response message
		 */
		String message = MISSING_PARAMETERS;
		
		jResponse.put(JsonConstants.J_SUCCESS, false);
		jResponse.put(JsonConstants.J_MESSAGE, message);
	
		/*
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();
	
		/*
		 * Initialize data
		 */
		JSONObject jForm = new JSONObject(data);

		/*
		 * Create or retrieve registry package that is 
		 * responsible for managing external links
		 */
		RegistryPackageImpl container = null;		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		List<RegistryPackageImpl> list = dqm.getRegistryPackage_ByClasNode(FncConstants.FNC_ID_Link);
		if (list.size() == 0) {
			/*
			 * Create container
			 */
			container = createLinkPackage();
			
		} else {
			/*
			 * Retrieve container
			 */
			container = list.get(0);

		}

		/*
		 * Determine whether registry object exists
		 */
		RegistryObjectImpl ro = getObject(jForm);
		if (ro == null) {
			/*
			 * create request (note, that the created
			 * object is added to the transaction within
			 * the subsequent method call
			 */
			ro = createExternalLink(jForm, transaction);

			/*
			 * Allocate external link to container
			 */
			container.addRegistryObject(ro);
			transaction.addObjectToSave(container);
			
		} else {
			/*
			 * update request (it is expected that the
			 * respective object is already attached to
			 * a top registry package)
			 */
			ro = updateExternalLink((ExternalLinkImpl)ro, jForm, transaction);

		}

		/*
		 * Save objects	
		 */
		saveObjects(transaction.getObjectsToSave(), false, false);
		
		/*
		 * Add identifier to the response
		 * for retrieval purpose
		 */
		jResponse.put(JsonConstants.J_ID, ro.getId());

		/*
		 * Update response message
		 */
		message = EXTERNAL_LINK_CREATED;
		
		jResponse.put(JsonConstants.J_SUCCESS, true);
		jResponse.put(JsonConstants.J_MESSAGE, message);

		return jResponse.toString();

	}

	/**
	 * A helper method to determine whether a referenced
	 * registry object exists or not
	 * 
	 * @param jForm
	 * @return
	 * @throws Exception
	 */
	private RegistryObjectImpl getObject(JSONObject jForm) throws Exception {
		
		/*
		 * Determine unique identifier from JSON Object
		 */
		if (jForm.has(RIM_ID) == false) return null;

		String uid = jForm.getString(RIM_ID);
		return getRegistryObjectById(uid);
		
	}
	
	private ExternalLinkImpl createExternalLink(JSONObject jForm, JaxrTransaction transaction) throws Exception {
		// TODO
		return null;
	}
	
	private ExternalLinkImpl updateExternalLink(ExternalLinkImpl el, JSONObject jForm, JaxrTransaction transaction) throws Exception {
		// TODO
		return null;
	}
	
	/**
	 * A helper method to create a top registry package
	 * to manage external links
	 * 
	 * @return
	 * @throws JAXRException
	 */
	private RegistryPackageImpl createLinkPackage() throws JAXRException  {

		FncParams params = new FncParams();
		
		/*
		 * Name & description
		 */
		params.put(FncParams.K_NAME, "External Links");
		params.put(FncParams.K_DESC, FncMessages.LINK_DESC);
		
		/*
		 * Prefix
		 */
		params.put(FncParams.K_PRE, FncConstants.LINK_PRE);
		
		/*
		 * Classification
		 */
		params.put(FncParams.K_CLAS, FncConstants.FNC_ID_Link);
		
		/*
		 * Create package
		 */
		return createBusinessPackage(params);
	
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
		String uid = JaxrIdentity.getInstance().getPrefixUID(FncParams.K_PRE);
	
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
		String home = jaxrHandle.getEndpoint().replace("/soap", "");
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
