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

import java.util.List;

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.freebxml.omar.client.xml.registry.infomodel.SlotImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.shared.ClassificationConstants;

public class NamespaceObject extends BusinessObject {

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 * @param jaxrLCM
	 */
	public NamespaceObject(JaxrHandle jaxrHandle, JaxrLCM jaxrLCM) {
		super(jaxrHandle, jaxrLCM);
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
	public RegistryObjectImpl create(JSONObject jForm) throws Exception {

		/* 
		 * Create registry package 
		 */
		String name = jForm.getString(RIM_NAME);

		// 
		RegistryPackageImpl rp = jaxrLCM.createRegistryPackage(jaxrLCM.createInternationalString(name));
		if (rp == null) throw new JAXRException("[NamespaceObject] Creation of RegistryPackage failed.");

		/* 
		 * Identifier
		 */
		String rid = JaxrIdentity.getInstance().getPrefixUID(FncConstants.FOLDER_PRE);

		rp.setLid(rid);
		rp.getKey().setId(rid);

		/* 
		 * Home url
		 */
		String home = jaxrHandle.getEndpoint().replace("/saml", "");
		rp.setHome(home);

		/*
		 * Description
		 */
		String desc = jForm.getString(RIM_DESC);
		rp.setDescription(jaxrLCM.createInternationalString(desc));

		/*
		 * Classifications
		 */
		JSONArray jClases = jForm.has(RIM_CLAS) ? new JSONArray(jForm.getString(RIM_CLAS)) : null;
		if (jClases != null) {
			
			List<ClassificationImpl> classifications = createClassifications(jClases);			
			/*
			 * Set composed object
			 */
			rp.addClassifications(classifications);
			
		}
		
		ClassificationImpl classification = jaxrLCM.createClassification(ClassificationConstants.FNC_ID_Folder);
		rp.addClassification(classification);
		
		/* 
		 * Slots
		 */
		JSONObject jSlots = jForm.has(RIM_SLOT) ? new JSONObject(jForm.getString(RIM_SLOT)) : null;
		if (jSlots != null) {

			List<SlotImpl> slots = createSlots(jSlots);
			/*
			 * Set composed object
			 */
			rp.addSlots(slots);
		
		}
		
		/*
		 * Indicate as created
		 */
		this.created = true;

		return rp;
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
	public RegistryObjectImpl update(JSONObject jForm) throws Exception {

		/* 
		 * Determine namespaces from unique identifier
		 */
		String nid = jForm.getString(RIM_ID);
		
		RegistryPackageImpl folder = (RegistryPackageImpl)jaxrLCM.getRegistryObjectById(nid);
		if (folder == null) throw new Exception("[NamespaceObject] RegistryObject with id <" + nid + "> does not exist.");
	
		/*
		 * Update metadata
		 */
		updateMetadata(folder, jForm);
		
		/*
		 * Indicate as updated
		 */
		this.created = false;
		
		return folder;
		
	}

}
