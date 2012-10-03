package de.kp.ames.web.function.user;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.user
 *  Module: UserLCM
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #function #lcm #user #web
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

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.json.JSONObject;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrTransaction;
import de.kp.ames.web.core.regrep.lcm.PartyLCM;
import de.kp.ames.web.function.FncMessages;
import de.kp.ames.web.function.domain.model.UserObject;

public class UserLCM extends PartyLCM {

	public UserLCM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}
	
	public String submitUser(String data) throws Exception {
		
		/*
		 * Initialize transaction
		 */
		JaxrTransaction transaction = new JaxrTransaction();
	
		
		/*
		 * Create UserObject
		 */
		UserObject UserObject = new UserObject(jaxrHandle, this);
		RegistryObjectImpl ro = UserObject.submit(data);

		/*
		 * Save objects (without versioning)
		 */
		transaction.addObjectToSave(ro);
		saveObjects(transaction.getObjectsToSave(), false, false);
	
		/*
		 * Retrieve response message
		 */
		JSONObject jResponse = transaction.getJResponse(ro.getId(), FncMessages.USER_UPDATED);
		return jResponse.toString();
		
	}


}
