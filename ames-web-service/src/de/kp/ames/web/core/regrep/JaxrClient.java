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

import javax.xml.registry.ConnectionFactory;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryService;

import org.freebxml.omar.client.xml.registry.ConnectionImpl;
import org.freebxml.omar.client.xml.registry.util.JAXRUtility;
import org.freebxml.omar.client.xml.registry.util.ProviderProperties;
import org.opensaml.saml2.core.Assertion;

public class JaxrClient {

	private boolean connected = false;
	private static JaxrClient instance = new JaxrClient();
	
	private JaxrClient() {}
	
	public static JaxrClient getInstance() {
		if (instance == null) instance = new JaxrClient();
		return instance;
	}

    /**
     * @param handle
     * @return
     */
    public boolean logon(JaxrHandle handle) {		
    	return createSecureConnection(handle);     	
    }

	/**
	 * @param handle
	 * @return
	 */
	public boolean logoff(JaxrHandle handle) {
		
		boolean success = true;
		try {

			ConnectionImpl connection = handle.getConnection();
			if (connection == null) return success;

			connection.logoff();
		
		} catch (final JAXRException e) {
    		success = false;

    	} finally {}
    	
		return success;

	}

    /**
     * @param jaxrHandle
     * @return
     */
    private synchronized boolean createSecureConnection(JaxrHandle jaxrHandle) {
		
		String endpoint = jaxrHandle.getEndpoint();
    	
    	try {

            // Create connection
            ConnectionImpl connection = createConnection(endpoint);
     		connection.setLocalCallMode(false);

			jaxrHandle.setConnection(connection);

            RegistryService service = connection.getRegistryService();
            jaxrHandle.setService(service);
            
            // Assign credentials to connection
            HashSet<Assertion> credentials = jaxrHandle.getCredentials();
            if (credentials != null) connection.setCredentials(credentials);
			
            connection.setSynchronous(true);
           
            this.connected = true;
			
            
        } catch (final JAXRException e) {
            this.connected = false;

        }
    	
        return this.connected;
    
    }

    /**
     * This helper method creates a connection object without 
     * any security credentials provided.
     * 
     * @param endpoint
     * @return
     * @throws JAXRException
     */
    public ConnectionImpl createConnection(String endpoint) throws JAXRException {
		
	    ProviderProperties.getInstance().put("javax.xml.registry.queryManagerURL", endpoint);
	    ConnectionFactory connectionFactory = JAXRUtility.getConnectionFactory();
	    
	    return (ConnectionImpl) connectionFactory.createConnection();
	    
	}

}
