package de.kp.ames.web.function.upload;
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

import de.kp.ames.web.core.cache.CacheManager;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.function.transform.cache.XslCacheManager;

public class UploadFactory {

	public UploadFactory() {
	}
	
	public CacheManager getCacheManager(String type) throws Exception {
		
		if (type.equals(FncConstants.FNC_ID_Transformator)) {
			return XslCacheManager.getInstance();

		} else {
			throw new Exception("[UploadFactory] Information type <" + type + "> is not supported");

		}

	}
}
