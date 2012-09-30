package de.kp.ames.web.function.frame;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.frame
 *  Module: FrameServiceImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #frame #function #service #web
 * </SemanticAssist>
 *
 */

/**
 *	Copyright 2012 Dr. Krusche & Partner PartG
 *
 *	AMES-Web-GUI is free software: you can redistribute it and/or 
 *	modify it under the terms of the GNU General Public License 
 *	as published by the Free Software Foundation, either version 3 of 
 *	the License, or (at your option) any later version.
 *
 *	AMES- Web-GUI is distributed in the hope that it will be useful,
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

import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;

public class FrameServiceImpl extends BusinessImpl {

	/**
	 * Constructor
	 */
	public FrameServiceImpl() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String format = this.method.getAttribute(MethodConstants.ATTR_FORMAT);	
		String service = this.method.getAttribute(MethodConstants.ATTR_SERVICE);
		
		if ((format == null) || (service == null)) {
			this.sendNotImplemented(ctx);
			
		} else {

			if (format.startsWith(FormatConstants.FNC_FORMAT_ID_File)) {

				try {
				
					String requestUrl = buildRequestUrl(ctx);
					String content = "<html><body style=\"margin:0;border:0;padding:0;\"><iframe src=\"" + requestUrl + "\" style=\"margin:0;border:0;padding:0;\" width=\"100%\" height=\"100%\"/></body></html>";

					this.sendHTMLResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);
					
				}
				
			} else if (format.startsWith(FormatConstants.FNC_FORMAT_ID_Image)) {

				try {
				
					String requestUrl = buildRequestUrl(ctx);
					String content = "<html><body><img src=\"" + requestUrl + "\" /></body></html>";

					this.sendHTMLResponse(content, ctx.getResponse());
				
				} catch (Exception e) {
					this.sendBadRequest(ctx, e);
					
				}

			} else {
				this.sendNotImplemented(ctx);
				
			}
			
		}

	}

	/**
	 * Build request url to redirect the actual
	 * request to another service within the framework
	 * 
	 * @param ctx
	 * @return
	 */
	private String buildRequestUrl(RequestContext ctx) {
		
		HttpServletRequest request = ctx.getRequest();
		String requestURI = request.getRequestURI();
		
		int pos = requestURI.lastIndexOf("/");
		return requestURI.substring(0, pos) + "/" + this.method.getAttribute(MethodConstants.ATTR_SERVICE) + this.method.toQuery();
		
	}
}
