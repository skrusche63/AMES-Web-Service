package de.kp.ames.web.function.office;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.office
 *  Module: OdtBuilder
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #builder #function #odt #office #web
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

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import de.kp.ames.office.OOConstants;
import de.kp.ames.office.OOImage;
import de.kp.ames.office.util.ImageCallback;
import de.kp.ames.office.xml.WriterBuilder;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.util.FileUtil;

public class OdtBuilder extends BaseBuilder implements OfficeBuilder {

	private FileUtil file;

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 * @param file
	 */
	public OdtBuilder(JaxrHandle jaxrHandle, FileUtil file) {
		this.file = file;
		this.jaxrHandle = jaxrHandle;
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.function.office.OfficeBuilder#build()
	 */
	public FileUtil build() {

		InputStream stream = file.getInputStream();

		WriterBuilder writerBuilder = new WriterBuilder();
		byte[] odtFile = writerBuilder.buildFromStream(stream, new ImageCallback() {

			public Map<String, OOImage> getImages(List<String> ids) {
				return getOOImages(ids);
			}
			
		});
		
		String mimetype = OOConstants.MIMETYPE_ID_Odt;
		
		/*
		 * Build response
		 */
		return new FileUtil(odtFile, mimetype);

	}

}
