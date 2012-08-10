package de.kp.ames.web.core.search;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.search
 *  Module: IndexerImpl
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #core #indexer #search #web
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

import org.apache.solr.common.SolrInputDocument;

import de.kp.ames.web.core.search.data.SolrEntry;
import de.kp.ames.web.core.search.util.IndexUtil;

public class IndexerImpl implements Indexer {

	/*
	 * Reference to SolrProxy
	 */
	private SolrProxy solrProxy;
	
	public IndexerImpl() {
		solrProxy = SolrProxy.getInstance();
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.search.Indexer#deleteIndex()
	 */
	public void deleteIndex() {
		solrProxy.deleteIndex();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.search.Indexer#removeFromIndex(java.lang.String)
	 */
	public void removeFromIndex(String id) {
		solrProxy.removeIndexEntry(id);
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.search.Indexer#createIndexEntry(de.kp.ames.web.core.search.data.SolrEntry)
	 */
	public void createIndexEntry(SolrEntry entry) throws Exception {
		SolrInputDocument document = getSolrDocument(entry);
		solrProxy.createIndexEntry(document);
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.core.search.Indexer#getSolrDocument(de.kp.ames.web.core.search.data.SolrEntry)
	 */
	public SolrInputDocument getSolrDocument(SolrEntry entry) throws Exception {
		return IndexUtil.getSolrDocument(entry);
	}

}
