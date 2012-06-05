package de.kp.ames.web.core.transform;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.util.FileUtil;

public class XslEntityResolver implements EntityResolver {

	/*
	 * Reference to the JaxrHandle associated
	 * with the actual request
	 */
	private JaxrHandle jaxrHandle;
	
	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public XslEntityResolver(JaxrHandle jaxrHandle) {
		this.jaxrHandle = jaxrHandle;
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
	 */
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {

		/* 
		 * publicId is always set to 'null' (unused)
		 */

		InputSource entitySource = null;
		
		/*
		 * Determine DTD identifier from system ID
		 */
		String[] tokens = systemId.split(":");
		
		String filename = tokens[tokens.length-1];
		String uid = JaxrIdentity.getInstance().getXslUID(filename);			

		/*
		 * Call XslProvider to get Dtd file
		 */
		XslProvider xslProvider = new XslProvider(jaxrHandle);

		FileUtil dtdDoc = xslProvider.getDtd(uid);
		if (dtdDoc == null) throw new SAXException("DTD with id: " + systemId + " not found.");

		ByteArrayInputStream bais = new ByteArrayInputStream(dtdDoc.getFile());			
		entitySource = new InputSource(bais);
		
		return entitySource;

	}

}
