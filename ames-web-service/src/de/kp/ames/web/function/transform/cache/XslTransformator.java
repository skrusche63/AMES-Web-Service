package de.kp.ames.web.function.transform.cache;
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

public class XslTransformator implements CacheEntry{

	private InputStream transformator;

	private String key;
	private String name;
	private String mimetype;

	/* 
	 * A cached transformator may also be stored as a tempfile
	 */
	private String path;
	
	private byte[] bytes;
	
	/**
	 * Constructor
	 * 
	 * @param transformator
	 */
	public XslTransformator(InputStream transformator) {
		this.transformator = transformator;
	}

	/**
	 * Constructor
	 * 
	 * @param key
	 * @param name
	 * @param mimetype
	 * @param bytes
	 */
	public XslTransformator(String key, String name, String mimetype, byte[]bytes) {
		
		this.key      = key;
		this.name     = name;
		this.mimetype = mimetype;
		
		this.bytes = bytes;
	}

	/**
	 * Constructor
	 * 
	 * @param key
	 * @param name
	 * @param mimetype
	 * @param transformator
	 */
	public XslTransformator(String key, String name, String mimetype, InputStream transformator) {
		
		this.key      = key;
		this.name     = name;
		this.mimetype = mimetype;
		
		this.transformator = transformator;
	}

	/**
	 * @return
	 */
	public byte[] getBytes() {

		if (this.bytes == null) this.bytes = FileUtil.getByteArrayFromInputStream(transformator);
		return this.bytes;
	
	}

	/**
	 * @return
	 */
	public InputStream getInputStream() {
		return this.transformator;
	}
	
	/**
	 * @return
	 */
	public String getKey() {
		return this.key;		
	}
	
	/**
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return
	 */
	public String getMimetype() {
		return this.mimetype;
	}
	
	/**
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * @return
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * @return
	 */
	public FileUtil asFile() {
		return new FileUtil(getBytes(), getMimetype());
	}

}
