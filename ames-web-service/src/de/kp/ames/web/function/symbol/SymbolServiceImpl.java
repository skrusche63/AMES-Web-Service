package de.kp.ames.web.function.symbol;
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
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;

public class SymbolServiceImpl extends BusinessImpl {

	public SymbolServiceImpl() {
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

		/*
		 * Call keys method
		 */
		String format = this.method.getAttribute(MethodConstants.ATTR_FORMAT);	
		String type   = this.method.getAttribute(MethodConstants.ATTR_TYPE);	

		if ((format == null) || (type == null)) {
			this.sendNotImplemented(ctx);

		} else {

			if (format.equals(FormatConstants.FNC_FORMAT_ID_Tree)) {
			
				String parent = this.method.getAttribute(MethodConstants.ATTR_PARENT);
	
				try {
					/*
					 * JSON response
					 */
					String content = keys(type, parent);
					sendJSONResponse(content, ctx.getResponse());
	
				} catch (Exception e) {
					this.sendBadRequest(ctx, e);
	
				}
				
			} else if (format.equals(FormatConstants.FNC_FORMAT_ID_Grid)) {
				
				String item = this.method.getAttribute(MethodConstants.ATTR_ITEM);	

				String affiliation = this.method.getAttribute(FncConstants.ATTR_AFFILIATION);
				String echelon     = this.method.getAttribute(FncConstants.ATTR_ECHELON);

				try {
					/*
					 * JSON response
					 */
					String content = symbols(type, item, affiliation, echelon);
					sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}

			}
			
		}
		
	} 
	
	/**
	 * Get control information for either APP-6-B or
	 * Icon-based symbols
	 * 
	 * @param type
	 * @param parent
	 * @return
	 * @throws Exception
	 */
	private String keys(String type, String parent) throws Exception {
		
		String content = null;
		
		if (type.equals(ClassificationConstants.FNC_SYMBOL_ID_APP6B)) {
			
			SymbolDQM dqm = new SymbolDQM();
			JSONArray jArray = dqm.getAPP6bKeys(parent);
			
			/*
			 * Render result
			 */
			String format = FormatConstants.FNC_FORMAT_ID_Tree;
			content = render(jArray, format);
			
		} else if (type.equals(ClassificationConstants.FNC_SYMBOL_ID_Icon)) {

			SymbolDQM dqm = new SymbolDQM();
			JSONArray jArray = dqm.getIconKeys(parent);
			
			/*
			 * Render result
			 */
			String format = FormatConstants.FNC_FORMAT_ID_Tree;
			content = render(jArray, format);
			
		}
		
		return content;
	}

	/**
	 * Get either APP6-B or Icon-based symbols
	 * in a fixed (128 x 128) dimension
	 * 
	 * @param type
	 * @param item
	 * @param affiliation
	 * @param echelon
	 * @return
	 * @throws Exception
	 */
	private String symbols(String type, String item, String affiliation, String echelon) throws Exception {
		
		String content = null;

		if (type.equals(ClassificationConstants.FNC_SYMBOL_ID_APP6B)) {

			SymbolDQM dqm = new SymbolDQM();
			JSONArray jArray = dqm.getAPP6bSymbols(item, affiliation, echelon);
			
			/*
			 * Render result
			 */
			String format = FormatConstants.FNC_FORMAT_ID_Grid;
			content = render(jArray, format);
			
		} else if (type.equals(ClassificationConstants.FNC_SYMBOL_ID_Icon)) {

			SymbolDQM dqm = new SymbolDQM();
			JSONArray jArray = dqm.getIconSymbols(item);
			
			/*
			 * Render result
			 */
			String format = FormatConstants.FNC_FORMAT_ID_Grid;
			content = render(jArray, format);
			
		}
		
		return content;
		
	}
	
}
