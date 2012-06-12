package de.kp.ames.web.function.transform;
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

import java.io.InputStream;
import java.util.ArrayList;

import javax.activation.DataHandler;

import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.common.CanonicalSchemes;
import org.json.JSONObject;

import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.core.util.FileUtil;

public class XslConsumer extends JaxrLCM {

	private static String EXTRINSIC_OBJECT = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_ExtrinsicObject;

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public XslConsumer(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Attach XSL result (stream) to the target object
	 * 
	 * @param target
	 * @param stream
	 * @return
	 * @throws Exception
	 */
	public String setRepositoryItem(String target, InputStream stream) throws Exception {
		/* 
		 * Determine target object from unique identifier
		 */
		RegistryObjectImpl ro = getRegistryObjectById(target);
		if (ro == null) throw new Exception("[XslConsumer] Target object not found.");

		String objectType = ro.getObjectType().getKey().getId();	    	
    	if (objectType.startsWith(EXTRINSIC_OBJECT)) {   		
    		/*
    		 * The stream must be allocated to an existing extrinsic object
    		 */
    		ExtrinsicObjectImpl eo = (ExtrinsicObjectImpl)ro;
    		eo.setMimeType(GlobalConstants.MT_XML);

    		/*
    		 * Create and set repository item
    		 */
       		byte[] bytes = FileUtil.getByteArrayFromInputStream(stream);

    		DataHandler handler = new DataHandler(FileUtil.createByteArrayDataSource(bytes, GlobalConstants.MT_XML));                	
	    	eo.setRepositoryItem(handler);				

	    	/*
	    	 * Save extrinsic object
	    	 */
	    	ArrayList<RegistryObjectImpl> objectsToSave = new ArrayList<RegistryObjectImpl>();
	    	objectsToSave.add(eo);
		    	
	    	saveObjects(objectsToSave, false, false);

	    	/*
	    	 * Build response
	    	 */
	    	JSONObject jResponse = new JSONObject();
	    	jResponse.put("uid", target);
	    	
	    	return jResponse.toString();
    		
    	} else {
    		throw new Exception("[XslConsumer] The object type " + objectType +  " is not supported.");
    		
    	}
	
	}
	
}
