package de.kp.ames.web.core.graphics;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.graphics
 *  Module: GraphicsUtil
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #graphics #util #web
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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class GraphicsUtil {

	public static int PRE_WIDTH  = 480;
	public static int PRE_HEIGHT = 480;

	public static int SRC_WIDTH  = 200;
	public static int SRC_HEIGHT = 200;

	public static int THUMB_WIDTH  = 96;
	public static int THUMB_HEIGHT = 64;

	/**
	 * @param img
	 * @param x
	 * @param y
	 * @return
	 */
	public static Rectangle getClipRectangle(BufferedImage img, int x, int y) {

		int[] aux     = { 255, 255, 255, 255 };
		int[] bgColor = { 255, 255, 255, 255 };

		Raster raster = img.getRaster();

		/* 
		 * Retrieve the background color
		 */
		raster.getPixel(x, y, bgColor);

		Point tl = new Point(0, 0);
		Point br = new Point(raster.getWidth() - 1, raster.getHeight() - 1);

		/* 
		 * Find the left border
		 */
		boolean gotLef = false;
		for (int c = 0; !gotLef && (c < raster.getWidth()); c++) {
			for (int r = 0; r < raster.getHeight(); r++) {
				int[] pix = raster.getPixel(c, r, aux);
				if (comparePixel(bgColor, pix)) {
					tl.x = c;
					gotLef = true;
					break;
				}
			}
		}

		/* 
		 * Find the right border
		 */
		boolean gotRig = false;
		for (int c = raster.getWidth() - 1; !gotRig && (c >= 0); c--) {
			// Find the right
			for (int r = 0; r < raster.getHeight(); r++) {
				int[] pix = raster.getPixel(c, r, aux);
				if (comparePixel(bgColor, pix)) {
					br.x = c;
					gotRig = true;
					break;
				}
			}
		}

		/* 
		 * Find the top border
		 */
		boolean gotTop = false;
		for (int r = 0; !gotTop && (r < raster.getHeight()); r++) {
			for (int c = tl.x; c < br.x; c++) {
				int[] pix = raster.getPixel(c, r, aux);
				if (comparePixel(bgColor, pix)) {
					tl.y = r;
					gotTop = true;
					break;
				}
			}
		}

		/* 
		 * Find the bottom border
		 */
		boolean gotBot = false;
		for (int r = raster.getHeight() - 1; !gotBot && (r >= 0); r--) {
			for (int c = tl.x; c < br.x; c++) {
				int[] pix = raster.getPixel(c, r, aux);
				if (comparePixel(bgColor, pix)) {
					br.y = r;
					gotBot = true;
					break;
				}
			}
		}

		Rectangle rect = new Rectangle(tl.x, tl.y, Math.abs(br.x - tl.x) + 1, Math.abs(br.y - tl.y) + 1);
		return rect;
		
	}
	
	/**
	 * Compares two pixels and returns true if they 
	 * are different, otherwise returns false
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static boolean comparePixel(int[] p1, int[] p2) {
		return ((p2[0] == p1[0]) && (p2[1] == p1[1]) && (p2[2] == p1[2]) && (p2[3] == p1[3]) ? false : true);
	}

	/**
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage createImage(int width, int height) {
		return new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
	}

	/**
	 * @param image
	 * @param clip
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage crop(BufferedImage image, Rectangle clip) throws Exception {

		/* 
		 * Create the return image
		 */
		BufferedImage retval = createImage(clip.width, clip.height);
		Graphics2D g2 = retval.createGraphics();
		
		/* 
		 * Render the clip region
		 */
		g2.drawImage(image.getSubimage(clip.x, clip.y, clip.width, clip.height), 0, 0, null);

		g2.dispose();
		retval.flush();

		return retval;
		
	}

	/**
	 * @param img
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage createPreview(BufferedImage img) throws Exception {
		
		BufferedImage preview = null;
			
		Graphics2D g2 = null;
		
		/* 
		 * Retrieve image dimensions
		 */
		int imgHeight = img.getHeight(null);
		int imgWidth  = img.getWidth(null);
		
		int scaledWidth  = 0;
		int scaledHeight = 0 ;

		/* 
		 * Position the image on the background
		 */
		int scaledTop  = 0;
		int scaledLeft = 0;

		if (imgWidth > imgHeight) {
				
			scaledWidth  = PRE_WIDTH;
			scaledHeight = (PRE_HEIGHT * imgHeight) / imgWidth;
			
			scaledLeft = 0;
			scaledTop  = (PRE_HEIGHT -  scaledHeight) / 2;
				
		} else {

			scaledWidth  = (PRE_WIDTH * imgWidth) / imgHeight;
			scaledHeight = PRE_HEIGHT;

			scaledLeft = (PRE_WIDTH - scaledWidth) / 2;
			scaledTop  = 0;
		}
			
		BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
		g2 = scaledImage.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		g2.drawImage(img, 0, 0, scaledWidth, scaledHeight, null);
		g2.dispose();
			
		preview = new BufferedImage(PRE_WIDTH, PRE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g2 = preview.createGraphics();


		g2.drawImage(scaledImage, scaledLeft, scaledTop, null);
		g2.dispose();

		return trim(preview);
				
	}

	/**
	 * @param image
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage createReflection(BufferedImage image) throws Exception {
		
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

	/**
	 * @param img
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage createSource(BufferedImage img) throws Exception {

		BufferedImage source = null;		
		Graphics2D g2 = null;
				
		/* 
		 * Retrieve image dimensions
		 */
		int imgHeight = img.getHeight(null);
		int imgWidth  = img.getWidth(null);
		
		int scaledWidth  = 0;
		int scaledHeight = 0 ;

		/* 
		 * Position the image on the background
		 */
		int scaledTop  = 0;
		int scaledLeft = 0;

		if (imgWidth > imgHeight) {
				
			scaledWidth  = SRC_WIDTH;
			scaledHeight = (SRC_HEIGHT * imgHeight) / imgWidth;
			
			scaledLeft = 0;
			scaledTop  = (SRC_HEIGHT -  scaledHeight) / 2;
				
		} else {

			scaledWidth  = (SRC_WIDTH * imgWidth) / imgHeight;
			scaledHeight = PRE_HEIGHT;

			scaledLeft = (SRC_WIDTH - scaledWidth) / 2;
			scaledTop  = 0;
		}
			
		BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
		g2 = scaledImage.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		g2.drawImage(img, 0, 0, scaledWidth, scaledHeight, null);
		g2.dispose();
			
		source = new BufferedImage(SRC_WIDTH, SRC_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g2 = source.createGraphics();


		g2.drawImage(scaledImage, scaledLeft, scaledTop, null);
		g2.dispose();

		return source;

	}
	
	/**
	 * @param img
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage createThumbnail(BufferedImage img) throws Exception {
		
		BufferedImage thumbnail = null;			
		Graphics2D g2 = null;
			
		/* 
		 * Retrieve image dimensions
		 */
		int imgHeight = img.getHeight(null);
		int imgWidth  = img.getWidth(null);
		
		int scaledWidth  = 0;
		int scaledHeight = 0 ;

		/* 
		 * Position the image on the thumbnail
		 */
		int scaledTop  = 0;
		int scaledLeft = 0;

		if (imgWidth > imgHeight) {
			
			scaledWidth  = THUMB_WIDTH;
			scaledHeight = (THUMB_HEIGHT * imgHeight) / imgWidth;
			
			scaledLeft = 0;
			scaledTop  = (THUMB_HEIGHT -  scaledHeight) / 2;
			
		} else {

			scaledWidth  = (THUMB_WIDTH * imgWidth) / imgHeight;
			scaledHeight = THUMB_HEIGHT;

			scaledLeft = (THUMB_WIDTH - scaledWidth) / 2;
			scaledTop  = 0;
		}
			
		BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);

		g2 = scaledImage.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		g2.drawImage(img, 0, 0, scaledWidth, scaledHeight, null);
		g2.dispose();
			
		thumbnail = new BufferedImage(THUMB_WIDTH, THUMB_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g2 = thumbnail.createGraphics();


		g2.drawImage(scaledImage, scaledLeft, scaledTop, null);
		g2.dispose();
			
		return thumbnail;
				
	}

	/**
	 * Trim excess whitespace from the given image
	 * 
	 * @param image
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage trim(BufferedImage image) throws Exception {

		Rectangle clip = getClipRectangle(image, 0, 0);
		return crop(image, clip);
		
	}

	/**
	 * @param img
	 * @param center
	 * @param bgColor
	 * @return
	 */
	public static BufferedImage trimAroundCenter(BufferedImage img, Point center, Color bgColor) {
		
		int width = img.getWidth(null);
		width = 2 * Math.max(center.x, (width - center.x));

		int height = img.getHeight(null);
		height = 2 * Math.max(center.y, (height - center.y));

		/* 
		 * Create the return image
		 */
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bi.createGraphics();
		g2.setColor(bgColor);
		g2.fillRect(0, 0, width, height);

		g2.drawImage(img, width / 2 - center.x, height / 2 - center.y, null);

		g2.dispose();

		bi.flush();

		return bi;
		
	}

}
