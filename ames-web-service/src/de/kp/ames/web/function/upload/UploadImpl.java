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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import de.kp.ames.web.core.RequestContext;
import de.kp.ames.web.core.malware.MalwareScanner;
import de.kp.ames.web.core.transform.cache.XslCacheManager;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.function.FncConstants;

public class UploadImpl extends BusinessImpl {

	public UploadImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.core.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(FncConstants.METH_SET)) {

			/*
			 * The result of the upload request, returned
			 * to the requestor; note, that the result must
			 * be a text response
			 */
			String result = "false";
			HttpServletRequest request = ctx.getRequest();

			try {
				
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if (isMultipart) {
					
					/* 
					 * Create new file upload handler
					 */
					ServletFileUpload upload = new ServletFileUpload();
		
					/*
					 * Parse the request
					 */
					FileItemIterator iter = upload.getItemIterator( request );
					while ( iter.hasNext() ) {
						
						FileItemStream fileItem = iter.next();
						if ( fileItem.isFormField() ) {
							// not supported
							
						} else {
								
							/* 
							 * Hook into the upload request to some virus scanning
							 * using the scanner factory of this application
							 */
							
							byte[] bytes = FileUtil.getByteArrayFromInputStream(fileItem.openStream());
							boolean checked = MalwareScanner.scanForViruses(bytes);						
							
							if (checked == false) {
								result = "false";
	
							} else {

								String item = this.method.getAttribute(FncConstants.ATTR_ITEM);		
								String type = this.method.getAttribute(FncConstants.ATTR_TYPE);			
								if ((item == null) || (type == null)) {
									this.sendNotImplemented(ctx);

								} else {
									
									/*
									 * Set upload result to 'true'
									 */
									result = "true";
									
									if (type.equals(FncConstants.FNC_ID_Transformator)) {
										
										/* 
										 * This is an upload request for a transformation
										 */
										String filename = FilenameUtils.getName( fileItem.getName() );
										String mimetype = fileItem.getContentType();
										
										/* 
										 * "save" uploaded transformator for later processing in
										 * the transformator cache of the transformator processor
										 */
										XslCacheManager.getInstance().setToCache(item, filename, mimetype, bytes);

									} else {
										Exception e = new Exception("[UploadImpl] Information type <" + type + "> is not supported");
										sendBadRequest(ctx, e);
										
									}
									
								}
	
							}
							
						}
						
					}
	
				}
				
				/*
				 * Send text response
				 */
				this.sendTextResponse(result, ctx.getResponse());
				
			} catch ( Exception e ) {
				this.sendBadRequest(ctx, e);
	
			} finally {}

		}

	}

}
