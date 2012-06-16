package de.kp.ames.web.core.reactor;

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;

import de.kp.ames.web.core.regrep.JaxrHandle;

public class ReactorParams {

	public enum RAction {
		C_INDEX, 
		D_INDEX,
		
		C_RSS,
		D_RSS,
		
		C_INDEX_RSS
		
	};
	
	private RAction action;
	
	private String domain;
	
	/*
	 * Reference to RegistryObject
	 */
	private RegistryObjectImpl ro;

	/*
	 * Reference to JaxrHandle
	 */
	private JaxrHandle jaxrHandle;
	
	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 * @param ro
	 * @param action
	 */
	public ReactorParams(JaxrHandle jaxrHandle, RegistryObjectImpl ro, RAction action) {
		this(jaxrHandle, ro, null, action);
	}

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 * @param ro
	 * @param domain
	 * @param action
	 */
	public ReactorParams(JaxrHandle jaxrHandle, RegistryObjectImpl ro, String domain, RAction action) {		
		/* 
		 * Register action
		 */
		this.action = action;
		
		/* 
		 * Register domain and object
		 */
		this.ro = ro;
		this.domain = domain;
		
	}
	
	/**
	 * @return
	 */
	public JaxrHandle getJaxrHandle() {
		return this.jaxrHandle;
	}
	
	/**
	 * @return
	 */
	public RegistryObjectImpl getRegistryObject() {
		return this.ro;
	}
	
	public String getDomain() {
		return this.domain;
	}
	
	public RAction getAction() {
		return this.action;
	}
}
