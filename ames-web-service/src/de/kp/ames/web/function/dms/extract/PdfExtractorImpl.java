package de.kp.ames.web.function.dms.extract;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.dms.extract
 *  Module: PdfExtractorImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #dms #extract #extractor #function #pdf #web
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
import java.util.Set;

import de.kp.ames.nlp.PdfEngine;

public class PdfExtractorImpl implements Extractor {

	/**
	 * Constructor
	 */
	public PdfExtractorImpl() {	
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.function.dms.extract.Extractor#extract(java.io.InputStream)
	 */
	public Set<String> extract(InputStream stream) {
		
		PdfEngine engine = new PdfEngine();
		return engine.pdfToText(stream);

	}

}
