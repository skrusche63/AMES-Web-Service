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

import java.util.Locale;

import javax.xml.registry.JAXRException;
import javax.xml.registry.LifeCycleManager;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.InternationalString;

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

import de.kp.ames.web.core.regrep.JaxrBase;
import de.kp.ames.web.core.regrep.JaxrHandle;

/**
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */
public class JaxrLCM extends JaxrBase {

	public JaxrLCM(JaxrHandle requestHandle) {
		super(requestHandle);
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
