package de.kp.ames.web.core.util;
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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javax.activation.DataSource;

/**
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */

public class FileUtil {

	/* 
	 * The predefined buffer size when retrieving repository items
	 */
	private static final int BUFFER_SIZE = 1024;

	private String filename;
	private String mimetype;

	private String name;
	
	private int length;
	
	private byte[] file;
	private InputStream inputStream;

	private String identifier;

	/**
	 * Constructor
	 */
	public FileUtil() {		
	}

	/**
	 * Constructor requires byte array and mimetype
	 * 
	 * @param file
	 * @param mimetype
	 */
	public FileUtil(byte[] file, String mimetype) {
		setFile(file, mimetype);
	}	
	
	/**
	 * Constructor requires input stream and mimetype
	 * 
	 * @param inputStream
	 * @param mimetype
	 */
	public FileUtil(InputStream inputStream, String mimetype) {
		setInputStream(inputStream, mimetype);
	}	

	/**
	 * @return
	 */
	public InputStream getInputStream() {
		
		if (this.inputStream == null)
			return new ByteArrayInputStream(file);

		return this.inputStream;
	}
	
	/**
	 * @return
	 */
	public byte[] getFile() {
		return this.file;
	}
	
	/**
	 * @return
	 */
	public int getLength() {
		return this.length;		
	}
	
	/**
	 * @return
	 */
	public String getMimetype() {
		return this.mimetype;
	}
	
	/**
	 * @return
	 */
	public String getFilename() {
		return this.filename;
	}

	/**
	 * @param identifier
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	/**
	 * @return
	 */
	public String getIdentifier() {
		return this.identifier;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @param file
	 * @param mimetype
	 */
	public void setFile(byte[] file, String mimetype) {

		this.file   = file;

		this.length = this.file.length;
		this.mimetype = mimetype;
		
	}
	
	/**
	 * @param filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @param mimetype
	 */
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	/**
	 * @param inputStream
	 * @param mimetype
	 */
	public void setInputStream(InputStream inputStream, String mimetype) {

		this.file = getByteArrayFromInputStream(inputStream);

		this.length = this.file.length;
		this.mimetype = mimetype;
		
	}

    /**
     * @param is
     * @return
     */
    public static byte[] getByteArrayFromInputStream(InputStream is) {
    	
    	ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();

    	byte[] buffer = new byte[BUFFER_SIZE];
        int len;
        
        try {
        	while ((len = is.read(buffer, 0, buffer.length)) != -1) {
        		baos.write(buffer, 0, len);
        	}
        	//is.close();

        } catch (IOException e) {
        	e.printStackTrace();
        }
        
        return baos.toByteArray();
        //return content;
    
    }

    /**
     * A helper method to convert an InputStream into a String
     * 
     * @param is
     * @return
     */
    public static String getStringFromInputStream(InputStream is) {
 
		try {

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));			
			StringBuilder stringBuilder = new StringBuilder();

			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}

			bufferedReader.close();
			return stringBuilder.toString();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
   	
    }
    
    /**
     * @param bytes
     * @param mimetype
     * @return
     */
    public static ByteArrayDataSource createByteArrayDataSource(byte[] bytes, String mimetype) {
    	return new ByteArrayDataSource(bytes, mimetype);
    }
    
	/**
	 * @param value
	 * @return
	 */
	public static int toIntegerValue(String value) {

		try {
			return Float.valueOf(value).intValue();		
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} 
			
		finally {}
		
		return 0;

	}
  
}

class ByteArrayDataSource implements DataSource {
   
	byte bytes[];
	String contentType;
   
	public ByteArrayDataSource(byte bytes[], String contentType) {
	   
		this.bytes = bytes;
		this.contentType = contentType;
       
	}
   
	public String getContentType() {
		return contentType;
	}
   
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(bytes);
	}
   
	public String getName() {
		// unknown
		throw new UnsupportedOperationException("ByteArrayDataSource.getName()");
	}
   
	public OutputStream getOutputStream() throws java.io.IOException {
		// not required, do not expose
		throw new UnsupportedOperationException("ByteArrayDataSource.getOutputStream()");
	}

}
