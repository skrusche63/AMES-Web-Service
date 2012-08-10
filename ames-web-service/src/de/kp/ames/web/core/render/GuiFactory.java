package de.kp.ames.web.core.render;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.render
 *  Module: GuiFactory
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #factory #gui #render #web
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

import java.lang.reflect.Constructor;

import de.kp.ames.web.Bundle;
import de.kp.ames.web.GlobalConstants;

public class GuiFactory {

	private static GuiFactory instance = new GuiFactory();
	/*
	 * Reference to Gui Renderer
	 */
	private GuiRenderer renderer;
	
	/**
	 * Singleton
	 */
	private GuiFactory() {
		
		Bundle bundle = Bundle.getInstance();
		String clazzName = bundle.getString(GlobalConstants.GUI_RENDERER);
		
		try {

			Class<?> clazz = Class.forName(clazzName);
			Constructor<?> constructor = clazz.getConstructor();
			
			Object instance = constructor.newInstance();
			this.renderer = (GuiRenderer)instance;

		} catch (Exception e) {
			e.printStackTrace();
		
		} finally {}

	}
	
	public static GuiFactory getInstance() {
		if (instance == null) instance = new GuiFactory();
		return instance;
	}
	
	public GuiRenderer getRenderer() {
		return this.renderer;
	}
	
}
