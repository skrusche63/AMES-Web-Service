package de.kp.ames.web.core.search.data;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.search.data
 *  Module: SolrEntryImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #data #entry #search #solr #web
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.freebxml.omar.client.xml.registry.infomodel.ClassificationImpl;
import org.freebxml.omar.client.xml.registry.infomodel.ConceptImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;

import de.kp.ames.web.core.regrep.JaxrBase;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.search.util.IndexUtil;

public class SolrEntryImpl implements SolrEntry {

	/*
	 * Reference to JaxrHandle
	 */
	protected JaxrHandle jaxrHandle;
	
	/*
	 * Reference to JaxrBase
	 */
	private JaxrBase jaxrBase;
	
	/*
	 * Reference to registry object
	 */
	private RegistryObjectImpl ro;
	
	/*
	 * Reference to semantic domain
	 */
	private String domain;
	
	public SolrEntryImpl(JaxrHandle jaxrHandle) {
		this.jaxrHandle = jaxrHandle;
		/*
		 * Instantiate JaxrBase
		 */
		jaxrBase = new JaxrBase(jaxrHandle);
	}
	
	/**
	 * Set registry object to be converted into
	 * a SolrEntry
	 * 
	 * @param ro
	 */
	public void setRegistryObject(RegistryObjectImpl ro) {
		this.ro = ro;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	/**
	 * @return
	 */
	public String getDomain() {
		return this.domain;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String getId() throws Exception {
		return this.ro.getId();
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String getName() throws Exception {
		return this.jaxrBase.getName(this.ro);
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String getDescription() throws Exception {
		return this.jaxrBase.getDescription(this.ro);
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public Collection<String> getFacets() throws Exception {
		
		/*
		 * Facets are retrieved from the assigned concepts
		 */
		List<String> facets = new ArrayList<String>();
		
		Collection<?> clases = ro.getClassifications();
		if (clases.isEmpty()) return facets;
		
		Iterator<?> iter = clases.iterator();
		while (iter.hasNext()) {
			
			ClassificationImpl clas = (ClassificationImpl)iter.next();
			ConceptImpl cpt = (ConceptImpl)clas.getConcept();
			
			String conceptType = cpt.getId();
			int pos = conceptType.lastIndexOf(":");
			
			String facet = conceptType.substring(pos+1);
			facets.add(facet);
			
		}
		
		return facets;

	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection<String> getTerms() throws Exception {

		Set<String> terms = new HashSet<String>();
		
		String chunk = getName() + " " + getDescription();
		String[] tokens = chunk.split(" ");
		
		for (String token:tokens) {
			
			String term = token.trim();
			List<String> keywords = IndexUtil.termAsKeywords(term);
		
			terms.addAll(keywords);
			
		}
		
		return new ArrayList<String>(terms);
		
	}
}
