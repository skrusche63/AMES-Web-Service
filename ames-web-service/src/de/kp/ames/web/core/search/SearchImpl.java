package de.kp.ames.web.core.search;
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

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import de.kp.ames.web.core.service.ServiceImpl;
import de.kp.ames.web.http.RequestContext;

public class SearchImpl extends ServiceImpl {

	/**
	 * Constructor
	 */
	public SearchImpl() {
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.core.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {		

		String methodName = this.method.getName();
		if (methodName.equals(SearchConstants.METH_SUGGEST)) {
			
			/*
			 * Call suggest method
			 */
			String request = this.method.getAttribute(SearchConstants.ATTR_REQUEST);
			String start   = this.method.getAttribute(SearchConstants.ATTR_START);
			String limit   = this.method.getAttribute(SearchConstants.ATTR_LIMIT);
			
			if ((request == null) || (start == null) || (limit == null)) {

				String errorMessage = "[" + this.getClass().getName() + "] Required parameters not provided.";
				int errorStatus = HttpServletResponse.SC_NOT_IMPLEMENTED;
				
				try {
					sendErrorResponse(errorMessage, errorStatus, ctx.getResponse());

				} catch (IOException e1) {
					// do nothing
				}
				
			} else {
				
				try {
					String content = suggest(request, start, limit);
					this.sendJSONResponse(content, ctx.getResponse());
					
				} catch (Exception e) {

					String errorMessage = "[" + this.getClass().getName() + "] " + e.getMessage();
					int errorStatus = HttpServletResponse.SC_BAD_REQUEST;
					
					try {
						sendErrorResponse(errorMessage, errorStatus, ctx.getResponse());

					} catch (IOException e1) {
						// do nothing
					}

				}
				
			}
			
		}
	}

	/**
	 * Term suggestion returns a JSON object as response
	 * 
	 * @param request
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	private String suggest(String request, String start, String limit) throws Exception {
		return SearchClient.getInstance().suggest(request, start, limit);
	}
	
}
