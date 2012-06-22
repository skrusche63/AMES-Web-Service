package de.kp.ames.web.function.map;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.freebxml.omar.client.xml.registry.infomodel.AssociationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.freebxml.omar.client.xml.registry.infomodel.SlotImpl;
import org.freebxml.omar.common.CanonicalSchemes;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.domain.JsonConstants;
import de.kp.ames.web.core.format.kml.KmlObject;
import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.function.access.wms.WmsConsumer;

public class MapDQM extends JaxrDQM {

	private static String ASSOCIATION      = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_Association;
	private static String REGISTRY_PACKAGE = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_RegistryPackage;
	
	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public MapDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}
	
	/**
	 * Retrieve WMS layers in a JSON representation
	 * 
	 * @param endpoint
	 * @return
	 * @throws Exception
	 */
	public JSONArray getLayers(String endpoint) throws Exception {
		
		/*
		 * Connect to WMS service defined by endpoint parameter
		 */
		WmsConsumer wmsConsumer = new WmsConsumer(endpoint);		
		return wmsConsumer.getCapabilitiesAsJson();

	}
	
	/**
	 * This method retrieves a kml representation of a certain registry 
	 * package; the kml however only contains the nodes of the respective 
	 * package, the according edges are retrieved by another method
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public String getNodes(String uid) throws Exception {
				
		RegistryObjectImpl ro = getRegistryObjectById(uid);
		if (ro == null) throw new Exception("[MapDQM] Registry Object does not exist for " + uid);
		
		/*
		 * Convert registry object into kml representation
		 */
		KmlObject kmlObject = new KmlObject(jaxrHandle);
		kmlObject.set(ro);

		return kmlObject.toXml();

	}
	
	/**
	 * This method retrieves a json representation of all associations
	 * that are members of a specific registry package
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public String getEdges(String uid) throws Exception {

		ArrayList<JSONObject> edges = getLines(uid);
		return new JSONArray(edges).toString();	

	}

	/**
	 * Helper method to represent all association members
	 * of a certain registry package as Geo lines
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	private ArrayList<JSONObject> getLines(String uid) throws Exception {
		
		ArrayList<JSONObject> associations = new ArrayList<JSONObject>();

		RegistryObjectImpl ro = getRegistryObjectById(uid);
		if (ro == null) throw new Exception("[MapDQM] Registry Object does not exist for " + uid);

		String objectType = ro.getObjectType().getKey().getId();
		if (objectType.equals(REGISTRY_PACKAGE) == false) return associations;
		
		RegistryPackageImpl rp = (RegistryPackageImpl)ro;
		List<RegistryObjectImpl> members = getPackageMembers(rp);
		
		for (RegistryObjectImpl member:members) {
			
			objectType = member.getObjectType().getKey().getId();
			if (objectType.equals(ASSOCIATION) ){

				AssociationImpl edge = (AssociationImpl)member;

				RegistryObjectImpl source = (RegistryObjectImpl)edge.getSourceObject();
				RegistryObjectImpl target = (RegistryObjectImpl)edge.getTargetObject();
				
				JSONObject jStart = getJPoint(source);
				JSONObject jEnd   = getJPoint(target);

				if ((jStart != null) && (jEnd != null)) {
					
					String name = getName(edge);
					
					JSONObject jEdge = new JSONObject();				
					jEdge.put(JsonConstants.J_NAME, name);
					
					jEdge.put(JsonConstants.J_START, jStart);
					jEdge.put(JsonConstants.J_END,   jEnd);
	
					associations.add(jEdge);
				
				}
				
			} else if (objectType.equals(REGISTRY_PACKAGE)) {				
				associations.addAll(getLines(member.getId()));
				
			}
			
		}				
		
		return associations;	

	}

	/**
	 * Create (Geo) Point representation of a registry object
	 * 
	 * @param ro
	 * @return
	 * @throws Exception
	 */
	private JSONObject getJPoint(RegistryObjectImpl ro) throws Exception {

		String lat = null;
		String lon = null;
		
		SlotImpl slot = null;
		Collection<?> values;
		
		/* 
		 * Latitude
		 */
		slot = (SlotImpl)ro.getSlot(JaxrConstants.SLOT_LATITUDE);
		if (slot != null) {
		
			values = slot.getValues();
			lat = (String)values.toArray()[0];
		
		}
		
		/* 
		 * Longitude
		 */
		slot = (SlotImpl)ro.getSlot(JaxrConstants.SLOT_LONGITUDE);
		if (slot != null) {
			
			values = slot.getValues();
			lon = (String)values.toArray()[0];
			
		}
		
		if ((lat == null) || (lon == null)) return null;
		
		/* 
		 * Create JSON representation of point
		 */
		JSONObject jPoint = new JSONObject();
		jPoint.put(JsonConstants.J_ID, ro.getId());
		
		jPoint.put(JsonConstants.J_LAT, lat);
		jPoint.put(JsonConstants.J_LON, lon);
		
		return jPoint;
		
	}
}
