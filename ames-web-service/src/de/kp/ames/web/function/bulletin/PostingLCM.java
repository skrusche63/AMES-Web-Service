package de.kp.ames.web.function.bulletin;
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

import javax.activation.DataHandler;
import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.AssociationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.json.JSONObject;

import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.reactor.ReactorParams;
import de.kp.ames.web.core.reactor.ReactorParams.RAction;
import de.kp.ames.web.core.reactor.ReactorImpl;
import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.regrep.JaxrTransaction;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.FncConstants;

public class PostingLCM extends JaxrLCM {

	/**
	 * Constructor requires jaxrHandle
	 * 
	 * @param requestHandle
	 */
	public PostingLCM(JaxrHandle requestHandle) {
		super(requestHandle);
	}

	/**
	 * Submit posting for a certain recipient; a posting is an 
	 * extrinsic object that is classified as posting and contained
	 * within the respective positing container; 
	 * 
	 * the association to the addressed recipient is mapped onto an 
	 * association instance
	 * 
	 * @param recipient
	 * @param data
	 * @return
	 * @throws Exception 
	 */
	public String submit(String recipient, String data) throws Exception {

		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);
		
		/*
		 * Create or retrieve registry package that is 
		 * responsible for managing posting
		 */
		RegistryPackageImpl container = null;		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		List<RegistryPackageImpl> list = dqm.getRegistryPackage_ByClasNode(FncConstants.FNC_ID_Posting);
		if (list.size() == 0) {
			/*
			 * Create container
			 */
			container = createPostingPackage();
			
		} else {
			/*
			 * Retrieve container
			 */
			container = list.get(0);

		}

		/* 
		 * A positing is a certain extrinsic object that holds all relevant
		 * and related information in a single JSON repository item
		 */

		ExtrinsicObjectImpl eo = null;
		JaxrTransaction transaction = new JaxrTransaction();

		// create extrinsic object that serves as a container for
		// the respective posting
		eo = createExtrinsicObject();
		if (eo == null) throw new JAXRException();
				
		/* 
		 * Identifier
		 */
		String eid = JaxrIdentity.getInstance().getPrefixUID(FncConstants.POSTING_PRE);
		/*
		* Name & description using default locale
		*/
		
		JSONObject jPosting = new JSONObject(data);

		String name = jPosting.getString(JaxrConstants.RIM_NAME);				
		String desc = jPosting.getString(JaxrConstants.RIM_DESC);
		/* 
		 * Home url
		 */
		String home = jaxrHandle.getEndpoint().replace("/soap", "");
		
		/*
		 * Set properties
		 */
		setProperties(eo, eid, name, desc, home);
				
		/* 
		 * Mime type & handler
		 */
		String mimetype = GlobalConstants.MT_JSON;
		eo.setMimeType(mimetype);
				
		byte[] bytes = data.getBytes(GlobalConstants.UTF_8);

		DataHandler handler = new DataHandler(FileUtil.createByteArrayDataSource(bytes, mimetype));                	
    	eo.setRepositoryItem(handler);				

    	transaction.addObjectToSave(eo);

		/*
		 * Create classification
		 */
		ClassificationImpl c = createClassification(FncConstants.FNC_ID_Posting);
		c.setName(createInternationalString("Posting Classification"));

		/* 
		 * Associate classification and posting container
		 */
		eo.addClassification(c);
		transaction.addObjectToSave(c);				

		/* 
		 * Add extrinsic object to container
		 */
		container.addRegistryObject(eo);
		transaction.addObjectToSave(container);
				
		/*
		 * Create association
		 */
		String aid = JaxrIdentity.getInstance().getPrefixUID(FncConstants.POSTING_PRE);
		AssociationImpl a = this.createAssociation_RelatedTo(eo);		
		/*
		 * Set properties
		 */
		setProperties(a, aid, "Recipient Link", "This is a directed association between a recipient and the respective posting.", home);

		/*
		 * Source object
		 */
		RegistryObjectImpl so = (RegistryObjectImpl)jaxrHandle.getDQM().getRegistryObject(recipient);
		
		so.addAssociation(a);
		transaction.addObjectToSave(a);
		
		/*
		 * Add association to container
		 */
		container.addRegistryObject(a);

		/*
		 * Save objects
		 */
		transaction.addObjectToSave(container);
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Supply reactor
		 */
		ReactorParams reactorParams = new ReactorParams(eo, FncConstants.FNC_ID_Posting, RAction.C_INDEX);
		ReactorImpl.execute(reactorParams);

		/*
		 * Set JSON response
		 */
		JSONObject jResponse = new JSONObject();
				
		jResponse.put("success", true);
		jResponse.put("message", "Posting successfully created.");
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return jResponse.toString();
		
	}

	private void setProperties(RegistryObjectImpl ro, String uid, String name, String desc, String home) throws JAXRException {

		/*
		 * Identifier
		 */
		ro.setLid(uid);
		ro.getKey().setId(uid);

		/*
		 * Name & description using default locale
		 */
		ro.setName(createInternationalString(name));
		ro.setDescription(createInternationalString(desc));

		/* 
		 * Home url
		 */
		ro.setHome(home);

	}
	
	/**
	 * A helper method to create a new posting container
	 * 
	 * @return
	 * @throws JAXRException
	 */
	private RegistryPackageImpl createPostingPackage() throws JAXRException  {

		JaxrTransaction transaction = new JaxrTransaction();
		RegistryPackageImpl rp = this.createRegistryPackage(Locale.US, "Postings");
	
		/* 
		 * Identifier
		 */
		String uid = JaxrIdentity.getInstance().getPrefixUID(FncConstants.POSTING_PRE);
	
		rp.setLid(uid);
		rp.getKey().setId(uid);

		/* 
		 * Description
		 */
		rp.setDescription(createInternationalString(Locale.US, "This is the top package to manage all postings submitted to this RegRep instance."));
		
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
		ClassificationImpl c = createClassification(FncConstants.FNC_ID_Posting);
		c.setName(createInternationalString("Posting Classification"));

		/* 
		 * Associate classification and posting container
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
