package de.kp.ames.web.function.symbol;

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
		
		init();
		
	}
	
	public static APP6bProcessor getInstance() {
		if (instance == null) instance = new APP6bProcessor();
		return instance;
	}
	
}
