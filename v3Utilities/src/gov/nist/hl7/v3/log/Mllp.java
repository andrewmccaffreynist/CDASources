/*
 * Mllp.java
 *
 * Created on September 15, 2006, 12:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gov.nist.hl7.v3.log;

import gov.nist.hl7.v3.log.jdbc.DatabaseFacade;
import gov.nist.hl7.v3.misc.Configuration;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author mccaffrey
 */
public class Mllp {
    
    private Configuration config = null;
    
    private String baseId = null;
    private String hostname = null;
    private String ip = null;
    private String fullMessage = null;
    private boolean successfulAcknowledgement = false;
    
    public Mllp(ResultSet result) {
        try {
            this.setBaseId(result.getString(DatabaseFacade.MLLP_TABLE_BASE_ID_COLUMN));
            this.setFullMessage(result.getString(DatabaseFacade.MLLP_TABLE_FULL_MESSAGE_COLUMN));
            this.setHostname(result.getString(DatabaseFacade.MLLP_TABLE_HOSTNAME_COLUMN));
            this.setIp(result.getString(DatabaseFacade.MLLP_TABLE_IP_COLUMN));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
    
    /** Creates a new instance of Mllp */
    public Mllp(Configuration config, String baseId) {
        this.config = config;
        this.setBaseId(baseId);
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
    
    public String getHostname() {
        return hostname;
    }
    
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String getFullMessage() {
        return fullMessage;
    }
    
    public void setFullMessage(String fullMessage) {
        this.fullMessage = fullMessage;
    }
    
    public boolean isSuccessfulAcknowledgement() {
        return successfulAcknowledgement;
    }
    
    public void setSuccessfulAcknowledgement(boolean successfulAcknowledgement) {
        this.successfulAcknowledgement = successfulAcknowledgement;
    }
    
    public boolean save() {
        try {
            return DatabaseFacade.getInstance(getConfig()).addMllp(this);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }
    
    public static Mllp createFromId(Configuration config, String baseId) {
        
        try {
            return DatabaseFacade.getInstance(config).getMllp(baseId);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
        
    }
    
}
