package de.kp.ames.web.function.domain.model;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.domain.model
 *  Module: JsonEvaluation
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #domain #evaluation #function #json #model #web
 * </SemanticAssist>
 *
 */

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

import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.common.CanonicalSchemes;
import org.json.JSONException;
import org.json.JSONObject;

import de.kp.ames.web.core.domain.model.JsonExtrinsicObject;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.shared.constants.JaxrConstants;

public class JsonEvaluation extends JsonExtrinsicObject {

	private static String REGISTRY_PACKAGE = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_RegistryPackage;
	private static String SERVICE          = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_Service;

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public JsonEvaluation(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

    /* (non-Javadoc)
     * @see de.kp.ames.web.core.domain.JsonExtrinsicObject#set(org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl, java.util.Locale)
     */
    public void set(RegistryObjectImpl ro, Locale locale) throws JSONException, JAXRException {    
    	
    	/*
    	 * Convert registry object
    	 */
    	super.set(ro, locale);
    	
    	/*
    	 * Convert evaluation specific information
    	 */
    	ExtrinsicObjectImpl evaluation = (ExtrinsicObjectImpl)ro;

		Object[] associatives = evaluation.getAssociatedObjects().toArray();

		int card = associatives.length;			
		if (card != 2) throw new JAXRException("[JsonEvaluation] An evaluation must have exactly two associations.");
		
		for (int ix=0; ix < card; ix++) {
			
			RegistryObjectImpl associative = (RegistryObjectImpl)associatives[ix];
			
			String associativeId   = associative.getId();
			
			String associativeName = jaxrBase.getName(associative);
			String associativeDesc = jaxrBase.getDescription(associative);
			
			String associativeType = associative.getObjectType().getKey().getId();
			
			if (associativeType.equals(REGISTRY_PACKAGE)) {
				
				/*
				 * Evaluation source (fact base)
				 */
				JSONObject jBase = new JSONObject();

				jBase.put(JaxrConstants.RIM_ID,   associativeId);
				jBase.put(JaxrConstants.RIM_NAME, associativeName);
				jBase.put(JaxrConstants.RIM_DESC, associativeDesc);

				put(JaxrConstants.RIM_BASE,     jBase.toString());

			} else if (associativeType.equals(SERVICE)) {
				
				/*
				 * Evaluation reasoner
				 */				
				JSONObject jReasoner = new JSONObject();
				
				jReasoner.put(JaxrConstants.RIM_ID,   associativeId);
				jReasoner.put(JaxrConstants.RIM_NAME, associativeName);
				jReasoner.put(JaxrConstants.RIM_DESC, associativeDesc);

				put(JaxrConstants.RIM_REASONER, jReasoner.toString());

			}

		}
	
    }
}
