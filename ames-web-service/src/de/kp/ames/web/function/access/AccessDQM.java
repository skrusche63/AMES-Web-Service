package de.kp.ames.web.function.access;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.access
 *  Module: AccessDQM
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #access #dqm #function #web
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

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.function.domain.JsonBusinessProvider;
import de.kp.ames.web.shared.constants.ClassificationConstants;

public class AccessDQM extends JaxrDQM {

	public AccessDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Retrieve single accessor 
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public JSONObject getAccessor(String item) throws Exception {
		
		/*
		 * Determine accessor
		 */
		ServiceImpl accessor = (ServiceImpl)getRegistryObjectById(item);
		if (accessor == null) throw new Exception("[AccessDQM] Accessor with id <" + item + "> does not exist.");
		
		return JsonBusinessProvider.getAccessor(jaxrHandle, accessor);
		
	}
	
	/**
	 * Get either single accessor or all
	 * registered accessors
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public JSONArray getAccessors(String item) throws Exception {

		/*
		 * Determine accessors
		 */		
		List<RegistryObjectImpl> accessors = getRegistryObjects_ByClasNode(item, ClassificationConstants.FNC_ID_Accessor);

		/*
		 * Build JSON representation
		 */
		return JsonBusinessProvider.getAccessors(jaxrHandle, accessors);

	}

}
