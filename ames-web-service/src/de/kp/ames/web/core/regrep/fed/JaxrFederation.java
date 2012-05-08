package de.kp.ames.web.core.regrep.fed;
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
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.registry.JAXRException;
import javax.xml.registry.Query;
import javax.xml.registry.RegistryService;

import org.freebxml.omar.client.xml.registry.BulkResponseImpl;
import org.freebxml.omar.client.xml.registry.BusinessLifeCycleManagerImpl;
import org.freebxml.omar.client.xml.registry.ConnectionImpl;
import org.freebxml.omar.client.xml.registry.DeclarativeQueryManagerImpl;
import org.freebxml.omar.client.xml.registry.infomodel.FederationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectRef;
import org.freebxml.omar.common.BindingUtility;
import org.opensaml.saml2.core.Assertion;

import de.kp.ames.web.Bundle;
import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.sql.JaxrSQL;

/**
 * JaxrFederation supports federation handling for
 * a SAML v2.0 assertion based security mechanism,
 * i.e. that the caller's user is automatically
 * created on the remote RegRep due to his assertion
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */
public class JaxrFederation {

	/*
	 * Reference to caller's JaxrHandle
	 */
	private JaxrHandle jaxrHandle;

	public JaxrFederation(JaxrHandle jaxrHandle) {
		/*
		 * Register JaxrHandle of caller's user 
		 */
		this.jaxrHandle = jaxrHandle;

	}

	/**
	 * Add registry reference to remote federation instance
	 * that is hosted on a central Federation Registry
	 * 
	 * @param fid
	 * @param registryRef
	 * @throws JAXRException
	 */
	public void joinFederation(String fid, RegistryObjectRef registryRef) throws JAXRException {

		String endpoint = Bundle.getInstance().getString(GlobalConstants.FEDREP_ENDPOINT);
		joinFederation(fid, registryRef, endpoint);
	
	}

	/**
	 * Add registry reference to remote federation instance
	 * 
	 * @param registryRef
	 * @param endpoint (Federation Registry)
	 * @param fid
	 * @throws JAXRException
	 */
	public void joinFederation(String fid, RegistryObjectRef registryRef, String endpoint) throws JAXRException {

		/* 
		 * Establish connection to the remote registry and
		 * derive according declarative query manager
		 */
        ConnectionImpl connection = JaxrClient.getInstance().createConnection(endpoint);

        /* 
         * Assign SAML v2.0 assertion to remote connection
         */
        HashSet<Assertion> credentials = jaxrHandle.getCredentials();
        if (credentials == null) return;
        
        connection.setCredentials(credentials);            

        RegistryService service = connection.getRegistryService();

        DeclarativeQueryManagerImpl dqm   = (DeclarativeQueryManagerImpl)service.getDeclarativeQueryManager();
        BusinessLifeCycleManagerImpl blcm = (BusinessLifeCycleManagerImpl)service.getBusinessLifeCycleManager();

        /* 
         * Determine federation by unique identifier
         */
		FederationImpl federation = (FederationImpl)dqm.getRegistryObject(fid);
		federation.join(registryRef);

        /*
         * Version control is not supported
         * for this use case
         */
        HashMap<String,String> slotsMap = new HashMap<String,String>();
        
        slotsMap.put(BindingUtility.CANONICAL_SLOT_LCM_DONT_VERSION, "true");
        slotsMap.put(BindingUtility.CANONICAL_SLOT_LCM_DONT_VERSION_CONTENT, "true");

        /*
         * Save modified federation instance
         */
        ArrayList<Object> objectsToSave = new ArrayList<Object>();
        objectsToSave.add(federation);

        blcm.saveObjects(objectsToSave, slotsMap);

        /*
         * Logoff from remote connection
         */
        connection.logoff();       
 
	}

	/**
	 * Remove registry reference from remote federation instance
	 * that is hosted on a central Federation Registry
	 * 
	 * @param fid
	 * @param registryRef
	 * @throws JAXRException
	 */
	public void leavFederation(String fid, String rid) throws JAXRException {

		String endpoint = Bundle.getInstance().getString(GlobalConstants.FEDREP_ENDPOINT);
		leaveFederation(fid, rid, endpoint);
	
	}

