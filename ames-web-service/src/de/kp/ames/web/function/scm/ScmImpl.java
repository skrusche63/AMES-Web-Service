package de.kp.ames.web.function.scm;
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

import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.http.RequestContext;

/**
 * ScmImpl supports access to an OASIS ebXML RegRep to retrieve
 * registered anchors to a GIT repository
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */
public class ScmImpl extends BusinessImpl {

	public ScmImpl() {		
		super();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.core.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(FncConstants.METH_EXPLORE)) {

			/*
			 * Call explore method
			 */
			String parent = this.method.getAttribute(FncConstants.ATTR_URI);
			if (parent == null) {
				this.sendNotImplemented(ctx);
				
			} else {

				try {
					String content = explore(parent);
					this.sendHTMLResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			}

		} else if (methodName.equals(FncConstants.METH_MODULE)) {

			/*
			 * Call module method
			 */
			String uri = this.method.getAttribute(FncConstants.ATTR_URI);
			if (uri == null) {
				this.sendNotImplemented(ctx);
				
			} else {

				try {
					String content = module(uri);
					if (content == null) {
						this.sendNotFound(ctx);
						
					} else {
						this.sendHTMLResponse(content, ctx.getResponse());
						
					}

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			}

		}
		
	}

	/**
	 * A helper method to retrieve the (tree) structure
	 * of a certain source code project from an OASIS
	 * ebXML RegRep
	 * 
	 * @param parent
	 * @return
	 */
	private String explore(String parent) {
		/*
		 * Access to the OASIS ebXML RegRep to retrieve
		 * the structure of a source code project
		 */
		ScmDQM dqm = new ScmDQM(jaxrHandle);
		return dqm.explore(parent);
		
	}
		
	/**
	 * A helper method to (a) retrieve a source code module from
	 * an associated SCM system, (b) convert the module into html,
	 * and return it back to the caller's user
	 * 
	 * Actually only Java modules are supported
	 * 
	 * @param uri
	 * @return
	 */
	private String module(String uri) {
		/*
		 * Access the local Git Repository to
		 * retrieve a certain Java source module
		 */
		RepoClient repoClient = new RepoClient();
		return repoClient.getModuleAsHtml(uri);

	}

	/**
	 * A helper method to return a Not Found HTML response
	 * @param ctx
	 * @return
	 * @throws IOException 
	 */
	private void sendNotFound(RequestContext ctx) throws IOException {
		String content = null;
		// TODO
		sendHTMLResponse(content, ctx.getResponse());
	}
}
