package de.kp.ames.web.core.domain;

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

}
