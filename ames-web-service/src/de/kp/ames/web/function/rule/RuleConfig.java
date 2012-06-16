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

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import de.kp.ames.web.Bundle;
import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.function.rule.data.XslLoader;

public class RuleConfig {
	/*
	 * Reference to predefined XSL files
	 */
	private Source factBuilder;
	private Source ruleBuilder;

	private Source resultConverter;

	private static RuleConfig instance = new RuleConfig();
	
	private RuleConfig() {
		loadXsl();
	}
	
	public static RuleConfig getInstance() {
		if (instance == null) instance = new RuleConfig();
		return instance;
	}
	
	/**
	 * A helper method to load predefined Xsl files
	 * to support Rule-based transformations
	 * 
	 */
	private void loadXsl() {
		
		Bundle bundle = Bundle.getInstance();
		
		/* 
		 * Load and initialize fact builder
		 */
		String fb = bundle.getString(GlobalConstants.FACTBASE_BUILDER);
		factBuilder = new StreamSource(XslLoader.load(fb));
		
		/* 
		 * Load and initialize rule builder
		 */
		String rb = bundle.getString(GlobalConstants.RULEBASE_BUILDER);
		ruleBuilder = new StreamSource(XslLoader.load(rb));

		/* 
		 * Load and initialize result converter
		 */
		String rc  = bundle.getString(GlobalConstants.RESULT_CONVERTER);
		resultConverter = new StreamSource(XslLoader.load(rc));

	}

	/**
	 * @return
	 */
	public Source getFactBuilder() {
		return factBuilder;
	}
	
	/**
	 * @return
	 */
	public Source getRuleBuilder() {
		return ruleBuilder;
	}
	
	/**
	 * @return
	 */
	public Source getResultConverter() {
		return resultConverter;
	}
}
