package de.kp.ames.web.function.comm;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.comm
 *  Module: CommLCM
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #comm #function #lcm #web
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

import java.io.InputStream;
import java.util.List;
import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.AssociationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.json.JSONObject;

import de.kp.ames.web.core.domain.DomainLCM;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.regrep.JaxrTransaction;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.function.FncMessages;
import de.kp.ames.web.function.FncParams;
import de.kp.ames.web.function.domain.model.AttachmentObject;
import de.kp.ames.web.function.domain.model.ChatObject;
import de.kp.ames.web.function.domain.model.MailObject;
import de.kp.ames.web.shared.constants.ClassificationConstants;

public class CommLCM extends JaxrLCM {

	public CommLCM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Register a new chat message
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String submitChat(String data) throws Exception {

		/*
		 * Create or retrieve registry package that is 
		 * responsible for managing chat message
		 */
		RegistryPackageImpl container = null;		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		List<RegistryPackageImpl> list = dqm.getRegistryPackage_ByClasNode(ClassificationConstants.FNC_ID_Chat);
		if (list.size() == 0) {
			/*
			 * Create container
			 */
			container = createChatPackage();
			
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
		 * Create ChatObject
		 */
		ChatObject chatObject = new ChatObject(jaxrHandle, this);
		RegistryObjectImpl ro = chatObject.create(data);

    	transaction.addObjectToSave(ro);
		container.addRegistryObject(ro);

		/*
		 * Save objects	
		 */		
		transaction.addObjectToSave(container);
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Get response 
		 */
		JSONObject jResponse = transaction.getJResponse(ro.getId(), FncMessages.CHAT_CREATED);		
		return jResponse.toString();
		
	}

	/**
	 * Register a new mail message
	 * 
	 * @param data
	 * @param attachment
	 * @param mimetype
	 * @return
	 * @throws Exception
	 */
	public String submitMail(String data, InputStream attachment, String mimetype) throws Exception {

		/*
		 * Create or retrieve registry package that is 
		 * responsible for managing mail message
		 */
		RegistryPackageImpl container = null;		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		List<RegistryPackageImpl> list = dqm.getRegistryPackage_ByClasNode(ClassificationConstants.FNC_ID_Mail);
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
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();

		/*
		 * Create MailObject
		 */
		MailObject mailObject = new MailObject(jaxrHandle, this);
		RegistryObjectImpl mailRo = mailObject.create(data);

    	transaction.addObjectToSave(mailRo);
		container.addRegistryObject(mailRo);

		/*
		 * Attachment handling
		 */
		if (attachment != null) {
			
			RegistryObjectImpl attachRo = submitAttachment(attachment, mimetype, transaction);

			JaxrLCM lcm = new JaxrLCM(jaxrHandle);
			
			/*
			 * Associate mail and attachment
			 */
			AssociationImpl a = lcm.createAssociation_RelatedTo(attachRo);
			mailRo.addAssociation(a);
			
			/* 
			 * Identifier
			 */
			String aid = JaxrIdentity.getInstance().getPrefixUID(FncConstants.MAIL_PRE);
			
			a.setLid(aid);
			a.getKey().setId(aid);

			/* 
			 * Name & description
			 */
			a.setName(lcm.createInternationalString("Mail - relatedTo - Attachment"));
			a.setDescription(lcm.createInternationalString("This is a relation between a mail and its attachment."));
				
			/* 
			 * Home url
			 */
			a.setHome(jaxrHandle.getEndpoint().replace("/saml", ""));
			
			/* 
			 * Confirm association
			 */
			lcm.confirmAssociation(a);
			
		}
		
		/*
		 * Save objects	
		 */		
		transaction.addObjectToSave(container);
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Get response 
		 */
		JSONObject jResponse = transaction.getJResponse(mailRo.getId(), FncMessages.MAIL_CREATED);		
		return jResponse.toString();
		
	}
	
	private RegistryObjectImpl submitAttachment(InputStream attachment, String mimetype, JaxrTransaction transaction) throws Exception {

		/*
		 * Create or retrieve registry package that is 
		 * responsible for managing mail attachments
		 */
		RegistryPackageImpl container = null;		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		List<RegistryPackageImpl> list = dqm.getRegistryPackage_ByClasNode(ClassificationConstants.FNC_ID_Attachment);
		if (list.size() == 0) {
			/*
			 * Create container
			 */
			container = createAttachmentPackage();
			
		} else {
			/*
			 * Retrieve container
			 */
			container = list.get(0);

		}

		/*
		 * Create AttachmentObject
		 */
		AttachmentObject attachmentObject = new AttachmentObject(jaxrHandle, this);
		RegistryObjectImpl ro = attachmentObject.create(attachment, mimetype);

    	transaction.addObjectToSave(ro);
		container.addRegistryObject(ro);

		transaction.addObjectToSave(container);

		return ro;
		
	}
	
	/**
	 * A helper method to create a new chat message container
	 * 
	 * @return
	 * @throws JAXRException
	 */
	private RegistryPackageImpl createChatPackage() throws JAXRException  {

		FncParams params = new FncParams();
		
		/*
		 * Name & description
		 */
		params.put(FncParams.K_NAME, "Chat Messages");
		params.put(FncParams.K_DESC, FncMessages.CHAT_DESC);
		
		/*
		 * Prefix
		 */
		params.put(FncParams.K_PRE, FncConstants.CHAT_PRE);
		
		/*
		 * Classification
		 */
		params.put(FncParams.K_CLAS, ClassificationConstants.FNC_ID_Chat);
		
		/*
		 * Create package
		 */
		DomainLCM lcm = new DomainLCM(jaxrHandle);
		return lcm.createBusinessPackage(params);
	
	}

	/**
	 * A helper method to create a new attachment container
	 * 
	 * @return
	 * @throws JAXRException
	 */
	private RegistryPackageImpl createAttachmentPackage() throws JAXRException  {

		FncParams params = new FncParams();
		
		/*
		 * Name & description
		 */
		params.put(FncParams.K_NAME, "Attachments");
		params.put(FncParams.K_DESC, FncMessages.ATTACHMENT_DESC);
		
		/*
		 * Prefix
		 */
		params.put(FncParams.K_PRE, FncConstants.ATTACHMENT_PRE);
		
		/*
		 * Classification
		 */
		params.put(FncParams.K_CLAS, ClassificationConstants.FNC_ID_Attachment);
		
		/*
		 * Create package
		 */
		DomainLCM lcm = new DomainLCM(jaxrHandle);
		return lcm.createBusinessPackage(params);
	
	}	
	
	/**
	 * A helper method to create a new mail message container
	 * 
	 * @return
	 * @throws JAXRException
	 */
	private RegistryPackageImpl createMailPackage() throws JAXRException  {

		FncParams params = new FncParams();
		
		/*
		 * Name & description
		 */
		params.put(FncParams.K_NAME, "Mails");
		params.put(FncParams.K_DESC, FncMessages.MAIL_DESC);
		
		/*
		 * Prefix
		 */
		params.put(FncParams.K_PRE, FncConstants.MAIL_PRE);
		
		/*
		 * Classification
		 */
		params.put(FncParams.K_CLAS, ClassificationConstants.FNC_ID_Mail);
		
		/*
		 * Create package
		 */
		DomainLCM lcm = new DomainLCM(jaxrHandle);
		return lcm.createBusinessPackage(params);
	
	}
}
