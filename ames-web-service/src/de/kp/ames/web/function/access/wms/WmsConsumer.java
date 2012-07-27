package de.kp.ames.web.function.access.wms;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.geotools.data.ows.AbstractGetCapabilitiesRequest;
import org.geotools.data.ows.GetCapabilitiesResponse;
import org.geotools.data.ows.Request;
import org.geotools.data.ows.Response;
import org.geotools.ows.ServiceException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.kp.ames.web.GlobalConstants;

/**
 * WmsConsumer is a WMS client that actually initiates
 * a GetCapabilities Request to retrieve the set of
 * registered layers from a certain WMS server
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */
public class WmsConsumer {
	
	private URL serverUrl;

	/*
	 * JSON names
	 */
	private static String J_BBOX  = "bbox";
	private static String J_MAXX  = "maxx";
	private static String J_MAXY  = "maxy";
	private static String J_MINX  = "minx";
	private static String J_MINY  = "miny";
	private static String J_NAME  = "name";
	private static String J_SRS   = "srs";
	private static String J_TITLE = "title";
	
	/* 
	 * WMS Tag Names
	 */
	private static String WMS_BBOX      = "LatLonBoundingBox";
	private static String WMS_LAYER     = "Layer";
	private static String WMS_MAXX      = "maxx";
	private static String WMS_MAXY      = "maxy";
	private static String WMS_MINX      = "minx";
	private static String WMS_MINY      = "miny";
	private static String WMS_NAME      = "Name";
	private static String WMS_QUERYABLE = "queryable";
	private static String WMS_SRS       = "SRS";
	private static String WMS_TITLE     = "Title";
	
    public WmsConsumer(String endpoint) {
   	
	   	 /* 
	   	  * Build WMS Server Url
	   	  */
	   	 try {
	   		 this.serverUrl = new URL(endpoint + GlobalConstants.WMS_PATH);
	   		 
	   	 } catch (Exception e) {
	   		 e.printStackTrace();
	   	 
	   	 } finally {}
	
	}

    /**
     * Wrapper method to return a response of 
     * a WMS CapabilitiesRequest request as a
     * stream
     * 
     * @return
     */
    public InputStream getCapabilitiesAsStream() {
    	
    	if (this.serverUrl == null) return null;
    	
    	try {
	    	CapabilitiesRequest request = new CapabilitiesRequest(this.serverUrl);

	    	CapabilitiesResponse response = (CapabilitiesResponse)sendRequest(request);	    	
	    	return response.asStream();
	    	
    	} catch (Exception e) {
    		e.printStackTrace();
    		
    	} finally {}
    	
    	return null;
    	
    }

	/**
     * Wrapper method to return a response of 
     * a WMS CapabilitiesRequest request as a
     * JSON array
     * 
	 * @return
	 */
	public JSONArray getCapabilitiesAsJson() {
    	
    	if (this.serverUrl == null) return null;
 
    	try {
	    	CapabilitiesRequest request = new CapabilitiesRequest(this.serverUrl);
	    	
	    	CapabilitiesResponse response = (CapabilitiesResponse)sendRequest(request);
	    	return response.asJSON();
	    	
    	} catch (Exception e) {
    		e.printStackTrace();
    		
    	} finally {}
    	
    	return null;

    }
     
    /**
     * Issues a request to the server and returns that 
     * server's response. It asks the server to send the 
     * response gzipped to provide a faster transfer time.
     * 
     * @param request
     * @return
     * @throws IOException
     * @throws ServiceException
     */
    public Response sendRequest(Request request) throws IOException, ServiceException {

    	// retrieve server url
    	URL finalURL = request.getFinalURL();

        HttpURLConnection connection = (HttpURLConnection) finalURL.openConnection();
        connection.addRequestProperty("Accept-Encoding", "gzip");

       	connection.setRequestMethod("GET");
        InputStream is = connection.getInputStream();
 
        if (connection.getContentEncoding() != null && connection.getContentEncoding().indexOf("gzip") != -1) {
            is = new GZIPInputStream(is);
        }

        String contentType = connection.getContentType();
        return request.createResponse(contentType, is);        
        
    }
 
    /**
     * A capabilities request for a WMS getCapabilities Request
     * 
     * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
     *
     */
    class CapabilitiesRequest extends AbstractGetCapabilitiesRequest {

        public CapabilitiesRequest( URL serverUrl ) {
            super(serverUrl);
        }

        @Override
        protected void initService() {
            setProperty(REQUEST, "GetCapabilities");
            setProperty(SERVICE, "WMS");
            setProperty(VERSION, "1.1.0");
        }

        @Override
        protected void initVersion() {
            // not used
        }

        public GetCapabilitiesResponse createResponse( String contentType, InputStream inputStream ) throws ServiceException, IOException {
            return new CapabilitiesResponse(contentType, inputStream);
        }
    }

