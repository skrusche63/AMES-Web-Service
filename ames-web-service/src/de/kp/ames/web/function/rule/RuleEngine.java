package de.kp.ames.web.function.rule;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.rule
 *  Module: RuleEngine
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #engine #function #rule #web
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

import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import jdrew.oo.bu.ForwardReasoner;
import jdrew.oo.util.DefiniteClause;
import jdrew.oo.util.RuleMLParser;
import jdrew.oo.util.SymbolTable;

public class RuleEngine {

	public RuleEngine() {		
	}

	public String process(String rmlFacts, String rmlRules) throws Exception {	
		
		/*
		 * Combine the RuleML Rule and Fact artifacts to create a valid RuleML document  
		 */
		StringWriter kbWriter = new StringWriter();

		kbWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		kbWriter.write("<Assert>");
		kbWriter.write("<Rulebase mapClosure=\"universal\">");

		kbWriter.write(rmlFacts);
		kbWriter.write(rmlRules);
		
		kbWriter.write("</Rulebase>");
		kbWriter.write("</Assert>");
		
		String knowledgeBase = kbWriter.toString();

		/*
		 * Parse the RuleML document and start the bottom-up reasoning
		 */
		SymbolTable.reset();
		
		RuleMLParser rulemlParser = new RuleMLParser();		
		rulemlParser.parseRuleMLString(RuleMLParser.RULEML91, knowledgeBase);

		ForwardReasoner fwReasoner = new ForwardReasoner();
		fwReasoner.loadClauses(rulemlParser.iterator());
		
		fwReasoner.runForwardReasoner();

		/*
		 * Combine the RuleML facts to create a valid RuleML document
		 */
		StringWriter resultWriter = new StringWriter();
		resultWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		resultWriter.write("<Assert>");
		resultWriter.write("<Rulebase mapClosure=\"universal\">");

		@SuppressWarnings("unchecked")
		Hashtable<Integer,Vector<DefiniteClause>> oldFacts = fwReasoner.getOldFacts();
		
        Enumeration<Vector<DefiniteClause>> e = oldFacts.elements();
        while (e.hasMoreElements()) {

        	Vector<DefiniteClause> newFacts = e.nextElement();
        	Iterator<DefiniteClause> it = newFacts.iterator();
            
        	while (it.hasNext()) {                
            	DefiniteClause dc = (DefiniteClause)it.next();
    			resultWriter.write(dc.toRuleMLString(RuleMLParser.RULEML91));
          
        	}      
        
        }

		resultWriter.write("</Rulebase>");
		resultWriter.write("</Assert>");
		
		return resultWriter.toString();
	}

}

