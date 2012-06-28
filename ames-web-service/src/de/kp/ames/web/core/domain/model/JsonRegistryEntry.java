package de.kp.ames.web.core.domain.model;

import java.util.List;

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.SlotImpl;
import org.json.JSONException;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrBase;
import de.kp.ames.web.core.regrep.JaxrHandle;

public class JsonRegistryEntry extends JSONObject {
	
	/*
	 * Reference to caller's JaxrHandle
	 */
	protected JaxrHandle jaxrHandle;
	
	/*
	 * Reference to Jaxr Base Functionality
	 */
	protected JaxrBase jaxrBase;
	
	/**
	 * Constructor requires JaxrHandle
	 * 
	 * @param jaxrHandle
	 */
	public JsonRegistryEntry(JaxrHandle jaxrHandle) {
		/*
		 * Register JaxrHandle of caller's user 
		 */
		this.jaxrHandle = jaxrHandle;
		
		/*
		 * Build accessor to Jaxr base functionality
		 */
		this.jaxrBase = new JaxrBase(jaxrHandle);
		
	}
	
    /**
     * Get JSON representation from RegistryEntry
     * 
     * @return
     */
    public JSONObject get() {   
    	return (JSONObject)this;
    }


    /**
     * A helper method to retrieve the slots of a
     * certain registry object as a JSONObject
     * 
     * @param ro
     * @return
     * @throws JAXRException
     * @throws JSONException
     */
    protected JSONObject getSlots(RegistryObjectImpl ro) throws JAXRException, JSONException {

    	JSONObject jSlot = new JSONObject();
    	
    	List<SlotImpl> slots = jaxrBase.getSlotList(ro);
    	for (SlotImpl slot:slots) {

			String name = slot.getName();				
			
			/*
			 * A single slot value is supported
			 */
			Object[] values = slot.getValues().toArray();
			String value = (values.length > 0) ? (String)values[0] : "";

			jSlot.put(name, value);
			
    	}
    	
    	return jSlot;
    	
    }
}
