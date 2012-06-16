package de.kp.ames.web.function.symbol;

import org.json.JSONArray;

import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.function.FncConstants;
import de.kp.ames.web.http.RequestContext;

public class SymbolServiceImpl extends BusinessImpl {

	public SymbolServiceImpl() {
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.http.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(FncConstants.METH_KEYS)) {

			/*
			 * Call keys method
			 */
			String type = this.method.getAttribute(FncConstants.ATTR_TYPE);	

			if (type == null) {
				this.sendNotImplemented(ctx);

			} else {

				String parent = this.method.getAttribute(FncConstants.ATTR_PARENT);

				try {
					/*
					 * JSON response
					 */
					String content = keys(type, parent);
					sendJSONResponse(content, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}

			}
			
		} else if (method.equals(FncConstants.METH_SYMBOLS)) {

			String type = this.method.getAttribute(FncConstants.ATTR_TYPE);	
			String item = this.method.getAttribute(FncConstants.ATTR_ITEM);	

			if (type == null) {
				this.sendNotImplemented(ctx);
				
			} else {

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
		
		if (type.equals(FncConstants.FNC_SYMBOL_ID_APP6B)) {
			
			SymbolDQM dqm = new SymbolDQM();
			JSONArray jArray = dqm.getAPP6bKeys(parent);
			
			/*
			 * Render result
			 */
			String format = FncConstants.FNC_FORMAT_ID_Tree;
			content = render(jArray, format);
			
		} else if (type.equals(FncConstants.FNC_SYMBOL_ID_Icon)) {

			SymbolDQM dqm = new SymbolDQM();
			JSONArray jArray = dqm.getIconKeys(parent);
			
			/*
			 * Render result
			 */
			String format = FncConstants.FNC_FORMAT_ID_Tree;
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

		if (type.equals(FncConstants.FNC_SYMBOL_ID_APP6B)) {

			SymbolDQM dqm = new SymbolDQM();
			JSONArray jArray = dqm.getAPP6bSymbols(item, affiliation, echelon);
			
			/*
			 * Render result
			 */
			String format = FncConstants.FNC_FORMAT_ID_Grid;
			content = render(jArray, format);
			
		} else if (type.equals(FncConstants.FNC_SYMBOL_ID_Icon)) {

			SymbolDQM dqm = new SymbolDQM();
			JSONArray jArray = dqm.getIconSymbols(item);
			
			/*
			 * Render result
			 */
			String format = FncConstants.FNC_FORMAT_ID_Grid;
			content = render(jArray, format);
			
		}
		
		return content;
		
	}
	
}
