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

import java.util.Locale;

import javax.activation.DataHandler;
import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.shared.ClassificationConstants;

public class MailObject extends BusinessObject {

	public MailObject(JaxrHandle jaxrHandle, JaxrLCM jaxrLCM) {
		super(jaxrHandle, jaxrLCM);		
	}
	
	/**
	 * Create RegistryObject representation of MailObject
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public RegistryObjectImpl create(String data) throws Exception {

		/* 
		 * A chat message is a certain extrinsic object that holds all 
		 * relevant and related information in a single JSON repository item
		 */

		ExtrinsicObjectImpl eo = null;

		/* 
		 * Create extrinsic object that serves as a container for
		 * the respective mail message
		 */
		eo = jaxrLCM.createExtrinsicObject();
		if (eo == null)  throw new JAXRException("[MailObject] Creation of ExtrinsicObject failed.");
				
		/* 
		 * Identifier
		 */
		String eid = JaxrIdentity.getInstance().getPrefixUID(FncConstants.MAIL_PRE);

		eo.setLid(eid);
		eo.getKey().setId(eid);
		
		/* 
		 * Home url
		 */
		String home = jaxrHandle.getEndpoint().replace("/saml", "");
		eo.setHome(home);

		/* 
		 * Mime type & handler
		 */
		String mimetype = GlobalConstants.MT_JSON;
		eo.setMimeType(mimetype);
				
		byte[] bytes = data.getBytes(GlobalConstants.UTF_8);

		DataHandler handler = new DataHandler(FileUtil.createByteArrayDataSource(bytes, mimetype));                	
    	eo.setRepositoryItem(handler);				

		/*
		 * Create classification
		 */
		ClassificationImpl c = jaxrLCM.createClassification(ClassificationConstants.FNC_ID_Chat);
		c.setName(jaxrLCM.createInternationalString(Locale.US, "Chat Classification"));

		/* 
		 * Associate classification and mail message
		 */
		eo.addClassification(c);

		/*
		 * Indicate as created
		 */
		this.created = true;

		return eo;
		
	}

}
