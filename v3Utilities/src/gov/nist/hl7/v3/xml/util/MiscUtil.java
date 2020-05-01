/*
 * MiscUtil.java
 *
 * Created on March 30, 2007, 10:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gov.nist.hl7.v3.xml.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author mccaffrey
 */
public class MiscUtil {
    
    /** Creates a new instance of MiscUtil */
    public MiscUtil() {
    }
    
    public static String retrieveFromUrl(String fullUrl) throws IOException {
        
        StringBuffer sb = new StringBuffer();
        
        URL url = new URL(fullUrl);
        URLConnection connection = url.openConnection();
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = new String();
        
        while ((line = rd.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
}
