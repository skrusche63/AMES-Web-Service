package de.kp.ames.web.function.rule;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.rule
 *  Module: RuleServiceImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #function #rule #service #web
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
import java.util.ArrayList;

import org.json.JSONArray;

import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.core.util.BaseParam;
import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.function.role.RoleLCM;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.MethodConstants;

public class RuleServiceImpl extends BusinessImpl {

	public RuleServiceImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(MethodConstants.METH_APPLY)) {
			/*
			 * Call apply method
			 */
			doApplyRequest(ctx);

		} else if (methodName.equals(MethodConstants.METH_DELETE)) {
			/*
			 * Call delete method
			 */
			doDeleteRequest(ctx);
			
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
	 * @see de.kp.ames.web.core.service.ServiceImpl#doApplyRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doApplyRequest(RequestContext ctx) {

		String source  = this.method.getAttribute(MethodConstants.ATTR_SOURCE);
		String service = this.method.getAttribute(MethodConstants.ATTR_SERVICE);			

		if ((source == null) || (service == null)) {
			this.sendNotImplemented(ctx);
			
		} else {
			
			String data = this.getRequestData(ctx);
			if (data == null) {
				this.sendNotImplemented(ctx);
				
			} else {

				try {
					/*
					 * JSON response
					 */
					String content = apply(source, service, data);
					sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			}

		}
		
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doDeleteRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doDeleteRequest(RequestContext ctx) {

		String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);
		String type = this.method.getAttribute(MethodConstants.ATTR_TYPE);	

		if ((item == null) || (type == null)) {
			this.sendNotImplemented(ctx);
			
		} else {

			try {
				/*
				 * JSON response
				 */
				String content = delete(type, item);
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
			 * Reference to single object (Format: Object)
			 */
			String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);
			
			/*
			 * Format: Grid
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
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doSubmitRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doSubmitRequest(RequestContext ctx) {

		String data = this.getRequestData(ctx);
		if (data == null) {
			this.sendNotImplemented(ctx);
			
		} else {

			try {
				/*
				 * JSON response
				 */
				String content = submit(data);
				sendJSONResponse(content, ctx.getResponse());

			} catch (Exception e) {
				this.sendBadRequest(ctx, e);

			}
			
		}
		
	}
	
	/**
	 * Apply ruleset (service) to a certain registry object (source)
	 * and register result in an OASIS ebXML RegRep
	 * 
	 * @param source
	 * @param service
	 * @param data
	 * @return
	 */
	private String apply(String source, String service, String data) throws Exception {
		
		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		

		/*
		 * Retrieve transformed stream from source and respective
		 * service; actually no params are necessary
		 */
		ArrayList<BaseParam> xslParams = null;
		
		RuleProcessor ruleProcessor = new RuleProcessor(jaxrHandle);
		InputStream stream = ruleProcessor.execute(source, service, xslParams);
		
		if (stream == null) {
			/*
			 * Logoff
			 */
			JaxrClient.getInstance().logoff(jaxrHandle);
			throw new Exception("[RuleServiceImpl] Rule-based Transformation of " + source + " failed.");
			
		} else {

			RuleLCM lcm = new RuleLCM(jaxrHandle);
			content = lcm.createEvaluation(source, service, data, stream);

		}

		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

	/**
	 * A helper method to either delete an evaluation, reasoer or rule
	 * 
	 * @param type
	 * @param item
	 * @return
	 * @throws Exception
	 */
	private String delete(String type, String item) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		
		
		if (type.equals(ClassificationConstants.FNC_ID_Evaluation)) {

			RuleLCM lcm = new RuleLCM(jaxrHandle);
			content = lcm.deleteEvaluation(item);
			
		} else if (type.equals(ClassificationConstants.FNC_ID_Reasoner)) {

			RuleLCM lcm = new RuleLCM(jaxrHandle);
			content = lcm.deleteReasoner(item);

		} else if (type.equals(ClassificationConstants.FNC_ID_Rule)) {

			RuleLCM lcm = new RuleLCM(jaxrHandle);
			content = lcm.deleteRule(item);

		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

	/**
	 * Get reasoning specific information objects
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

		if (type.equals(ClassificationConstants.FNC_ID_Evaluation)) {
			
			RuleDQM dqm = new RuleDQM(jaxrHandle);
			JSONArray jArray = dqm.getEvaluations(item);
			
			/*
			 * Render result
			 */
			if ((start == null) || (limit == null)) {
				content = render(jArray, format);

			} else {
				content = render(jArray, start, limit, format);
			}
			
		} else if (type.equals(ClassificationConstants.FNC_ID_Reasoner)) {

			RuleDQM dqm = new RuleDQM(jaxrHandle);
			JSONArray jArray = dqm.getReasoners(item);
			
			/*
			 * Render result
			 */
			if ((start == null) || (limit == null)) {
				content = render(jArray, format);

			} else {
				content = render(jArray, start, limit, format);
			}
			
		} else {
			throw new Exception("[RuleServiceImpl] Information type <" + type + "> is not supported");
			
		}
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}
	
	/**
	 * Submit reasoner
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private String submit(String data) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);

		RuleLCM lcm = new RuleLCM(jaxrHandle);
		content = lcm.submitReasoner(data);
		
		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;

	}

}
