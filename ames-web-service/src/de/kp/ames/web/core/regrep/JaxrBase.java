package de.kp.ames.web.core.regrep;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import javax.activation.DataHandler;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import javax.xml.registry.JAXRResponse;
import javax.xml.registry.Query;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.Key;
import javax.xml.registry.infomodel.User;

import org.freebxml.omar.client.xml.registry.BusinessLifeCycleManagerImpl;
import org.freebxml.omar.client.xml.registry.DeclarativeQueryManagerImpl;
import org.freebxml.omar.client.xml.registry.infomodel.AssociationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.AuditableEventImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ConceptImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.freebxml.omar.client.xml.registry.infomodel.SlotImpl;
import org.freebxml.omar.common.BindingUtility;
import org.freebxml.omar.common.IterativeQueryParams;

import de.kp.ames.web.core.util.FileUtil;

/**
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */

public class JaxrBase {

	/*
	 * Reference to the JaxrHandle associated
	 * with the actual request
	 */
	protected JaxrHandle jaxrHandle;
	
	/*
	 * Default value of the locale used to build
	 * an international string
	 */
	protected Locale defaultLocale = Locale.GERMAN;

	protected static HashMap<Integer, String> eventMap;
	protected static HashMap<Integer, String> statusMap;
	
	static {
		
		eventMap = new HashMap<Integer, String>();
		
		eventMap.put(AuditableEventImpl.EVENT_TYPE_CREATED,      "created");
		eventMap.put(AuditableEventImpl.EVENT_TYPE_APPROVED,     "approved");
		eventMap.put(AuditableEventImpl.EVENT_TYPE_DELETED,      "deleted");
		eventMap.put(AuditableEventImpl.EVENT_TYPE_DEPRECATED,   "deprecated");
		eventMap.put(AuditableEventImpl.EVENT_TYPE_UNDEPRECATED, "undeprecated");
		eventMap.put(AuditableEventImpl.EVENT_TYPE_UPDATED,      "updated");
		eventMap.put(AuditableEventImpl.EVENT_TYPE_VERSIONED,    "versioned");
		eventMap.put(AuditableEventImpl.EVENT_TYPE_RELOCATED,    "relocated");
		
		statusMap = new HashMap<Integer, String>();
		
		statusMap.put(0, "Submitted");
		statusMap.put(1, "Approved");
		statusMap.put(2, "Deprecated");
		statusMap.put(3, "Withdrawn");
		
	}

	
	/**
	 * Constructor requires actual RequestHandle
	 * 
	 * @param requestHandle
	 */
	public JaxrBase(JaxrHandle requestHandle) {
		this.jaxrHandle = requestHandle;
	}

	/**
	 * Wrapper method of the respective (same) method of the 
	 * DeclarativeQueryManagerImpl of the OMAR Registry Client
	 * 
	 * @param oid
	 * @return
	 * @throws JAXRException
	 */
	public RegistryObjectImpl getRegistryObjectById(String oid) throws JAXRException {
		DeclarativeQueryManagerImpl dqm = jaxrHandle.getDQM();
		return (RegistryObjectImpl)dqm.getRegistryObject(oid);
	}

	/**
	 * A helper method to set the name of a certain
	 * registry object using the default locale
	 * 
	 * @param ro
	 * @param name
	 * @throws JAXRException
	 */
	public void setName(RegistryObjectImpl ro, String name) throws JAXRException {
		ro.setName(createInternationalString(defaultLocale, name) ); 
	}

	/**
	 * A helper method to set the name of a certain
	 * registry object with a specific locale
	 * 
	 * @param ro
	 * @param locale
	 * @param name
	 * @throws JAXRException
	 */
	public void setName(RegistryObjectImpl ro, Locale locale, String name) throws JAXRException {
		ro.setName(createInternationalString(locale, name) ); 
	}
	
