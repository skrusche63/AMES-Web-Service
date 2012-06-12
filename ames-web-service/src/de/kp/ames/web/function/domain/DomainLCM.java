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

import java.util.Locale;

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExternalLinkImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceImpl;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.regrep.JaxrTransaction;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.function.FncMessages;
import de.kp.ames.web.function.FncParams;

public class DomainLCM extends JaxrLCM {

	/*
	 * Registry object
	 */
	private static String RIM_ID = JaxrConstants.RIM_ID;
	
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
		JSONObject jResponse = transaction.getJResponse(uid, FncMessages.REGISTRY_OBJECT_DELETED);
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
		transaction.setData(data);

		/*
		 * Retrieve parent package
		 */
		RegistryPackageImpl container = (RegistryPackageImpl)getRegistryObjectById(parent);
		if (container == null) throw new Exception("[DomainLCM] Containing registry package for id <" + parent + "> does not exist.");

		/*
		 * Determine whether registry object exists
		 */
		RegistryObjectImpl ro = getObject(transaction);
		if (ro == null) {
			/*
			 * create request (note, that the created
			 * object is added to the transaction within
			 * the subsequent method call
			 */
			ro = createExternalLink(transaction);

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
			ro = updateExternalLink((ExternalLinkImpl)ro, transaction);

		}

		/*
		 * Save objects	
		 */
		saveObjects(transaction.getObjectsToSave(), false, false);
		
		/*
		 * Get response
		 */
		JSONObject jResponse = transaction.getJResponse(ro.getId(), FncMessages.EXTERNAL_LINK_CREATED);
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
		// TODO
		return null;
	}
	
	/**
	 * A helper method to determine whether a referenced
	 * registry object exists or not
	 * 
	 * @param jForm
	 * @return
	 * @throws Exception
	 */
	private RegistryObjectImpl getObject(JaxrTransaction transaction) throws Exception {
		
		/*
		 * Determine unique identifier from JSON Object
		 */
		JSONObject jForm = transaction.getData();
		if (jForm.has(RIM_ID) == false) return null;

		String uid = jForm.getString(RIM_ID);
		return getRegistryObjectById(uid);
		
	}
	
	private ExternalLinkImpl createExternalLink(JaxrTransaction transaction) throws Exception {
		// TODO
		return null;
	}
	
	private RegistryPackageImpl createRegistryPackage(JaxrTransaction transction) throws Exception {
		// TODO
		return null;
	}

	private ServiceImpl createService(JaxrTransaction transaction) {
		// TODO
		return null;
	}
	
	private ExternalLinkImpl updateExternalLink(ExternalLinkImpl el, JaxrTransaction transaction) throws Exception {
		// TODO
		return null;
	}

	private RegistryPackageImpl updateRegistryPackage(RegistryPackageImpl rp, JaxrTransaction transaction) throws Exception {
		// TODO
		return null;
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
