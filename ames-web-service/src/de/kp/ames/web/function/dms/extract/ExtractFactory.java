package de.kp.ames.web.function.dms.extract;
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

import de.kp.ames.web.GlobalConstants;

public class ExtractFactory {

	public ExtractFactory() {
	}
	
	/**
	 * Retrieve registered extractor from mimetype
	 * 
	 * @param mimeType
	 * @return
	 * @throws Exception
	 */
	public Extractor getExtractor(String mimeType) throws Exception {
		
		if (mimeType.equals(GlobalConstants.MT_PDF)) {
			return new PdfExtractorImpl();
			
		} else {
			throw new Exception("[ExtractFactory] Mimetype <" + mimeType + "> not supported.");
		}
		
	}
	
}
