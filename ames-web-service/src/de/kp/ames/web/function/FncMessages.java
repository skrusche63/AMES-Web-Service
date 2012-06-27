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

import de.kp.ames.web.core.CoreMessages;

public class FncMessages extends CoreMessages {

	/*
	 * Access specific messages
	 */
	public static String ACCESSOR_CREATED = "Accessor successfully created.";
	
	/*
	 * Bulletin specific messages
	 */
	public static String POSTING_CREATED = "Posting successfully created.";

	/*
	 * Communication specific messages
	 */
	public static String CHAT_CREATED = "Chat message successfully submitted.";
	public static String MAIL_CREATED = "Mail message successfully submitted.";

	/*
	 * DMS specific messages
	 */
	public static String DOCUMENT_CREATED = "Document successfully submitted.";
	public static String IMAGE_CREATED    = "Image successfully submitted.";
	
	/*
	 * Group specifc messages
	 */
	public static String AFFILIATION_CREATED   = "Affiliation successfully submitted.";
	public static String AFFILIATION_DELETED   = "Affiliation successfully deleted.";
	public static String AFFILIATION_UPDATED   = "Affiliation successfully updated.";
	public static String CATEGORY_CREATED      = "Category successfully submitted.";
	public static String CONTACT_CREATED       = "Primary contact successfully submitted.";
	public static String ORGANIZATION_CREATED  = "Organization successfully submitted.";
	public static String ORGANIZATION_UPDATED  = "Organization successfully updated.";
	
	/*
	 * Namespace specific messages
	 */
	public static String FOLDER_CREATED = "Namespace folder successfully submitted.";
	
	/*
	 * Transformation specific messages
	 */
	public static String EVALUATION_CREATED    = "Evaluation successfully submitted.";
	public static String PRODUCT_CREATED       = "Product successfully submitted.";
	public static String PRODUCTOR_CREATED     = "Productor successfully submitted.";
	public static String REASONER_CREATED      = "Reasoner successfully submitted.";
	public static String TRANSFORMATOR_CREATED = "Transformator successfully submitted.";

	/*
	 * User specific messages
	 */
	public static String USER_UPDATED = "User successfully updated.";

	/*
	 * Registry object specific messages
	 */
	public static String EXTERNAL_LINK_CREATED   = "External Link successfully submitted.";
	public static String REGISTRY_OBJECT_DELETED = "Registry Object sucessfully deleted.";

	/*
	 * Top pacakge descriptions
	 */
	public static String ACCESSOR_DESC      = "This is the top package to manage all submitted accessors.";
	public static String CHAT_DESC          = "This is the top package to manage all submitted chat messages.";
	public static String DOCUMENT_DESC      = "This is the top package to manage all submitted documents.";
	public static String EVALUATION_DESC    = "This is the top package to manage all submitted evaluations.";
	public static String IMAGE_DESC         = "This is the top package to manage all submitted images.";
	public static String LINK_DESC          = "This is the top package to manage all submitted external links.";
	public static String MAIL_DESC          = "This is the top package to manage all submitted mails.";
	public static String NAMESPACE_DESC     = "This is the top package to manage all submitted namespaces.";
	public static String POSTING_DESC       = "This is the top package to manage all submitted postings.";
	public static String PRODUCT_DESC       = "This is the top package to manage all submitted products.";
	public static String PRODUCTOR_DESC     = "This is the top package to manage all submitted productors.";
	public static String REASONER_DESC      = "This is the top package to manage all submitted reasoners.";
	public static String TRANSFORMATOR_DESC = "This is the top package to manage all submitted transformators.";
	
}
