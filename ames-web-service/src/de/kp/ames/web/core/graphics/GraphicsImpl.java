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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

import de.kp.ames.web.Bundle;
import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.RequestContext;
import de.kp.ames.web.core.service.ServiceImpl;
import de.kp.ames.web.function.FncConstants;

public class GraphicsImpl extends ServiceImpl {

	public GraphicsImpl() {		
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.service.ServiceImpl#processRequest(de.kp.ames.web.core.RequestContext)
	 */
	public void processRequest(RequestContext ctx) {	

		String methodName = this.method.getName();
		if (methodName.equals(FncConstants.METH_GET)) {
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
					BufferedImage reflection = createReflection(ImageIO.read(stream));

					sendImageResponse(reflection, ctx.getResponse());

				} catch (Exception e) {
					this.sendBadRequest(ctx, e);

				}

			}
			
		}
		
	}

	/**
	 * @param image
	 * @return
	 * @throws Exception
	 */
	private BufferedImage createReflection(BufferedImage image) throws Exception {
		
		double hfact = 0.5;
		
		/* 
		 * Dimension of original image (e.g. 96 x 96)
		 */
		int width  = image.getWidth(null);
		int height = image.getHeight(null);
		
		/* 
		 * Result is the final image including the reflection
		 */
		BufferedImage result = new BufferedImage(width, new Double(height*(1 + hfact)).intValue(), BufferedImage.TYPE_INT_ARGB);		
		Graphics2D g = result.createGraphics();

		/* 
		 * Paint original image onto graphics
		 */
		g.drawImage(image, 0, 0, null);
		
		/* 
		 * Paint mirrored image
		 */
		g.scale(1.0, -1.0);
		g.drawImage(image, 0, -image.getHeight() * 2, null);
		g.scale(1.0, -1.0);
		
		/* 
		 * Move to mirror's origin
		 */
		g.translate(0, image.getHeight());
		
		/* 
		 * Create gradient mask
		 */
		GradientPaint mask = new GradientPaint(0, 0, new Color(1f, 1f, 1f, 0.5f), 0, image.getHeight() / 2, new Color(1f, 1f, 1f, 0f));
		g.setPaint(mask);

	    /* 
	     * Set alpha composite
	     */
		g.setComposite(AlphaComposite.DstIn);
			
		/* 
		 * Paint the mask
		 */
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.dispose();
		
		return result;

	}

}
