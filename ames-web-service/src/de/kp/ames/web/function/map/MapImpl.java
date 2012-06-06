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

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;

import de.kp.ames.web.core.RequestContext;
import de.kp.ames.web.core.regrep.JaxrClient;
import de.kp.ames.web.core.service.ServiceImpl;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.function.GuiFactory;
import de.kp.ames.web.function.GuiRenderer;
import de.kp.ames.web.function.access.wms.WmsConsumer;

public class MapImpl extends ServiceImpl {

	/*
	 * Reference to the registered renderer
	 */
	private GuiRenderer renderer;
	
	public MapImpl() {		
		renderer = GuiFactory.getInstance().getRenderer();
	}
	
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(FncConstants.METH_NODES)) {
			/*
			 * Call nodes method
			 */
			String source = this.method.getAttribute(FncConstants.ATTR_SOURCE);			
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

		} else if (methodName.equals(FncConstants.METH_EDGES)) {
			/*
			 * Call edges method
			 */
			String source = this.method.getAttribute(FncConstants.ATTR_SOURCE);			
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

		} else if (methodName.equals(FncConstants.METH_LAYERS)) {
			/*
			 * Call layers method
			 */
			String endpoint = this.method.getAttribute(FncConstants.ATTR_ENDPOINT);			
			if (endpoint == null) {
				this.sendNotImplemented(ctx);

			} else {
				/*
				 * Additional request parameters are directly provided
				 * by a (e.g.) SmartGwt 3.0 widget (Grid) and must be 
				 * retrieved from the respective Http Request
				 */
				HttpServletRequest request = ctx.getRequest();
				
				String startParam = renderer.getStartParam();
				String start = request.getParameter(startParam);
				
				String limitParam = renderer.getLimitParam();
				String limit = request.getParameter(limitParam);

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
		content =dqm.getEdges(source);

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
	private String layers(String endpoint, String start, String limit) {
		
		/*
		 * Connect to WMS service defined by endpoint parameter
		 */
		WmsConsumer wmsConsumer = new WmsConsumer(endpoint);		
		JSONArray jCapabilities = wmsConsumer.getCapabilitiesAsJson();
		
		/*
		 * Process result to be compliant to request SmartGwt 3.0 GUI
		 */
		return renderer.createGrid(jCapabilities, start, limit);

	}
	
}
