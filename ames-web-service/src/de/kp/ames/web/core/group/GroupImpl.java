package de.kp.ames.web.core.group;

import de.kp.ames.web.core.RequestContext;
import de.kp.ames.web.core.service.ServiceImpl;
import de.kp.ames.web.function.FncConstants;

public class GroupImpl extends ServiceImpl {

	public GroupImpl() {		
	}

	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(FncConstants.METH_DELETE)) {
			/*
			 * Call delete method
			 */
		} else if (methodName.equals(FncConstants.METH_SUBMIT)) {
			/*
			 * Call submit method
			 */
		}
		
	}
	

	/*
	 * APP-6 Maker
	 * 
	 * - get
	 * 
	 * - html
	 * 
	 * - load
	 * 
	 * Sense Maker
	 * 
	 * - community as grid
	 * 
	 * - category as grid
	 * 
	 * - get namespaces
	 * 
	 * - html
	 * 
	 * - post as grid
	 * 
	 * - responsibility as grid
	 * 
	 * - role as grid
	 * 
	 * - load
	 * 
	 * - user as grid
	 * 
	 * 
	 */
}
