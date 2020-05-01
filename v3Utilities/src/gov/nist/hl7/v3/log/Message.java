/*
 * Message.java
 *
 * Created on September 15, 2006, 10:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gov.nist.hl7.v3.log;

import gov.nist.hl7.v3.log.jdbc.DatabaseFacade;
import gov.nist.hl7.v3.misc.Configuration;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 * @author mccaffrey
 */
public class Message {
    
    private Configuration config = null;
    
    private String baseId = null;
    private int counter = 0;
    private String message = null;
    
    /** Creates a new instance of Message */
    public Message(Configuration config, String baseId) {
        this.setConfig(config);
        this.setBaseId(baseId);
    }
    public Message(ResultSet result) {
        try {
            this.setBaseId(result.getString(DatabaseFacade.MESSAGE_TABLE_BASE_ID_COLUMN));
            try {
                this.setCounter(Integer.parseInt(result.getString(DatabaseFacade.MESSAGE_TABLE_COUNTER_COLUMN)));
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                this.setCounter(0);
            }
            this.setMessage(result.getString(DatabaseFacade.MESSAGE_TABLE_MESSAGE_COLUMN));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
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
    
    public int getCounter() {
        return counter;
    }
    
    public void setCounter(int counter) {
        this.counter = counter;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean save() {
        try {
            return DatabaseFacade.getInstance(getConfig()).addMessage(this);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }
    public static Collection<Message> getMessages(Configuration config, String baseId) {
        try {
            return DatabaseFacade.getInstance(config).getMessages(baseId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
}
