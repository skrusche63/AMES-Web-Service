package de.kp.ames.web.core.regrep.lcm;
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
import java.util.List;
import java.util.Locale;

import javax.xml.registry.JAXRException;
import javax.xml.registry.LifeCycleManager;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.Key;

import org.freebxml.omar.client.xml.registry.BusinessLifeCycleManagerImpl;
import org.freebxml.omar.client.xml.registry.DeclarativeQueryManagerImpl;
import org.freebxml.omar.client.xml.registry.infomodel.AssociationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ConceptImpl;
import org.freebxml.omar.client.xml.registry.infomodel.EmailAddressImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExternalLinkImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.OrganizationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.PersonNameImpl;
import org.freebxml.omar.client.xml.registry.infomodel.PostalAddressImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceBindingImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceImpl;
import org.freebxml.omar.client.xml.registry.infomodel.SpecificationLinkImpl;
import org.freebxml.omar.client.xml.registry.infomodel.TelephoneNumberImpl;
import org.freebxml.omar.common.CanonicalSchemes;

import de.kp.ames.web.core.reactor.ReactorImpl;
import de.kp.ames.web.core.regrep.JaxrBase;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.JaxrTransaction;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;

/**
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */
public class JaxrLCM extends JaxrBase {

	private static String REGISTRY_PACKAGE = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_RegistryPackage;

