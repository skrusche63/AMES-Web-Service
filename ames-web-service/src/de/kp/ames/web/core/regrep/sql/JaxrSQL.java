package de.kp.ames.web.core.regrep.sql;
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

import org.freebxml.omar.common.CanonicalSchemes;

public class JaxrSQL {

	private static String AFFILIATED_WITH  		= CanonicalSchemes.CANONICAL_ASSOCIATION_TYPE_ID_AffiliatedWith;
	private static String EXTERNALLY_LINKS 		= CanonicalSchemes.CANONICAL_ASSOCIATION_TYPE_ID_ExternallyLinks;
	private static String HAS_FEDERATION_MEMBER = CanonicalSchemes.CANONICAL_ASSOCIATION_TYPE_ID_HasFederationMember;
	private static String HAS_MEMBER       		= CanonicalSchemes.CANONICAL_ASSOCIATION_TYPE_ID_HasMember;
	private static String RESPONSIBLE_FOR  		= CanonicalSchemes.CANONICAL_ASSOCIATION_TYPE_ID_ResponsibleFor;

	/************************************************************************
	 * 
	 * ASSOCIATONS      ASSOCIATONS      ASSOCIATONS      ASSOCIATONS
	 * 
	 ***********************************************************************/

	/**
	 * Retrieve all associations of type 'affiliated with' that refer to
	 * a certain user and a specific group
	 * 
	 * @param user
	 * @param group
	 * @return
	 */
	public static String getSQLAssociations_AffiliatedWith(String user, String group) {
		
		String query = "SELECT a.* FROM Association a, RegistryObject ro WHERE a.associationType='" + AFFILIATED_WITH + "'" + 
		" AND a.sourceObject='" + user + "' AND a.targetObject='" + group + "'";
    	
    	return query;    	

	}

	/**
	 * Retrieve all associations of type 'externally links' that refer to
	 * a certain target object
	 * 
	 * @param target
	 * @return
	 */
	public static String getSQLAssociations_ExternallyLinks(String target) {
		
		String query = "SELECT a.* FROM Association a, RegistryObject ro WHERE a.associationType='" + EXTERNALLY_LINKS + "'" + 
		" AND a.targetObject='" + target + "'";
		
		return query;
		
	}

	/**
	 * Retrieve all associations of type 'has member' that refer to a 
	 * certain source and a specific target object
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static String getSQLAssociations_HasMember(String source, String target) {
		
		String query = "SELECT a.* FROM Association a WHERE a.associationType='" + HAS_MEMBER + "'" + 
	    " AND a.sourceObject='" + source + "' AND a.targetObject='" + target + "'";
		
		return query;
		
	}

	/**
	 * Retrieve all associations that refer to a certain source and
	 * target object
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static String getSQLAssociation(String source, String target) {
		
    	String query = "SELECT DISTINCT a.* FROM Association a WHERE a.sourceObject='" + source + "'" + 
    	" AND a.targetObject='" + target + "'";
    	
    	return query;    	

	}

	/**
	 * Retrieve all associations that are specified with a certain
	 * source object and classified by a specific classification node
	 * 
	 * @param source
	 * @param cn
	 * @return
	 */
	public static String getSQLAssociations_BySource(String source, String cn) {
		
		String query = "SELECT a.* FROM Association a, RegistryObject ro, Classification clas" + 
		" WHERE a.sourceObject='" + source + "' AND clas.classifiedObject=a.id AND clas.classificationNode='" + cn + "'";

		return query;
		
	}

	/**
	 * Retrieve all associations that refer to a certain registry
	 * object either as source or target object
	 * 
	 * @param id
	 * @return
	 */
	public static String getSQLAssociations_All(String id) {
		
		String query = "SELECT a.* FROM Association a WHERE a.sourceObject='" + id + "'" + 
		" OR a.targetObject='" + id + "'";

		return query;
		
	}

