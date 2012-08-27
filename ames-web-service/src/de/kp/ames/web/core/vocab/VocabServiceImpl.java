package de.kp.ames.web.core.vocab;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.vocab
 *  Module: VocabServiceImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #service #vocab #web
 * </SemanticAssist>
 *
 */

import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.core.service.ServiceImpl;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.MethodConstants;

public class VocabServiceImpl extends ServiceImpl {

	public VocabServiceImpl() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(MethodConstants.METH_SUBMIT)) {
			/*
			 * Call submit method
			 */
			doSubmitRequest(ctx);

		}
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doSubmitRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doSubmitRequest(RequestContext ctx) {

		String data = this.getRequestData(ctx);
		String type = this.method.getAttribute(MethodConstants.ATTR_TYPE);

		if ((data == null)  || (type == null)) {
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
	 * A helper method to either submit a classification
	 * scheme or classification node
	 * 
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
			
		if (type.equals(ClassificationConstants.FNC_ID_Scheme)) {

			VocabLCM lcm = new VocabLCM(jaxrHandle);
			content = lcm.submitScheme(data);
		
		} else if (type.equals(ClassificationConstants.FNC_ID_Concept)) {

			VocabLCM lcm = new VocabLCM(jaxrHandle);
			content = lcm.submitConcept(data);
			
		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

}
