package de.kp.ames.web.function.dms.cache;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.dms.cache
 *  Module: ImageCacheManager
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #cache #dms #function #image #manager #web
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.cache.CacheEntry;
import de.kp.ames.web.core.cache.CacheManager;
import de.kp.ames.web.shared.constants.JsonConstants;

public class ImageCacheManager implements CacheManager {

	private static ImageCacheManager instance = new ImageCacheManager();

	private ImageCache cache = null;

	public static ImageCacheManager getInstance() {
		if (instance == null) instance = new ImageCacheManager();
		return instance;		
	}

	private ImageCacheManager() {				
		this.cache = new ImageCache();			
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.cache.CacheManager#setToCache(java.lang.String, java.lang.String, java.lang.String, java.io.InputStream)
	 */
	public void setToCache(String key, String name, String mimetype, InputStream stream) throws IOException {		
		DmsImage image = new DmsImage(key, name, mimetype, ImageIO.read(stream));		
		cache.put(key, image);
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.cache.CacheManager#setToCache(java.lang.String, java.lang.String, java.lang.String, byte[])
	 */
	public void setToCache(String key, String name, String mimetype, byte[] bytes) throws IOException {		
		DmsImage image = new DmsImage(key, name, mimetype, ImageIO.read(new ByteArrayInputStream(bytes)));		
		cache.put(key, image);
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
		 * Remove temp file assigned with image
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
		
		List<CacheEntry> images = cache.getAll();
		for (int ix = 0; ix < images.size(); ix++) {

			DmsImage image = (DmsImage)images.get(ix);
			String key = image.getKey();

			JSONObject jImage = new JSONObject();

			jImage.put(JsonConstants.J_KEY, key);
			jImage.put(JsonConstants.J_NAME, image.getName());
			
			jImage.put(JsonConstants.J_DESC, "No description available.");				
			jImage.put(JsonConstants.J_MIME, image.getMimetype());
			
			jEntries.put(jEntries.length(), jImage);

		}
		
		return jEntries;
		
	}

}
