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

import java.io.InputStream;

import de.kp.ames.web.core.cache.CacheEntry;
import de.kp.ames.web.core.util.FileUtil;

public class DmsDocument implements CacheEntry {

	private InputStream document;

	private String key;
	private String name;
	private String mimetype;

	/* 
	 * A cached document may also be stored as a tempfile
	 */
	private String path;
	
	private byte[] bytes;

	/**
	 * Constructor
	 * 
	 * @param document
	 */
	public DmsDocument(InputStream document) {
		this.document = document;
	}

	/**
	 * Constructor
	 * 
	 * @param key
	 * @param name
	 * @param mimetype
	 * @param bytes
	 */
	public DmsDocument(String key, String name, String mimetype, byte[]bytes) {
		
		this.key      = key;
		this.name     = name;
		this.mimetype = mimetype;
		
		this.bytes = bytes;
	}

	/**
	 * @param key
	 * @param name
	 * @param mimetype
	 * @param document
	 */
	public DmsDocument(String key, String name, String mimetype, InputStream document) {
		
		this.key      = key;
		this.name     = name;
		this.mimetype = mimetype;
		
		this.document = document;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.cache.CacheEntry#getBytes()
	 */
	public byte[] getBytes() {

		if (this.bytes == null) this.bytes = FileUtil.getByteArrayFromInputStream(document);
		return this.bytes;
	
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.cache.CacheEntry#getInputStream()
	 */
	public InputStream getInputStream() {
		return this.document;
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

}
