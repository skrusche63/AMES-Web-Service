package de.kp.ames.web.function.access.dav;
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

import org.json.JSONArray;

import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.webdav.WebDAVClient;
import de.kp.ames.webdav.WebDAVFile;

public class DavConsumer {

	/**
	 * Constructor
	 */
	public DavConsumer() {
	}
	
	/**
	 * A wrapper method to access the WebDAV client and retrieve
	 * a sorted list of resources as a JSON representation
	 * 
	 * @param alias
	 * @param keypass
	 * @param uri
	 * @return
	 */
	public JSONArray getResources(String alias, String keypass, String uri) {
		
		WebDAVClient client = new WebDAVClient(alias, keypass, uri);
		return client.getResources();
		
	}
	
	/**
	 * A wrapper method to access the WebDAV client and retrieve
	 * a File-based resource
	 * 
	 * @param alias
	 * @param keypass
	 * @param uri
	 * @param mimetype
	 * @return
	 */
	public FileUtil getFile(String alias, String keypass, String uri, String mimetype) {

		WebDAVClient client = new WebDAVClient(alias, keypass, uri);
		WebDAVFile webDavFile = client.getFile();
		
		/*
		 * Convert WebDAVFile into FileUtil
		 */
		
		FileUtil file = new FileUtil(webDavFile.getInputStream(), mimetype);
					
		int pos = uri.lastIndexOf("/") + 1;
		String fileName = uri.substring(pos);
			
		file.setFilename(fileName);
		return file;
		
	}

}
