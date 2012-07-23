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

import java.io.InputStream;
import javax.activation.DataHandler;
import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.AssociationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.json.JSONObject;

import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.regrep.lcm.JaxrLCM;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.shared.constants.ClassificationConstants;

public class EvaluationObject extends BusinessObject {

	public EvaluationObject(JaxrHandle jaxrHandle, JaxrLCM jaxrLCM) {
		super(jaxrHandle, jaxrLCM);
	}

	/**
	 * Create EvaluationObject
	 * 
	 * @param source
	 * @param reasoner
	 * @param data
	 * @param stream
	 * @return
	 * @throws Exception
	 */
	public RegistryObjectImpl create(String source, String reasoner, String data, InputStream stream) throws Exception {
		
		/*
		 * Initialize data
		 */
		JSONObject jForm = new JSONObject(data);
		
		/* 
		 * Create extrinsic object that serves as a wrapper 
		 * for the respective evaluation
		 */
		// 
		ExtrinsicObjectImpl eo = jaxrLCM.createExtrinsicObject();
		if (eo == null) throw new JAXRException("[EvaluationObject] Creation of ExtrinsicObject failed.");

		/* 
		 * Identifier
		 */
		String eid = JaxrIdentity.getInstance().getPrefixUID(FncConstants.EVALUATION_PRE);

		eo.setLid(eid);
		eo.getKey().setId(eid);

		/* 
		 * Home url
		 */
		String home = jaxrHandle.getEndpoint().replace("/saml", "");
		eo.setHome(home);

		/*
		 * Name & description
		 */
		String name = jForm.getString(RIM_NAME);
		String desc = jForm.getString(RIM_DESC);
		
		String dtime = jForm.getString(RIM_DATE);
		name = name.trim() + ", " + dtime.trim();

		eo.setName(jaxrLCM.createInternationalString(name));
		eo.setDescription(jaxrLCM.createInternationalString(desc));
		
		/*
		 * Associations
		 */
		
		/*
		 * Build directed graph between evaluation and its source
		 */
		RegistryObjectImpl sourceObject = jaxrLCM.getRegistryObjectById(source);
		if (sourceObject == null) throw new JAXRException("[EvaluationObject] RegistryObject with id <." + source + "> does not exist.");
		
		AssociationImpl sourceAssociation = jaxrLCM.createAssociation_RelatedTo(sourceObject);
		eo.addAssociation(sourceAssociation);

		/*
		 * Build directed graph between evaluation and its reasoner
		 */
		RegistryObjectImpl reasonerObject = jaxrLCM.getRegistryObjectById(reasoner);
		if (reasonerObject == null) throw new JAXRException("[EvaluationObject] RegistryObject with id <." + reasoner + "> does not exist.");
		
		AssociationImpl reasonerAssociation = jaxrLCM.createAssociation_RelatedTo(reasonerObject);
		eo.addAssociation(reasonerAssociation);

		/*
		 * Classifications
		 */
		ClassificationImpl classification = jaxrLCM.createClassification(ClassificationConstants.FNC_ID_Evaluation);
		eo.addClassification(classification);
		
		/*
		 * Mimetype & repository item
		 */
		String mimetype = GlobalConstants.MT_XML;
		eo.setMimeType(mimetype);
		
		byte[] bytes = 	FileUtil.getByteArrayFromInputStream(stream);

		DataHandler handler = new DataHandler(FileUtil.createByteArrayDataSource(bytes, mimetype));                	
    	eo.setRepositoryItem(handler);				

    	/*
		 * Indicate as created
		 */
		this.created = true;

		return eo;

	}

}
