/*
 * Io.java
 *
 * Created on January 16, 2007, 11:01 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gov.nist.hl7.v3.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author mccaffrey
 */
public class Io {
    
    public static String getUrlContents(String url) throws IOException {
        
        StringBuffer sb = new StringBuffer();
                
        URL destination = new URL(url);
        URLConnection connection = destination.openConnection();
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = new String();
        
        while ((line = rd.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        
        return sb.toString();
        
    }
    
}
