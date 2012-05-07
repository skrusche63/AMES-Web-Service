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

import java.security.SecureRandom;

/**
 * JaxrIdentity is an identity generator for unique
 * identifiers to be assigned to registry objects
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 */

public class JaxrIdentity {

	private JaxrIdentity() {}

	private static JaxrIdentity instance = new JaxrIdentity();

    /*
     * Random number generator for UUID generation
     */
	private final SecureRandom secRand = new SecureRandom();

    /*
     * 128-bit buffer for use with secRand
     */
    private final byte[] secRandBuf16 = new byte[16];
    
	public static JaxrIdentity getInstance() {
		return instance;
	}

	/**
	 * @return
	 */
	public String getUID() {
		return newUID();
	}

	/**
	 * @param prefix
	 * @return
	 */
	public String getPrefixUID(String prefix) {
		return prefix + ":" + newUID();		
	}
	
   /**
    * this method is taken from the ebXML registry service (OMAR) reference 
    * implementation and generates a new uuid.
    *
    * it returns a 36-character string of six fields separated by hyphens,
    * with each field represented in lowercase hexadecimal with the same
    * number of digits as in the field. The order of fields is: time_low,
    * time_mid, version and time_hi treated as a single field, variant and
    * clock_seq treated as a single field, and node.
    */
    
   private String newUID() {

	   secRand.nextBytes(secRandBuf16);
       secRandBuf16[6] &= 0x0f;
       secRandBuf16[6] |= 0x40; /* version 4 */
       secRandBuf16[8] &= 0x3f;
       secRandBuf16[8] |= 0x80; /* IETF variant */
       secRandBuf16[10] |= 0x80; /* multicast bit */

       long mostSig = 0;

       for (int i = 0; i < 8; i++) {
           mostSig = (mostSig << 8) | (secRandBuf16[i] & 0xff);
       }

       long leastSig = 0;

       for (int i = 8; i < 16; i++) {
           leastSig = (leastSig << 8) | (secRandBuf16[i] & 0xff);
       }

       return (digits(mostSig >> 32, 8) + "-" + digits(mostSig >> 16, 4) +
    	       "-" + digits(mostSig, 4) + "-" + digits(leastSig >> 48, 4) + "-" +
    	       digits(leastSig, 12));
    	        
   }

   // returns val represented by the specified number of hex digits

   private String digits(long val, int digits) {
       long hi = 1L << (digits * 4);
       return Long.toHexString(hi | (val & (hi - 1))).substring(1);
   }

}
