package de.kp.ames.web.function.dms;
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
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.function.dms.extract.ExtractFactory;
import de.kp.ames.web.function.dms.extract.Extractor;
import de.kp.ames.web.function.office.OfficeConverter;
import de.kp.ames.web.function.office.OfficeFactory;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.ClassificationConstants;
import de.kp.ames.web.shared.FormatConstants;
import de.kp.ames.web.shared.MethodConstants;

public class DmsServiceImpl extends BusinessImpl {

	/**
	 * Constructor
	 */
	public DmsServiceImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(MethodConstants.METH_DOWNLOAD)) {
			/*
			 * Call download method
			 */
			doDownloadRequest(ctx);

		} else if (methodName.equals(MethodConstants.METH_EXTRACT)) {
			/*
			 * Call extract method
			 */
			doExtractRequest(ctx);

		} else if (methodName.equals(MethodConstants.METH_GET)) {
			/*
			 * Call get method
			 */
			doGetRequest(ctx);
			
		} else if (methodName.equals(MethodConstants.METH_SUBMIT)) {
			/*
			 * Call submit method
			 */
			doSubmitRequest(ctx);
			
		}
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doDownloadRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doDownloadRequest(RequestContext ctx) {
		/*
		 * This is an optional parameter that determines 
		 * a certain registry object
		 */
		String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);
		try {
			/*
			 * File response
			 */
			FileUtil file = getFileResponse(item);			

			HttpServletResponse response = ctx.getResponse();
			response.setHeader("Content-disposition", "attachment; filename=" + file.getFilename());
			
			sendFileResponse(file, response);			
			
		} catch (Exception e) {
			this.sendBadRequest(ctx, e);
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doExtractRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doExtractRequest(RequestContext ctx) {

		String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);
		if (item == null) {
			this.sendNotImplemented(ctx);
			
		} else {

			try {

				FileUtil file = getFileResponse(item);

				/*
				 * Retrieve terms from file
				 */
				String mimeType = file.getMimetype();
				
				ExtractFactory factory = new ExtractFactory();
				Extractor extractor = factory.getExtractor(mimeType);
				
				Set<String> terms = extractor.extract(file.getInputStream());
				JSONArray jArray = new JSONArray(terms);
				
				String content = jArray.toString();
				sendJSONResponse(content, ctx.getResponse());
				
			} catch (Exception e) {
				this.sendBadRequest(ctx, e);
				
			}

		}

	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doGetRequest(de.kp.ames.web.http.RequestContext)
	 */
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
			 * a certain registry object
			 */
			String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);

			if (format.startsWith(FormatConstants.FNC_FORMAT_ID_File)) {

				try {
					/*
					 * File response
					 */
					FileUtil file = getFileResponse(item);

					/*
					 * Retrieve file in PDF format
					 */
					OfficeFactory factory = new OfficeFactory(jaxrHandle, file);
					OfficeConverter converter = factory.getOfficeConverter();
					
					file = converter.convert();
					sendFileResponse(file, ctx.getResponse());
					
				} catch (Exception e) {
					this.sendBadRequest(ctx, e);
					
				}
								
			} else if (format.startsWith(FormatConstants.FNC_FORMAT_ID_Image)) {

				try {
					/*
					 * Image response
					 */
					BufferedImage image = getImageResponse(item);
					sendImageResponse(image, ctx.getResponse());
					
				} catch (Exception e) {
					this.sendBadRequest(ctx, e);
					
				}
				
			} else if (format.startsWith(FormatConstants.FNC_FORMAT_ID_Json)) {
				/*
				 * Optional parameters that may be used to describe
				 * a Grid-oriented response
				 */
				String start = this.method.getAttribute(FncConstants.ATTR_START);
				String limit = this.method.getAttribute(FncConstants.ATTR_LIMIT);

				try {
					/*
					 * JSON response
					 */
					String content = getJSONResponse(type, item, start, limit, format);
					sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			}
			
		}
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doSubmitRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doSubmitRequest(RequestContext ctx) {

		String data = this.getRequestData(ctx);
		String type = this.method.getAttribute(MethodConstants.ATTR_TYPE);	
		
		if ((data == null) || (type == null)) {
			this.sendNotImplemented(ctx);
			
		} else {

			try {
				/*
				 * JSON response
				 */
				String content = submit(type, data);
				sendJSONResponse(content, ctx.getResponse());

			} catch (Exception e) {
				this.sendBadRequest(ctx, e);

			}
			
		}
		
	}

	/**
	 * Get a single file
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	private FileUtil getFileResponse(String item) throws Exception {

		FileUtil file = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		

		DmsDQM dqm = new DmsDQM(jaxrHandle);
		file = dqm.getDocument(item);
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return file;
	
	}
	
	/**
	 * Get a single image
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	private BufferedImage getImageResponse(String item) throws Exception {

		BufferedImage image = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		

		DmsDQM dqm = new DmsDQM(jaxrHandle);
		image = dqm.getImage(item);
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return image;

	}

	/**
	 * Get DMS specific information objects
	 * 
	 * @param type
	 * @param item
	 * @param start
	 * @param limit
	 * @param format
	 * @return
	 * @throws Exception
	 */
	private String getJSONResponse(String type, String item, String start, String limit, String format) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		
			
		if (type.equals(ClassificationConstants.FNC_ID_Document)) {

			DmsDQM dqm = new DmsDQM(jaxrHandle);
			JSONArray jArray = dqm.getDocuments(item);
			
			/*
			 * Render result
			 */
			if ((start == null) || (limit == null)) {
				content = render(jArray, format);

			} else {
				content = render(jArray, start, limit, format);
			}
			
		} else if (type.equals(ClassificationConstants.FNC_ID_Image)) {

			DmsDQM dqm = new DmsDQM(jaxrHandle);
			JSONArray jArray = dqm.getImages(item);
			
			/*
			 * Render result
			 */
			if ((start == null) || (limit == null)) {
				content = render(jArray, format);

			} else {
				content = render(jArray, start, limit, format);
			}
			
		} else {
			throw new Exception("[DmsServiceImpl] Information type <" + type + "> is not supported");
			
		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
		
	}

	/**
	 * A helper method to either submit a
	 * document or image
	 * 
	 * @param type
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private String submit(String type, String data) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

		if (type.equals(ClassificationConstants.FNC_ID_Document)) {

			DmsLCM lcm = new DmsLCM(jaxrHandle);
			content = lcm.submitDocument(data);
			
		} else if (type.equals(ClassificationConstants.FNC_ID_Image)) {

			DmsLCM lcm = new DmsLCM(jaxrHandle);
			content = lcm.submitImage(data);

		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

}
