package de.kp.ames.web.core.transform.cache;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.format.json.JsonConstants;

public class XslCacheManager {

	private static XslCacheManager instance = new XslCacheManager();

	private XslCache cache = null;

	public static XslCacheManager getInstance() {
		if (instance == null) instance = new XslCacheManager();
		return instance;		
	}

	private XslCacheManager() {				
		this.cache = new XslCache();			
	}

	/**
	 * This method retrieves the actually uploaded transformators 
	 * that are currently not registered in the OASIS ebXML RegRep
	 * 
	 * @return
	 * @throws Exception
	 */
	public JSONArray getUploaded() throws Exception {
	
		JSONArray jUploaded = new JSONArray();
		
		List<XslTransformator> transformators = cache.getAll();
		for (int ix = 0; ix < transformators.size(); ix++) {

			XslTransformator transformator = transformators.get(ix);
			String key = transformator.getKey();

			JSONObject jTransformator = new JSONObject();

			jTransformator.put(JsonConstants.J_KEY, key);
			jTransformator.put(JsonConstants.J_NAME, transformator.getName());
			
			jTransformator.put(JsonConstants.J_DESC, "No description available.");				
			jTransformator.put(JsonConstants.J_MIME, transformator.getMimetype());
			
			jUploaded.put(jUploaded.length(), jTransformator);

		}
		
		return jUploaded;
		
	}
	
	/**
	 * @param key
	 * @param name
	 * @param mimetype
	 * @param stream
	 * @throws IOException
	 */
	public void setToCache(String key, String name, String mimetype, InputStream stream) throws IOException {
		cache.put(key, new XslTransformator(key, name, mimetype, stream));
	}

	/**
	 * @param key
	 * @param name
	 * @param mimetype
	 * @param bytes
	 * @throws IOException
	 */
	public void setToCache(String key, String name, String mimetype, byte[]bytes) throws IOException {
		cache.put(key, new XslTransformator(key, name, mimetype, bytes));
	}

	/**
	 * Retrieve a certain transformator from the transformator cache
	 * 
	 * @param key
	 * @return
	 */
	public XslTransformator getFromCache(String key) {
		if (cache.hasKey(key) == false) return null;
		return cache.get(key);		
	}

	/**
	 * @param key
	 */
	public void removeFromCache(String key) {

		if (cache.hasKey(key) == false) return;
		
		/* 
		 * Remove temp file assigned with transformator
		 */
		String path = cache.get(key).getPath();
		if (path != null) new File(path).delete();
		
		cache.remove(key);
	}

}