	/**
	 * A helper method to retrieve the name of a certain
	 * registry object refering to the default locale
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	public String getName(RegistryObjectImpl ro) throws JAXRException {
		return getValueFromInternationalString(ro.getName());
	}

	/**
	 * A helper method to retrieve the name of a certain
	 * registry object refering to a specific locale
	 * 
	 * @param ro
	 * @param locale
	 * @return
	 * @throws JAXRException
	 */
	public String getName(RegistryObjectImpl ro, Locale locale) throws JAXRException {
		return getValueFromInternationalString(ro.getName(), locale);
	}

	/**
	 * A helper method to set the description of a certain
	 * registry object using the default locale
	 * 
	 * @param ro
	 * @param description
	 * @throws JAXRException
	 */
	public void setDescription(RegistryObjectImpl ro, String description) throws JAXRException {
		ro.setDescription( createInternationalString(defaultLocale, description) );
	}

	/**
	 * A helper method to set the description of a 
	 * certain registry object with a specific locale
	 * 
	 * @param ro
	 * @param locale
	 * @param description
	 * @throws JAXRException
	 */
	public void setDescription(RegistryObjectImpl ro, Locale locale, String description) throws JAXRException {
		ro.setDescription( createInternationalString(locale, description) );
	}

	/**
	 * A helper method to retrieve the description of a certain
	 * registry object refering to the default locale
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	public String getDescription(RegistryObjectImpl ro) throws JAXRException {		
		return getValueFromInternationalString(ro.getDescription());
	}

	/**
	 * A helper method to retrieve the name of a certain
	 * registry object refering to a specific locale
	 * 
	 * @param ro
	 * @param locale
	 * @return
	 * @throws JAXRException
	 */
	public String getDescription(RegistryObjectImpl ro, Locale locale) throws JAXRException {		
		return getValueFromInternationalString(ro.getDescription(), locale);
	}

	/**
	 * A helper method to retrieve the home url of 
	 * a certain registry object
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	public String getHome(RegistryObjectImpl ro) throws JAXRException {

		String home = ro.getHome();
		String endpoint = jaxrHandle.getEndpoint();
		
		home = (home==null) ? endpoint : home;
		home = home.replace("/soap", "");

		return home;
		
	}

	/**
	 * A helper method to retrieve the unique identifier of the
	 * classification node that classifies a certain registry
	 * object as object type
	 * 
	 * @param registryObject
	 * @return
	 * @throws JAXRException
	 */
	public String getObjectType(RegistryObjectImpl registryObject) throws JAXRException {
		
		String objectType = null;
			
		ConceptImpl concept = (ConceptImpl) registryObject.getObjectType();
		if (concept != null) objectType = concept.getId();
		
		return objectType;
		
	}

	/**
	 * A helper method to retrieve the unique identifiers of the
	 * classification nodes that are actually used to classify
	 * a certain registry object
	 * 
	 * @param ro
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getConcepts(RegistryObjectImpl ro) throws Exception {

		ArrayList<String> concepts = new ArrayList<String>();

		Object[] cs = ro.getClassifications().toArray();
		if (cs.length == 0) return concepts;
			
		for (int i=0; i < cs.length; i++) {
			
			ClassificationImpl c = (ClassificationImpl) cs[i];
			ConceptImpl concept = (ConceptImpl)c.getConcept();
			
			concepts.add(concept.getId());
				
		}

		return concepts;
		
	}

	/**
	 * A helper method to determine whether the actual classifications
	 * of a certain registry object refer to the provided concept id
	 * @param ro
	 * @param concept
	 * @return
	 * @throws JAXRException
	 */
	public boolean hasConcept(RegistryObjectImpl ro, String concept) throws JAXRException {

		boolean result = false;

		Collection<?> cs = ro.getClassifications();
		if (cs.isEmpty()) return false;
		
		Iterator<?> iterator = cs.iterator();
		while(iterator.hasNext()) {

			ClassificationImpl c = (ClassificationImpl)iterator.next();
			ConceptImpl cpt = (ConceptImpl) c.getConcept();
			
			if (cpt.getId().equals(concept)) {
				result = true;
				break;
			}
		
		}
		
		return result;
		
	}
	
