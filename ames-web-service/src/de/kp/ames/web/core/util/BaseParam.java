package de.kp.ames.web.core.util;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.util
 *  Module: BaseParam
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #base #core #param #util #web
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

public class BaseParam {

	/*
	 * Reference to key
	 */
	private String key;
	/*
	 * Reference to value
	 */
	private String value;
	
	/**
	 * Constructor
	 * 
	 * @param key
	 * @param value
	 */
	public BaseParam(String key, String value) {
		
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Get key
	 * 
	 * @return
	 */
	public String getKey() {
		return this.key;
	}
	
	/**
	 * Get value
	 * 
	 * @return
	 */
	public String getValue() {
		return this.value;
	}
	
}
