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

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import de.kp.ames.web.core.format.kml.KmlObject;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;

public class MapDQM extends JaxrDQM {

	/**
	 * Constructor
	 * 
	 * @param jaxrHandle
	 */
	public MapDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
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
		
		/*
		 * Retrieve registry object from source
		 */
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		RegistryObjectImpl ro = dqm.getRegistryObjectById(uid);
		if (ro == null) throw new Exception("[MapDQM] Registry Object does not exist for " + uid);
		
		/*
		 * Convert registry object into kml representation
		 */
		KmlObject kmlObject = new KmlObject(jaxrHandle);
		kmlObject.set(ro);

		return kmlObject.toXml();

	}
	
}
