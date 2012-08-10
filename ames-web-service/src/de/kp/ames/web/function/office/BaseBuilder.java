package de.kp.ames.web.function.office;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.office
 *  Module: BaseBuilder
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #base #builder #function #office #web
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;

import de.kp.ames.office.OOImage;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;

public class BaseBuilder {

	protected JaxrHandle jaxrHandle;

	/**
	 * A helper method to retrieve a list of images
	 * from an OASIS ebXML RegRep
	 * 
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	protected Map<String, OOImage> getOOImages(List<String> ids) {
		
		Map<String, OOImage> images = new HashMap<String, OOImage>();
		
		try {
			
			for (String id:ids) {
				
				OOImage image = getOOImage(id);
				if (image == null) continue;
				
				images.put(id, image);
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			
		} finally {}
		
		return images;

	}
	
	/**
	 * A helper method to create an OOImage from an ExtrinsicObject
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	protected OOImage getOOImage(String uid) throws Exception {

		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		RegistryObjectImpl ro = dqm.getRegistryObjectById(uid);
		if (ro == null) return null;
		
		ExtrinsicObjectImpl eo = (ExtrinsicObjectImpl)ro;
		DataHandler handler = eo.getRepositoryItem();

		if (handler == null) return null;

		return new OOImage(handler.getInputStream(), eo.getMimeType());
		
	}
	
}
