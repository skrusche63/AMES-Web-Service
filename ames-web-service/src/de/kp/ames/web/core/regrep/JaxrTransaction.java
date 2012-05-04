package de.kp.ames.web.core.regrep;
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.registry.infomodel.Key;

import org.freebxml.omar.client.xml.registry.infomodel.RegistryObjectImpl;
import org.freebxml.omar.client.xml.registry.infomodel.RegistryPackageImpl;

/**
 * JaxrTransaction is a temporary data structure that is used to
 * support write request to an OASIS ebXML RegRep
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */
public class JaxrTransaction {

	private String taid;	

	/* 
	 * This flag indicates whether the current transaction encloses
	 * a single registry object or a set of multiple registry objects
	 */
	
	private boolean multiple = false;
	
	/* A list of objects that has been successfully created or
	 * modified during this transaction, and which finally 
	 * must be saved to the OASIS ebXML registry service
	 */ 
	
	private ArrayList<RegistryObjectImpl> objectsToSave;
	
	/* 
	 * A list of objects that have to be deleted
	 */
	
	private ArrayList<Key> objectsTodelete;
	
	/* 
	 * The container describes a registry package that serves as a 
	 * container for all of the registry objects processed within 
	 * this transaction
	 */
	
	private RegistryPackageImpl container;
	
	/* 
	 * This input stream represents a repository item
	 */
	private InputStream stream;
	
	/* 
	 * This indicator determines whether this transaction affects 
	 * the search index (default == true)
	 */

	private boolean index = true;

	public JaxrTransaction() {		
		
		this.objectsToSave   = new ArrayList<RegistryObjectImpl>();
		this.objectsTodelete = new ArrayList<Key>();

	}

	/**
	 * @param taid
	 */
	public void setTransactionId(String taid) {
		this.taid = taid;
	}
	
	/**
	 * @return
	 */
	public String getTransactionId() {
		return this.taid;
	}
	
	/**
	 * @param ro
	 */
	public void addObjectToSave(RegistryObjectImpl ro) {
		this.objectsToSave.add(ro);
	}

	/**
	 * @param roList
	 */
	public void addObjectsToSaveAll(ArrayList<RegistryObjectImpl> roList) {
		this.objectsToSave.addAll(roList);
	}
	
	/**
	 * @param key
	 */
	public void addObject2Delete(Key key) {
		this.objectsTodelete.add(key);
	}
	
	/**
	 * @return
	 */
	public ArrayList<RegistryObjectImpl> getObjectsToSave() {
		return this.objectsToSave;
	}
	
	/**
	 * @return
	 */
	public ArrayList<Key> getObjectsToDelete() {
		return this.objectsTodelete;
	}
	
	/**
	 * @param value
	 */
	public void setContainer(RegistryPackageImpl value) {
		this.container = value;
	}
	
	/**
	 * @return
	 */
	public RegistryPackageImpl getContainer() {
		return this.container;
	}

	/**
	 * @param value
	 */
	public void setIndex(boolean value) {
		this.index = value;
	}

	/**
	 * @return
	 */
	public boolean getIndex() {
		return this.index;
	}
	
	/**
	 * @param value
	 */
	public void setMultiple(boolean value) {
		this.multiple = value;
	}
	
	/**
	 * @return
	 */
	public boolean isMultiple() {
		return this.multiple;
	}
	
	/**
	 * @param repositoryItem
	 */
	public void setRepositoryItem(String repositoryItem) {

		try {
			this.stream = new ByteArrayInputStream(repositoryItem.getBytes("UTF-8"));
		
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	/**
	 * @param repositoryItem
	 */
	public void setRepositoryItem(InputStream repositoryItem) {
		this.stream = repositoryItem;
		
	}
	
	/**
	 * @return
	 */
	public InputStream getRepositoryItem() {
		return this.stream;
		
	}

}
