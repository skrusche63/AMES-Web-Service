package de.kp.ames.web.core.util;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.core.util
 *  Module: CommonUtil
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #common #core #util #web
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

import java.util.regex.Pattern;

public class CommonUtil {

	public static final Pattern URN_PATTERN = Pattern.compile(
        "^urn:[a-z0-9][a-z0-9-]{0,31}:([a-z0-9()+,\\-.:=@;$_!*']|%[0-9a-f]{2})+$"
	);

	public static boolean isURN(String urn) {
        return URN_PATTERN.matcher(urn).matches();        
	}
	
    public static String capitalize(String words) {

    	StringBuffer buffer = new StringBuffer();
        
    	boolean isNewWord = true;
        int length = words.length();
        
        for (int i = 0; i < length; ++i) {
        
        	char c = words.charAt(i);
            if (Character.isWhitespace(c)) {
                isNewWord = true;
            
            } else if (isNewWord) {
                c = Character.toUpperCase(c);
                isNewWord = false;
            }
            
            buffer.append(c);
        
        }
        
        return buffer.toString();
    
    }

}