	/**
	 * This method evaluates whether the registry object is classified by 
	 * all of the concepts provided with the actual request
     *
	 * @param ro
	 * @param concepts
	 * @return
	 * @throws JAXRException
	 */
	public boolean hasConcepts(RegistryObjectImpl ro, ArrayList<String> concepts) throws JAXRException {
		
		if (concepts.isEmpty()) return true;

		// we build a copy of the original list and remove all clases from this copy
		// the refer to clases of the registry object

		Object[] cs = ro.getClassifications().toArray();		
		if (cs.length == 0) return false;
		
		ArrayList<String> clone = new ArrayList<String>();
		clone.addAll(concepts);

		for (int i=0; i < cs.length; i ++) {

			ClassificationImpl c = (ClassificationImpl) cs[i];

			ConceptImpl cpt = (ConceptImpl) c.getConcept();			
			if (cpt != null) {
				
				String cid = cpt.getId();
				if (concepts.contains(cid)) clone.remove(cid);
				
			}

		}
		
		return clone.isEmpty();

	}

	/**
	 * A helper method to evaluate whether a certain registry objects
	 * holds associations to other objects, that are classified by
	 * the actualy set of semantic concepts
	 * 
	 * @param ro
	 * @param concepts
	 * @return
	 * @throws JAXRException
	 */
	public boolean hasAssociations(RegistryObjectImpl ro, ArrayList<String> concepts) throws JAXRException {
		
		if (concepts.isEmpty()) return true;

		Object[] as = ro.getAssociations().toArray();		
		if (as.length == 0) return false;

		ArrayList<String> clone = new ArrayList<String>();
		clone.addAll(concepts);

		for (int i=0; i < as.length; i ++) {

			AssociationImpl a = (AssociationImpl)as[i];

			ConceptImpl cpt = (ConceptImpl) a.getAssociationType();			
			if (cpt != null) {
				
				String cid = cpt.getId();
				if (concepts.contains(cid)) clone.remove(cid);
				
			}

		}

		return clone.isEmpty();
	
	}

	/**
	 * Wrapper method of the respective (same) method of the 
	 * BusinessLifeCycleManagerImpl of the OMAR Registry Client
	 * 
	 * @param text
	 * @param locale
	 * @return
	 * @throws JAXRException
	 */
	public InternationalString createInternationalString(String text) throws JAXRException {
		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return blcm.createInternationalString(defaultLocale, text);
	}

	/**
	 * Wrapper method of the respective (same) method of the 
	 * BusinessLifeCycleManagerImpl of the OMAR Registry Client
	 * 
	 * @param text
	 * @param locale
	 * @return
	 * @throws JAXRException
	 */
	public InternationalString createInternationalString(Locale locale, String text) throws JAXRException {
		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return blcm.createInternationalString(locale, text);
	}

	/**
	 * A helper method to return the value from an international string
	 * that refers to the default locale
	 * 
	 * @param internationalString
	 * @return
	 * @throws JAXRException
	 */
	public String getValueFromInternationalString(InternationalString internationalString) throws JAXRException {		
		String value = internationalString.getValue(defaultLocale);	
		return (value == null) ? "" : value;
	}

	/**
	 * A helper method to return the value from an international string
	 * that refers to a certain locale
	 * 
	 * @param internationalString
	 * @param locale
	 * @return
	 * @throws JAXRException
	 */
	public String getValueFromInternationalString(InternationalString internationalString, Locale locale) throws JAXRException {		
		String value = internationalString.getValue(locale);	
		return (value == null) ? "" : value;
	}

