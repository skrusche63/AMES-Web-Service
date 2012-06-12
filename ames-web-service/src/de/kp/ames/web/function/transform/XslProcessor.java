package de.kp.ames.web.function.transform;
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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import net.sf.saxon.TransformerFactoryImpl;

import de.kp.ames.web.Bundle;
import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.util.BaseParam;
import de.kp.ames.web.core.util.FileUtil;

public class XslProcessor {

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
	public XslProcessor(JaxrHandle jaxrHandle) {
		this.jaxrHandle = jaxrHandle;
	}
	
	/**
	 * @param source
	 * @param service
	 * @param params
	 * @return
	 */
	public InputStream execute(String source, String service, ArrayList<BaseParam> params) {

		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

		try {

			XslProvider xslProvider = new XslProvider(jaxrHandle);
			
			/*
			 * As a first step, an xml document (stream) is derived from the provided
			 * source object; these may either be the repository item of a specific
			 * extrinsic object or the xml representation of a concept graph component
			 */
			
			FileUtil xmlDoc = xslProvider.getSource(source);		
			
			/*
			 * As a second step, a set of xsl transformations is retrieved from the 
			 * service instance provided; these stylesheets describe stylesheets 
			 * that are related to service objects; each of these stylesheets may
			 * also retrieve further stylesheets through input or include statements.
			 */
			
			Collection<FileUtil> xslDocs = xslProvider.getStylesheets(service);
			if (xslDocs == null) return null;

			/*
			 * As a third and final step, we transform the source with the set of 
			 * XSL stylesheets
			 */
			
			Iterator<FileUtil> iterator = xslDocs.iterator();
			while (iterator.hasNext()) {
				
				FileUtil xslDoc = iterator.next();				
				xmlDoc = transform(xmlDoc, xslDoc, params);
	
			}
			
			return xmlDoc.getInputStream();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {}
		
		return null;
	}

	/**
	 * Transform a certain xml document with a specific xsl file
	 * 
	 * @param xmlDoc
	 * @param xslDoc
	 * @param params
	 * @return
	 */
	private FileUtil transform(FileUtil xmlDoc, FileUtil xslDoc, ArrayList<BaseParam> params) {
	
		ByteArrayOutputStream target = new ByteArrayOutputStream();

		try {
		
			/* 
			 * SAXON-HE interface
			 */
			TransformerFactoryImpl factory = new TransformerFactoryImpl();
			factory.setURIResolver(new XslURIResolver(jaxrHandle));

			/* 
			 * Xml source document
			 */
			StreamSource xmlSource = new StreamSource(xmlDoc.getInputStream());
			xmlSource.setSystemId(xmlDoc.getIdentifier());
		
			/* 
			 * Xsl stylesheet
			 */
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setEntityResolver(new XslEntityResolver(jaxrHandle));
		
			ByteArrayInputStream bais = new ByteArrayInputStream(xslDoc.getFile());			
			
			Source xslSource = new SAXSource(reader, new InputSource(bais));
			xslSource.setSystemId(xslDoc.getIdentifier());
			
			Transformer transformer = factory.newTransformer(xslSource);
		
			// add parameters if present
			if (params != null) {
				for (BaseParam param:params) {
					
					String key = param.getKey();
					String val = param.getValue();
					
					transformer.setParameter(key, val);
					
				}
			}
	
			transformer.transform(xmlSource, new StreamResult(target));
		
	
		} catch (SAXException e) {
			e.printStackTrace();
			
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			
		} catch (TransformerException e) {
			e.printStackTrace();			
		}
		
		FileUtil file = new FileUtil(target.toByteArray(), GlobalConstants.MT_XML);		

		/* 
		 * this is a unique identifier that is temporarily used 
		 * by the xsl transformation
		 */
		String prefix = Bundle.getInstance().getString(GlobalConstants.BASE_PRE) + ":xml";
		file.setIdentifier(JaxrIdentity.getInstance().getPrefixUID(prefix));		
		
		return file;
	
	}
	
}
