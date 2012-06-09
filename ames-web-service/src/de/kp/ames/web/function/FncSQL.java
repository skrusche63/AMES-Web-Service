package de.kp.ames.web.function;
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

public class FncSQL {

	/*
	 * This is the classification of a password safe that may be assigned to the
	 * caller's user; actually, this safe is used to manage credentials for the
	 * chat and also mail service that is affiliated with the sense maker
	 * 
	 * The password safe is actually restricted to support an automatic login to 
	 * chat and mail servers associated with AMES
	 */
	private static String SAFE = FncConstants.FNC_SECURITY_ID_Safe;

	/*
	 * This is the classification of a certain namespace
	 */
	private static String NAMESPACE = FncConstants.FNC_ID_Namespace;
	
	/*
	 * This is the classification of a posting associated with the bulletin
	 * board functionality
	 */
	private static String POSTING = FncConstants.FNC_ID_Posting;

	/*
	 * Association classification nodes
	 */
	private static String RESPONSIBLE_FOR = CanonicalSchemes.CANONICAL_ASSOCIATION_TYPE_ID_ResponsibleFor;
	
	/**
	 * @param user
	 * @return
	 */
	public static String getSQLAssertions_Safe(String user) {

		String query = "SELECT a.* FROM Association a, RegistryObject ro, Classification clas" + 
		" WHERE a.sourceObject='" + user + "' AND a.targetObject=ro.id" + " AND clas.classifiedObject=ro.id AND clas.classificationNode='" + SAFE + "'";

		return query;

	}

	/**
	 * @param recipient
	 * @return
	 */
	public static String getSQLPostings_All(String recipient) {
		
		String query = "SELECT DISTINCT eo.* FROM ExtrinsicObject eo, Association a, Classification clas" + 
		" WHERE a.sourceObject='" + recipient + "' AND a.targetObject=eo.id AND clas.classifiedObject=eo.id AND clas.classificationNode='" + POSTING + "'";

		return query;
		
	}

	/**
	 * @param responsible
	 * @return
	 */
	public static String getSQLResponsibilities_All(String responsible) {
		
		String query = null;

		if (responsible == null) {
			/* 
			 * In case of no responsible provided, all registered namespaces
			 * must be retrieved
			 */
			query = "SELECT DISTINCT rp.* FROM RegistryPackage rp, Classification clas" + 
			" WHERE clas.classifiedObject=rp.id AND clas.classificationNode='" + NAMESPACE + "'";
			
		} else {
			/* 
			 * In this case we retrieve all registry packages that are characterised 
			 * as target objects in an association between an organization or user; 
			 * the respective association type describes the relation as 'responsible for'
			 */
			query = "SELECT DISTINCT rp.* FROM RegistryPackage rp, Association a WHERE a.sourceObject='" + responsible + "'" +
			" AND a.associationType='" + RESPONSIBLE_FOR + "' AND a.targetObject=rp.id";
    	
		}

		return query;    	

	}

}