	/**
	 * A helper method to retrieve the unique identifier
	 * of the user that own a certain registry object
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	public String getOwner(RegistryObjectImpl ro) throws JAXRException {
		User owner = ro.getOwner();
		return owner.getKey().getId();	
	}

	/**
	 * Wrapper method of the respective (same) method of the 
	 * DeclarativeQueryManagerImpl of the OMAR Registry Client
	 * 
	 * @return
	 * @throws JAXRException
	 */
	public User getCallersUser() throws JAXRException {
		DeclarativeQueryManagerImpl dqm = jaxrHandle.getDQM();
		return dqm.getCallersUser();
	}
	
	/**
	 * A helper method to determine the status of a registry
	 * object in a String representation
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	public String getStatus(RegistryObjectImpl ro) throws JAXRException {
		
		int status = ro.getStatus();

		if (statusMap.containsKey(status) == false) return "not defined";
		return statusMap.get(status);
				
	}

	/**
	 * A helper method to retrieve the last event
	 * associated with a certain registry object in
	 * a String representation
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	public String getLastEventType(RegistryObjectImpl ro) throws JAXRException {

		AuditableEventImpl auditableEvent = getLastEvent(ro);
		if (auditableEvent == null) return "not defined";

		int eventType = auditableEvent.getEventType();
		
		if (eventMap.containsKey(eventType)) return eventMap.get(eventType);
		return "not defined";		
	
	}

	/**
	 * A helper method to retrieve the last event
	 * associated with a certain registry object
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	public AuditableEventImpl getLastEvent(RegistryObjectImpl ro) throws JAXRException {
		
		Collection<?> auditableEvents = ro.getAuditTrail();
		
		int eventType = -1;
		AuditableEventImpl auditableEvent = null;

		Iterator<?> eventIterator = auditableEvents.iterator();
		while (eventIterator.hasNext() && eventType == -1) {				
			auditableEvent = (AuditableEventImpl)eventIterator.next();
		}

		return auditableEvent;
		
	}

	/**
	 * A helper method to retrieve the date of the latest
	 * modification event associated with a certain registry
	 * object
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	public Date getLastModified(RegistryObjectImpl ro) throws JAXRException {
		
		AuditableEventImpl lastEvent = getLastEvent(ro);
		if (lastEvent == null) return null;
	
		Timestamp lastModified = lastEvent.getTimestamp();

		long milliseconds = lastModified.getTime() + (lastModified.getNanos() / 1000000);
		return new Date(milliseconds);			
		
	}

	/**
	 * A helper method to retrieve the date of the latest
	 * modification event associated with a certain registry
	 * object as a String representation
	 *
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	public String getLastTimeStamp(RegistryObjectImpl ro) throws JAXRException {
		
		Date lastModified = getLastModified(ro);
		if (lastModified == null) return null;
		
		return lastModified.toString();
		
	}

	/**
	 * A helper method to retrieve the FIRST registry package
	 * that holds a hasMember association with a specific
	 * registry object as target object
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	public RegistryPackageImpl getObjectParent(RegistryObjectImpl ro) throws JAXRException {
		
		Collection<?> packages = ro.getRegistryPackages();
		if (packages.size() == 0) return null;
		
		return (RegistryPackageImpl) packages.toArray()[0];
		
	}

	/**
	 * A helper method to retrieve the repository item
	 * of a certain extrinsic object as a FileUtil instance
	 * 
	 * @param eo
	 * @return
	 * @throws JAXRException
	 */
	public FileUtil getRepositoryItemImpl(ExtrinsicObjectImpl eo) throws JAXRException {

		DataHandler handler = eo.getRepositoryItem();
		if (handler != null) {

			FileUtil file = null;
			
			try {
			
				file = new FileUtil();

				String mimetype = eo.getMimeType();
				mimetype = (mimetype==null) ? "application/octet-stream" : mimetype;			
					    	
		    	file.setName(eo.getDisplayName());
		    	file.setInputStream(handler.getInputStream(), mimetype);
	    	
		    	/* 
		    	 * Determine filename (optional)
		    	 */
		    	SlotImpl slot = (SlotImpl) eo.getSlot("File");
		    	if (slot != null) {
		    		
		    		Object[] values = slot.getValues().toArray();
		    		if (values.length > 0) file.setFilename((String)values[0]);
		    		
		    	}
	    	
			} catch(Exception e) {
				// do nothing
			}
			
	    	return file;
	    	
		}
	   	   	
    	return null;
    	
	}
	
