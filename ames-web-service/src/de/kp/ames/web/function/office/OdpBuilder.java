package de.kp.ames.web.function.office;
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
import java.util.List;
import java.util.Map;

import de.kp.ames.office.OOConstants;
import de.kp.ames.office.OOImage;
import de.kp.ames.office.util.ImageCallback;
import de.kp.ames.office.xml.ImpressBuilder;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.util.FileUtil;

public class OdpBuilder extends BaseBuilder implements OfficeBuilder {

	private FileUtil file;
	
	public OdpBuilder(JaxrHandle jaxrHandle, FileUtil file) {		
		this.file = file;
		this.jaxrHandle = jaxrHandle;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.function.office.OfficeBuilder#build()
	 */
	public FileUtil build() {
		
		InputStream stream = file.getInputStream();

		ImpressBuilder impressBuilder = new ImpressBuilder();
		byte[] odpFile = impressBuilder.buildFromStream(stream, new ImageCallback() {

			public Map<String, OOImage> getImages(List<String> ids) {
				return getOOImages(ids);
			}
			
		});
		
		String mimetype = OOConstants.MIMETYPE_ID_Odp;
		
		/*
		 * Build response
		 */
		return new FileUtil(odpFile, mimetype);

	}
	
}
