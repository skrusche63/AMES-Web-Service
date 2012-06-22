package de.kp.ames.web.core.domain;
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

import java.util.Locale;

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.AssociationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.json.JSONException;

import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;

public class JsonAssociation extends JsonRegistryObject {

    /**
     * Constructor
     * 
     * @param jaxrHandle
     */
    public JsonAssociation(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
     * Convert association into JSON object
     * 
     * @param ro
     * @param locale
     * @return
     * @throws JAXRException 
     * @throws JSONException 
     * @throws Exception
     */
    public void set(RegistryObjectImpl ro, Locale locale) throws JSONException, JAXRException {    
    	
    	/*
    	 * Convert registry object
    	 */
    	super.set(ro, locale);
    	
    	/*
    	 * Convert association specific information
    	 */
    	AssociationImpl as = (AssociationImpl)ro;
    	
    	/*
    	 * Source object
    	 */
    	String soid = as.getSourceObject().getKey().getId();
    	put(JaxrConstants.RIM_SOURCE, soid);
    	
    	/*
    	 * Target object
    	 */
    	String toid = as.getTargetObject().getKey().getId();
    	put(JaxrConstants.RIM_TARGET, toid);
    	
    }

}
