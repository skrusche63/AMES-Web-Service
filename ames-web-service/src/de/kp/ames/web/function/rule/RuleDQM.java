package de.kp.ames.web.function.rule;
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

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.json.JSONArray;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.function.domain.JsonBusinessProvider;
import de.kp.ames.web.shared.constants.ClassificationConstants;

public class RuleDQM extends JaxrDQM {
	
	public RuleDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}
	
	/**
	 * Retrieve either a specific (single) evaluation or
	 * all registered evaluations
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public JSONArray getEvaluations(String item) throws Exception {

		/*
		 * Determine evaluations
		 */
		
		List<RegistryObjectImpl> evaluations = getRegistryObjects_ByClasNode(item, ClassificationConstants.FNC_ID_Evaluation);

		/*
		 * Build JSON representation
		 */
		return JsonBusinessProvider.getEvaluations(jaxrHandle, evaluations);

	}

	/**
	 * Retrieve either a specific (single) reasoner or
	 * all registered reasoners
	 * 
	 * @param item
	 * @return
	 */
	public JSONArray getReasoners(String item) throws Exception {

		/*
		 * Determine reasoners
		 */
		
		List<RegistryObjectImpl> reasoners = getRegistryObjects_ByClasNode(item, ClassificationConstants.FNC_ID_Reasoner);

		/*
		 * Build JSON representation
		 */
		return JsonBusinessProvider.getReasoners(jaxrHandle, reasoners);
	
	}

}
