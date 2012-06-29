package de.kp.ames.web.core.domain.model;
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

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.ExternalLinkImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.function.FncConstants;

public class ExternalLink extends CoreObject {

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 * @param jaxrLCM
	 */
	public ExternalLink(JaxrHandle jaxrHandle, JaxrLCM jaxrLCM) {
		super(jaxrHandle, jaxrLCM);
	}

	/**
	 * Create RegistryObject from JSON representation
	 * 
	 * @param jForm
	 * @return
	 * @throws Exception
	 */
	public RegistryObjectImpl create(JSONObject jForm) throws Exception {

		/*
		 * Create external link
		 */
		String uri = jForm.getString(RIM_URI);
		
		ExternalLinkImpl el = jaxrLCM.createExternalLink(uri);
		if (el == null) throw new JAXRException("[ExternalLink] Creation of ExternalLink failed.");

		/*
		 * Create metadata
		 */
		createMetadata(el, jForm, FncConstants.CORE_PRE);
		
		/*
		 * Indicate as created
		 */
		this.created = true;

		return el;

	}

	/**
	 * Update RegistryObject from JSON representation
	 * 
	 * @param jForm
	 * @return
	 * @throws Exception
	 */
	public RegistryObjectImpl update(JSONObject jForm) throws Exception {	

		/* 
		 * Determine external link from unique identifier
		 */
		String rid = jForm.getString(RIM_ID);
		
		ExternalLinkImpl el = (ExternalLinkImpl)jaxrLCM.getRegistryObjectById(rid);
		if (el == null) throw new Exception("[ExternalLink] RegistryObject with id <" + rid + "> does not exist.");
	
		/*
		 * Update metadata
		 */
		updateMetadata(el, jForm);
		
		/*
		 * Indicate as updated
		 */
		this.created = false;
		
		return el;
		
	}
	
}
