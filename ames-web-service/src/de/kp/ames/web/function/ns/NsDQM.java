package de.kp.ames.web.function.ns;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.ns
 *  Module: NsDQM
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #dqm #function #ns #web
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

import java.util.ArrayList;
import java.util.List;

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.json.JSONArray;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.function.domain.JsonBusinessProvider;
import de.kp.ames.web.shared.constants.ClassificationConstants;

public class NsDQM extends JaxrDQM {

	public NsDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Get either single namespace or all registered
	 * namespaces of a certain parent
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public JSONArray getNamespaces(String item, String parent) throws Exception {

		List<RegistryObjectImpl> folders = new ArrayList<RegistryObjectImpl>();

		if (item == null) {
			
			if (parent == null) {

				List<RegistryPackageImpl> list = getRegistryPackage_ByClasNode(ClassificationConstants.FNC_ID_Namespace);
				if (list.size() == 0) throw new JAXRException("[NsDQM] Namespace container does not exist.");

				RegistryPackageImpl container = list.get(0);
				parent = container.getId();
				
			} 

			/*
			 * Retrieve all packages that are members of a parent package
			 */
			folders = getPackageMembers(parent);
			
		} else {

			/*
			 * Determine folder
			 */
			RegistryPackageImpl folder = (RegistryPackageImpl)getRegistryObjectById(item);
			if (folder == null) throw new JAXRException("[NsDQM] RegistryObject with id <" + item + "> not found.");
			
			folders.add(folder);
			
		}
		
		/*
		 * Build JSON representation
		 */
		return JsonBusinessProvider.getNamespaces(jaxrHandle, folders);
		
	}

}
