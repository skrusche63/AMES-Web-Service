package de.kp.ames.web.function.dms.cache;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.dms.cache
 *  Module: DocumentCacheManager
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #cache #dms #document #function #manager #web
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.cache.CacheEntry;
import de.kp.ames.web.core.cache.CacheManager;
import de.kp.ames.web.shared.constants.JsonConstants;

public class DocumentCacheManager implements CacheManager {

	private static DocumentCacheManager instance = new DocumentCacheManager();

	private DocumentCache cache = null;

	public static DocumentCacheManager getInstance() {
		if (instance == null) instance = new DocumentCacheManager();
		return instance;		
	}

	private DocumentCacheManager() {				
		this.cache = new DocumentCache();			
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.cache.CacheManager#setToCache(java.lang.String, java.lang.String, java.lang.String, java.io.InputStream)
	 */
	public void setToCache(String key, String name, String mimetype, InputStream stream) throws IOException {
		cache.put(key, new DmsDocument(key, name, mimetype, stream));
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.cache.CacheManager#setToCache(java.lang.String, java.lang.String, java.lang.String, byte[])
	 */
	public void setToCache(String key, String name, String mimetype, byte[] bytes) throws IOException {
		cache.put(key, new DmsDocument(key, name, mimetype, bytes));
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.cache.CacheManager#getFromCache(java.lang.String)
	 */
	public CacheEntry getFromCache(String key) {
		if (cache.hasKey(key) == false) return null;
		return cache.get(key);		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.cache.CacheManager#removeFromCache(java.lang.String)
	 */
	public void removeFromCache(String key) {

		if (cache.hasKey(key) == false) return;
		
		/* 
		 * Remove temp file assigned with document
		 */
		String path = cache.get(key).getPath();
		if (path != null) new File(path).delete();
		
		cache.remove(key);
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.cache.CacheManager#getJEntries()
	 */
	public JSONArray getJEntries() throws Exception {

		JSONArray jEntries = new JSONArray();
		
		List<CacheEntry> documents = cache.getAll();
		for (int ix = 0; ix < documents.size(); ix++) {

			DmsDocument document = (DmsDocument)documents.get(ix);
			String key = document.getKey();

			JSONObject jDocument = new JSONObject();

			jDocument.put(JsonConstants.J_KEY, key);
			jDocument.put(JsonConstants.J_NAME, document.getName());
			
			jDocument.put(JsonConstants.J_DESC, "No description available.");				
			jDocument.put(JsonConstants.J_MIME, document.getMimetype());
			
			jEntries.put(jEntries.length(), jDocument);

		}
		
		return jEntries;

	}

}
