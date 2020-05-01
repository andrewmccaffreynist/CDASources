/*
 * HttpHeader.java
 *
 * Created on September 15, 2006, 5:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gov.nist.hl7.v3.log;

import gov.nist.hl7.v3.log.jdbc.DatabaseFacade;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author mccaffrey
 */
public class HttpHeader {
    
    private String name = null;
    private String value = null;
    
    public HttpHeader(ResultSet result) {
        try {
            this.setName(result.getString(DatabaseFacade.HTTP_HEADER_TABLE_HEADER_NAME_COLUMN));
            this.setValue(result.getString(DatabaseFacade.HTTP_HEADER_TABLE_HEADER_VALUE_COLUMN));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    /** Creates a new instance of HttpHeader */
    public HttpHeader() {
    }
    public HttpHeader(String name, String value) {
        this.setName(name);
        this.setValue(value);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String toString() {
        return this.getName() + ": " + this.getValue();
    }
}
