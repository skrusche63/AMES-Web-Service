package de.kp.ames.web;
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
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */

public class Bundle extends ResourceBundle {

	public static final String BASE_NAME = "de.kp.ames.web.Settings";
    private static Bundle instance;

	private ResourceBundle bundle;
	private ArrayList<String> keys;

    protected Bundle() {
        bundle = ResourceBundle.getBundle(BASE_NAME);
        initKeys();
    }

    public synchronized static Bundle getInstance() {
        if (instance == null) instance = new Bundle();
        return instance;
    }
	
	public ResourceBundle getBundle() {
		return bundle;
	}

    protected Object handleGetObject(String key) {
        return getBundle().getObject(key);
     }

    public final Enumeration<String> getKeys() {
        return getBundle().getKeys();
     }

    public boolean hasKey(String key) {
    	return keys.contains(key);
    }
    
    private void initKeys() {
    	keys = new ArrayList<String>();
    	
    	Enumeration<String> e = getKeys();
		while(e.hasMoreElements()) {
			keys.add(e.nextElement());
		}
		
    }
}
