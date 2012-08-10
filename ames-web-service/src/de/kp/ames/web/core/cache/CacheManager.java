package de.kp.ames.web.core.cache;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.cache
 *  Module: CacheManager
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #cache #core #manager #web
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

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;

public interface CacheManager {

	/**
	 * @param key
	 * @param name
	 * @param mimetype
	 * @param stream
	 * @throws IOException
	 */
	public void setToCache(String key, String name, String mimetype, InputStream stream) throws IOException;

	/**
	 * @param key
	 * @param name
	 * @param mimetype
	 * @param bytes
	 * @throws IOException
	 */
	public void setToCache(String key, String name, String mimetype, byte[]bytes) throws IOException;

	/**
	 * Retrieve a certain cache entry from a file cache
	 * 
	 * @param key
	 * @return
	 */
	public CacheEntry getFromCache(String key);

	/**
	 * @param key
	 */
	public void removeFromCache(String key);

	/**
	 * Retrieve all cache entries in a JSON representation
	 * 
	 * @return
	 * @throws Exception
	 */
	public JSONArray getJEntries() throws Exception;

}
