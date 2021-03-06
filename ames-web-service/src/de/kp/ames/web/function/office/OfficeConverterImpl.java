package de.kp.ames.web.function.office;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.office
 *  Module: OfficeConverterImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #converter #function #office #web
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

import java.io.File;
import java.io.FileInputStream;

import de.kp.ames.office.OOConstants;
import de.kp.ames.office.jod.OOConnector;
import de.kp.ames.office.jod.OOEngine;
import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.regrep.JaxrIdentity;
import de.kp.ames.web.core.util.FileUtil;

public class OfficeConverterImpl implements OfficeConverter {

	protected OOConnector connector;
	protected FileUtil input;
	
	public OfficeConverterImpl(FileUtil file) {
		
		/*
		 * Connect to OpenOffice.org service
		 */
		connector = OOConnector.getInstance();
		connector.connect();
		
		/*
		 * Register file
		 */
		this.input = file;
		
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.function.office.OfficeConverter#convert()
	 */
	public FileUtil convert() {

		/* 
		 * Prepare input file
		 */

		String mimetype = input.getMimetype();		
		mimetype = (mimetype == null) ? "application/octet-stream" : mimetype.toLowerCase();

		String filename = OOConstants.OFFICE_CACHE  + "/" + JaxrIdentity.getInstance().getUID();

		OOEngine converter = new OOEngine();
		boolean result = converter.convertToPdf(filename, mimetype, input.getFile());
		
		if (result == false) return input;

		FileUtil output = getOutFile(filename + ".pdf");
		return (output == null) ? input : output;

	}

	/**
	 * @param fileName
	 * @return
	 */
	private FileUtil getOutFile(String fileName) {

		try {
			
			File tempFile = new File(fileName);
			FileInputStream fis = new FileInputStream(tempFile);
		
			byte[] bytes = FileUtil.getByteArrayFromInputStream(fis);
			FileUtil outFile = new FileUtil(bytes, GlobalConstants.MT_PDF);
		
			outFile.setFilename(fileName);
			return outFile;
		
		} catch (Exception e) {
			e.printStackTrace();
		
		} finally {}
		
		return null;

	}

}
