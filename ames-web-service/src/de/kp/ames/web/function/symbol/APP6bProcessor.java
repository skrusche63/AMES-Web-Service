package de.kp.ames.web.function.symbol;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.symbol
 *  Module: APP6bProcessor
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #6 #app #b #function #processor #symbol #web
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

import de.kp.ames.web.Bundle;
import de.kp.ames.web.GlobalConstants;

public class APP6bProcessor extends SymbolProcessor {

	private static APP6bProcessor instance = new APP6bProcessor();
	private APP6bProcessor() {
		
		/*
		 * Initialize control information
		 */
		SYMBOL_PATH = Bundle.getInstance().getString(GlobalConstants.SYMBOL_APP6B_FILE);
		SYMBOL_ROOT = "1.X";

		SYMBOL_URI = Bundle.getInstance().getString(GlobalConstants.SYMBOL_APP6B_URI);
		
	}
	
	public static APP6bProcessor getInstance() {
		if (instance == null) instance = new APP6bProcessor();
		return instance;
	}
	
}
