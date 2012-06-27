package de.kp.ames.web.function.dms;
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

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

import javax.activation.DataHandler;
import javax.imageio.ImageIO;

import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.json.JSONArray;

import de.kp.ames.web.core.graphics.GraphicsUtil;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.dms.cache.DmsDocument;
import de.kp.ames.web.function.dms.cache.DmsImage;
import de.kp.ames.web.function.dms.cache.DocumentCacheManager;
import de.kp.ames.web.function.dms.cache.ImageCacheManager;
import de.kp.ames.web.function.domain.DomainJsonProvider;
import de.kp.ames.web.shared.ClassificationConstants;

public class DmsDQM extends JaxrDQM {

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public DmsDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Get either single documents or all
	 * registered documents
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public JSONArray getDocuments(String item) throws Exception {

		/*
		 * Determine documents
		 */		
		List<RegistryObjectImpl> documents = getRegistryObjects_ByClasNode(item, ClassificationConstants.FNC_ID_Document);

		/*
		 * Build JSON representation
		 */
		return DomainJsonProvider.getDocuments(jaxrHandle, documents);
		
	}

	/**
	 * Get either single images or all
	 * registered images
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public JSONArray getImages(String item) throws Exception {

		/*
		 * Determine images
		 */		
		List<RegistryObjectImpl> images = getRegistryObjects_ByClasNode(item, ClassificationConstants.FNC_ID_Image);

		/*
		 * Build JSON representation
		 */
		return DomainJsonProvider.getImages(jaxrHandle, images);
		
	}

	/**
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public BufferedImage getImage(String item) throws Exception {
		
		ImageCacheManager cacheManager = ImageCacheManager.getInstance();
		DmsImage cacheEntry = (DmsImage)cacheManager.getFromCache(item);
		
		BufferedImage image = null;
		if (cacheEntry == null) {

	  		ExtrinsicObjectImpl eo = (ExtrinsicObjectImpl) getRegistryObjectById(item);
			if (eo == null) throw new Exception("[DmsDQM] An image with id <" + item + "> does not exist.");

			String mimetype = eo.getMimeType();
			if ((mimetype == null) || mimetype.startsWith("image") == false) {
		
				DataHandler handler = eo.getRepositoryItem();
				image = ImageIO.read(handler.getInputStream());
				
			}
			
		} else {
			
			image = cacheEntry.getImage();
			
		}
		
		return GraphicsUtil.createSource(image);

	}
	
	/**
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public FileUtil getDocument(String item) throws Exception {
		
		DocumentCacheManager cacheManager = DocumentCacheManager.getInstance();
		DmsDocument cacheEntry = (DmsDocument)cacheManager.getFromCache(item);

		FileUtil file = null;
		if (cacheEntry == null) {
			
	  		ExtrinsicObjectImpl eo = (ExtrinsicObjectImpl) getRegistryObjectById(item);
			if (eo == null) throw new Exception("[DmsDQM] A document with id <" + item + "> does not exist.");

			String mimetype = eo.getMimeType();
			
			DataHandler handler = eo.getRepositoryItem();
			InputStream stream = handler.getInputStream();
			
			file = new FileUtil(stream, mimetype);

		} else {			
			file = cacheEntry.asFile();
			
		}
		
		return file;
	}
	
}
