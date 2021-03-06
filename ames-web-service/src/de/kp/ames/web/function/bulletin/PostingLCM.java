package de.kp.ames.web.function.bulletin;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.bulletin
 *  Module: PostingLCM
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #bulletin #function #lcm #posting #web
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
import java.util.Locale;

import javax.activation.DataHandler;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.RegistryObject;

import org.freebxml.omar.client.xml.registry.infomodel.AssociationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.json.JSONObject;

import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.domain.DomainLCM;
import de.kp.ames.web.core.reactor.ReactorImpl;
import de.kp.ames.web.core.reactor.ReactorParams;
import de.kp.ames.web.core.reactor.ReactorParams.RAction;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.regrep.JaxrTransaction;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.function.FncMessages;
import de.kp.ames.web.function.FncParams;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.JaxrConstants;

public class PostingLCM extends JaxrLCM {

	/**
	 * Constructor requires jaxrHandle
	 * 
	 * @param jaxrHandle
	 */
	public PostingLCM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Submit comment for a certain posting
	 * 
	 * @param posting
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String submitComment(String posting, String data) throws Exception {

		/*
		 * Create or retrieve registry package that is 
		 * responsible for managing comments
		 */
		RegistryPackageImpl container = null;		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		List<RegistryPackageImpl> list = dqm.getRegistryPackage_ByClasNode(ClassificationConstants.FNC_ID_Comment);
		if (list.size() == 0) {
			/*
			 * Create container
			 */
			container = createCommentPackage();
			
		} else {
			/*
			 * Retrieve container
			 */
			container = list.get(0);

		}

		/* 
		 * A comment is a certain extrinsic object that holds all relevant
		 * and related information in a single JSON repository item
		 */

		ExtrinsicObjectImpl eo = null;
		JaxrTransaction transaction = new JaxrTransaction();

		// create extrinsic object that serves as a container for
		// the respective comment
		eo = createExtrinsicObject();
		if (eo == null) throw new JAXRException();
				
		/* 
		 * Identifier
		 */
		String eid = JaxrIdentity.getInstance().getPrefixUID(FncConstants.COMMENT_PRE);

		/*
		* Name & description using default locale
		*/
		
		JSONObject jPosting = new JSONObject(data);

		String name = "[COMMENT] " + jPosting.getString(JaxrConstants.RIM_NAME);	
		String desc = "[SUBJ] " + jPosting.getString(JaxrConstants.RIM_SUBJECT);

		/* 
		 * Home url
		 */
		String home = jaxrHandle.getEndpoint().replace("/saml", "");
		
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


		/*
		 * Create classification
		 */
		ClassificationImpl c = createClassification(ClassificationConstants.FNC_ID_Comment);
		c.setName(createInternationalString(Locale.US, "Comment Classification"));

		/* 
		 * Associate classification and posting container
		 */
		eo.addClassification(c);

		/* 
		 * Add extrinsic object to container
		 */
		container.addRegistryObject(eo);
				
		/*
		 * Create association
		 */
		String aid = JaxrIdentity.getInstance().getPrefixUID(FncConstants.COMMENT_PRE);
		AssociationImpl a = this.createAssociation_RelatedTo(eo);		
		/*
		 * Set properties
		 */
		setProperties(a, aid, "Posting Link", "This is a directed association between a posting and the respective comment.", home);

		/*
		 * Source object
		 */
		ExtrinsicObjectImpl so = (ExtrinsicObjectImpl)jaxrHandle.getDQM().getRegistryObject(posting);
		
		so.addAssociation(a);
		
		/*
		 * Add association to container
		 */
		container.addRegistryObject(a);

		/*
		 * Save objects
		 */
		confirmAssociation(a);
		
		transaction.addObjectToSave(container);
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Supply reactor
		 */
		ReactorParams reactorParams = new ReactorParams(jaxrHandle, eo, ClassificationConstants.FNC_ID_Comment, RAction.C_INDEX);
		ReactorImpl.onSubmit(reactorParams);

		/*
		 * Retrieve response message
		 */
		JSONObject jResponse = transaction.getJResponse(eid, FncMessages.COMMENT_CREATED);
		return jResponse.toString();
	
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
	public String submitPosting(String recipient, String data) throws Exception {
		
		/*
		 * Create or retrieve registry package that is 
		 * responsible for managing posting
		 */
		RegistryPackageImpl container = null;		
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		List<RegistryPackageImpl> list = dqm.getRegistryPackage_ByClasNode(ClassificationConstants.FNC_ID_Posting);
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
		 * A posting is a certain extrinsic object that holds all relevant
		 * and related information in a single JSON repository item
		 */
		ExtrinsicObjectImpl eo = null;
		JaxrTransaction transaction = new JaxrTransaction();

		/* 
		 * Create extrinsic object that serves as a container for
		 * the respective posting
		 */
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

		/*
		 * Name is the 'name' of the recipient
		 */
		String name = "[POST] " + jPosting.getString(JaxrConstants.RIM_NAME);				
		String desc = "[SUBJ] " + jPosting.getString(JaxrConstants.RIM_SUBJECT);
		
		/* 
		 * Home url
		 */
		String home = jaxrHandle.getEndpoint().replace("/saml", "");
		
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

		/*
		 * Create classification
		 */
		ClassificationImpl c = createClassification(ClassificationConstants.FNC_ID_Posting);
		c.setName(createInternationalString(Locale.US, "Posting Classification"));

		/* 
		 * Associate classification and posting container
		 */
		eo.addClassification(c);

		/* 
		 * Add extrinsic object to container
		 */
		container.addRegistryObject(eo);
				
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
		 * Source object (could be User or Organization)
		 */
		
		RegistryObject so = (RegistryObject)jaxrHandle.getDQM().getRegistryObject(recipient);
		
		so.addAssociation(a);
		
		/*
		 * Add association to container
		 */
		container.addRegistryObject(a);

		/*
		 * Save objects
		 */
		confirmAssociation(a);
		
		/*
		 * Only the registryPackage needs to be added for save
		 * All dependent objects will be added automatically 
		 */
		transaction.addObjectToSave(container);
		saveObjects(transaction.getObjectsToSave(), false, false);

		/*
		 * Supply reactor
		 */
		ReactorParams reactorParams = new ReactorParams(jaxrHandle, eo, ClassificationConstants.FNC_ID_Posting, RAction.C_INDEX);
		ReactorImpl.onSubmit(reactorParams);

		/*
		 * Retrieve response message
		 */
		JSONObject jResponse = transaction.getJResponse(eid, FncMessages.POSTING_CREATED);
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
	 * A helper method to create a new comment container
	 * 
	 * @return
	 * @throws JAXRException
	 */
	private RegistryPackageImpl createCommentPackage() throws JAXRException  {

		FncParams params = new FncParams();
		
		/*
		 * Name & description
		 */
		params.put(FncParams.K_NAME, "Comments");
		params.put(FncParams.K_DESC, FncMessages.COMMENT_DESC);
		
		/*
		 * Prefix
		 */
		params.put(FncParams.K_PRE, FncConstants.COMMENT_PRE);
		
		/*
		 * Classification
		 */
		params.put(FncParams.K_CLAS, ClassificationConstants.FNC_ID_Comment);
		
		/*
		 * Create package
		 */
		DomainLCM lcm = new DomainLCM(jaxrHandle);
		return lcm.createBusinessPackage(params);
	
	}

	
	/**
	 * A helper method to create a new posting container
	 * 
	 * @return
	 * @throws JAXRException
	 */
	private RegistryPackageImpl createPostingPackage() throws JAXRException  {

		FncParams params = new FncParams();
		
		/*
		 * Name & description
		 */
		params.put(FncParams.K_NAME, "Postings");
		params.put(FncParams.K_DESC, FncMessages.POSTING_DESC);
		
		/*
		 * Prefix
		 */
		params.put(FncParams.K_PRE, FncConstants.POSTING_PRE);
		
		/*
		 * Classification
		 */
		params.put(FncParams.K_CLAS, ClassificationConstants.FNC_ID_Posting);
		
		/*
		 * Create package
		 */
		DomainLCM lcm = new DomainLCM(jaxrHandle);
		return lcm.createBusinessPackage(params);
	
	}
	
}
