package de.kp.ames.web.function.map;
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

import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.FormatConstants;
import de.kp.ames.web.shared.MethodConstants;

public class MapServiceImpl extends BusinessImpl {
	
	public MapServiceImpl() {		
		super();
	}
	
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(MethodConstants.METH_NODES)) {
			/*
			 * Call nodes method
			 */
			String source = this.method.getAttribute(MethodConstants.ATTR_SOURCE);			
			if (source == null) {
				this.sendNotImplemented(ctx);

			} else {

				try {
					/*
					 * Kml response
					 */
					String content = nodes(source);
					sendXMLResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			}

		} else if (methodName.equals(MethodConstants.METH_EDGES)) {
			/*
			 * Call edges method
			 */
			String source = this.method.getAttribute(MethodConstants.ATTR_SOURCE);			
			if (source == null) {
				this.sendNotImplemented(ctx);

			} else {

				try {
					/*
					 * JSON response
					 */
					String content = edges(source);
					sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}
				
			}

		} else if (methodName.equals(MethodConstants.METH_LAYERS)) {
			/*
			 * Call layers method
			 */
			String endpoint = this.method.getAttribute(FncConstants.ATTR_ENDPOINT);			
			if (endpoint == null) {
				this.sendNotImplemented(ctx);

			} else {

				String start = this.method.getAttribute(FncConstants.ATTR_START);			
				String limit = this.method.getAttribute(FncConstants.ATTR_LIMIT);			

				try {
					/*
					 * JSON response
					 */
					String content = layers(endpoint, start, limit);
					sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}

			}
			
		}
	}

	/**
	 * This method retrieves a kml representation of a certain registry package; 
	 * the kml however only contains the nodes of the respective package, 
	 * the according edges are retrieved by another method
	 * 
	 * @param source
	 * @return
	 * @throws Exception 
	 */
	private String nodes(String source) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);
		
		/*
		 * Retrieve kml representation from source
		 */
		MapDQM dqm = new MapDQM(jaxrHandle);
		content =dqm.getNodes(source);

		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
	
	}

	/**
	 * This method retrieves a kml representation of all associations
	 * that are members of a specific registry package
	 * 
	 * @param source
	 * @return
	 * @throws Exception 
	 */
	private String edges(String source) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);
		
		/*
		 * Retrieve kml representation from source
		 */
		MapDQM dqm = new MapDQM(jaxrHandle);
		content = dqm.getEdges(source);

		/*
		 * Logoff
		 */
		JaxrClient.getInstance().logoff(jaxrHandle);
		return content;
	
	}

	/**
	 * This method returns all registered layers of a certain
	 * geo server in a SmartGwt 3.0 compliant Grid (JSON) result
	 * 
	 * @return
	 */
	private String layers(String endpoint, String start, String limit) throws Exception {
		
		/*
		 * Connect to WMS service defined by endpoint parameter
		 */
		MapDQM dqm = new MapDQM(jaxrHandle);
		JSONArray jArray = dqm.getLayers(endpoint);
		
		/*
		 * Render result
		 */
		String format = FormatConstants.FNC_FORMAT_ID_Grid;
		return render(jArray, start, limit, format);

	}
	
}
