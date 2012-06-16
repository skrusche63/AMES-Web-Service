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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.TransformerFactoryImpl;

import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.util.BaseParam;
import de.kp.ames.web.core.util.FileUtil;

public class RuleProcessor {

	/*
	 * Reference to RuleProvider
	 */
	private RuleProvider ruleProvider;

	/*
	 * Reference to TransformerFactory
	 */
	TransformerFactoryImpl factory;

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public RuleProcessor(JaxrHandle jaxrHandle) {
		/*
		 * Instantiate RuleProvider
		 */
		ruleProvider = new RuleProvider(jaxrHandle);
		
		/*
		 * Instantiate TransformerFactory
		 */
		factory = new TransformerFactoryImpl();
	}

	/**
	 * This method invokes the OO jDREW rule engine and derives 	
	 * a set of facts from the provided fact base and the respective
	 * reasoner (set of rules); the result is return as a rim based
	 * artefact that is represented as an inputstream for further
	 * processing
	 * 
	 * @param source
	 * @param service
	 * @param params
	 * @return
	 */
	public InputStream execute(String source, String service, ArrayList<BaseParam> params) {
	
		try {			
			/*
			 * Create RuleML artefact from FactBase
			 */
			String ruleMLFacts = setFactBase(source);
			
			/*
			 * Create RuleML artefact from RuleBase
			 */
			String ruleMLRules = setRuleBase(service);

			/*
			 * Invoke Rule Engine
			 */
			RuleEngine ruleEngine = new RuleEngine();
			String ruleMLDerived = ruleEngine.process(ruleMLFacts, ruleMLRules);
					
			/*
			 * Convert RuleML artefect for DerivedFacts
			 */
			return convertResult(ruleMLDerived);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {}
		
		return null;

	}
	
	/**
	 * A helper method to convert an OASIS ebXML RegRep
	 * based source into a RuleML FactBase
	 *
	 * @param source
	 * @return
	 * @throws Exception
	 */
	private String setFactBase(String source) throws Exception {
		
		/*
		 * An xml document (stream) is derived from the provided
		 * source object; these may either be the repository item 
		 * of a specific extrinsic object or the xml representation 
		 * of a concept graph component
		 */
	
		FileUtil factsDoc = ruleProvider.getSource(source);		
		Source factbase = new StreamSource(factsDoc.getInputStream());

		/* 
		 * Create the RuleML artefact from FactBase
		 */
		Templates templates = factory.newTemplates(RuleConfig.getInstance().getRuleBuilder());
		Transformer transformer = templates.newTransformer();
		
		StreamResult ruleML = new StreamResult(new StringWriter());
		transformer.transform(factbase, ruleML);

		return ruleML.getWriter().toString();
		
	}
		
	/**
	 * A helper method to convert an OASIS ebXML RegRep
	 * based reasoner (service) into a RuleML RuleBase
	 * 
	 * @param service
	 * @return
	 * @throws Exception
	 */
	private String setRuleBase(String service) throws Exception {

		/* 
		 * An xml document is derived from the 
		 * provided reasoner object (service)
		 */ 
		FileUtil reasonerDoc = ruleProvider.getReasoner(service);
		Source rulebase = new StreamSource(reasonerDoc.getInputStream());

		/*
		 * Create RuleML artefact from RuleBase
		 */
		Templates templates = factory.newTemplates(RuleConfig.getInstance().getRuleBuilder());
		Transformer	transformer = templates.newTransformer();

		StreamResult ruleML = new StreamResult(new StringWriter());		
		transformer.transform(rulebase, ruleML);

		return ruleML.getWriter().toString();

	}
	
	/**
	 * A helper method to convert a RuleML based representation
	 * of derived facts into an InputStream
	 * 
	 * @param ruleML
	 * @return
	 * @throws Exception
	 */
	private InputStream convertResult(String ruleML) throws Exception {
		
		Source resultbase = new StreamSource(new StringReader(ruleML));
		
		Templates templates = factory.newTemplates(RuleConfig.getInstance().getResultConverter());
		Transformer transformer = templates.newTransformer();

		ByteArrayOutputStream result = new ByteArrayOutputStream();
		transformer.transform(resultbase, new StreamResult(result));

		FileUtil derivedFacts = new FileUtil(result.toByteArray(), GlobalConstants.MT_XML);		
		return derivedFacts.getInputStream();
		
	}

}
