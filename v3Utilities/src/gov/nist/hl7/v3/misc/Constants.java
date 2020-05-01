/*
 * Constants.java
 *
 * Created on June 29, 2006, 5:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gov.nist.hl7.v3.misc;

/**
 * A list of static constants used by various components.
 * Much of this will move to web.xml eventually...
 * @author mccaffrey
 */
public class Constants {    
    /**
     * The OID representing the NIST Registry.
     */
    public static final String NIST_REGISTRY_OID = "2.16.840.1.113883.3.72.1";
    /**
     * Every URL that represents something in the NIST registry will begin with this
     * prefix.
     */
    public static final String NIST_REGISTRY_URL_PREFIX = "http://xreg2.nist.gov:8080/omar/registry/http/RefImpXMLDocuments/";
    public static final String NIST_REGISTRY_URL_ALL_PREFIX = "http://xreg2.nist.gov:8080/omar/registry/http/";
    
}
