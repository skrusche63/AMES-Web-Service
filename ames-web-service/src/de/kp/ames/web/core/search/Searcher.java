package de.kp.ames.web.core.search;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.search
 *  Module: Searcher
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #search #searcher #web
 * </SemanticAssist>
 *
 */

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

public interface Searcher {

	/**
	 * Retrieve facets from Apache Solr
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String facet() throws Exception;

	/**
	 * Retrieve entries from Apache Solr search index
	 * 
	 * @param request
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public String search(String request, String start, String limit) throws Exception;

	/**
	 * External search method that supports term suggestion
	 * 
	 * @param request
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public String suggest(String request, String start, String limit) throws Exception;

}
