package de.kp.ames.web.function.access;
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
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.function.domain.DomainJsonProvider;

public class AccessDQM extends JaxrDQM {

	public AccessDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
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
		List<RegistryObjectImpl> accessors = getRegistryObjects_ByClasNode(item, FncConstants.FNC_ID_Accessor);

		/*
		 * Build JSON representation
		 */
		return DomainJsonProvider.getProductors(jaxrHandle, accessors);

	}

}
