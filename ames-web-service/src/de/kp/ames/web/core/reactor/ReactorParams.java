package de.kp.ames.web.core.reactor;

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;

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
	private RegistryObjectImpl ro;

	public ReactorParams(RegistryObjectImpl ro, RAction action) {
		this(ro, null, action);
	}

	public ReactorParams(RegistryObjectImpl ro, String domain, RAction action) {
		
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
	
	public RegistryObjectImpl getRO() {
		return this.ro;
	}
	
	public String getDomain() {
		return this.domain;
	}
	
	public RAction getAction() {
		return this.action;
	}
}
