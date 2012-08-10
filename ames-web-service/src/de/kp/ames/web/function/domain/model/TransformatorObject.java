package de.kp.ames.web.function.domain.model;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.domain.model
 *  Module: TransformatorObject
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #domain #function #model #object #transformator #web
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

import javax.activation.DataHandler;
import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.function.FncMessages;
import de.kp.ames.web.function.transform.cache.XslCacheManager;
import de.kp.ames.web.function.transform.cache.XslTransformator;
import de.kp.ames.web.shared.constants.JsonConstants;

public class TransformatorObject extends BusinessObject {

	/**
	 * Constructor
	 * 
	 * @param handle
	 * @param jaxrLCM
	 */
	public TransformatorObject(JaxrHandle handle, JaxrLCM jaxrLCM) {
		super(handle, jaxrLCM);
	}
	
	/**
	 * Create RegistryObject representation of TransformatorObject
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public RegistryObjectImpl create(String data) throws Exception {
	
		/*
		 * Initialize data
		 */
		JSONObject jForm = new JSONObject(data);

		/* 
		 * Create extrinsic object that serves as a wrapper 
		 * for the respective transformator
		 */
		// 
		ExtrinsicObjectImpl eo = jaxrLCM.createExtrinsicObject();
		if (eo == null) throw new JAXRException("[TransformatorObject] Creation of ExtrinsicObject failed.");
		
		/* 
		 * Identifier
		 */
		String eid = JaxrIdentity.getInstance().getPrefixUID(FncConstants.TRANSFORMATOR_PRE);

		eo.setLid(eid);
		eo.getKey().setId(eid);

		/* 
		 * Home url
		 */
		String home = jaxrHandle.getEndpoint().replace("/saml", "");
		eo.setHome(home);

		/* 
		 * The transformator is actually transient and managed by the xslt processor;
		 * to register the respective xslt file, we have to invoke the transient
		 * cache and get the file
		 */
		
		XslCacheManager cacheManager = XslCacheManager.getInstance();
		
		String key = jForm.getString(JsonConstants.J_KEY);
		XslTransformator transformator = (XslTransformator)cacheManager.getFromCache(key);
		
		if (transformator == null) throw new Exception("[TransformatorObject] XSL Transformator with id <" + key + "> not found.");
		
		/*
		 * Name & description
		 */
		String name = jForm.has(RIM_NAME) ? jForm.getString(RIM_NAME) : null;
		String desc = jForm.has(RIM_DESC) ? jForm.getString(RIM_DESC) : null;

		name = (name == null) ? transformator.getName() : name;
		
		int pos = name.lastIndexOf(".");
		if (pos != -1) name = name.substring(0,pos);
		
		eo.setName(jaxrLCM.createInternationalString(name));
		
		desc = (desc == null) ? FncMessages.NO_DESCRIPTION_DESC : desc;
		eo.setDescription(jaxrLCM.createInternationalString(desc));

		/*
		 * Classifications
		 */
		JSONArray jClases = jForm.has(RIM_CLAS) ? new JSONArray(jForm.getString(RIM_CLAS)) : null;
		if (jClases != null) {
			
			List<ClassificationImpl> classifications = createClassifications(jClases);			
			/*
			 * Set composed object
			 */
			eo.addClassifications(classifications);
			
		}

		/*
		 * Mimetype & repository item
		 */
		String mimetype = transformator.getMimetype();				
		DataHandler handler = new DataHandler(FileUtil.createByteArrayDataSource(transformator.getBytes(), mimetype));                	

		eo.setMimeType(mimetype);
		eo.setRepositoryItem(handler);				

		/*
		 * Indicate as created
		 */
		this.created = true;

		return eo;
		
	}
		
}