	/**
	 * A helper method to determine the size of a repository
	 * item attached to a certain extrinsic object
	 * 
	 * @param eo
	 * @return
	 * @throws Exception
	 */
	public Integer getRepositoryItemSize(ExtrinsicObjectImpl eo) throws Exception {

		DataHandler handler = eo.getRepositoryItem();
		if (handler == null) return null;
			
    	byte[] bytes = FileUtil.getByteArrayFromInputStream(handler.getInputStream());
    	return bytes.length;
    	
	}

	/**
	 * Wrapper method of the respective (same) method of the 
	 * DeclarativeQueryManagerImpl of the OMAR Registry Client
	 * 
	 * @param queryString
	 * @return
	 * @throws JAXRException
	 */
	public BulkResponse executeQuery(String queryString) throws JAXRException {

		DeclarativeQueryManagerImpl dqm = jaxrHandle.getDQM();
		Query query = dqm.createQuery(Query.QUERY_TYPE_SQL, queryString);
		
		return dqm.executeQuery(query);

	}	

	/**
	 * Wrapper method to registry objects in an iterative query
	 * 
	 * @param queryString
	 * @param start
	 * @param limit
	 * @return
	 * @throws JAXRException
	 */
	public BulkResponse executeIterativeQuery(String queryString, String start, String limit) throws JAXRException {

		int begin = toIntegerValue(start);
		int end   = begin + toIntegerValue(limit);

		DeclarativeQueryManagerImpl dqm = jaxrHandle.getDQM();
		Query query = dqm.createQuery(Query.QUERY_TYPE_SQL, queryString);

		IterativeQueryParams iterativeQueryParams = new IterativeQueryParams(begin, end);
		return dqm.executeQuery(query, null, iterativeQueryParams);

	}

	/**
	 * Wrapper method of the respective (same) method of the 
	 * BusinessLifeCycleManagerImpl of the OMAR Registry Client
	 * 
	 * @param association
	 * @throws JAXRException
	 */
	public void confirmAssociation(AssociationImpl association) throws JAXRException {
		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		blcm.confirmAssociation(association);
	}

	/**
	 * Wrapper method of the respective (same) method of the 
	 * BusinessLifeCycleManagerImpl of the OMAR Registry Client
	 * 
	 * @param name
	 * @param value
	 * @param type
	 * @return
	 * @throws JAXRException
	 */
	public SlotImpl createSlot(String name, String value, String type) throws JAXRException {
		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (SlotImpl) blcm.createSlot(name, value, type);
	}

	/**
	 * Wrapper method of the respective (same) method of the 
	 * BusinessLifeCycleManagerImpl of the OMAR Registry Client
	 * 
	 * @param name
	 * @param values
	 * @param type
	 * @return
	 * @throws JAXRException
	 */
	public SlotImpl createSlot(String name, Collection<?> values, String type) throws JAXRException {
		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (SlotImpl) blcm.createSlot(name, values, type);
	}

	/**
	 * This method updates a single value slot in case of existence
	 * or create a new one for a specific registry object
	 * 
	 * @param ro
	 * @param name
	 * @param value
	 * @param type
	 * @throws JAXRException
	 */
	public void updateSlot(RegistryObjectImpl ro, String name, String value, String type)  throws JAXRException {

		SlotImpl slot = (SlotImpl) ro.getSlot(name);
		if (slot == null) {

			// create new slot
			slot = createSlot(name, value, type);
			ro.addSlot(slot);
			
		} else {

			// update existing slot which has a single value
			ArrayList<String> values = new ArrayList<String>();
			values.add(value);
			
			slot.setValues(values);
			
		}

	}

