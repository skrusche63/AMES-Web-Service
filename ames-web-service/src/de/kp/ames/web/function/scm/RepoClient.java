package de.kp.ames.web.function.scm;

import java.io.File;
import de.kp.ames.web.function.FncConstants;

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

/**
 * RepoClient supports the access to the local Git repository
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */
public class RepoClient {

	private String FILE_PROTOCOL = "file://";
	
	public RepoClient() {		
	}
	
	public String getModuleAsHtml(String uri) {

		String path = getPathFromUri(uri);
		
		String fileName = FncConstants.GIT_HOME + File.separator + path;
		File module = new File(fileName);
		
		if (module == null) return null;
		// TODO
		
		return null;
	}
	
	/**
	 * A helper method to retrieve the file system path to
	 * a certain source code module from an external uri
	 * 
	 * @param uri
	 * @return
	 */
	private String getPathFromUri(String uri) {
		/*
		 * The file protocol is file://host/path
		 */
		return uri.replace(FILE_PROTOCOL, "");
	}
	
}
