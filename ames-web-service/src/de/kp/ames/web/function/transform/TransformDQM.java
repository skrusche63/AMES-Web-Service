package de.kp.ames.web.function.transform;
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
import org.json.JSONArray;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.function.domain.JsonBusinessProvider;
import de.kp.ames.web.shared.ClassificationConstants;

public class TransformDQM extends JaxrDQM {

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public TransformDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
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
