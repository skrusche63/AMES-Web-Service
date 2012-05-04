package de.kp.ames.web.core.regrep.dqm;
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

import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;

import org.freebxml.omar.client.xml.registry.infomodel.AssociationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ConceptImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExternalLinkImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.FederationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.OrganizationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ServiceImpl;
import org.freebxml.omar.client.xml.registry.infomodel.UserImpl;

import de.kp.ames.web.core.regrep.JaxrBase;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.sql.JaxrSQL;

public class JaxrDQM extends JaxrBase {

	public JaxrDQM(JaxrHandle requestHandle) {
		super(requestHandle);
	}

	/************************************************************************
	 * 
	 * ASSOCIATION     ASSOCIATION     ASSOCIATION     ASSOCIATION
	 * 
	 ************************************************************************/

	/**
	 * Retrieve all associations that refer to a certain source and
	 * target object
	 * 
	 * @param so
	 * @param to
	 * @return
	 * @throws JAXRException
	 */
	public List<AssociationImpl> getAssociations(RegistryObjectImpl so, RegistryObjectImpl to) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLAssociation(so.getId(), to.getId());
		return getAssociationsByQuery(sqlString);

	}

	/**
	 * Retrieve all associations that refer to a certain registry
	 * object either as source or target object
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	public List<AssociationImpl> getAssociations_All(RegistryObjectImpl ro) throws JAXRException {		

		String sqlString = JaxrSQL.getSQLAssociations_All(ro.getId());		
		return getAssociationsByQuery(sqlString);
	
	}

	/**
	 * Retrieve all associations of type 'affiliated with' that refer to
	 * a certain user and a specific group
	 * 
	 * @param user
	 * @param group
	 * @return
	 * @throws JAXRException
	 */
	public List<AssociationImpl> getAssociations_AffiliatedWith(UserImpl user, OrganizationImpl group) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLAssociations_AffiliatedWith(user.getId(), group.getId());
		return getAssociationsByQuery(sqlString);

	}

	/**
	 * Retrieve all associations of type 'externally links' that refer to
	 * a certain target object
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	public List<AssociationImpl> getAssociations_ExternallyLinks(RegistryObjectImpl ro) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLAssociations_ExternallyLinks(ro.getId());
		return getAssociationsByQuery(sqlString);
		
	}

	/**
	 * Retrieve all associations of type 'has member' that refer to a 
	 * certain source and a specific target object
	 * 
	 * @param so
	 * @param to
	 * @return
	 * @throws JAXRException
	 */
	public List<AssociationImpl> getAssociations_HasMember(RegistryObjectImpl so, RegistryObjectImpl to) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLAssociations_HasMember(so.getId(), to.getId());
		return getAssociationsByQuery(sqlString);
		
	}

	
	/**
	 * Retrieve all associations of type 'responsible for' that refer
	 * to a certain source object
	 *  
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	public List<AssociationImpl> getAssociations_ResponsibleFor(RegistryObjectImpl ro) throws JAXRException {

		String sqlString = JaxrSQL.getSQLAssociations_ResponsibleFor(ro.getId());
		return getAssociationsByQuery(sqlString);
		
	}

	/**
	 * Retrieve all associations that are specified with a certain
	 * source object and classified by a specific classification node
	 * 
	 * @param ro
	 * @param cn
	 * @return
	 * @throws JAXRException
	 */
	public List<AssociationImpl> getAssociations_BySource(RegistryObjectImpl ro, String cn) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLAssociations_BySource(ro.getId(), cn);
		return getAssociationsByQuery(sqlString);
		
	}

	/**
	 * A common method to retrieve association from an sql statement
	 * 
	 * @param sqlString
	 * @return
	 * @throws JAXRException
	 */
	public List<AssociationImpl> getAssociationsByQuery(String sqlString) throws JAXRException {

		BulkResponse bulkResponse = executeQuery(sqlString);
		
		@SuppressWarnings("unchecked")
		List<AssociationImpl> list = new ArrayList<AssociationImpl>(bulkResponse.getCollection());
		return list;
		
	}

	/************************************************************************
	 * 
	 * CLASSIFICATIONS     CLASSIFICATIONS     CLASSIFICATIONS 
	 * 
	 ***********************************************************************/
	
	/**
	 * Retrieve all classification nodes that refer to a certain
	 * unique identifier
	 * 
	 * @param id
	 * @return
	 * @throws JAXRException 
	 */
	public List<ConceptImpl> getClassificationNode_ById(String id) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLClassificationNode(id);
		return getConceptsByQuery(sqlString);
		
	}	 

	/**
	 * Retrieve all classification nodes that are subordinates
	 * of a certain parent node
	 * 
	 * @param pn
	 * @return
	 * @throws JAXRException 
	 */
	public List<ConceptImpl> getClassificationNodes_ByParent(String pn) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLClassificationNodes_ByParent(pn);
		return getConceptsByQuery(sqlString);
		
	}	 

	/**
	 * Retrieve all classifications nodes that are subordinates
	 * of a certain parent nodes and that are used to classify
	 * a specific registry object
	 * 
	 * @param ro
	 * @param pn
	 * @return
	 * @throws JAXRException
	 */
	public List<ConceptImpl> getClassificationNodes_ByObject(RegistryObjectImpl ro, String pn) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLClassificationNodes_ByObject(ro.getId(), pn);
		return getConceptsByQuery(sqlString);
		
	}

	/**
	 * Retrieve all classifications that refer to a certain registry
	 * object as classified object
	 * 
	 * @param id
	 * @return
	 * @throws JAXRException 
	 */
	public List<ClassificationImpl> getClassifications_ByObject(RegistryObjectImpl ro) throws JAXRException {

		String sqlString = JaxrSQL.getSQLClassifications_ByObject(ro.getId());
		return getClassificationsByQuery(sqlString);
		
	}

	/**
	 * A common method to retrieve classifications from an sql statement
	 * 
	 * @param sqlString
	 * @return
	 * @throws JAXRException
	 */
	public List<ClassificationImpl> getClassificationsByQuery(String sqlString) throws JAXRException {

		BulkResponse bulkResponse = executeQuery(sqlString);
		
		@SuppressWarnings("unchecked")
		List<ClassificationImpl> list = new ArrayList<ClassificationImpl>(bulkResponse.getCollection());
		return list;
		
	}

	/**
	 * A common method to retrieve concepts from an sql statement
	 * 
	 * @param sqlString
	 * @return
	 * @throws JAXRException
	 */
	public List<ConceptImpl> getConceptsByQuery(String sqlString) throws JAXRException {

		BulkResponse bulkResponse = executeQuery(sqlString);
		
		@SuppressWarnings("unchecked")
		List<ConceptImpl> list = new ArrayList<ConceptImpl>(bulkResponse.getCollection());
		return list;
		
	}

	/************************************************************************
	 * 
	 * EXTERNAL LINK     EXTERNAL LINK     EXTERNAL LINK     EXTERNAL LINK
	 * 
	 ***********************************************************************/

	/**
	 * Retrieve all external links that are classified by a certain
	 * classification node
	 * 
	 * @param cn
	 * @return
	 * @throws JAXRException 
	 */
	public List<ExternalLinkImpl> getExternalLinks_ByClasNode(String cn) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLExternalLinks_ByClasNode(cn);
		return getExternalLinksByQuery(sqlString);
		
	}

	/**
	 * Retrieve all external links that are classified by a certain
	 * classification node, and that are members of a specific
	 * registry package
	 * 
	 * @param cn
	 * @param rp
	 * @return
	 * @throws JAXRException 
	 */
	public List<ExternalLinkImpl> getExternalLinks_ByClasNode(String cn, RegistryPackageImpl rp) throws JAXRException {
			
		String sqlString = JaxrSQL.getSQLExternalLinks_ByClasNode(cn, rp.getId());
		return getExternalLinksByQuery(sqlString);
		
	}

	/**
	 * A common method to retrieve external links from an sql statement
	 * 
	 * @param sqlString
	 * @return
	 * @throws JAXRException
	 */
	public List<ExternalLinkImpl> getExternalLinksByQuery(String sqlString) throws JAXRException {

		BulkResponse bulkResponse = executeQuery(sqlString);
		
		@SuppressWarnings("unchecked")
		List<ExternalLinkImpl> list = new ArrayList<ExternalLinkImpl>(bulkResponse.getCollection());
		return list;
		
	}

	/************************************************************************
	 * 
	 * EXTRINSIC OBJECT     EXTRINSIC OBJECT     EXTRINSIC OBJECT 
	 * 
	 ***********************************************************************/

	/**
	 * Retrieve all extrinsic objects that are classified by
	 * a certain classification node
	 * 
	 * @param cn
	 * @return
	 */
	public static String getSQLExtrinsicObjects_ByClasNode(String cn) {
		
		String query = "SELECT eo.* FROM ExtrinsicObject eo, Classification clas WHERE clas.classifiedObject=eo.id AND clas.classificationNode='" + cn + "'";				
		return query;
		
	}

	/**
	 * A common method to retrieve extrinsic objects from an sql statement
	 * 
	 * @param sqlString
	 * @return
	 * @throws JAXRException
	 */
	public List<ExtrinsicObjectImpl> getExtrinsicObjectsByQuery(String sqlString) throws JAXRException {

		BulkResponse bulkResponse = executeQuery(sqlString);
		
		@SuppressWarnings("unchecked")
		List<ExtrinsicObjectImpl> list = new ArrayList<ExtrinsicObjectImpl>(bulkResponse.getCollection());
		return list;
		
	}

	/************************************************************************
	 * 
	 * FEDERATION      FEDERATION      FEDERATION      FEDERATION
	 * 
	 ***********************************************************************/
	
	/**
	 * Retrieve all federations that are registered in an
	 * OASIS ebXML RegRep
	 * 
	 * @return
	 * @throws JAXRException 
	 */
	public List<FederationImpl> getFederations_All() throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLFederations_All();
		return getFederationsByQuery(sqlString);
		
	}

	/**
	 * A common method to retrieve federations from an sql statement
	 * 
	 * @param sqlString
	 * @return
	 * @throws JAXRException
	 */
	public List<FederationImpl> getFederationsByQuery(String sqlString) throws JAXRException {

		BulkResponse bulkResponse = executeQuery(sqlString);
		
		@SuppressWarnings("unchecked")
		List<FederationImpl> list = new ArrayList<FederationImpl>(bulkResponse.getCollection());
		return list;
		
	}

	/************************************************************************
	 * 
	 * OBJECTS     OBJECTS     OBJECTS     OBJECTS     OBJECTS     OBJECTS
	 * 
	 ***********************************************************************/

	/**
	 * Retrieve all registry objects that refer to a certain
	 * unique identifier
	 * 
	 * @param id
	 * @return
	 * @throws JAXRException 
	 */
	public List<RegistryObjectImpl> getRegistryObject(String id) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLRegistryObject(id);
    	return getRegistryObjectsByQuery(sqlString);   	

	}

	/**
	 * Retrieve all registry objects that are classified by
	 * a certain certain classification node
	 * 
	 * @param cn
	 * @return
	 * @throws JAXRException 
	 */
	public List<RegistryObjectImpl> getRegistryObject_ByClasNode(String cn) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLRegistryObject_ByClasNode(cn);
    	return getRegistryObjectsByQuery(sqlString);   	
		
	}

	/**
	 * Retrieve all registry objects that refer to a certain 
	 * unique identifier and are classified by a specific
	 * classification node
	 * 
	 * @param ro
	 * @param cn
	 * @return
	 * @throws JAXRException
	 */
	public List<RegistryObjectImpl> getRegistryObject_ByClasNode(RegistryObjectImpl ro, String cn) throws JAXRException {

		String sqlString = JaxrSQL.getSQLRegistryObject_ByClasNode(ro.getId(), cn);
    	return getRegistryObjectsByQuery(sqlString);   	

	}

	/**
	 * Retrieve all registry objects that are related to a 
	 * certain registry object as target objects
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	public List<RegistryObjectImpl> getRegistryObject_RelatedAll(RegistryObjectImpl ro) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLRegistryObject_RelatedAll(ro.getId());
    	return getRegistryObjectsByQuery(sqlString);   	
		
	}

	/**
	 * Retrieve all registry objects that refer to a certain
	 * user through associated auditable events
	 * 
	 * @param user
	 * @return
	 * @throws JAXRException 
	 */
	public List<RegistryObjectImpl> getRegistryObject_byUser(UserImpl user) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLRegistryObject_byUser(user.getId());
	   	return getRegistryObjectsByQuery(sqlString);   	
	
	}

	/**
	 * Retrieve all registry objects that refer to a certain
	 * user through associated auditable events, and that
	 * are classified by a specific classification node
	 * 
	 * @param user
	 * @param cn
	 * @return
	 * @throws JAXRException 
	 */
	public List<RegistryObjectImpl> getRegistryObject_ByUser(UserImpl user, String cn) throws JAXRException {

		String sqlString = JaxrSQL.getSQLRegistryObject_ByUser(user.getId(), cn);
	   	return getRegistryObjectsByQuery(sqlString);   	
	
	}

	/**
	 * Retrieve all registry objects that are members of
	 * a certain registry package
	 * 
	 * @param rp
	 * @return
	 * @throws JAXRException 
	 */
	public List<RegistryObjectImpl> getPackageMembers(RegistryPackageImpl rp) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLPackageMembers(rp.getId());
	   	return getRegistryObjectsByQuery(sqlString);   	
		
	}

	/**
	 * Retrieve all registry objects that refer to a certain
	 * unique identifier and that are members of a specific
	 * registry package
	 * 
	 * @param ro
	 * @param rp
	 * @return
	 * @throws JAXRException
	 */
	public List<RegistryObjectImpl> getPackageMembers(RegistryObjectImpl ro, RegistryPackageImpl rp) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLPackageMembers(ro.getId(), rp.getId());
	   	return getRegistryObjectsByQuery(sqlString);   	
		
	}

	/**
	 * Retrieve all registry objects that are members of
	 * a certain registry package and that are classified
	 * by a specific classification node
	 * 
	 * @param rp
	 * @param cn
	 * @return
	 * @throws JAXRException 
	 */
	public List<RegistryObjectImpl> getPackageMembers_ByClasNode(RegistryPackageImpl rp, String cn) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLPackageMembers_ByClasNode(rp.getId(), cn);
	   	return getRegistryObjectsByQuery(sqlString);   	
		
	}

	/**
	 * A common method to retrieve registry objects from an sql statement
	 * 
	 * @param sqlString
	 * @return
	 * @throws JAXRException
	 */
	public List<RegistryObjectImpl> getRegistryObjectsByQuery(String sqlString) throws JAXRException {

		BulkResponse bulkResponse = executeQuery(sqlString);
		
		@SuppressWarnings("unchecked")
		List<RegistryObjectImpl> list = new ArrayList<RegistryObjectImpl>(bulkResponse.getCollection());
		return list;
		
	}

	/************************************************************************
	 * 
	 * ORGANIZATIONS     ORGANIZATIONS     ORGANIZATIONS     ORGANIZATIONS
	 * 
	 ***********************************************************************/
	
	/**
	 * Retrieve all organizations that are affiliated with
	 * a certain user
	 * 
	 * @param user
	 * @return
	 * @throws JAXRException 
	 */
	public List<OrganizationImpl> getOrganisations_AffiliatedWith(UserImpl user) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLOrganisations_AffiliatedWith(user.getId());
    	return getOrganizationsByQuery(sqlString);    	

	}

	/**
	 * Retrieve all organizations that are registered in
	 * the OASIS ebXML RegRep
	 * 
	 * @return
	 * @throws JAXRException 
	 */
	public List<OrganizationImpl> getOrganization_All() throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLOrganization_All();
    	return getOrganizationsByQuery(sqlString);    	

	}

	/**
	 * A common method to retrieve organizations from an sql statement
	 * 
	 * @param sqlString
	 * @return
	 * @throws JAXRException
	 */
	public List<OrganizationImpl> getOrganizationsByQuery(String sqlString) throws JAXRException {

		BulkResponse bulkResponse = executeQuery(sqlString);
		
		@SuppressWarnings("unchecked")
		List<OrganizationImpl> list = new ArrayList<OrganizationImpl>(bulkResponse.getCollection());
		return list;
		
	}

	/************************************************************************
	 * 
	 * PACKAGES     PACKAGES     PACKAGES     PACKAGES     PACKAGES
	 * 
	 ***********************************************************************/

	/**
	 * Retrieve all registry packages that are classified by 
	 * a certain classification node
	 * 
	 * @param cn
	 * @return
	 * @throws JAXRException 
	 */
	public List<RegistryPackageImpl> getRegistryPackage_ByClasNode(String cn) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLRegistryPackage_ByClasNode(cn);
		return getRegistryPackagesByQuery(sqlString); 

	}

	/**
	 * Retrieve all registry packages that are members of a 
	 * certain registry package and are classified by a 
	 * specific classification node
	 * 
	 * @param rp
	 * @param cn
	 * @return
	 * @throws JAXRException 
	 */
	public List<RegistryPackageImpl> getRegistryPackage_ByParent(RegistryPackageImpl rp, String cn) throws JAXRException {
			
		String sqlString = JaxrSQL.getSQLRegistryPackage_ByParent(rp.getId(), cn);
		return getRegistryPackagesByQuery(sqlString); 
		
	}

	/**
	 * Retrieve all registry packages that are NOT members of
	 * any other registry package
	 * 
	 * @return
	 * @throws JAXRException 
	 */
	public List<RegistryPackageImpl> getTopPackages() throws JAXRException {
	
		String sqlString = JaxrSQL.getSQLTopPackages();
		return getRegistryPackagesByQuery(sqlString); 
	
	}

	/**
	 * Retrieve all registry packages that are NOT members of
	 * any other registry package but are classified by a 
	 * specific classification node
	 * 
	 * @param cn
	 * @return
	 * @throws JAXRException 
	 */
	public List<RegistryPackageImpl> getTopPackages_ByClasNode(String cn) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLTopPackages_ByClasNode(cn);
		return getRegistryPackagesByQuery(sqlString); 
	
	}

	/**
	 * A common method to retrieve registry packages from an sql statement
	 * 
	 * @param sqlString
	 * @return
	 * @throws JAXRException
	 */
	public List<RegistryPackageImpl> getRegistryPackagesByQuery(String sqlString) throws JAXRException {

		BulkResponse bulkResponse = executeQuery(sqlString);
		
		@SuppressWarnings("unchecked")
		List<RegistryPackageImpl> list = new ArrayList<RegistryPackageImpl>(bulkResponse.getCollection());
		return list;
		
	}

	/************************************************************************
	 * 
	 * REGISTRY      REGISTRY      REGISTRY      REGISTRY      REGISTRY
	 * 
	 ***********************************************************************/

	/**
	 * Retrieve all registries that are registered in an
	 * OASIS ebXML Registry
	 * 
	 * @return
	 * @throws JAXRException 
	 */
	public List<RegistryImpl> getRegistries_All() throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLRegistries_All();
		return getRegistriesByQuery(sqlString);
		
	}

	/**
	 * Retrieve all registries that contribute to a
	 * certain federation object
	 * 
	 * @param federation
	 * @return
	 * @throws JAXRException
	 */
	public List<RegistryImpl> getRegistries_HasFederationMember(FederationImpl federation) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLRegistries_HasFederationMember(federation.getId());
		return getRegistriesByQuery(sqlString);
		
	}

	/**
	 * A common method to retrieve registries from an sql statement
	 * 
	 * @param sqlString
	 * @return
	 * @throws JAXRException
	 */
	public List<RegistryImpl> getRegistriesByQuery(String sqlString) throws JAXRException {

		BulkResponse bulkResponse = executeQuery(sqlString);
		
		@SuppressWarnings("unchecked")
		List<RegistryImpl> list = new ArrayList<RegistryImpl>(bulkResponse.getCollection());
		return list;
		
	}

	/************************************************************************
	 * 
	 * SERVICES     SERVICES     SERVICES     SERVICES     SERVICES
	 * 
	 ***********************************************************************/

	/**
	 * Retrieve all service objects that are classified by a 
	 * certain classification node
	 * 
	 * @param cn
	 * @return
	 * @throws JAXRException 
	 */
	public List<ServiceImpl> getServices_ByClasNode(String cn) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLServices_ByClasNode(cn);
		return getServicesByQuery(sqlString);
		
	}

	/**
	 * Retrieve all service objects that are classified by a 
	 * certain classification node and that are members of
	 * a specific registry package
	 * 
	 * @param cn
	 * @param rp
	 * @return
	 * @throws JAXRException
	 */
	public List<ServiceImpl> getServices_ByClasNode(String cn, RegistryPackageImpl rp) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLServices_ByClasNode(cn, rp.getId());
		return getServicesByQuery(sqlString);
		
	}
	
	/**
	 * Retrieve all service objects that are specified by
	 * a certain specification object
	 * 
	 * @param ro
	 * @return
	 * @throws JAXRException
	 */
	public List<ServiceImpl> getServices_BySpec(RegistryObjectImpl ro) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLServices_BySpec(ro.getId());
		return getServicesByQuery(sqlString);
		
	}

	/**
	 * A common method to retrieve services from an sql statement
	 * 
	 * @param sqlString
	 * @return
	 * @throws JAXRException
	 */
	public List<ServiceImpl> getServicesByQuery(String sqlString) throws JAXRException {

		BulkResponse bulkResponse = executeQuery(sqlString);
		
		@SuppressWarnings("unchecked")
		List<ServiceImpl> list = new ArrayList<ServiceImpl>(bulkResponse.getCollection());
		return list;
		
	}

	/************************************************************************
	 * 
	 * USERS     USERS     USERS     USERS     USERS     USERS     USERS
	 * 
	 ***********************************************************************/

	/**
	 * Retrieve all user objects that are affiliated with a
	 * certain organization
	 * 
	 * @param group
	 * @return
	 * @throws JAXRException
	 */
	public List<UserImpl> getUser_AffiliatedWith(OrganizationImpl group) throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLUser_AffiliatedWith(group.getId());
    	return getUsersByQuery(sqlString);    	

	}

	/**
	 * Retrieve all user objects that are registered in an
	 * OASIS ebXML RegRep
	 * 
	 * @return
	 * @throws JAXRException 
	 */
	public List<UserImpl> getUser_All() throws JAXRException {
		
		String sqlString = JaxrSQL.getSQLUser_All();
    	return getUsersByQuery(sqlString);    	

	}

	/**
	 * A common method to retrieve users from an sql statement
	 * 
	 * @param sqlString
	 * @return
	 * @throws JAXRException
	 */
	public List<UserImpl> getUsersByQuery(String sqlString) throws JAXRException {

		BulkResponse bulkResponse = executeQuery(sqlString);
		
		@SuppressWarnings("unchecked")
		List<UserImpl> list = new ArrayList<UserImpl>(bulkResponse.getCollection());
		return list;
		
	}

}
