package de.kp.ames.web.core.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

public class ImageUtil {

	private String mimeType;
	private BufferedImage image;
	private String fileName;
	private byte[] bytes; 

	public ImageUtil() {}
	
	public ImageUtil(BufferedImage image, String mimeType) throws Exception {
		this.image = image;
		this.mimeType = mimeType;
		
		this.bytes = getBufferedImageAsByteArray();
	}

	private byte[] getBufferedImageAsByteArray() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		return baos.toByteArray();

	}

	/*
	 * Mimetype
	 */
	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/*
	 * Image
	 */
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/*
	 * Filename
	 */

	/**
	 * @return the fileName
	 */
	public String getFilename() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getLength() {
		return bytes.length;
	}

	public byte[] getBytes() {
		return bytes;
	}
}
