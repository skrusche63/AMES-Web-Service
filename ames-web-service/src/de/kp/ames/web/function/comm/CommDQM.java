package de.kp.ames.web.function.comm;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.comm
 *  Module: CommDQM
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #comm #dqm #function #web
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

import javax.activation.DataHandler;
import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.domain.JsonBusinessProvider;
import de.kp.ames.web.shared.constants.ClassificationConstants;

public class CommDQM extends JaxrDQM {

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public CommDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Get either single chat message or all registered
	 * products
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public JSONArray getChatMessages(String item) throws Exception {

		if (item == null) {
			/*
			 * Determine chat messages (metadata)
			 */
			List<RegistryObjectImpl> messages = getRegistryObjects_ByClasNode(item, ClassificationConstants.FNC_ID_Chat);
	
			/*
			 * Build JSON representation
			 */
			return JsonBusinessProvider.getChatMessages(jaxrHandle, messages);
			
		} else {
			/*
			 * Determine content of chat message
			 */
			ExtrinsicObjectImpl eo = (ExtrinsicObjectImpl)getRegistryObjectById(item);
			if (eo == null) throw new JAXRException("[CommDQM] RegistryObject with id <" + item + "> not found.");
			
			DataHandler handler = eo.getRepositoryItem();
			InputStream stream = handler.getInputStream();
			
			byte[] bytes = FileUtil.getByteArrayFromInputStream(stream);
			
			JSONObject jChatMessage = new JSONObject(new String(bytes));
			return new JSONArray().put(jChatMessage);
			
		}
		
	}

	/**
	 * Get either single mail message or all registered
	 * products
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public JSONArray getMailMessages(String item) throws Exception {

		if (item == null) {
			/*
			 * Determine mail messages (metadata)
			 */
			List<RegistryObjectImpl> messages = getRegistryObjects_ByClasNode(item, ClassificationConstants.FNC_ID_Mail);
	
			/*
			 * Build JSON representation
			 */
			return JsonBusinessProvider.getMailMessages(jaxrHandle, messages);
			
		} else {
			/*
			 * Determine content of mail message
			 */
			ExtrinsicObjectImpl eo = (ExtrinsicObjectImpl)getRegistryObjectById(item);
			if (eo == null) throw new JAXRException("[CommDQM] RegistryObject with id <" + item + "> not found.");
			
			DataHandler handler = eo.getRepositoryItem();
			InputStream stream = handler.getInputStream();
			
			byte[] bytes = FileUtil.getByteArrayFromInputStream(stream);
			
			JSONObject jMailMessage = new JSONObject(new String(bytes));
			return new JSONArray().put(jMailMessage);
			
		}
		
	}

}
