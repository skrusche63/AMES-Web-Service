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

import java.awt.image.BufferedImage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.cache.CacheManager;
import de.kp.ames.web.core.graphics.GraphicsUtil;
import de.kp.ames.web.core.malware.MalwareScanner;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.function.FncMessages;
import de.kp.ames.web.function.dms.cache.DmsDocument;
import de.kp.ames.web.function.dms.cache.DmsImage;
import de.kp.ames.web.function.dms.cache.DocumentCacheManager;
import de.kp.ames.web.function.dms.cache.ImageCacheManager;
import de.kp.ames.web.function.office.OfficeConverter;
import de.kp.ames.web.function.office.OfficeFactory;
import de.kp.ames.web.function.transform.cache.XslCacheManager;
import de.kp.ames.web.function.transform.cache.XslTransformator;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.JsonConstants;
import de.kp.ames.web.shared.constants.MethodConstants;

public class UploadServiceImpl extends BusinessImpl {

	/**
	 * Constructor
	 */
	public UploadServiceImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.core.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(MethodConstants.METH_DELETE)) {
			/*
			 * Call delete method
			 */
			doDeleteRequest(ctx);
			
		} else if (methodName.equals(MethodConstants.METH_GET)) {
			/*
			 * Call get method
			 */
			doGetRequest(ctx);
			
		} else if (methodName.equals(MethodConstants.METH_SET)) {

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

								String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);		
								String type = this.method.getAttribute(MethodConstants.ATTR_TYPE);			
								if ((item == null) || (type == null)) {
									this.sendNotImplemented(ctx);

								} else {
									
									String fileName = FilenameUtils.getName( fileItem.getName() );
									String mimeType = fileItem.getContentType();
									
									try {
										result = upload(item, type, fileName, mimeType, bytes);
										
									} catch (Exception e) {
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

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doDeleteRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doDeleteRequest(RequestContext ctx) {

		String type = this.method.getAttribute(MethodConstants.ATTR_TYPE);			
		String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);			

		if ((type == null) || (item == null)) {
			this.sendNotImplemented(ctx);
			
		} else {
				
			try {
				/*
				 * JSON response
				 */
				String content = delete(item, type);
				sendJSONResponse(content, ctx.getResponse());

			} catch (Exception e) {
				this.sendBadRequest(ctx, e);

			}
			
		}		
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doGetRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doGetRequest(RequestContext ctx) {

		String format = this.method.getAttribute(MethodConstants.ATTR_FORMAT);	
		String type   = this.method.getAttribute(MethodConstants.ATTR_TYPE);	
		
		if ((format == null) || (type == null)) {
			this.sendNotImplemented(ctx);
			
		} else {

			/*
			 * This is an optional parameter that determines 
			 * a certain cache object
			 */
			String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);

			if (format.startsWith(FormatConstants.FNC_FORMAT_ID_File)) {

				try {
					/*
					 * File response
					 */
					FileUtil file = getFileResponse(type, item);
					if (file.getMimetype().equals(GlobalConstants.MT_XML) == false) {
						/*
						 * Retrieve file in PDF format
						 */
						OfficeFactory factory = new OfficeFactory(jaxrHandle, file);
						OfficeConverter converter = factory.getOfficeConverter();
						
						file = converter.convert();
						
					}

					sendFileResponse(file, ctx.getResponse());
					
				} catch (Exception e) {
					this.sendBadRequest(ctx, e);
					
				}
								
			} else if (format.startsWith(FormatConstants.FNC_FORMAT_ID_Image)) {

				try {
					/*
					 * Image response
					 */
					BufferedImage image = getImageResponse(type, item);
					sendImageResponse(image, ctx.getResponse());
					
				} catch (Exception e) {
					this.sendBadRequest(ctx, e);
					
				}
				
			} else if (format.startsWith(FormatConstants.FNC_FORMAT_ID_Json)) {

				try {
					/*
					 * JSON response
					 */
					String content = getJSONResponse(type, format);
					sendJSONResponse(content, ctx.getResponse());
	
				} catch (Exception e) {
					this.sendBadRequest(ctx, e);
	
				}
				
			}

		}
		
	}

	/**
	 * Get a single file
	 * 
	 * @param type
	 * @param item
	 * @return
	 * @throws Exception
	 */
	private FileUtil getFileResponse(String type, String item) throws Exception {

		FileUtil file = null;
		
		if (type.equals(ClassificationConstants.FNC_ID_Document)) {
		
			DocumentCacheManager cacheManager = DocumentCacheManager.getInstance();
			DmsDocument cacheEntry = (DmsDocument)cacheManager.getFromCache(item);

			if (cacheEntry == null) throw new Exception("[UploadServiceImpl] Cache Entry with key <" + item + "> not found.");			
			file = cacheEntry.asFile();
			
		} else if (type.equals(ClassificationConstants.FNC_ID_Transformator)) {

			XslCacheManager cacheManager = XslCacheManager.getInstance();
			XslTransformator cacheEntry = (XslTransformator)cacheManager.getFromCache(item);

			if (cacheEntry == null) throw new Exception("[UploadServiceImpl] Cache Entry with key <" + item + "> not found.");			
			file = cacheEntry.asFile();
			
		}
		
		return file;
	
	}

	/**
	 * Get a single image
	 * 
	 * @param type
	 * @param item
	 * @return
	 * @throws Exception
	 */
	private BufferedImage getImageResponse(String type, String item) throws Exception {

		BufferedImage image = null;
		
		if (type.equals(ClassificationConstants.FNC_ID_Image)) {

			ImageCacheManager cacheManager = ImageCacheManager.getInstance();
			DmsImage cacheEntry = (DmsImage)cacheManager.getFromCache(item);
			
			if (cacheEntry == null) throw new Exception("[UploadServiceImpl] Cache Entry with key <" + item + "> not found.");	
				
			image = cacheEntry.getImage();
			return GraphicsUtil.createSource(image);

		}

		return image;

	}

	/**
	 * A helper method to retrieve cache entries in a JSON representation
	 * 
	 * @param type
	 * @param format
	 * @return
	 * @throws Exception
	 */
	private String getJSONResponse(String type, String format) throws Exception {

		UploadFactory factory = new UploadFactory();
		CacheManager manager = factory.getCacheManager(type);

		JSONArray jArray = manager.getJEntries();
		
		/*
		 * Render result
		 */
		return render(jArray, format);

	}

	/**
	 * A helper method to delete a certain cache entry
	 * 
	 * @param item
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private String delete(String item, String type) throws Exception {

		/*
		 * Determine cache manager
		 */
		UploadFactory factory = new UploadFactory();
		CacheManager manager = factory.getCacheManager(type);

		/*
		 * Remove from cache
		 */
		manager.removeFromCache(item);
		
		/*
		 * Build response
		 */
		JSONObject jResponse = new JSONObject();
		
		jResponse.put(JsonConstants.J_SUCCESS, true);
		jResponse.put(JsonConstants.J_MESSAGE, FncMessages.CACHE_ENTRY_DELETE);
		
		return jResponse.toString();
		
	}
	
	/**
	 * A helper method to process an uploaded file
	 * 
	 * @param item
	 * @param type
	 * @param fileName
	 * @param mimeType
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	private String upload(String item, String type, String fileName, String mimeType, byte[] bytes) throws Exception {
		
		UploadFactory factory = new UploadFactory();
		CacheManager manager = factory.getCacheManager(type);

		manager.setToCache(item, fileName, mimeType, bytes);
		return "true";
		
	}
	
}
