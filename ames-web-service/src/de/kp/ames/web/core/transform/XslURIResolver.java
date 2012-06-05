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

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.util.FileUtil;

public class XslURIResolver implements URIResolver {

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
	public XslURIResolver(JaxrHandle jaxrHandle) {
		this.jaxrHandle = jaxrHandle;
	}
	
	/* (non-Javadoc)
	 * @see javax.xml.transform.URIResolver#resolve(java.lang.String, java.lang.String)
	 */
	public Source resolve(String href, String base) throws TransformerException {
		
		/* 
		 * base is the system id of the enclosing document, and
		 * href is a fragment of the child's unique identifier
		 */
		
		Source xslSource = null;

		try {
			
			String uid = JaxrIdentity.getInstance().getXslUID(href);			
			XslProvider xslProvider = new XslProvider(jaxrHandle);
			
			FileUtil xslDoc = xslProvider.getStylesheet(uid);
			if (xslDoc == null) throw new TransformerException("Stylesheet with id: " + href + " not found.");
			
			XMLReader reader = XMLReaderFactory.createXMLReader();		
			reader.setEntityResolver(new XslEntityResolver(jaxrHandle));
	
			ByteArrayInputStream bais = new ByteArrayInputStream(xslDoc.getFile());			
			InputSource inputSource = new InputSource(bais);
			
			xslSource = new SAXSource(reader, inputSource);
			xslSource.setSystemId(uid);
		
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		return xslSource;
		
	}

}