	/**
	 * Remove registry reference from remote federation instance
	 * 
	 * @param fid
	 * @param rid
	 * @param endpoint
	 * @throws JAXRException
	 */
	public void leaveFederation(String fid, String rid, String endpoint) throws JAXRException {

		/* 
		 * Establish connection to the remote registry and
		 * derive according declarative query manager
		 */
        ConnectionImpl connection = JaxrClient.getInstance().createConnection(endpoint);

        /* 
         * Assign SAML v2.0 assertion to remote connection
         */
        HashSet<Assertion> credentials = jaxrHandle.getCredentials();
        if (credentials == null) return;
        
        connection.setCredentials(credentials);            

        RegistryService service = connection.getRegistryService();

        DeclarativeQueryManagerImpl dqm   = (DeclarativeQueryManagerImpl)service.getDeclarativeQueryManager();
        BusinessLifeCycleManagerImpl blcm = (BusinessLifeCycleManagerImpl)service.getBusinessLifeCycleManager();

        /* 
         * Determine federation by unique identifier
         */
		FederationImpl federation = (FederationImpl)dqm.getRegistryObject(fid);

		/*
		 * Determine registry by unique identifier
		 */
		RegistryImpl registry = (RegistryImpl) dqm.getRegistryObject(rid);

		/* 
		 * Remove registry from federation
		 */
		federation.leave(registry);

        /*
         * Version control is not supported
         * for this use case
         */
	    HashMap<String,String> slotsMap = new HashMap<String,String>();
        
        slotsMap.put(BindingUtility.CANONICAL_SLOT_LCM_DONT_VERSION, "true");
        slotsMap.put(BindingUtility.CANONICAL_SLOT_LCM_DONT_VERSION_CONTENT, "true");

        /*
         * Save modified federation instance
         */
        ArrayList<Object> objectsToSave = new ArrayList<Object>();
        objectsToSave.add(federation);

        blcm.saveObjects(objectsToSave, slotsMap);

        /*
         * Logoff from remote connection
         */
        connection.logoff();       

	}

	
	/**
	 * This method is used to retrieve a single reference of a 
	 * registry instance from a potential federate described by 
	 * its endpoint.
	 * 
	 * @param endpoint
	 * @return
	 * @throws JAXRException
	 */
	public RegistryObjectRef getRegistryRef(String endpoint) throws JAXRException {

		RegistryObjectRef registryRef = null;
		
		/* 
		 * Establish connection to the remote registry and
		 * derive according declarative query manager
		 */
        ConnectionImpl connection = JaxrClient.getInstance().createConnection(endpoint);

        /* 
         * Assign SAML v2.0 assertion to remote connection
         */
        HashSet<Assertion> credentials = jaxrHandle.getCredentials();
        if (credentials == null) return null;
        
        connection.setCredentials(credentials);            

        /*
         * Retrieve declarative query manager from
         * remote connection
         */
        RegistryService service = connection.getRegistryService();
        DeclarativeQueryManagerImpl dqm = (DeclarativeQueryManagerImpl)service.getDeclarativeQueryManager();

        String request = JaxrSQL.getSQLRegistries_All();
		Query query = dqm.createQuery(Query.QUERY_TYPE_SQL, request);

		BulkResponseImpl bulkResponse = (BulkResponseImpl) dqm.executeQuery(query);
		Collection<?> registries = bulkResponse.getCollection();

		if (registries.isEmpty() == false) {

			/*
			 * Retrieve the FIRST remote registry instance
			 * and build object reference
			 */
			RegistryImpl registry = (RegistryImpl)registries.iterator().next();
		   
		   /* 
		    * Create object reference
		    */
		   BusinessLifeCycleManagerImpl blcm = (BusinessLifeCycleManagerImpl)service.getBusinessLifeCycleManager();
		   registryRef = new RegistryObjectRef(blcm, registry);

		}
		
		/*
		 * Logoff from remote connection
		 */
        connection.logoff();   
        
        /*
         * Return ObjectRef for remote registry instance
         */
 		return registryRef;
		
	}

}
