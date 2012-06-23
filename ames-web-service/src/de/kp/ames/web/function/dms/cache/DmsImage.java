package de.kp.ames.web.function.dms.cache;
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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import de.kp.ames.web.core.cache.CacheEntry;
import de.kp.ames.web.core.util.FileUtil;

public class DmsImage implements CacheEntry {

	private BufferedImage image;
	
	private String key;
	private String name;
	private String mimetype;

	/* 
	 * A cached image may also be stored as a tempfile
	 */
	private String path;

	/**
	 * Constructor
	 * 
	 * @param image
	 */
	public DmsImage(BufferedImage image) {
		this.image = image;
	}
	
	/**
	 * Constructor
	 * 
	 * @param key
	 * @param name
	 * @param mimetype
	 * @param image
	 */
	public DmsImage(String key, String name, String mimetype, BufferedImage image) {
		
		this.key      = key;
		this.name     = name;
		this.mimetype = mimetype;
		
		this.image = image;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.cache.CacheEntry#getBytes()
	 */
	public byte[] getBytes() {

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(this.image, "png", baos);

			return baos.toByteArray();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return null;
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.cache.CacheEntry#getInputStream()
	 */
	public InputStream getInputStream() {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.cache.CacheEntry#getKey()
	 */
	public String getKey() {
		return this.key;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.cache.CacheEntry#getName()
	 */
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.cache.CacheEntry#getMimetype()
	 */
	public String getMimetype() {
		return this.mimetype;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.cache.CacheEntry#setPath(java.lang.String)
	 */
	public void setPath(String path) {
		this.path = path;		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.cache.CacheEntry#getPath()
	 */
	public String getPath() {
		return this.path;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.cache.CacheEntry#asFile()
	 */
	public FileUtil asFile() {
		return new FileUtil(getBytes(), getMimetype());
	}
	
	/**
	 * Retrieve a buffered image
	 * 
	 * @return
	 */
	public BufferedImage getImage() {	
		return this.image;			
	}

}
