package de.kp.ames.web.core.search;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.search
 *  Module: SearchServiceImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #search #service #web
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

import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.MethodConstants;

public class SearchServiceImpl extends BusinessImpl {

	/**
	 * Constructor
	 */
	public SearchServiceImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {		

		String methodName = this.method.getName();
		if (methodName.equals(MethodConstants.METH_FACET)) {
			
			/*
			 * Call facet method
			 */

			try {
				/*
				 * JSON response
				 */
				String content = facet();
				this.sendJSONResponse(content, ctx.getResponse());
				
			} catch (Exception e) {
				this.sendBadRequest(ctx, e);

			}

		} else if (methodName.equals(MethodConstants.METH_SEARCH)) {
			/*
			 * Call search method
			 */
			String query = this.method.getAttribute(MethodConstants.ATTR_QUERY);

			String start = this.method.getAttribute(SearchConstants.ATTR_START);
			String limit = this.method.getAttribute(SearchConstants.ATTR_LIMIT);

			if ((query == null) || (start == null) || (limit == null)) {
				this.sendNotImplemented(ctx);
			
			} else {
				
				try {
					/*
					 * JSON response
					 */
					String content = search(query, start, limit);
					this.sendJSONResponse(content, ctx.getResponse());
					
				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			}

			
		} else if (methodName.equals(MethodConstants.METH_SUGGEST)) {			
			/*
			 * Call suggest method
			 */
			String query = this.method.getAttribute(MethodConstants.ATTR_QUERY);
			//String start = this.method.getAttribute(SearchConstants.ATTR_START);
			//String limit = this.method.getAttribute(SearchConstants.ATTR_LIMIT);
			
			String start = "0";
			String limit = "10";
			
			//if ((query == null) || (start == null) || (limit == null)) {
			if (query == null) {
				this.sendNotImplemented(ctx);
				
			} else {
				
				try {
					/*
					 * JSON response
					 */
					String content = suggest(query, start, limit);
					this.sendJSONResponse(content, ctx.getResponse());
					
				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			}
			
		}
	}

	/**
	 * Zero-term search
	 * 
	 * @return
	 * @throws Exception
	 */
	private String facet() throws Exception {
		return new SearcherImpl().facet();
	}

	/**
	 * Term-based search
	 * 
	 * @param query
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	private String search(String query, String start, String limit) throws Exception {
		return new SearcherImpl().search(query, start, limit);
	}
	
	/**
	 * Term suggestion returns a JSON object as response
	 * 
	 * @param query
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	private String suggest(String query, String start, String limit) throws Exception {
		return new SearcherImpl().suggest(query, start, limit);
	}
	
}
