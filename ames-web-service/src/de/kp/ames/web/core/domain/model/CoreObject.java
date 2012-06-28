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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.SlotImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.json.JsonUtil;
import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;

public class CoreObject implements ICoreObject {

	/*
	 * Reference to JaxrLCM
	 */
	protected JaxrLCM jaxrLCM;
	
	/*
	 * Reference to JaxrHandle
	 */
	protected JaxrHandle jaxrHandle;

	/*
	 * Flag to indicate whether this a 
	 * new object
	 */
	protected boolean created = true;
	
	/*
	 * Core Object
	 */
	protected static String RIM_PRE = JaxrConstants.RIM_PRE;
	
	protected static String RIM_CLAS  = JaxrConstants.RIM_CLAS;
	protected static String RIM_DATE  = JaxrConstants.RIM_DATE;
	protected static String RIM_DESC  = JaxrConstants.RIM_DESC;
	protected static String RIM_ID    = JaxrConstants.RIM_ID;
	protected static String RIM_NAME  = JaxrConstants.RIM_NAME;
	protected static String RIM_SLOT  = JaxrConstants.RIM_SLOT;

	public CoreObject(JaxrHandle jaxrHandle, JaxrLCM jaxrLCM) {
	
		this.jaxrLCM = jaxrLCM;
		this.jaxrHandle = jaxrHandle;

	}

	/**
	 * Submit RegistryObject
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
	 * Create RegistryObject from JSON representation
	 * 
	 * @param jForm
	 * @return
	 * @throws Exception
	 */
	public RegistryObjectImpl create(JSONObject jForm) throws Exception {
		/*
		 * Must be overridden
		 */
		return null;
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
		 * Must be overridden
		 */
		return null;
		
	}

	/**
	 * @return
	 */
	public boolean isCreated() {
		return created;
	}
	
	/**
	 * Create classifications from a JSONArray
	 * 
	 * @param jClases
	 * @return
	 * @throws Exception
	 */
	protected List<ClassificationImpl> createClassifications(JSONArray jClases) throws Exception {
	
		ArrayList<String> conceptTypes = JsonUtil.getStringArray(jClases);
		return jaxrLCM.createClassifications(conceptTypes);
		
	}

	/**
	 * Create slots from JSONObject
	 * 
	 * @param jSlots
	 * @return
	 * @throws Exception
	 */
	protected List<SlotImpl> createSlots(JSONObject jSlots) throws Exception {
		
		ArrayList<SlotImpl> slots = new ArrayList<SlotImpl>();
		
		@SuppressWarnings("unchecked")
		Iterator<String> keys = jSlots.keys();
		while (keys.hasNext()) {

			String key = keys.next();
			String value = jSlots.getString(key);
			
			/*
			 * Create slot
			 */
			SlotImpl slot = jaxrLCM.createSlot(key, value, JaxrConstants.SLOT_TYPE);
			slots.add(slot);

		}

		return slots;
		
	}

	/**
	 * Update slots for existing RegistryObject from JSONObject
	 * 
	 * @param ro
	 * @param jSlots
	 * @return
	 * @throws Exception
	 */
	public List<SlotImpl> updateSlots(RegistryObjectImpl ro, JSONObject jSlots) throws Exception {

		/*
		 * Reference to slots that MUST be created
		 */
		ArrayList<SlotImpl> slots = new ArrayList<SlotImpl>();

		@SuppressWarnings("unchecked")
		Iterator<String> keys = jSlots.keys();
		while (keys.hasNext()) {

			String key = keys.next();
			String value = jSlots.getString(key);

			SlotImpl slot = (SlotImpl)ro.getSlot(key);
			if (slot == null) {
				
				slot = jaxrLCM.createSlot(key, value, JaxrConstants.SLOT_TYPE);
				slots.add(slot);
				
			} else {
				
				Collection<String> values = new ArrayList<String>();
				values.add(value);

				slot.setValues(values);

			}
			
		}

		return slots;
		
	}
}
