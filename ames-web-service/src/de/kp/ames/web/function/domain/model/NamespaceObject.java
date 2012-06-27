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

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;

public class NamespaceObject extends BusinessObject {

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 * @param lcm
	 */
	public NamespaceObject(JaxrHandle jaxrHandle, JaxrLCM lcm) {
		super(jaxrHandle, lcm);
	}

	/**
	 * Submit NamespaceObject
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public RegistryObjectImpl submit(String data) throws Exception {

		/*
		 * Initialize data
		 */
		JSONObject jForm = new JSONObject(data);
		
		/*
		 * Unique identifier
		 */
		String item = jForm.has(RIM_ID) ? jForm.getString(RIM_ID) : null;
		if (item == null) {
			return create(jForm);
		
		} else {
			return update(jForm);
		}
		
	}

	/**
	 * Create NamespaceObject
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public RegistryObjectImpl create(String data) throws Exception {

		/*
		 * Initialize data
		 */
		JSONObject jForm = new JSONObject(data);

		/*
		 * Create NamespaceObject
		 */
		return create(jForm);

	}

	/**
	 * Create NamespaceObject from JSON representation
	 * 
	 * @param jForm
	 * @return
	 * @throws Exception
	 */
	private RegistryObjectImpl create(JSONObject jForm) throws Exception {
		// TODO
		return null;
	}

	/**
	 * Update NamespaceObject
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public RegistryObjectImpl update(String data) throws Exception {

		/*
		 * Initialize data
		 */
		JSONObject jForm = new JSONObject(data);

		/*
		 * Update NamespaceObject
		 */
		return update(jForm);

	}

	/**
	 * Update NamespaceObject from JSON representation
	 * 
	 * @param jForm
	 * @return
	 * @throws Exception
	 */
	private RegistryObjectImpl update(JSONObject jForm) throws Exception {

		/* 
		 * Determine service from unique identifier
		 */
		String nid = jForm.getString(RIM_ID);
		
		RegistryPackageImpl folder = (RegistryPackageImpl)lcm.getRegistryObjectById(nid);
		if (folder == null) throw new Exception("[NamespaceObject] RegistryObject with id <" + nid + "> does not exist.");
	
		// TODO
		return null;
		
	}

}
