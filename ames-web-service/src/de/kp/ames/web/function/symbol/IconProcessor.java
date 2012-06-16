package de.kp.ames.web.function.symbol;

import de.kp.ames.web.Bundle;
import de.kp.ames.web.GlobalConstants;

public class IconProcessor extends SymbolProcessor {

	private static IconProcessor instance = new IconProcessor();
	private IconProcessor() {
		
		/*
		 * Initialize control information
		 */
		SYMBOL_PATH = Bundle.getInstance().getString(GlobalConstants.SYMBOL_ICON_FILE);
		SYMBOL_ROOT = "GEN.X";

		SYMBOL_URI = Bundle.getInstance().getString(GlobalConstants.SYMBOL_ICON_URI);

		init();
		
	}
	
	/**
	 * Singleton
	 * 
	 * @return
	 */
	public static IconProcessor getInstance() {
		if (instance == null) instance = new IconProcessor();
		return instance;
	}

}
