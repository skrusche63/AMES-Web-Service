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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.NameID;

import de.kp.omar.jaas.realm.UserPrincipal;

/**
 * This is a utility class that provides access to the
 * SAML 2.0 assertion associated with the caller's user.
 * 
 * Note: SamlUtil must be used in a federated environment,
 * where user authentication is performed by a SAML IdP.
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */

public class SamlUtil {

	/*
	 * Reference to session principal parameter
	 */
	private static String SESSION_PRINCIPAL = "picketlink.principal";
	
	/*
	 * OASIS ebXML RegRep compliant name format
	 */
	private static String SAML2_NAME_FORMAT = "urn:oasis:names:tc:SAML:2.0:nameid-format:persistent";

	private static String NO_HTTP_SESSION   = "[SamlUtil] No Http Session established.";
	private static String NO_USER_PRINCIPAL = "[SamlUtil] No User Principal derived from Http Session.";
	
	/**
	 * This method retrieves the SAML v2.0 assertion associated
	 * with the caller's user from a predefined session parameter
	 * 
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public static Assertion getSamlAssertion(HttpServletRequest request) throws Exception {

		if (request == null) return null;

		// The session MUST exist
		HttpSession session = request.getSession(false);
		if (session == null) throw new Exception(NO_HTTP_SESSION);
		
		// The user principal MUST exist 
		UserPrincipal userPrincipal = (UserPrincipal) session.getAttribute(SESSION_PRINCIPAL);
		if (userPrincipal == null) throw new Exception(NO_USER_PRINCIPAL);
		
		return userPrincipal.getAssertion();

	}
	
	/**
	 * The value of the SAML V2.0 Assertion Parameter
	 * NAMEID is equivalent to the unique user identifier
	 * 
	 * @param assertion
	 * @return
	 */
	public static String getUser(Assertion assertion) {

		NameID nameId = assertion.getSubject().getNameID();
		if (nameId.getFormat().equals(SAML2_NAME_FORMAT)) {
			return nameId.getValue();
		}

		return null;
	}

}
