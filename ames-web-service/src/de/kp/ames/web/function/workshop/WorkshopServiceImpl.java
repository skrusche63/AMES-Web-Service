package de.kp.ames.web.function.workshop;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.workshop
 *  Module: WorkshopServiceImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #function #workshop #service #web
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

import java.io.InputStream;

import javax.servlet.ServletContext;

import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.BusinessImpl;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.constants.MethodConstants;

public class WorkshopServiceImpl extends BusinessImpl {

	
	/**
	 * Constructor
	 */
	public WorkshopServiceImpl() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.core.RequestContext)
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
		 * Retrieve disclaimer as html file from the container's context
		 */		
		try {
			/*
			 * Html response
			 */
			String content = getWorkshopFile(ctx);
			this.sendHTMLResponse(content, ctx.getResponse());
			
		} catch (Exception e) {
			this.sendBadRequest(ctx, e);
			
		}
		
	}

	/**
	 * Retrieve the workshop file from the server's file system
	 * 
	 * @param ctx
	 * @return
	 */
	private String getWorkshopFile(RequestContext ctx) {

		ServletContext context = ctx.getContext();
		
		String filename = "/WEB-INF/classes/resources/workshop.html";		  
		InputStream is = context.getResourceAsStream(filename);

		return FileUtil.getStringFromInputStream(is);

	}
	
		
}