	/**
	 * This method evaluates wether the registry object has all 
	 * the slots allocated that are provided by the slotNames
	 * 
	 * @param ro
	 * @param names
	 * @return
	 * @throws JAXRException
	 */
	public boolean hasSlots(RegistryObjectImpl ro, ArrayList<String> names) throws JAXRException {
		
		if (names.isEmpty()) return true;
		
		// we build a copy of the original list and remove all slot names 
		// from this copy the refer to slots of the registry object

		Object[] slots = ro.getSlots().toArray();		
		if (slots.length == 0) return false;
		
		ArrayList<String> clone = new ArrayList<String>();
		clone.addAll(names);

		for (int i=0; i < slots.length; i ++) {;

			String slotName = ((SlotImpl)slots[i]).getName();
			if (names.contains(slotName)) clone.remove(slotName);

		}
		
		return clone.isEmpty();
	}

	/**
	 * Wrapper method of the respective (same) method of the 
	 * BusinessLifeCycleManagerImpl of the OMAR Registry Client
	 *
	 * @param keys
	 * @return
	 * @throws JAXRException
	 */
	public BulkResponse deleteObjects(ArrayList<Key> keys) throws JAXRException {

		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		BulkResponse bulkResponse = blcm.deleteObjects(keys);

        checkBulkResponse(bulkResponse);    
        return bulkResponse;

	}

	/**
	 * Wrapper method of the respective (same) method of the 
	 * BusinessLifeCycleManagerImpl of the OMAR Registry Client
	 * 
	 * @param objectsToSave
	 * @param versionMetadata
	 * @param versionContent
	 * @return
	 * @throws JAXRException
	 */
	public BulkResponse saveObjects(Collection<Object> objectsToSave, boolean versionMetadata, boolean versionContent) throws JAXRException {

    	BulkResponse bulkResponse = null;
        HashMap<String,String> slotsMap = new HashMap<String, String>();

        if (versionMetadata == false) {
        	// ignore versioning of registry object (metadata)
            slotsMap.put(BindingUtility.CANONICAL_SLOT_LCM_DONT_VERSION, "true");
        }

        if (versionContent == false) {
        	// ignore version of repository item (content)
            slotsMap.put(BindingUtility.CANONICAL_SLOT_LCM_DONT_VERSION_CONTENT, "true");
        }
 
        BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
        bulkResponse = blcm.saveObjects(objectsToSave, slotsMap);
 
        checkBulkResponse(bulkResponse);    
        return bulkResponse;
    
    }

    /**
     * This method checks OASIS ebXML RegRep response in case
     * of failure and determines the exceptions, sent by the
     * RegRep server
     * 
     * @param bulkResponse
     * @throws JAXRException
     */
    private void checkBulkResponse(BulkResponse bulkResponse) throws JAXRException {

    	if ((bulkResponse != null) && (!(bulkResponse.getStatus() == JAXRResponse.STATUS_SUCCESS))) {
            
    		Collection<?> exceptions = bulkResponse.getExceptions();
            if (exceptions == null) return;

        	Iterator<?> iter = exceptions.iterator();
            while (iter.hasNext()) {
                Exception e = (Exception) iter.next();
            	e.printStackTrace();
            }

    	}

    }

	/**
	 * @param value
	 * @return
	 */
	public float toFloatValue(String value) {		

		try {
			return Float.valueOf(value).floatValue();
		
		} catch (NumberFormatException e) {
			// do nothing
		} finally {
			// do nothing
		}
		
		return Float.NEGATIVE_INFINITY;
		
	}
	
	/**
	 * @param value
	 * @return
	 */
	public int toIntegerValue(String value) {

		try {
			return Float.valueOf(value).intValue();
		
		} catch (NumberFormatException e) {
			// do nothing

		} finally {
			// do nothing
		}
		
		return 0;

	}

}
