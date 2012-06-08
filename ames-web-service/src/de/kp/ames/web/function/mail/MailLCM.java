package de.kp.ames.web.function.mail;
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

import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.json.JSONObject;

import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.format.json.JsonConstants;
import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.regrep.JaxrTransaction;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.FncConstants;

public class MailLCM extends JaxrLCM {

	public MailLCM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}
	
	/**
	 * Register a new mail message
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String submitMail(String data) throws Exception {
		
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
		 * responsible for managing mail message
		 */
		RegistryPackageImpl container = null;		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		List<RegistryPackageImpl> list = dqm.getRegistryPackage_ByClasNode(FncConstants.FNC_ID_Mail);
		if (list.size() == 0) {
			/*
			 * Create container
			 */
			container = createMailPackage();
			
		} else {
			/*
			 * Retrieve container
			 */
			container = list.get(0);

		}

		/* 
		 * A chat message is a certain extrinsic object that holds all 
		 * relevant and related information in a single JSON repository item
		 */

		ExtrinsicObjectImpl eo = null;

		/* 
		 * Create extrinsic object that serves as a container for
		 * the respective mail message
		 */
		eo = createExtrinsicObject();
		if (eo == null) throw new JAXRException();
				
		/* 
		 * Identifier
		 */
		String eid = JaxrIdentity.getInstance().getPrefixUID(FncConstants.MAIL_PRE);

		eo.setLid(eid);
		eo.getKey().setId(eid);

		/*
		 * Name & description using default locale
		 */
		String name = jForm.getString(JaxrConstants.RIM_NAME);				
		eo.setName(createInternationalString(name));

		String desc = jForm.getString(JaxrConstants.RIM_DESC);
		eo.setDescription(createInternationalString(desc));
		
		/* 
		 * Home url
		 */
		String home = jaxrHandle.getEndpoint().replace("/soap", "");
		eo.setHome(home);

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
		ClassificationImpl c = createClassification(FncConstants.FNC_ID_Chat);
		c.setName(createInternationalString(Locale.US, "Chat Classification"));

		/* 
		 * Associate classification and mail message
		 */
		eo.addClassification(c);
		transaction.addObjectToSave(c);				

		/* 
		 * Add extrinsic object to container
		 */
		container.addRegistryObject(eo);
		transaction.addObjectToSave(container);

		/*
		 * Save objects	
		 */
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Prepare response 
		 */
		JSONObject jResponse = new JSONObject();
		jResponse.put(JsonConstants.J_ID, eid);
		
		return jResponse.toString();
		
	}

	/**
	 * A helper method to create a new mail message container
	 * 
	 * @return
	 * @throws JAXRException
	 */
	private RegistryPackageImpl createMailPackage() throws JAXRException  {

		JaxrTransaction transaction = new JaxrTransaction();
		RegistryPackageImpl rp = this.createRegistryPackage(Locale.US, "Mails");
	
		/* 
		 * Identifier
		 */
		String uid = JaxrIdentity.getInstance().getPrefixUID(FncConstants.MAIL_PRE);
	
		rp.setLid(uid);
		rp.getKey().setId(uid);

		/* 
		 * Description
		 */
		rp.setDescription(createInternationalString(Locale.US, "This is the top package to manage all mails submitted to this RegRep instance."));
		
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
		ClassificationImpl c = createClassification(FncConstants.FNC_ID_Mail);
		c.setName(createInternationalString(Locale.US, "Mail Classification"));

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