	public JaxrLCM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
	}

	/**
	 * A helper method to create an Association instance
	 * 
	 * @param associationType
	 * @param targetObject
	 * @return
	 * @throws JAXRException
	 */
	public AssociationImpl createAssociation(String associationType, RegistryObjectImpl targetObject) throws JAXRException {
		
		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (AssociationImpl) blcm.createAssociation(targetObject, getConcept(associationType));
		
	}

	/**
	 * A helper method to create an AffiliatedWith Association instance
	 * 
	 * @param targetObject
	 * @return
	 * @throws JAXRException
	 */
	public AssociationImpl createAssociation_AffiliatedWith(RegistryObjectImpl targetObject) throws JAXRException {

		String associationType = CanonicalSchemes.CANONICAL_ASSOCIATION_TYPE_ID_AffiliatedWith;
    	return createAssociation(associationType, targetObject);

	}

	/**
	 * A helper method to create a RelatedTo Association instance
	 * 
	 * @param targetObject
	 * @return
	 * @throws Exception
	 */
	public AssociationImpl createAssociation_RelatedTo(RegistryObjectImpl targetObject) throws JAXRException {

		String associationType = CanonicalSchemes.CANONICAL_ASSOCIATION_TYPE_ID_RelatedTo;
    	return createAssociation(associationType, targetObject);

	}

	/**
	 * A helper method to create a Classification instance
	 * 
	 * @param conceptType
	 * @return
	 * @throws JAXRException
	 */
	public ClassificationImpl createClassification(String conceptType) throws JAXRException {

		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (ClassificationImpl) blcm.createClassification(getConcept(conceptType));

	}

	/**
	 * A helper method to create a Classification instance
	 * 
	 * @param concept
	 * @return
	 * @throws JAXRException
	 */
	public ClassificationImpl createClassification(ConceptImpl concept) throws JAXRException {

		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (ClassificationImpl) blcm.createClassification(concept);

	}

	/**
	 * A helper method to create a list of classification instances
	 * 
	 * @param conceptTypes
	 * @return
	 * @throws JAXRException
	 */
	public ArrayList<ClassificationImpl> createClassifications(ArrayList<String> conceptTypes) throws JAXRException {
		
		ArrayList<ClassificationImpl> clases = new ArrayList<ClassificationImpl>();

		for (String conceptType:conceptTypes) {
			clases.add(createClassification(conceptType));
		}
		
		return clases;

	}

	/**
	 * A helper method to create a Concept instance
	 * 
	 * @param parent
	 * @param name
	 * @param code
	 * @return
	 * @throws JAXRException
	 */
	public ConceptImpl createConcept(ConceptImpl parent, String name, String code) throws JAXRException {

		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return(ConceptImpl) blcm.createConcept(parent, name, code);
	
	}

	/**
	 * A helper method to create an Email address
	 * 
	 * @param email
	 * @return
	 * @throws JAXRException
	 */
	public EmailAddressImpl createEmailAddress(String email) throws JAXRException {

		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
        return (EmailAddressImpl)blcm.createEmailAddress(email);

	}		

	/**
	 * A helper method to create an ExternalLink instance
	 * 
	 * @param uri
	 * @return
	 * @throws JAXRException
	 */
	public ExternalLinkImpl createExternalLink(String uri) throws JAXRException {
		
		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (ExternalLinkImpl) blcm.createExternalLink(uri, "");

	}

	/**
	 * A helper method to create a new ExtrinsicObject instance
	 * 
	 * @return
	 * @throws JAXRException
	 */
	public ExtrinsicObjectImpl createExtrinsicObject() throws JAXRException {
		
    	String objectType = CanonicalSchemes.CANONICAL_OBJECT_TYPE_ID_ExtrinsicObject;
		return createExtrinsicObject(objectType);
		
	}

	/**
	 * A helper method to create a new ExtrinsicObject instance 
	 * from an object type
	 * 
	 * @param objectType
	 * @return
	 * @throws JAXRException
	 */
	public ExtrinsicObjectImpl createExtrinsicObject(String objectType) throws JAXRException {
		
		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (ExtrinsicObjectImpl) blcm.createExtrinsicObject(getConcept(objectType));
		
	}

	/**
	 * A helper method to create a new Organization instance
	 * 
	 * @param name
	 * @return
	 * @throws JAXRException
	 */
	public OrganizationImpl createOrganization(String name) throws JAXRException {

		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (OrganizationImpl)blcm.createOrganization(createInternationalString(name));
		
	}

	/**
	 * A helper method to create a new Organization instance
	 * 
	 * @param locale
	 * @param name
	 * @return
	 * @throws JAXRException
	 */
	public OrganizationImpl createOrganization(Locale locale, String name) throws JAXRException {

		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (OrganizationImpl)blcm.createOrganization(createInternationalString(locale, name));
		
	}

	/**
	 * A helper method to create a PersonName instance
	 * 
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @return
	 * @throws JAXRException
	 */
	public PersonNameImpl createPersonName(String firstName, String middleName, String lastName) throws JAXRException {

		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (PersonNameImpl)blcm.createPersonName(firstName, middleName, lastName);

	}

	/**
	 * A helper method to create a new PostalAddress instance
	 * 
	 * @param streetNumber
	 * @param street
	 * @param city
	 * @param stateOrProvince
	 * @param country
	 * @param postalCode
	 * @return
	 * @throws JAXRException
	 */
	public PostalAddressImpl createPostalAddress(String streetNumber, String street, String city, String stateOrProvince, String country, String postalCode) throws JAXRException {

		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
        return (PostalAddressImpl)blcm.createPostalAddress(streetNumber, street, city, stateOrProvince, country, postalCode, "Office");

	}

	/**
	 * A helper method to create a new RegistryPackage instance
	 * 
	 * @param name
	 * @return
	 * @throws JAXRException
	 */
	public RegistryPackageImpl createRegistryPackage(String name) throws JAXRException {
 		
		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (RegistryPackageImpl) blcm.createRegistryPackage(createInternationalString(name));
		
	}

	/**
	 * A helper method to create a new RegistryPackage instance
	 * 
	 * @param locale
	 * @param name
	 * @return
	 * @throws JAXRException
	 */
	public RegistryPackageImpl createRegistryPackage(Locale locale, String name) throws JAXRException {
 		
		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (RegistryPackageImpl) blcm.createRegistryPackage(createInternationalString(locale, name));
		
	}

	/**
	 * A helper method to create a new RegistryPackage instance
	 * 
	 * @param name
	 * @return
	 * @throws JAXRException
	 */
	public RegistryPackageImpl createRegistryPackage(InternationalString name) throws JAXRException {
 		
		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (RegistryPackageImpl) blcm.createRegistryPackage(name);
		
	}

	/**
	 * A helper method to create a Service instance
	 * 
	 * @param name
	 * @return
	 * @throws JAXRException
	 */
	public ServiceImpl createService(String name) throws JAXRException {

		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (ServiceImpl)blcm.createService(createInternationalString(name));
		
	}

	/**
	 * A helper method to create a Service instance
	 * 
	 * @param locale
	 * @param name
	 * @return
	 * @throws JAXRException
	 */
	public ServiceImpl createService(Locale locale, String name) throws JAXRException {

		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (ServiceImpl)blcm.createService(createInternationalString(locale, name));
		
	}

	/**
	 * A helper method to create a Service instance
	 * 
	 * @param name
	 * @return
	 * @throws JAXRException
	 */
	public ServiceImpl createService(InternationalString name) throws JAXRException {

		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (ServiceImpl)blcm.createService(name);
		
	}

	/**
	 * A helper method to create a ServiceBindung instance
	 * 
	 * @return
	 * @throws JAXRException
	 */
	public ServiceBindingImpl createServiceBinding() throws JAXRException {

		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (ServiceBindingImpl)blcm.createServiceBinding();
		
	}
	
	/**
	 * A helper method to create a SpecificationLink instance
	 * 
	 * @return
	 * @throws JAXRException
	 */
	public SpecificationLinkImpl createSpecificationLink() throws JAXRException {

		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		return (SpecificationLinkImpl)blcm.createSpecificationLink();
		
	}

	/**
	 * A helper method to create a TelephoneNumber instance
	 * 
	 * @return
	 * @throws JAXRException
	 */
	public TelephoneNumberImpl createTelephoneNumber() throws JAXRException {

		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
        return (TelephoneNumberImpl)blcm.createTelephoneNumber();

	}
	
	/**
	 * This method updates the existing classifications of a 
	 * certain registry object from a list of classification 
	 * nodes; this implies that certain classifications must 
	 * be deleted and other one created
	 * 
	 * @param ro
	 * @param conceptTypes
	 * @return
	 * @throws JAXRException
	 */
	public List<ClassificationImpl> updateClassifications(RegistryObjectImpl ro, ArrayList<String>conceptTypes) throws JAXRException {
		
		/* 
		 * This list contains all classifications that have to be created
		 */
		List<ClassificationImpl> clases = null;

		/* 
		 * The list of classifications (key) that must be deleted
		 */
		ArrayList<Key> objectsToDelete = new ArrayList<Key>();

		/* 
		 * Determine classifications actually assigned to the registry object;
		 * at least an empty array list is returned (see RegistryObjectImpl)
		 */
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		List<ClassificationImpl> classifications = dqm.getClassifications_ByObject(ro);

		if ((classifications.size() == 0) && (conceptTypes == null)) return clases;

		if (classifications.size() == 0) {			
			/* 
			 * The registry object has no classifications assigned, 
			 * so create classifications for each classification node
			 */
			clases = createClassifications(conceptTypes);
			/*
			 * Classifications that have been added to a registry
			 * object must not be saved explicitly
			 */
			ro.addClassifications(clases);			
			
		} else {

			if (conceptTypes == null) {

				/*
				 * Remove all allocated classifications
				 */
				for (ClassificationImpl classification:classifications) {
					objectsToDelete.add(classification.getKey());
				}

				/*
				 * Delete classifications
				 */
				deleteObjects(objectsToDelete);
				
			} else {
				
				/* Determine which classifications have to be removed, 
				 * and which have to created
				 */
				for (ClassificationImpl classification:classifications) {
					
					ConceptImpl concept = (ConceptImpl) classification.getConcept();
					if (concept == null) continue;
					
					String conceptType = concept.getId();
					if (conceptTypes.contains(conceptType)) {
						/* 
						 * The respective classification already exists; this implies 
						 * that nothing must be done, so remove conceptType from input 
						 * list
						 */
						conceptTypes.remove(conceptType);
	
					} else {

						/* 
						 * Remove classification from registry object
						 */
						ro.removeClassification(classification);
						
						/* 
						 * In this case the respective classification 
						 * MUST be removed
						 */
						objectsToDelete.add(classification.getKey());
					}
				}
				
				/*
				 * Delete classifications
				 */
				deleteObjects(objectsToDelete);
				
				/* 
				 * Create classifications from remaining conceptTypes
				 */
				if (conceptTypes.isEmpty() == false) {

					clases = createClassifications(conceptTypes);
					/*
					 * Classifications that have been added to a registry
					 * object must not be saved explicitly
					 */
					ro.addClassifications(clases);			
				
				}
			}

		}
		
		return clases;
		
	}

	/**
	 * This method deletes a set of classifications from
	 * a certain registry object, that refer to classification
	 * nodes that start with a specific prefix
	 * 
	 * @param ro
	 * @param prefix
	 * @throws JAXRException
	 */
	public void deleteClassifications(RegistryObjectImpl ro, String prefix) throws JAXRException {

		/* 
		 * The list of classifications (key) that must be deleted
		 */
		ArrayList<Key> objectsToDelete = new ArrayList<Key>();

		/* 
		 * Determine classifications actually assigned to the registry object;
		 * at least an empty array list is returned (see RegistryObjectImpl)
		 */
		JaxrDQM dqm = new JaxrDQM(jaxrHandle);

		List<ClassificationImpl> classifications = dqm.getClassifications_ByObject(ro);
		if (classifications.size() > 0) return;
		
		/*
		 * Remove all allocated classifications
		 */
		for (ClassificationImpl classification:classifications) {
				
			ConceptImpl concept = (ConceptImpl) classification.getConcept();
			if (concept == null) continue;
			
			String conceptType = concept.getId();
			if (conceptType.startsWith(prefix)) {

				/* 
				 * Remove classification from registry object
				 */
				ro.removeClassification(classification);
				
				/*
				 * The respective classification MUST be removed
				 */
				objectsToDelete.add(classification.getKey());

			}
			
		}
			
		/* 
		 * Delete the detected classifications
		 */
		deleteObjects(objectsToDelete);
		
	}

	/**
	 * Public method to delete a registry object identified
	 * by its unique identifier; in case of a composite object
	 * a cascading deleted is invoked
	 * 
	 * @param uid
	 */
	public void deleteRegistryObject(String uid) {
		
		JaxrTransaction transaction = new JaxrTransaction();
		
		try {
			
			RegistryObjectImpl ro = getRegistryObjectById(uid);
			if (ro == null) return;				
			
			/* 
			 * Distinguish between single and composite objects
			 */
			String objectType = ro.getObjectType().getKey().getId();
			if (objectType.startsWith(REGISTRY_PACKAGE)) {
				
				/* 
				 * A composite object that requires a cascading delete; 
				 * actually deletion of these objects is restricted to 
				 * the system administrator
				 */
				this.deleteRegistryPackage((RegistryPackageImpl)ro, transaction);
				
			} else {				
				/* 
				 * A single object is prepared for deletion
				 */
				this.deleteRegistryObject(ro, transaction);
			}

			/*
			 * Finally delete objects
			 */
			this.removeObjects(transaction.getObjectsToDelete());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * A helper method to finally remove a list of 
	 * registry objects from an OASIS ebXML RegRep
	 * 
	 * @param objectsToDelete
	 * @throws JAXRException
	 */
	private void removeObjects(ArrayList<RegistryObjectImpl> objectsToDelete) throws JAXRException {

		/*
		 * Build list of keys from registry objects
		 */
		ArrayList<Key> keys = new ArrayList<Key>();

		for (RegistryObjectImpl object:objectsToDelete) {
			keys.add(object.getKey());
		}
		
		/*
		 * Remove referenced objects (by key) from RegRep
		 */
		BusinessLifeCycleManagerImpl blcm = jaxrHandle.getBLCM();
		blcm.deleteObjects(keys);

		/*
		 * Reactor post processing 
		 */
		ReactorImpl.onDelete(objectsToDelete);

	}
	
	/**
	 * A private method to delete a certain registry object and
	 * all of its associations, classificatons and external links
	 * 
	 * @param ro
	 * @param transaction
	 * @throws Exception
	 */
	public void deleteRegistryObject(RegistryObjectImpl ro, JaxrTransaction transaction) throws Exception {

		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		/* 
		 * Determine all associations that are affected by the
		 * current registry object and prepare them fro removal
		 * 
		 * This also removes the associations to the external
		 * links referenced by this object
		 */
		
		List<AssociationImpl> associations = dqm.getAssociations_All(ro);
		if (associations.size() > 0) {	
			
			for (AssociationImpl association:associations) {
				deleteRegistryObject(association, transaction);
			}

		}
		
		/* 
		 * Determine all classifications that are effected by the 
		 * current registry object and prepare them to be removed
		 */

		List<ClassificationImpl> classifications = dqm.getClassifications_ByObject(ro);
		if (classifications.size() > 0) {
			
			for (ClassificationImpl classification:classifications) {
				transaction.addObjectToDelete(classification);
			}

		}

		/* 
		 * Register this object to be deleted
		 */
		transaction.addObjectToDelete(ro);
				
	}

	/**
	 * A private method to delete a composite object
	 * 
	 * @param rp
	 * @param transaction
	 * @throws Exception
	 */
	private void deleteRegistryPackage(RegistryPackageImpl rp, JaxrTransaction transaction) throws Exception {

		JaxrDQM dqm = new JaxrDQM(jaxrHandle);
		
		/* 
		 * Delete registry objects bottom up, so first start with
		 * all members of this registry package; finally the package 
		 * is prepared of deleting
		 */

		List<RegistryObjectImpl> members = dqm.getPackageMembers(rp);
		if (members.size() > 0) {
			
			for (RegistryObjectImpl member:members) {
				String objectType = dqm.getObjectType(member);

				if (objectType.startsWith(REGISTRY_PACKAGE)) {
					/* 
					 * A composite object that requires a cascading delete
					 */
					this.deleteRegistryPackage((RegistryPackageImpl)member, transaction);
					
				} else {				
					/* 
					 * A single object is prepared for deletion
					 */
					this.deleteRegistryObject(member, transaction);

				}

			}
		}
		
		this.deleteRegistryObject((RegistryObjectImpl)rp, transaction);
		
	}
	
	/**
	 * A helper method to retrieve a concept from type
	 * 
	 * @param conceptType
	 * @return
	 * @throws JAXRException
	 */
	private Concept getConcept(String conceptType) throws JAXRException {

		DeclarativeQueryManagerImpl dqm = jaxrHandle.getDQM();
		return (Concept) dqm.getRegistryObject(conceptType, LifeCycleManager.CONCEPT);
		
	}
	
}
