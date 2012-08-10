package de.kp.ames.web.core.domain;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.domain
 *  Module: JsonCoreProvider
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #domain #json #provider #web
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

import org.freebxml.omar.client.xml.registry.infomodel.ExternalLinkImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.OrganizationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceImpl;
import org.freebxml.omar.client.xml.registry.infomodel.UserImpl;
import org.json.JSONObject;

import de.kp.ames.web.core.domain.model.JsonExternalLink;
import de.kp.ames.web.core.domain.model.JsonExtrinsicObject;
import de.kp.ames.web.core.domain.model.JsonOrganization;
import de.kp.ames.web.core.domain.model.JsonRefObject;
import de.kp.ames.web.core.domain.model.JsonRegistryObject;
import de.kp.ames.web.core.domain.model.JsonService;
import de.kp.ames.web.core.domain.model.JsonUser;
import de.kp.ames.web.core.regrep.JaxrHandle;

public class JsonCoreProvider {

	/**
	 * A helper method to convert an ExternalLink
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param externalLink
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getExternalLink(JaxrHandle jaxrHandle, ExternalLinkImpl externalLink) throws Exception {
		
		JsonExternalLink jExternalLink = new JsonExternalLink(jaxrHandle);
		jExternalLink.set(externalLink);

		return jExternalLink.get();
	
	}

	/**
	 * A helper method to convert an ExtrinsicObject
	 * into a JSON representation
	 *  
	 * @param jaxrHandle
	 * @param extrinsicObject
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getExtrinsicObject(JaxrHandle jaxrHandle, ExtrinsicObjectImpl extrinsicObject) throws Exception {

		JsonExtrinsicObject jExtrinsicObject = new JsonExtrinsicObject(jaxrHandle);
		jExtrinsicObject.set(extrinsicObject);

		return jExtrinsicObject.get();

	}

	/**
	 * A helper method to convert an Organization
	 * into a JSON representation
	 *  
	 * @param jaxrHandle
	 * @param organization
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getOrganization(JaxrHandle jaxrHandle, OrganizationImpl organization) throws Exception {
		
		JsonOrganization jOrganization = new JsonOrganization(jaxrHandle);
		jOrganization.set(organization);
		
		return jOrganization.get();

	}

	/**
	 * A helper method to convert a RegistryObject
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param registryObject
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getRegistryObject(JaxrHandle jaxrHandle, RegistryObjectImpl registryObject) throws Exception {
		
		JsonRegistryObject jRegistryObject = new JsonRegistryObject(jaxrHandle);
		jRegistryObject.set(registryObject);
		
		return jRegistryObject.get();
		
	}

	/**
	 * A helper method to convert a Service
	 * into a JSON representation
	 * 
	 * 
	 * @param jaxrHandle
	 * @param service
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getService(JaxrHandle jaxrHandle, ServiceImpl service) throws Exception {
		
		JsonService jService = new JsonService(jaxrHandle);
		jService.set(service);
		
		return jService.get();

	}

	/**
	 * A helper method to convert a RegistryObject
	 * into an optimized (short) JSON representation
	 * 
	 * @param jaxrHandle
	 * @param registryObject
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getRefObject(JaxrHandle jaxrHandle, RegistryObjectImpl registryObject) throws Exception {
		
		JsonRefObject jRefObject = new JsonRefObject(jaxrHandle);
		jRefObject.set(registryObject);
		
		return jRefObject.get();

	}
	
	/**
	 * A helper method to convert a User
	 * into a JSON representation
	 * 
	 * @param jaxrHandle
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getUser(JaxrHandle jaxrHandle, UserImpl user) throws Exception {
		
		JsonUser jUser = new JsonUser(jaxrHandle);
		jUser.set(user);
		
		return jUser.get();
		
	}
	
}
