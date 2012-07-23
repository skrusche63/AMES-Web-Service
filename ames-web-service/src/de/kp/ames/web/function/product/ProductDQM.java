package de.kp.ames.web.function.product;
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
import java.util.List;

import javax.activation.DataHandler;

import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.common.CanonicalSchemes;
import org.json.JSONArray;
import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.domain.JsonBusinessProvider;
import de.kp.ames.web.shared.constants.ClassificationConstants;

public class ProductDQM extends JaxrDQM {

	private static String EXTRINSIC_OBJECT = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_ExtrinsicObject;

	public ProductDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * Retrieve FileUtil representation of a 
	 * certain product
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public FileUtil getFile(String item) throws Exception {

		/* 
		 * Determine registry object from unique identifier
		 */
		RegistryObjectImpl ro = getRegistryObjectById(item);
		if (ro == null) throw new Exception("[ProductDQM] RegistryObject with id <" + item + "> does not exist.");

    	String objectType = ro.getObjectType().getKey().getId();	    	
    	if (!objectType.startsWith(EXTRINSIC_OBJECT)) throw new Exception("[ProductDQM] Product is no ExtrinsicObject");

		/* 
		 * Extrinsic objects must be restricted to xml document;
		 * this is asserted by evaluating the assciated mimetype
		 */		
		ExtrinsicObjectImpl eo = (ExtrinsicObjectImpl)ro;
		
		String mimeType = eo.getMimeType();    		
		if (!mimeType.equals(GlobalConstants.MT_XML)) throw new Exception("[ProductDQM] Product is no XML file.");

		/*
		 * Determine repository item as byte array		
		 */
		DataHandler handler = eo.getRepositoryItem();
		if (handler == null) throw new Exception("[ProductDQM] Product has no data.");

		InputStream stream = handler.getInputStream();
		byte[] bytes = FileUtil.getByteArrayFromInputStream(stream);

		return new FileUtil(bytes, mimeType);

	}
	
	/**
	 * Get either single product or all registered
	 * products
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public JSONArray getProducts(String item) throws Exception {

		/*
		 * Determine products
		 */
		List<RegistryObjectImpl> products = getRegistryObjects_ByClasNode(item, ClassificationConstants.FNC_ID_Product);

		/*
		 * Build JSON representation
		 */
		return JsonBusinessProvider.getProducts(jaxrHandle, products);
		
	}
	
	/**
	 * Get either single productor or all
	 * registered productors
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public JSONArray getProductors(String item) throws Exception {

		/*
		 * Determine productors
		 */		
		List<RegistryObjectImpl> productors = getRegistryObjects_ByClasNode(item, ClassificationConstants.FNC_ID_Productor);

		/*
		 * Build JSON representation
		 */
		return JsonBusinessProvider.getProductors(jaxrHandle, productors);

	}

}