    /**
     * Constructor
     * 
     * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
     *
     */
    class CapabilitiesResponse extends GetCapabilitiesResponse {
    	
    	private String contentType;
    	private InputStream inputStream;
    	
    	public CapabilitiesResponse (String contentType, InputStream inputStream) throws ServiceException, IOException {
    		super(contentType, inputStream);
    	
    		this.contentType = contentType;
    		this.inputStream = inputStream;
    		
    	}
    	
    	public InputStream asStream() {
    		return this.inputStream;
    	}
    	
    	/**
    	 * This method returns the capabilities response as a string
    	 * 
    	 * @return
    	 * @throws IOException
    	 */
    	public String asString() throws IOException {
 
        	Writer writer = new StringWriter();
        	char[] buffer = new char[GlobalConstants.BUFFER_SIZE];
 
        	try {
        		Reader reader = new BufferedReader(new InputStreamReader(this.inputStream, GlobalConstants.UTF_8));
        		int n;
        		while ((n = reader.read(buffer)) != -1) {
        			writer.write(buffer, 0, n);
        		}
        	
        	} catch(Exception e) {
        		e.printStackTrace();
        	
        	} finally {
        		this.inputStream.close();
        	}
        	
        	return writer.toString();

    	}
    	
    	/**
    	 * This method returns the capabilities response as a json object
    	 * 
    	 * @return
    	 * @throws JSONException
    	 */
    	public JSONArray asJSON() throws JSONException {
    		
    		JSONArray jLayers = new JSONArray();
    		if (!this.contentType.equals(GlobalConstants.MT_WMS)) return jLayers;

    		try {
    			
    			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    			factory.setNamespaceAware(true);
				/*
				 * Determine layers from response
				 */
				Document xmlDoc = factory.newDocumentBuilder().parse(this.inputStream);
				NodeList layers = xmlDoc.getElementsByTagName(WMS_LAYER);
			
				for (int i=0; i < layers.getLength(); i++) {
					Node layer = layers.item(i);

					if (layer.hasAttributes() && (layer.getAttributes().getNamedItem(WMS_QUERYABLE) != null) && layer.getAttributes().getNamedItem(WMS_QUERYABLE).getTextContent().trim().equals("1")) {

							JSONObject jLayer =  getJLayer(layer);   				
		    				if (jLayer != null && !jLayer.has(WMS_NAME)) jLayers.put(jLayers.length(), jLayer);

					}

				}

    		} catch (Exception e) {
    			// nothing to do
    		}
    		
    		return jLayers;
    		
    	}
    	
    	/**
    	 * A helper method to convert a WMS layer into a JSON object
    	 * 
    	 * @param layer
    	 * @return
    	 * @throws JSONException
    	 */
    	private JSONObject getJLayer(Node layer) throws JSONException {
    		
    		JSONObject jLayer = new JSONObject();
    		NodeList nodes = layer.getChildNodes();
    		
    		for (int i=0; i < nodes.getLength(); i++) {
    			Node node = nodes.item(i);

                /*
                 * skip Layer which contains another layer as
                 * <Title>GeoServer Web Map Service</Title>
                 * which contains all other layers
                 */
                if (node.getNodeName().equals(WMS_LAYER)) continue;

    			
    			if (node.getNodeName().equals(WMS_NAME)) {
    				/*
    				 * Name
    				 */
    	    		jLayer.put(J_NAME, node.getTextContent().trim());
    				
    			} else if (node.getNodeName().equals(WMS_TITLE)) {
    				/*
    				 * Title
    				 */
    				jLayer.put(J_TITLE, node.getTextContent().trim());
    			
       			} else if (node.getNodeName().equals(WMS_SRS)) {
    				/*
    				 * SRS
    				 */
    				jLayer.put(J_SRS, node.getTextContent().trim());

       			} else if (node.getNodeName().equals(WMS_BBOX)) {
    				/*
    				 * Bounding Box
    				 */
       				NamedNodeMap attrs = node.getAttributes();
       	    		JSONObject jBBox = new JSONObject();
       	    	 
       	    		jBBox.put(J_MINX, attrs.getNamedItem(WMS_MINX).getTextContent().trim());
       	       		jBBox.put(J_MINY, attrs.getNamedItem(WMS_MINY).getTextContent().trim());
       	       		jBBox.put(J_MAXX, attrs.getNamedItem(WMS_MAXX).getTextContent().trim());
       	       		jBBox.put(J_MAXY, attrs.getNamedItem(WMS_MAXY).getTextContent().trim());
       	   		
       	       		jLayer.put(J_BBOX, jBBox.toString()); 
 
       			}
    			
    		}
   		
    		return jLayer;
    		
    	}
   	
    }
}
