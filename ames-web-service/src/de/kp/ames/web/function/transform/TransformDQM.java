package de.kp.ames.web.function.transform;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.transform
 *  Module: TransformDQM
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #dqm #function #transform #web
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

import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.json.JSONArray;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.domain.JsonBusinessProvider;
import de.kp.ames.web.shared.constants.ClassificationConstants;

public class TransformDQM extends JaxrDQM {

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public TransformDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}
	
	public FileUtil getTransformator(String item) throws Exception {
		
		/*
		 * Determine Xsl transformator from the repository
		 * item of the respective registry object
		 */
  		ExtrinsicObjectImpl eo = (ExtrinsicObjectImpl) getRegistryObjectById(item);
		if (eo == null) throw new Exception("[TransformDQM] A transformator with id <" + item + "> does not exist.");

		String mimetype = eo.getMimeType();
		
		DataHandler handler = eo.getRepositoryItem();
		InputStream stream = handler.getInputStream();
		
		FileUtil file = new FileUtil(stream, mimetype);
		return file;

	}
	
	/**
	 * Get either single transformator or all
	 * registered productors
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public JSONArray getTransformators(String item) throws Exception {

		/*
		 * Determine transformators
		 */		
		List<RegistryObjectImpl> productors = getRegistryObjects_ByClasNode(item, ClassificationConstants.FNC_ID_Transformator);

		/*
		 * Build JSON representation
		 */
		return JsonBusinessProvider.getTransformators(jaxrHandle, productors);
		
	}
}
