package de.kp.ames.web.core.cache;
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

import de.kp.ames.web.core.util.FileUtil;

public interface CacheEntry {

	/**
	 * @return
	 */
	public byte[] getBytes();

	/**
	 * @return
	 */
	public InputStream getInputStream();
	
	/**
	 * @return
	 */
	public String getKey();
	
	/**
	 * @return
	 */
	public String getName();
	
	/**
	 * @return
	 */
	public String getMimetype();
	
	/**
	 * @param path
	 */
	public void setPath(String path);
	
	/**
	 * @return
	 */
	public String getPath();

	/**
	 * Retrieve CacheEntry in a FileUtil representation
	 * 
	 * @return
	 */
	public FileUtil asFile();

}
