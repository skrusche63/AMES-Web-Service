package de.kp.ames.web.function.domain.model;
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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceBindingImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceImpl;
import org.freebxml.omar.client.xml.registry.infomodel.SpecificationLinkImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.kp.ames.web.core.domain.JsonService;
import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;

public class JsonReasoner extends JsonService {

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public JsonReasoner(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

    /* (non-Javadoc)
     * @see de.kp.ames.web.core.domain.JsonRegistryObject#set(org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl, java.util.Locale)
     */
    public void set(RegistryObjectImpl ro, Locale locale) throws JSONException, JAXRException {    
    	
    	/*
    	 * Convert registry object
    	 */
    	super.set(ro, locale);
    	
    	/*
    	 * Convert reasoner specific information
    	 */
    	ServiceImpl reasoner = (ServiceImpl)ro;

    	/*
    	 * Convert specifications
    	 */
    	JSONArray jArray = getSpecifications(reasoner);
    	put(JaxrConstants.RIM_SPEC, jArray.toString());

    }
 
    /**
     * A helper method to retrieve the transformators
     * from a service object
     * 
     * @param service
     * @return
     * @throws JAXRException
     * @throws JSONException
     */
    private JSONArray getSpecifications(ServiceImpl service) throws JAXRException, JSONException {
    	
		Map<Integer, JSONObject> collector = new TreeMap<Integer, JSONObject>(new Comparator<Integer>(){
			public int compare(Integer seqno1, Integer seqno2) {
				return seqno1.compareTo(seqno2);
			}
		});

		/* 
    	 * Specifications
    	 */
    	Collection<?> bindings = service.getServiceBindings();
    	if ((bindings == null) || (bindings.size() == 0)) return new JSONArray();

    	/*
    	 * Take the first binding of the respective service into account
    	 */
    	ServiceBindingImpl binding = (ServiceBindingImpl) bindings.toArray()[0];

    	/* 
    	 * Next the specification links of the respective binding are determined
    	 */
    	Collection<?> specs = binding.getSpecificationLinks();
    	if ((specs == null) || (specs.size() == 0)) return new JSONArray();

    	Iterator<?> iterator = specs.iterator();
    	while (iterator.hasNext()) {
    		
    		SpecificationLinkImpl spec = (SpecificationLinkImpl) iterator.next();
    		
    		Object[] values = spec.getSlot(JaxrConstants.SLOT_SEQNO).getValues().toArray();
    		int seqNo = Integer.parseInt((String)values[0]);
    		
    		RegistryObjectImpl ro = (RegistryObjectImpl)spec.getSpecificationObject();

    		JSONObject jTransformator = new JSONObject();
    		
    		jTransformator.put(JaxrConstants.RIM_ID, ro.getId());
    		jTransformator.put(JaxrConstants.RIM_NAME, jaxrBase.getName(ro));
    			
    		collector.put(seqNo, jTransformator);
    		
    	}
    	
    	return new JSONArray(collector.values());
    	
    }
}
