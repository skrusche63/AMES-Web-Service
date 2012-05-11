package de.kp.ames.web.core.regrep;
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

import java.util.HashSet;

import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryService;

import org.freebxml.omar.client.xml.registry.BusinessLifeCycleManagerImpl;
import org.freebxml.omar.client.xml.registry.ConnectionImpl;
import org.freebxml.omar.client.xml.registry.DeclarativeQueryManagerImpl;
import org.opensaml.saml2.core.Assertion;

import de.kp.ames.web.core.util.SamlUtil;

public class JaxrHandle {
	
	/*
	 * Endpoint of the OASIS ebXML RegRep
	 */
	protected String endpoint;

	/* 
	 * SAML v2.0 User Assertions
	 */
	protected HashSet<Assertion> assertions = new HashSet<Assertion>();	   
	
	/*
	 * JAXR 1.0 ConnectionImpl
	 */
	protected ConnectionImpl connection = null;
	
	/*
	 * JAXR 1.0 RegistryService
	 */
	protected RegistryService service = null;

	public JaxrHandle() {		
	}
	
	
	/**
	 * Constructor that requires the endpoint of the
	 * OASIS ebXML RegRep associated as storage backend
	 * 
	 * @param endpoint
	 */
	public JaxrHandle(String endpoint) {
		this.endpoint = endpoint;
	}
	
	/**
	 * @param assertion
	 */
	public void setCredential(Assertion assertion) {
		this.assertions.add(assertion);
	}
	
	/**
	 * @return
	 */
	public Assertion getAssertion() {
		if (assertions.size() == 0) return null;
		return (Assertion)this.assertions.toArray()[0];
	}
	
	/**
	 * @param endpoint
	 */
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	/**
	 * @return
	 */
	public String getEndpoint() {
		return this.endpoint;
	}
	
	/**
	 * @return
	 */
	public HashSet<Assertion> getCredentials() {
		return this.assertions;
	}
	
	/**
	 * Retrieve unique identifier of caller's user
	 * from the associated SAML v2.0 assertion
	 * 
	 * @return
	 */
	public String getUser() {
		
		Assertion assertion = getAssertion();
		if (assertion == null) return null;
		
		String uid = SamlUtil.getUser(assertion);
		return uid;
		
	}
	
	/**
	 * @param service
	 */
	public void setService(RegistryService service) {
		this.service = service;
	}

	/**
	 * @param connection
	 */
	public void setConnection(ConnectionImpl connection) {
		this.connection = connection;
	}
	
	/**
	 * @return
	 */
	public ConnectionImpl getConnection() {
		return this.connection;
	}
	
    /**
     * Return Business LCM from registry service
     * 
     * @return
     * @throws JAXRException
     */
    public BusinessLifeCycleManagerImpl getBLCM() throws JAXRException {

		RegistryService service = this.service;
		if (service == null) service = this.connection.getRegistryService();
		
        return (BusinessLifeCycleManagerImpl)service.getBusinessLifeCycleManager();

    }

    /**
     * Return Declarative QM from registry service
     * 
     * @return
     * @throws JAXRException
     */
    public DeclarativeQueryManagerImpl getDQM() throws JAXRException {

		RegistryService service = this.service;
		if (service == null) service = this.connection.getRegistryService();

		return (DeclarativeQueryManagerImpl)service.getDeclarativeQueryManager();
 
    }

}
