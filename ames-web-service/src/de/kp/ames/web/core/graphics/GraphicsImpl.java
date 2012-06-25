package de.kp.ames.web.core.graphics;
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

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

import de.kp.ames.web.Bundle;
import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.service.ServiceImpl;
import de.kp.ames.web.http.RequestContext;
import de.kp.ames.web.shared.MethodConstants;

public class GraphicsImpl extends ServiceImpl {

	public GraphicsImpl() {		
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
			String imageParam = Bundle.getInstance().getString(GlobalConstants.ATTR_IMAGE);
			
			String image = this.method.getAttribute(imageParam);			
			if (image == null) {
				this.sendNotImplemented(ctx);
				
			} else {

				try {
					/*
					 * PNG response
					 */
					InputStream stream = ctx.getContext().getResourceAsStream("/WEB-INF/resources/" + image);
					BufferedImage reflection = GraphicsUtil.createReflection(ImageIO.read(stream));

					sendImageResponse(reflection, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}

			}
			
		}
		
	}

}
