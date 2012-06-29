package de.kp.ames.web.function.domain.model;
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
import de.kp.ames.web.function.dms.cache.DmsImage;
import de.kp.ames.web.function.dms.cache.ImageCacheManager;

public class ImageObject extends BusinessObject {

	public ImageObject(JaxrHandle jaxrHandle, JaxrLCM lcm) {
		super(jaxrHandle, lcm);
	}

	/**
	 * Create RegistryObject representation of ImageObject
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public RegistryObjectImpl create(JSONObject jForm) throws Exception {

		/* 
		 * Create extrinsic object that serves as a wrapper 
		 * for the respective image
		 */
		// 
		ExtrinsicObjectImpl eo = jaxrLCM.createExtrinsicObject();
		if (eo == null) throw new JAXRException("[ImageObject] Creation of ExtrinsicObject failed.");
		
		/* 
		 * Identifier
		 */
		String eid = JaxrIdentity.getInstance().getPrefixUID(FncConstants.IMAGE_PRE);

		eo.setLid(eid);
		eo.getKey().setId(eid);

		/* 
		 * Home url
		 */
		String home = jaxrHandle.getEndpoint().replace("/soap", "");
		eo.setHome(home);

		/* 
		 * The document is actually transient and managed by the image cache
		 */
		
		ImageCacheManager cacheManager = ImageCacheManager.getInstance();
		
		String key = jForm.getString(RIM_ID);
		DmsImage image = (DmsImage)cacheManager.getFromCache(key);
		
		if (image == null) throw new Exception("[ImageObject] Image with id <" + key + "> not found.");
		
		/*
		 * Name & description
		 */
		String name = jForm.has(RIM_NAME) ? jForm.getString(RIM_NAME) : null;
		String desc = jForm.has(RIM_DESC) ? jForm.getString(RIM_DESC) : null;

		name = (name == null) ? image.getName() : name;
		
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
		String mimetype = image.getMimetype();				
		DataHandler handler = new DataHandler(FileUtil.createByteArrayDataSource(image.getBytes(), mimetype));                	

		eo.setMimeType(mimetype);
		eo.setRepositoryItem(handler);				

		/*
		 * Indicate as created
		 */
		this.created = true;

		return eo;
		
	}

}
