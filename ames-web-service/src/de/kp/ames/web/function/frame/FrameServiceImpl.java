package de.kp.ames.web.function.frame;

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
		
		if (format == null) {
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
