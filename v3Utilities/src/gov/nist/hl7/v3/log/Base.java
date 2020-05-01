/*
 * Base.java
 *
 * Created on September 14, 2006, 3:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gov.nist.hl7.v3.log;

import gov.nist.hl7.v3.log.jdbc.DatabaseFacade;
import gov.nist.hl7.v3.misc.Configuration;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author mccaffrey
 */
public class Base {
    
    private Configuration config = null;
    
    private String id = null;
    private String timestamp = null;
    private String transportMethod = null;
    private String direction = null;
    
    private Collection<ErrorMessage> errors = null;
    
    private Base() {}
    
    /** Creates a new instance of Base */
    public Base(Configuration config) {
        this.setConfig(config);
        this.setTimestamp(String.valueOf(Calendar.getInstance().getTime().getTime()));
        this.setId(UUID.randomUUID().toString());
    }
    
    public Base(Configuration config, String transportMethod, String direction) {
        this.setConfig(config);
        this.setId(UUID.randomUUID().toString());
        this.setTimestamp(String.valueOf(Calendar.getInstance().getTime().getTime()));
        this.setTransportMethod(transportMethod);
        this.setDirection(direction);
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getTransportMethod() {
        return transportMethod;
    }
    
    public void setTransportMethod(String transportMethod) {
        this.transportMethod = transportMethod;
    }
    
    public Configuration getConfig() {
        return config;
    }
    
    public void setConfig(Configuration config) {
        this.config = config;
    }
    
    public boolean save() {
        try {
            return DatabaseFacade.getInstance(getConfig()).addBase(this);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }
    
    public String getDirection() {
        return direction;
    }
    
    public void setDirection(String direction) {
        this.direction = direction;
    }
    
    
    
    
    // This assumes the ResultSet has already had next() run on it.
    public static Base createFromResultSet(ResultSet result) {
        Base base = new Base();
        try {
            base.setId(result.getString(DatabaseFacade.BASE_TABLE_ID_COLUMN));
            base.setDirection(result.getString(DatabaseFacade.BASE_TABLE_DIRECTION_COLUMN));
            base.setTimestamp(result.getString(DatabaseFacade.BASE_TABLE_TIMESTAMP_COLUMN));
            base.setTransportMethod(result.getString(DatabaseFacade.BASE_TABLE_TRANSPORT_METHOD_COLUMN));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return base;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        long dateStamp = Long.parseLong(this.getTimestamp());
        Date date = new Date(dateStamp);
        sb.append(date.toString());
        sb.append(" ");
        sb.append(this.getTransportMethod());
        sb.append(" ");
        if(this.getDirection().equals(Constants.DIRECTION_INCOMING)) 
            sb.append("Incoming");
        else
            sb.append("Outgoing");
    
        return sb.toString();
    }
    
    public void parseErrors() {
        try {
            errors = DatabaseFacade.getInstance(this.getConfig()).getErrorMessages(this.getId());        
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            errors = new ArrayList<ErrorMessage>();
        }
        
    }
    public boolean hasErrors() {
        if(this.getErrors().size() == 0)
            return false;
        return true;
    }
    public Collection<ErrorMessage> getErrors() {
        if(errors == null) 
            this.parseErrors();        
        return errors;
    }    
   
    public void setErrors(Collection<ErrorMessage> errors) {
        this.errors = errors;
    }
    
}