	/**
	 * Retrieve all associations of type 'responsible for' that refer
	 * to a certain source object
	 *  
	 * @param source
	 * @return
	 */
	public static String getSQLAssociations_ResponsibleFor(String source) {
		
		String query = "SELECT a.* FROM Association a, RegistryObject ro WHERE a.associationType='" + RESPONSIBLE_FOR + "'" + 
		" AND a.sourceObject='" + source + "'";
		
		return query;
		
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
	 */
	public static String getSQLClassificationNode(String id ) {
		
		String query = "SELECT cn.* FROM ClassificationNode cn WHERE cn.id='" + id + "'";
		return query;
		
	}	 

	/**
	 * Retrieve all classification nodes that are subordinates
	 * of a certain parent node
	 * 
	 * @param pn
	 * @return
	 */
	public static String getSQLClassificationNodes_ByParent(String pn ) {
		
		String query = "SELECT cn.* FROM ClassificationNode cn WHERE cn.parent='" + pn +"'";
		return query;
		
	}	 

	/**
	 * Retrieve all classifications nodes that are subordinates
	 * of a certain parent nodes and that are used to classify
	 * a specific registry object
	 * 
	 * @param id
	 * @param pn
	 * @return
	 */
	public static String getSQLClassificationNodes_ByObject(String id, String pn) {
		
		String query = "SELECT DISTINCT cn.* FROM ClassificationNode cn, Classification c WHERE cn.parent='" + pn +"'" +
		" AND c.classificationNode=cn.id AND c.classifiedObject='" + id + "'";
		
		return query;
		
	}

	/**
	 * Retrieve all classifications that refer to a certain registry
	 * object as classified object
	 * 
	 * @param id
	 * @return
	 */
	public static String getSQLClassifications_ByObject(String id) {

		String query = "SELECT c.* FROM Classification c WHERE c.classifiedObject='" + id + "'"; 
		return query;
		
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
	 */
	public static String getSQLExternalLinks_ByClasNode(String cn) {
		
		String query = "SELECT DISTINCT el.* FROM ExternalLink el, Classification clas WHERE clas.classifiedObject=el.id AND clas.classificationNode='" + cn + "'";
		return query;
		
	}

	/**
	 * Retrieve all external links that are classified by a certain
	 * classification node, and that are members of a specific
	 * registry package
	 * 
	 * @param cn
	 * @param rp
	 * @return
	 */
	public static String getSQLExternalLinks_ByClasNode(String cn, String rp) {
		
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT DISTINCT el.* FROM ExternalLink el, Association a, Classification c, RegistryPackage rp");
		sb.append(" WHERE rp.id='" + rp + "' AND a.associationType='" + HAS_MEMBER + "' AND a.sourceObject=rp.id AND");
		sb.append(" a.targetObject=el.id AND c.classifiedObject=el.id AND c.classificationNode='" + cn + "'");
			
		return sb.toString();
		
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
	 */
	public static String getSQLFederations_All() {
		
		String query = "SELECT f.* FROM Federation f";
		return query;
		
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
	 */
	public static String getSQLRegistryObject(String id) {
		
    	String query = "SELECT ro.* FROM RegistryObject ro WHERE ro.id='" +id + "'";
    	return query;    	

	}

	/**
	 * Retrieve all registry objects that are classified by
	 * a certain certain classification node
	 * 
	 * @param cn
	 * @return
	 */
	public static String getSQLRegistryObject_ByClasNode(String cn) {
		
		String query = "SELECT ro.* FROM RegistryObject ro, Classification clas WHERE clas.classifiedObject=ro.id AND clas.classificationNode='" + cn + "'";
		return query;
		
	}

	/**
	 * Retrieve all registry objects that refer to a certain 
	 * unique identifier and are classified by a specific
	 * classification node
	 * 
	 * @param id
	 * @param cn
	 * @return
	 */
	public static String getSQLRegistryObject_ByClasNode(String id, String cn) {

		String query = "SELECT ro.* FROM RegistryObject, Classification clas ro WHERE clas.classifiedObject=ro.id AND ro.id='" + id + "'" +
    	" AND clas.classificationNode='" + cn + "'";
		
		return query;

	}

	/**
	 * Retrieve all registry objects that are related to a 
	 * certain registry object as target objects
	 * 
	 * @param source
	 * @return
	 */
	public static String getSQLRegistryObject_RelatedAll(String source) {
		
		String query = "SELECT DISTINCT ro.* FROM RegistryObject ro, Association a WHERE " + 
	    " a.sourceObject='" + source + "' AND a.targetObject=ro.id";
		
		return query;
		
	}

	/**
	 * Retrieve all registry objects that refer to a certain
	 * user through associated auditable events
	 * 
	 * @param user
	 * @return
	 */
	public static String getSQLRegistryObject_byUser(String user) {

		StringBuffer query = new StringBuffer();
		
		query.append("SELECT DISTINCT ro.* from RegistryObject ro, AffectedObject ao, AuditableEvent ae WHERE ae.user_ = '" + user + "'");
		query.append(" AND ao.id=ro.id AND ao.eventId=ae.id");
		
		return query.toString(); 
	
	}

	/**
	 * Retrieve all registry objects that refer to a certain
	 * user through associated auditable events, and that
	 * are classified by a specific classification node
	 * 
	 * @param user
	 * @param cn
	 * @return
	 */
	public static String getSQLRegistryObject_ByUser(String user, String cn) {

		StringBuffer query = new StringBuffer();
		
		query.append("SELECT DISTINCT ro.* from RegistryObject ro, AffectedObject ao, AuditableEvent ae, Classification clas WHERE ae.user_ = '" + user + "'");
		query.append(" AND ao.id=ro.id AND ao.eventId=ae.id AND clas.classifiedObject=ro.id AND clas.classificationNode='" + cn + "'");
		
		return query.toString(); 
	
	}

	/**
	 * Retrieve all registry objects that are members of
	 * a certain registry package
	 * 
	 * @param rp
	 * @return
	 */
	public static String getSQLPackageMembers(String rp) {
		
		String query = "SELECT ro.* FROM RegistryObject ro, RegistryPackage rp, Association a WHERE rp.id='" + rp +"'" + 
	    " AND a.associationType='" + HAS_MEMBER + "' AND a.sourceObject=rp.id AND a.targetObject=ro.id";
		
		return query;
		
	}

	/**
	 * Retrieve all registry objects that refer to a certain
	 * unique identifier and that are members of a specific
	 * registry package
	 * 
	 * @param id
	 * @param rp
	 * @return
	 */
	public static String getSQLPackageMembers(String id, String rp) {
		
		String query = "SELECT ro.* FROM RegistryObject ro, RegistryPackage rp, Association a WHERE rp.id='" + rp +"'" + 
	    " AND a.associationType='" + HAS_MEMBER + "' AND a.sourceObject=rp.id AND a.targetObject='" + id + "'";
		
		return query;
		
	}

	/**
	 * Retrieve all registry objects that are members of
	 * a certain registry package and that are classified
	 * by a specific classification node
	 * 
	 * @param rp
	 * @param cn
	 * @return
	 */
	public static String getSQLPackageMembers_ByClasNode(String rp, String cn) {
		
		String query = "SELECT ro.* FROM RegistryObject ro, RegistryPackage rp, Association a, Classification c" + 
		" WHERE rp.id='" + rp + "' AND a.associationType='" + HAS_MEMBER + "' AND a.sourceObject=rp.id AND" +
		" a.targetObject=ro.id AND c.classifiedObject=ro.id AND c.classificationNode='" + cn + "'";
		
		return query;
		
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
	 */
	public static String getSQLOrganisations_AffiliatedWith(String user) {
		
    	String query = "SELECT DISTINCT o.* FROM Organization o, Association a WHERE a.sourceObject='" + user + "'" +
    	" AND a.associationType='" + AFFILIATED_WITH + "' AND a.targetObject=o.id";
    	
    	return query;    	

	}

	/**
	 * Retrieve all organizations that are registered in
	 * the OASIS ebXML RegRep
	 * 
	 * @return
	 */
	public static String getSQLOrganizations_All() {
		
    	String query = "SELECT o.* FROM Organization o";
    	return query;    	

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
	 */
	public static String getSQLRegistryPackage_ByClasNode(String cn) {
		
		String query = "SELECT rp.* FROM RegistryPackage rp, Classification clas WHERE clas.classifiedObject=rp.id AND clas.classificationNode='" + cn + "'";
    	return query;

	}

	/**
	 * Retrieve all registry packages that are members of a 
	 * certain registry package and are classified by a 
	 * specific classification node
	 * 
	 * @param rp
	 * @param cn
	 * @return
	 */
	public static String getSQLRegistryPackage_ByParent(String rp, String cn) {
		
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT DISTINCT tp.* FROM RegistryPackage tp, Association a, Classification c, RegistryPackage sp");
		sb.append(" WHERE sp.id='" + rp + "' AND a.associationType='" + HAS_MEMBER + "' AND a.sourceObject=sp.id AND");
		sb.append(" a.targetObject=tp.id AND c.classifiedObject=tp.id AND c.classificationNode='" + cn + "'");
			
		return sb.toString();
		
	}

	/**
	 * Retrieve all registry packages that are NOT members of
	 * any other registry package
	 * 
	 * @return
	 */
	public static String getSQLTopPackages() {
		
		StringBuffer query = new StringBuffer();
		
		query.append("SELECT DISTINCT p.* FROM RegistryPackage p WHERE p.id NOT IN ");
		query.append("(SELECT a.targetObject FROM Association a WHERE a.associationType='" + HAS_MEMBER + "')");
		
		return query.toString(); 
	
	}

	/**
	 * Retrieve all registry packages that are NOT members of
	 * any other registry package but are classified by a 
	 * specific classification node
	 * 
	 * @param cn
	 * @return
	 */
	public static String getSQLTopPackages_ByClasNode(String cn) {
		
		StringBuffer query = new StringBuffer();
		
		query.append("SELECT DISTINCT p.* FROM RegistryPackage p, Classification c WHERE c.classifiedObject=p.id ");
		query.append(" AND c.classificationNode LIKE '" + cn + "%' AND p.id NOT IN ");
		query.append("(SELECT a.targetObject FROM Association a WHERE a.associationType='" + HAS_MEMBER + "')");
		
		return query.toString(); 
	
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
	 */
	public static String getSQLRegistries_All() {
		
		String query = "SELECT r.* FROM Registry r";		
		return query;
		
	}

	/**
	 * Retrieve all registries that contribute to a
	 * certain federation object
	 * 
	 * @param federation
	 * @return
	 */
	public static String getSQLRegistries_HasFederationMember(String federation) {
		
		String query = "SELECT DISTINCT r.* FROM Registry r, Association a WHERE a.sourceObject='" + federation + "'" +
		" AND a.associationType='" + HAS_FEDERATION_MEMBER + "' AND a.targetObject=r.id";
		
		return query;
		
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
	 */
	public static String getSQLServices_ByClasNode(String cn) {
		
		String query = "SELECT DISTINCT s.* FROM Service s, Classification c WHERE c.classifiedObject=s.id AND c.classificationNode='" + cn + "'";
		return query;
		
	}

	/**
	 * Retrieve all service objects that are classified by a 
	 * certain classification node and that are members of
	 * a specific registry package
	 * 
	 * @param cn
	 * @param rp
	 * @return
	 */
	public static String getSQLServices_ByClasNode(String cn, String rp) {
		
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT DISTINCT s.* FROM Service s, Association a, Classification c, RegistryPackage rp");
		sb.append(" WHERE rp.id='" + rp + "' AND a.associationType='" + HAS_MEMBER + "' AND a.sourceObject=rp.id AND");
		sb.append(" a.targetObject=s.id AND c.classifiedObject=se.id AND c.classificationNode='" + cn + "'");
			
		return sb.toString();
		
	}
	
	/**
	 * Retrieve all service objects that are specified by
	 * a certain specification object
	 * 
	 * @param id
	 * @return
	 */
	public static String getSQLServices_BySpec(String id) {
		
		String query = "SELECT DISTINCT s.* FROM Service s, ServiceBinding sb, SpecificationLink sl WHERE" + 
		" sl.specificationObject='" + id + "' AND sl.serviceBinding=sb.id AND sb.service=s.id";
		
		return query;
		
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
	 */
	public static String getSQLUsers_AffiliatedWith(String group) {
		
    	String query = "SELECT DISTINCT u.* FROM User_ u, Association a WHERE a.sourceObject=u.id" + 
    	" AND a.associationType='" + AFFILIATED_WITH + "' AND a.targetObject='" + group + "'";
    	
    	return query;    	

	}

	/**
	 * Retrieve all user objects that are registered in an
	 * OASIS ebXML RegRep
	 * 
	 * @return
	 */
	public static String getSQLUsers_All() {
		
    	String query = "SELECT u.* FROM User_ u";
    	return query;    	

	}

}