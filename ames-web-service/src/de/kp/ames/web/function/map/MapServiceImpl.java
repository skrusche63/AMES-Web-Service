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
import de.kp.ames.web.shared.ClassificationConstants;
import de.kp.ames.web.shared.FormatConstants;
import de.kp.ames.web.shared.MethodConstants;

public class MapServiceImpl extends BusinessImpl {
	
	public MapServiceImpl() {		
		super();
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(MethodConstants.METH_GET)) {
			/*
			 * Call get method
			 */
			doGetRequest(ctx);
			
		}

	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#doGetRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void doGetRequest(RequestContext ctx) {

		String type = this.method.getAttribute(MethodConstants.ATTR_TYPE);			

		if (type == null) {
			this.sendNotImplemented(ctx);
			
		} else {
			
			if (type.equals(ClassificationConstants.FNC_ID_Layer)) {

				String endpoint = this.method.getAttribute(MethodConstants.ATTR_ENDPOINT);			
				if (endpoint == null) {
					this.sendNotImplemented(ctx);

				} else {

					String start = this.method.getAttribute(FncConstants.ATTR_START);			
					String limit = this.method.getAttribute(FncConstants.ATTR_LIMIT);			

					try {
						/*
						 * JSON response
						 */
						String content = getJSONLayers(endpoint, start, limit);
						sendJSONResponse(content, ctx.getResponse());

					} catch (Exception e) {
						this.sendBadRequest(ctx, e);

					}

				}
				
			} else {

				String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);			

				if (item == null) {
					this.sendNotImplemented(ctx);

				} else {
					/*
					 * Retrieve Kml based information
					 */
					try {
						/*
						 * JSON response
						 */
						String content = getJSONResponse(type, item);
						sendJSONResponse(content, ctx.getResponse());
		
					} catch (Exception e) {
						this.sendBadRequest(ctx, e);
		
					}
					
				}
				
			}
			
		}

	}

	/**
	 * @param type
	 * @param item
	 * @return
	 * @throws Exception
	 */
	private String getJSONResponse(String type, String item) throws Exception {

		String content = null;
		
		/*
		 * Login
		 */		
		JaxrClient.getInstance().logon(jaxrHandle);		
			
		if (type.equals(ClassificationConstants.FNC_ID_Edge)) {			
			/*
			 * This method retrieves a kml representation of all associations
			 * that are members of a specific registry package
			 */
			MapDQM dqm = new MapDQM(jaxrHandle);
			content = dqm.getEdges(item);

		} else if (type.equals(ClassificationConstants.FNC_ID_Node)) {
			/*
			 * This method retrieves a kml representation of a certain registry package; 
			 * the kml however only contains the nodes of the respective package, 
			 * the according edges are retrieved by another method
			 */
			MapDQM dqm = new MapDQM(jaxrHandle);
			content = dqm.getNodes(item);

		} else {
			throw new Exception("[MapServiceImpl] Information type <" + type + "> is not supported");
			
		}
		
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
	private String getJSONLayers(String endpoint, String start, String limit) throws Exception {
		
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
