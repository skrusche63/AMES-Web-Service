package de.kp.ames.web.function.scm;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.scm
 *  Module: ScmServiceImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #function #scm #service #web
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

import org.json.JSONArray;

import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.http.RequestContext;

public class ScmServiceImpl extends BusinessImpl {

	public ScmServiceImpl() {		
		super();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web
	 * .http.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {

		String methodName = this.method.getName();
		if (!(methodName.equals("get") || methodName.equals("apply"))) {
			this.sendBadRequest(ctx, new Throwable("[ScmServiceImpl] only method=get & apply supported"));
		}


		String type = this.method.getAttribute("type");
		System.out.println("====> processRequest: " + type);
		
		if (type.equals("suggest")) {

			/*
			 * Call suggest method
			 */
			String query = this.method.getAttribute("query");
			String start = this.method.getAttribute("_startRow");
			String end = this.method.getAttribute("_endRow");

			if ((!methodName.equals("get")) || (query == null) || (start == null) || (end == null)) {
				this.sendNotImplemented(ctx);

			} else {

				try {
					/*
					 * JSON response
					 */
					String content = suggest(query, start, end);
					this.sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
			}
		} else if (type.equals("search")) {
			/*
			 * Call searchmethod
			 */
			String query = this.method.getAttribute("query");
			String start = this.method.getAttribute("_startRow");
			String end = this.method.getAttribute("_endRow");

			if ((!methodName.equals("get")) || (query == null) || (start == null) || (end == null)) {
				this.sendNotImplemented(ctx);

			} else {

				try {
					/*
					 * JSON response
					 */
					String content = getResult(query, start, end);
					this.sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
			}
		} else if (type.equals("similar")) {
			String query = this.method.getAttribute("query");
			String name = this.method.getAttribute("name");
			if ((!methodName.equals("get")) || (query == null) || (name == null)) {
				this.sendNotImplemented(ctx);

			} else {
				try {
					/*
					 * JSON response
					 */

					String content = getSimilar(query, name);
					this.sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
			}
		} else if (type.equals("checkout")) {

			// access post data
			String data = this.getRequestData(ctx);
			
			System.out.println("====> ScmServiceImpl.checkout> data.len: " + data.length());
				
			if ((!methodName.equals("apply")) || (data == null)) {
				this.sendNotImplemented(ctx);

			} else {
				try {
					/*
					 * JSON response
					 */

					String content = getCheckout(data);
					this.sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
			}
		} else if (type.equals("download")) {

			// access post data when send with doApply
			//	String data = this.getRequestData(ctx);
			
			// access post data, from named FORM field
			String data = ctx.getRequest().getParameter("hiddenField");
			
			System.out.println("====> ScmServiceImpl.download> data.len: " + data.length());
				
			
			if ((!methodName.equals("apply")) || (data == null)) {
				this.sendNotImplemented(ctx);

			} else {
				try {
					/*
					 * JSON response
					 */

					byte[] bytes = getDownload(data);
					this.sendZIPResponse(bytes, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
			}
		}
	}

	/**
	 * Term suggestion returns a JSON object as response
	 * 
	 * @param query
	 * @param start
	 * @return
	 * @throws Exception
	 */
	private String suggest(String query, String start, String end) throws Exception {
		
		return new ScmDQM().suggest(query, start, end);
	}

	/**
	 * Documents search based on suggestion returns a JSON object as response
	 * 
	 * @param query
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	private String getResult(String query, String start, String end) throws Exception {
	
		return new ScmDQM().result(query, start, end);
		
	}

	/**
	 * Similar documents returns a JSON object as response
	 * 
	 * @param query
	 * @param name
	 * @return
	 * @throws Exception
	 */
	private String getSimilar(String query, String name) throws Exception {

		return new ScmDQM().similar(query, name);
		
	}
	
	/**
	 * Computes a checkout HTML-form and embeds it in a JSON object as response
	 * 
	 * @param data
	 * @return
	 * @throws Exception 
	 */
	private String getCheckout(String data) throws Exception {

		JSONArray jCheckout = new JSONArray(data);
		return new ScmDQM().checkout(jCheckout);

	}

	/**
	 * Download all Java Module from a checkout as a ZIP response
	 * 
	 * @param data
	 * @return
	 * @throws Exception 
	 */
	private byte[] getDownload(String data) throws Exception {

		JSONArray jCheckout = new JSONArray(data);
		return new ScmDQM().download(jCheckout);
		
	}

	

}
