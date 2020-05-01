/*
 * ErrorMessage.java
 *
 * Created on September 15, 2006, 11:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gov.nist.hl7.v3.log;

import gov.nist.hl7.v3.log.jdbc.DatabaseFacade;
import gov.nist.hl7.v3.misc.Configuration;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 *
 * @author mccaffrey
 */
public class ErrorMessage {
    
    private Configuration config = null;
    
    private String baseId = null;
    private String errorId = null;
    private String errorMessage = null;
    
    public ErrorMessage(ResultSet result) {
        try {
            this.setBaseId(result.getString(DatabaseFacade.ERROR_TABLE_BASE_ID_COLUMN));
            this.setErrorId(result.getString(DatabaseFacade.ERROR_TABLE_ID_COLUMN));
            this.setErrorMessage(result.getString(DatabaseFacade.ERROR_TABLE_ERROR_MESSAGE_COLUMN));            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    /**
     * Creates a new instance of ErrorMessage
     */
    public ErrorMessage(Configuration config, String baseId) {
        errorId = UUID.randomUUID().toString();
        this.config = config;
        this.baseId = baseId;
    }
    
    public Configuration getConfig() {
        return config;
    }
    
    public void setConfig(Configuration config) {
        this.config = config;
    }
    
    public String getBaseId() {
        return baseId;
    }
    
    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }
    
    public String getErrorId() {
        return errorId;
    }
    
    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public boolean save() {
        try {
            return DatabaseFacade.getInstance(getConfig()).addError(this);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }
}
