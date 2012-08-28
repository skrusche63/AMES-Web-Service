package de.kp.ames.web.function.security;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.security
 *  Module: SecurityServiceImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #function #security #service #web
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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.activation.DataHandler;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.registry.JAXRException;
import javax.xml.registry.UnsupportedCapabilityException;

import org.freebxml.omar.client.xml.registry.infomodel.AssociationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.UserImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.regrep.JaxrTransaction;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.function.FncSQL;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.JsonConstants;
import de.kp.ames.web.shared.constants.MethodConstants;

public class SecurityServiceImpl extends BusinessImpl {

	/*
	 * A predefined user that must be adapted after a respective
	 * role system has been established
	 */
	private static String USER_ROLE = "Registered User";
	
	/**
	 * Constructor
	 */
	public SecurityServiceImpl() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.core.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(MethodConstants.METH_GET)) {			
			/*
			 * Call get method
			 */
			doGetRequest(ctx);

		} else if (methodName.equals(MethodConstants.METH_SET)) {
			/*
			 * Call set method
			 */
			doSetRequest(ctx);
			
		}

	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doGetRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doGetRequest(RequestContext ctx) {

		String type = this.method.getAttribute(MethodConstants.ATTR_TYPE);	
		
		if (type == null) {
			this.sendNotImplemented(ctx);

		} else {
			
			if (type.equals(ClassificationConstants.FNC_SECURITY_ID_App)) {
			
				/*
				 * Get apps description for the callers user
				 */
				try {
					
					String content = getUsersApps(ctx);
					this.sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			} else if (type.equals(ClassificationConstants.FNC_SECURITY_ID_Safe)) {

				/*
				 * Get credentials for a certain service
				 */
				String service = this.method.getAttribute(MethodConstants.ATTR_SERVICE);
				if (service == null) {
					this.sendNotImplemented(ctx);
					
				} else {
					
					try {
						String content = getCredentials(service);
						this.sendJSONResponse(content, ctx.getResponse());

					} catch (Exception e) {
						this.sendBadRequest(ctx, e);

					}
					
				}
				
			}

		}

	}
		
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doSetRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doSetRequest(RequestContext ctx) {

		String service = this.method.getAttribute(MethodConstants.ATTR_SERVICE);
		String data    = this.getRequestData(ctx);

		if ((service == null) || (data == null)) {
			this.sendNotImplemented(ctx);
			
		} else {

			try {
				String content = set(service, data);
				this.sendJSONResponse(content, ctx.getResponse());

			} catch (Exception e) {
				this.sendBadRequest(ctx, e);

			}
			
		}
		
	}

	/**
	 * Retrieve password safe associated with the caller's user
	 * 
	 * @param service
	 * @return
	 * @throws Exception
	 */
	private String getCredentials(String service) throws Exception {
		
		/*
		 * Retrieve caller's unique identifier
		 */
		String uid = jaxrHandle.getUser();
		if (uid == null) {
			/*
			 * Return empty JSON object
			 */
			return new JSONObject().toString();

		}
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		String sqlString = FncSQL.getSQLAssertions_Safe(uid);
		List<AssociationImpl> as = dqm.getAssociationsByQuery(sqlString);
		
		if (as.size() == 0) {
			/*
			 * Logoff
			 */
			JaxrClient.getInstance().logoff(jaxrHandle);

			/*
			 * Return empty JSON object
			 */
			return new JSONObject().toString();
			
		}

		ExtrinsicObjectImpl safe = (ExtrinsicObjectImpl)as.get(0).getTargetObject();
		DataHandler handler = safe.getRepositoryItem();
		
		if (handler == null) {
			/*
			 * Logoff
			 */
			JaxrClient.getInstance().logoff(jaxrHandle);

			/*
			 * Return empty JSON object
			 */
			return new JSONObject().toString();
			
		}

		byte[] bytes = FileUtil.getByteArrayFromInputStream(handler.getInputStream());
    	JSONObject jSafe = new JSONObject(new String(bytes));

    	if (jSafe.has(service) == false) {
			/*
			 * Logoff
			 */
			JaxrClient.getInstance().logoff(jaxrHandle);

			/*
			 * Return empty JSON object
			 */
			return new JSONObject().toString();

    	}

    	String content = jSafe.getJSONObject(service).toString();

		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		
		return content;

	}

	/**
	 * This method retrieves the user & his
	 * registered and accessible apps
	 * 
	 * @param ctx
	 * @return
	 * @throws Exception
	 */
	private String getUsersApps(RequestContext ctx) throws Exception {
		
		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

		/*
		 * Retrieve caller's unique identifier
		 */
		String uid = jaxrHandle.getUser();
		if (uid == null) {
			/*
			 * Return empty JSON object
			 */
			content = new JSONObject().toString();

		} else {
			
			JSONObject jResponse = new JSONObject();
			
			JaxrDQM dqm = new JaxrDQM(jaxrHandle);
			UserImpl user = (UserImpl)dqm.getRegistryObjectById(uid);

			/*
			 * Retrieve user name & role
			 * 
			 * __FUTURE__ Roles & Rights
			 * 
			 */
			String name = dqm.getUserFullName(user);
			String role = USER_ROLE;
			
			JSONObject jUser = new JSONObject();
			jUser.put("id", uid);
			
			jUser.put("name", name);
			jUser.put("role", role);
			
			jResponse.put("user", jUser);
			
			/*
			 * Retrieve default apps
			 * 
			 * __FUTURE__ Marketplace
			 * 
			 */
			JSONArray jApps = getDefaultApps(ctx);
			jResponse.put("apps", jApps);
			
			content = jResponse.toString();
			
		}

		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		
		return content;

	}
	
	/**
	 * A helper method to retrieve the registered apps
	 * for the caller's user
	 * 
	 * @param uid
	 * @return
	 */
	@SuppressWarnings("unused")
	private JSONArray getRegisteredApps(String uid) {
		/*
		 * This method must be adapted after a marketplace
		 * is implemented and associated with this framework.
		 */
		
		return new JSONArray();
	
	}
	
	/**
	 * A helper method to retrieve a set of default AND
	 * user independent set of application descriptions
	 * 
	 * @param ctx
	 * @return
	 * @throws Exception
	 */
	private JSONArray getDefaultApps(RequestContext ctx) throws Exception {

		ServletContext context = ctx.getContext();
		
		String filename = "/WEB-INF/resources/defaultapps.xml";		  
		InputStream is = context.getResourceAsStream(filename);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);

		JSONArray jDefaultApps = new JSONArray();
		
		try {
			
			Document xmlDoc = factory.newDocumentBuilder().parse(is);
			NodeList apps = xmlDoc.getElementsByTagName("app");

			for (int i=0; i < apps.getLength(); i++) {
				
				Element eService = (Element)apps.item(i);
				JSONObject jApp = getAppParams(eService);
				
				if (jApp == null) continue;
				
				jDefaultApps.put(jApp);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}

		return jDefaultApps;

	}
	
	/**
	 * A helper method to describe a W3C dom element
	 * in a JSON representation
	 * 
	 * @param eApp
	 * @return
	 * @throws Exception
	 */
	private JSONObject getAppParams(Element eApp) throws Exception {
		
		String id   = "";
		String name = "";
		String icon = "";
		
		NodeList properties = eApp.getChildNodes();
		for (int i=0; i < properties.getLength(); i++) {
			
			Element property = (Element)properties.item(i);

			if (property.getTagName().equals("app-id")) {
				id = property.getTextContent().trim();
			}

			if (property.getTagName().equals("app-name")) {
				name = property.getTextContent().trim();
			}

			if (property.getTagName().equals("app-icon")) {
				icon = property.getTextContent().trim();
			}

		}

		if (id.equals("") || name.equals("") || icon.equals("")) return null;
		
		JSONObject jApp = new JSONObject();
		jApp.put(JsonConstants.J_ID, id);
		
		jApp.put(JsonConstants.J_NAME, name);
		jApp.put(JsonConstants.J_ICON, icon);
		
		return jApp;

	}
	
	/**
	 * Create or update password safe for caller's user
	 * 
	 * @param service
	 * @param creds
	 * @return
	 * @throws Exception
	 */
	private String set(String service, String creds) throws Exception {

		/*
		 * Retrieve caller's unique identifier
		 */
		String uid = jaxrHandle.getUser();
		if (uid == null) {
			/*
			 * Return empty JSON object
			 */
			return new JSONObject().toString();

		}

		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		String sqlString = FncSQL.getSQLAssertions_Safe(uid);
		List<AssociationImpl> as = dqm.getAssociationsByQuery(sqlString);

		if (as.size() == 0) {
			/*
			 * The password safe does not exist for the caller's user
			 */
			UserImpl user = (UserImpl) jaxrHandle.getDQM().getRegistryObject(uid);
			createSafe(service, creds, user);
			
		} else {
			/*
			 * The password safe already exists
			 */
			ExtrinsicObjectImpl safe = (ExtrinsicObjectImpl)as.get(0).getTargetObject();
			updateSafe(service, creds, safe);
		}

		String content = new JSONObject().toString();

		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		
		return content;

	}

	/**
	 * A helper method to create a new password safe
	 * 
	 * @param service
	 * @param creds
	 * @param user
	 * @throws JAXRException
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 */
	private void createSafe(String service, String creds, UserImpl user) throws JAXRException, JSONException, UnsupportedEncodingException {
		
		JaxrTransaction transaction = new JaxrTransaction();
		
		/* 
		 * Create credentials as JSON object
		 */
		JSONObject jCredentials = new JSONObject(creds);

		String rimName = null;
		String rimDesc = null;
		String rimHome = null;
		
		JSONObject jSafe = new JSONObject();				
		jSafe.put(service, jCredentials);
		
		byte[] bytes = jSafe.toString().getBytes("UTF-8");
		DataHandler handler = new DataHandler(FileUtil.createByteArrayDataSource(bytes, "application/json"));                	

		/*
		 * Create extrinsic object 
		 */
		JaxrLCM lcm = new JaxrLCM(jaxrHandle);
		ExtrinsicObjectImpl eo = lcm.createExtrinsicObject();
		
		/* 
		 * Identifier
		 */
		String eoid = JaxrIdentity.getInstance().getPrefixUID(FncConstants.SECURITY_PRE);
		
		eo.setLid(eoid);
		eo.getKey().setId(eoid);

		/* 
		 * Name & description
		 */
		rimName = "Password Safe";
		eo.setName(lcm.createInternationalString(rimName));
		
		rimDesc = "This is an auto-generated password safe";
		eo.setDescription(lcm.createInternationalString(rimDesc));
			
		/* 
		 * Home url
		 */
		rimHome = jaxrHandle.getEndpoint().replace("/saml", "");
		eo.setHome(rimHome);

		/* 
		 * Mimetype & repository item
		 */
		eo.setMimeType("application/json");
		eo.setRepositoryItem(handler);				
		
		/*
		 * Make sure that the registry object is processed
		 * right before any references to this object are 
		 * made (e.g. classifications, external links)
		 */
		transaction.addObjectToSave(eo);
		
		/*
		 * Create classification
		 */
		ClassificationImpl c = lcm.createClassification(ClassificationConstants.FNC_SECURITY_ID_Safe);
		c.setName(lcm.createInternationalString("Security Classification"));

		/* 
		 * Associate classification and password safe
		 */
		eo.addClassification(c);
		transaction.addObjectToSave(c);				

		/*
		 * Associate user and safe instance
		 */
		AssociationImpl a = lcm.createAssociation_RelatedTo(eo);
		user.addAssociation(a);
		
		/* 
		 * Identifier
		 */
		String aid = JaxrIdentity.getInstance().getPrefixUID(FncConstants.SECURITY_PRE);
		
		a.setLid(aid);
		a.getKey().setId(aid);

		/* 
		 * Name & description
		 */
		rimName = "User - relatedTo - Safe";
		a.setName(lcm.createInternationalString(rimName));
	
		rimDesc = "This is a relation between a registry user and his assigned password safe";
		a.setDescription(lcm.createInternationalString(rimDesc));
			
		/* 
		 * Home url
		 */
		a.setHome(rimHome);

		transaction.addObjectToSave(a);
		
		/* 
		 * Confirm association
		 */
		lcm.confirmAssociation(a);
		lcm.saveObjects(transaction.getObjectsToSave(), false, false);

	}
	
	/**
	 * A helper method to update an existing password safe
	 * 
	 * @param service
	 * @param creds
	 * @param safe
	 * @throws JSONException 
	 * @throws JAXRException 
	 * @throws UnsupportedCapabilityException 
	 * @throws IOException 
	 */
	private void updateSafe(String service, String creds, ExtrinsicObjectImpl safe) throws JSONException, UnsupportedCapabilityException, JAXRException, IOException {
		
		JaxrTransaction transaction = new JaxrTransaction();
		/* 
		 * Create credentials as JSON object
		 */
		JSONObject jCredentials = new JSONObject(creds);
		
		DataHandler handler = safe.getRepositoryItem();
		if (handler == null) return;

    	byte[] bytes = FileUtil.getByteArrayFromInputStream(handler.getInputStream());
    	JSONObject jSafe = new JSONObject(new String(bytes));

		/* 
		 * Update password safe
		 */
		jSafe.put(service, jCredentials);
		
		bytes = jSafe.toString().getBytes("UTF-8");
		handler = new DataHandler(FileUtil.createByteArrayDataSource(bytes, "application/json"));                	

		safe.setRepositoryItem(handler);
		transaction.addObjectToSave(safe);
		
		JaxrLCM lcm = new JaxrLCM(jaxrHandle);
		lcm.saveObjects(transaction.getObjectsToSave(), false, false);
		
	}
		
}
