package de.kp.ames.web.core.vocab;
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

import java.util.List;

import org.freebxml.omar.client.xml.registry.infomodel.ConceptImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.json.StringCollector;
import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;

public class VocabDQM extends JaxrDQM {

	/*
	 * Registry object
	 */
	private static String RIM_ID   = JaxrConstants.RIM_ID;
	private static String RIM_NAME = JaxrConstants.RIM_NAME;

	public VocabDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * A helper method to retrieve all subordinate classification
	 * nodes that refer to a certain parent node as a sort list
	 * in a JSON representation
	 * 
	 * @param conceptType
	 * @return
	 * @throws Exception
	 */
	public JSONArray getConceptsByParent(String conceptType) throws Exception { 
		
		StringCollector collector = new StringCollector();
		
		List<ConceptImpl> concepts = getClassificationNodes_ByParent(conceptType);
		if (concepts == null) return new JSONArray();
		
		for (ConceptImpl concept:concepts) {

			String id   = concept.getId();
			String name = concept.getDisplayName();
			
			JSONObject jCategory = new JSONObject();

			jCategory.put(RIM_ID, id);
			jCategory.put(RIM_NAME, name);
				
			collector.put(name, jCategory);

		}
		
		return new JSONArray(collector.values());		
		
	}

}
