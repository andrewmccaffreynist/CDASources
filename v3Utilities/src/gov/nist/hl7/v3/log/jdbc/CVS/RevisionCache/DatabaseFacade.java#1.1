/*
 * DatabaseFacade.java
 *
 * Created on September 14, 2006, 5:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gov.nist.hl7.v3.log.jdbc;

import gov.nist.hl7.v3.log.Base;
import gov.nist.hl7.v3.log.Constants;
import gov.nist.hl7.v3.log.ErrorMessage;
import gov.nist.hl7.v3.log.Http;
import gov.nist.hl7.v3.log.HttpHeader;
import gov.nist.hl7.v3.log.Message;
import gov.nist.hl7.v3.log.Mllp;
import gov.nist.hl7.v3.log.Smtp;
import gov.nist.hl7.v3.misc.Configuration;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author mccaffrey
 */
public class DatabaseFacade {
    
    private DatabaseConnection connection = null;
    
    private Configuration config = null;
    private static DatabaseFacade instance = null;
    
    public static final String BASE_TABLE = "base";
    public static final String BASE_TABLE_ID_COLUMN = "id";
    public static final String BASE_TABLE_TIMESTAMP_COLUMN = "timestamp";
    public static final String BASE_TABLE_TRANSPORT_METHOD_COLUMN = "transportmethod";
    public static final String BASE_TABLE_DIRECTION_COLUMN = "direction";
    
    public static final String SMTP_TABLE = "smtp";
    public static final String SMTP_TABLE_BASE_ID_COLUMN = "baseid";
    public static final String SMTP_TABLE_FROM_COLUMN = "fromline";
    public static final String SMTP_TABLE_DESTINATION_COLUMN = "destination";
    public static final String SMTP_TABLE_CONTENT_COLUMN = "content";
    public static final String SMTP_TABLE_SUBJECT_COLUMN = "subject";
    
    public static final String MESSAGE_TABLE = "message";
    public static final String MESSAGE_TABLE_BASE_ID_COLUMN = "baseid";
    public static final String MESSAGE_TABLE_COUNTER_COLUMN = "counter";
    public static final String MESSAGE_TABLE_MESSAGE_COLUMN = "message";
    
    public static final String ERROR_TABLE = "error";
    public static final String ERROR_TABLE_ID_COLUMN = "id";
    public static final String ERROR_TABLE_BASE_ID_COLUMN = "baseid";
    public static final String ERROR_TABLE_ERROR_MESSAGE_COLUMN = "errormessage";
    
    public static final String MLLP_TABLE = "mllp";
    public static final String MLLP_TABLE_BASE_ID_COLUMN = "baseid";
    public static final String MLLP_TABLE_HOSTNAME_COLUMN = "hostname";
    public static final String MLLP_TABLE_IP_COLUMN = "ip";
    public static final String MLLP_TABLE_FULL_MESSAGE_COLUMN = "fullmessage";
    public static final String MLLP_TABLE_SUCCESSFUL_ACKNOWLEDGEMENT_COLUMN = "successfulacknowledgement";
    
    public static final String HTTP_TABLE = "http";
    public static final String HTTP_TABLE_BASE_ID_COLUMN = "baseid";
    public static final String HTTP_TABLE_IP_COLUMN = "ip";
    public static final String HTTP_TABLE_HOSTNAME_COLUMN = "hostname";
    public static final String HTTP_TABLE_PORT_COLUMN = "port";
    public static final String HTTP_TABLE_HTTP_BODY_COLUMN = "httpbody";
    
    public static final String HTTP_HEADER_TABLE = "httpheader";
    public static final String HTTP_HEADER_TABLE_BASE_ID_COLUMN = "baseid";
    public static final String HTTP_HEADER_TABLE_HEADER_NAME_COLUMN = "headername";
    public static final String HTTP_HEADER_TABLE_HEADER_VALUE_COLUMN = "headervalue";
    
    /** Creates a new instance of DatabaseFacade */
    public DatabaseFacade() {
    }
    
    public DatabaseFacade(Configuration config) throws java.sql.SQLException {
        this.setConnection(new DatabaseConnection(config));
    }
    
    static public DatabaseFacade getInstance(Configuration config) throws java.sql.SQLException {
        if (instance == null)
            instance = new DatabaseFacade(config);
        return instance;
    }
    
    static public DatabaseFacade getNewInstance(Configuration config) throws java.sql.SQLException {
        if(instance != null)
            instance.closeConnection();
        instance = new DatabaseFacade(config);
        return instance;
    }
    
    public void closeConnection() {
        try {
            this.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean addBase(Base base) {
        
        StringBuffer sb = new StringBuffer();
        
        sb.append("INSERT INTO ");
        sb.append(DatabaseFacade.BASE_TABLE);
        sb.append(" (");
        sb.append(DatabaseFacade.BASE_TABLE_ID_COLUMN + ",");
        sb.append(DatabaseFacade.BASE_TABLE_TIMESTAMP_COLUMN + ",");
        sb.append(DatabaseFacade.BASE_TABLE_DIRECTION_COLUMN + ",");
        sb.append(DatabaseFacade.BASE_TABLE_TRANSPORT_METHOD_COLUMN + ")");
        sb.append(" VALUES ");
        sb.append("('" + base.getId() + "',");
        sb.append("'" + base.getTimestamp() + "',");
        sb.append("'" + base.getDirection() + "',");
        sb.append("'" + base.getTransportMethod() + "');");
        
        int i = 0;
        try {
            i = this.getConnection().executeUpdate(sb.toString());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
        if (i == 0) return false;
        return true;
    }
    
    public Collection<Base> getOneDirectionBase(String direction) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * ");
        sb.append("FROM " + DatabaseFacade.BASE_TABLE + " ");
        if(direction != null)
            sb.append("WHERE " + DatabaseFacade.BASE_TABLE_DIRECTION_COLUMN + " = '" + direction + "' ");
        sb.append("ORDER BY " + DatabaseFacade.BASE_TABLE_TIMESTAMP_COLUMN);
        try {
            ResultSet result = this.getConnection().executeQuery(sb.toString());
            
            Collection<Base> bases = new ArrayList<Base>();
            while(result.next()) {
                Base base = Base.createFromResultSet(result);
                bases.add(base);
            }
            return bases;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }
    public boolean addSmtp(Smtp smtp) {
        StringBuffer sb = new StringBuffer();
        
        sb.append("INSERT INTO ");
        sb.append(DatabaseFacade.SMTP_TABLE);
        sb.append(" (");
        sb.append(DatabaseFacade.SMTP_TABLE_BASE_ID_COLUMN + ",");
        sb.append(DatabaseFacade.SMTP_TABLE_CONTENT_COLUMN + ",");
        sb.append(DatabaseFacade.SMTP_TABLE_FROM_COLUMN + ",");
        sb.append(DatabaseFacade.SMTP_TABLE_DESTINATION_COLUMN + ",");
        sb.append(DatabaseFacade.SMTP_TABLE_SUBJECT_COLUMN + ")");
        sb.append(" VALUES ");
        sb.append("('" + smtp.getBaseId() + "',");
        sb.append("'" + DatabaseConnection.makeSafe(smtp.getContent()) + "',");
        sb.append("'" + smtp.getFrom() + "',");
        sb.append("'" + smtp.getDestination() + "',");
        sb.append("'" + smtp.getSubject() + "');");
        
        int i = 0;
        try {
            i = this.getConnection().executeUpdate(sb.toString());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
        if (i == 0) return false;
        
        return true;
        
    }
    
    public boolean addMessage(Message message) {
        StringBuffer sb = new StringBuffer();
        
        sb.append("INSERT INTO ");
        sb.append(DatabaseFacade.MESSAGE_TABLE);
        sb.append(" (");
        sb.append(DatabaseFacade.MESSAGE_TABLE_BASE_ID_COLUMN + ",");
        sb.append(DatabaseFacade.MESSAGE_TABLE_COUNTER_COLUMN + ",");
        sb.append(DatabaseFacade.MESSAGE_TABLE_MESSAGE_COLUMN + ")");
        sb.append(" VALUES ");
        sb.append("('" + message.getBaseId() + "',");
        sb.append("'" + message.getCounter() + "',");
        sb.append("'" + DatabaseConnection.makeSafe(message.getMessage()) + "');");
        
        int i = 0;
        try {
            i = this.getConnection().executeUpdate(sb.toString());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
        if (i == 0) return false;
        
        return true;
        
    }
    
    
    public boolean addError(ErrorMessage error) {
        StringBuffer sb = new StringBuffer();
        
        sb.append("INSERT INTO ");
        sb.append(DatabaseFacade.ERROR_TABLE);
        sb.append(" (");
        sb.append(DatabaseFacade.ERROR_TABLE_ID_COLUMN + ",");
        sb.append(DatabaseFacade.ERROR_TABLE_BASE_ID_COLUMN + ",");
        sb.append(DatabaseFacade.ERROR_TABLE_ERROR_MESSAGE_COLUMN + ")");
        sb.append(" VALUES ");
        sb.append("('" + error.getErrorId() + "',");
        sb.append("'" + error.getBaseId() + "',");
        sb.append("'" + DatabaseConnection.makeSafe(error.getErrorMessage()) + "');");
        
        int i = 0;
        try {
            i = this.getConnection().executeUpdate(sb.toString());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
        if (i == 0) return false;
        return true;
        
    }
    
    
    public boolean addMllp(Mllp mllp) {
        StringBuffer sb = new StringBuffer();
        
        sb.append("INSERT INTO ");
        sb.append(DatabaseFacade.MLLP_TABLE);
        sb.append(" (");
        sb.append(DatabaseFacade.MLLP_TABLE_BASE_ID_COLUMN + ",");
        sb.append(DatabaseFacade.MLLP_TABLE_FULL_MESSAGE_COLUMN + ",");
        sb.append(DatabaseFacade.MLLP_TABLE_HOSTNAME_COLUMN + ",");
        sb.append(DatabaseFacade.MLLP_TABLE_IP_COLUMN + ",");
        sb.append(DatabaseFacade.MLLP_TABLE_SUCCESSFUL_ACKNOWLEDGEMENT_COLUMN + ")");
        
        sb.append(" VALUES ");
        sb.append("('" + mllp.getBaseId() + "',");
        sb.append("'" + DatabaseConnection.makeSafe(mllp.getFullMessage()) + "',");
        sb.append("'" + mllp.getHostname() + "',");
        sb.append("'" + mllp.getIp() + "',");
        sb.append("'" + mllp.isSuccessfulAcknowledgement() + "');");
        
        int i = 0;
        try {
            i = this.getConnection().executeUpdate(sb.toString());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
        if (i == 0) return false;
        return true;
        
    }
    
    public boolean addHttp(Http http) {
        StringBuffer sb = new StringBuffer();
        
        sb.append("INSERT INTO ");
        sb.append(DatabaseFacade.HTTP_TABLE);
        sb.append(" (");
        sb.append(DatabaseFacade.HTTP_TABLE_BASE_ID_COLUMN + ",");
        sb.append(DatabaseFacade.HTTP_TABLE_HOSTNAME_COLUMN + ",");
        sb.append(DatabaseFacade.HTTP_TABLE_IP_COLUMN + ",");
        sb.append(DatabaseFacade.HTTP_TABLE_PORT_COLUMN + ",");
        sb.append(DatabaseFacade.HTTP_TABLE_HTTP_BODY_COLUMN + ")");
        
        sb.append(" VALUES ");
        sb.append("('" + http.getBaseId() + "',");
        sb.append("'" + http.getHostname() + "',");
        sb.append("'" + http.getIp() + "',");
        sb.append("'" + http.getPort() + "',");
        sb.append("'" + DatabaseConnection.makeSafe(http.getHttpBody()) + "');");
        
        int i = 0;
        try {
            i = this.getConnection().executeUpdate(sb.toString());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
        if (i == 0) return false;
        
        Iterator<HttpHeader> it = http.getHeaders().iterator();
        while(it.hasNext()) {
            HttpHeader header = it.next();
            this.addHttpHeader(http.getBaseId(),header);
        }
        return true;
        
    }
    
    public boolean addHttpHeader(String baseId, HttpHeader header) {
        StringBuffer sb = new StringBuffer();
        
        sb.append("INSERT INTO ");
        sb.append(DatabaseFacade.HTTP_HEADER_TABLE);
        sb.append(" (");
        sb.append(DatabaseFacade.HTTP_HEADER_TABLE_BASE_ID_COLUMN + ",");
        sb.append(DatabaseFacade.HTTP_HEADER_TABLE_HEADER_NAME_COLUMN + ",");
        sb.append(DatabaseFacade.HTTP_HEADER_TABLE_HEADER_VALUE_COLUMN + ")");
        
        sb.append(" VALUES ");
        sb.append("('" + baseId + "',");
        sb.append("'" + header.getName() + "',");
        sb.append("'" + DatabaseConnection.makeSafe(header.getValue()) + "');");
        
        int i = 0;
        try {
            i = this.getConnection().executeUpdate(sb.toString());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
        if (i == 0) return false;
        return true;
    }
    
    public Smtp getSmtp(String id) {
        
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * ");
        sb.append("FROM " + DatabaseFacade.SMTP_TABLE + " ");
        sb.append("WHERE " + DatabaseFacade.SMTP_TABLE_BASE_ID_COLUMN + " = '" + id + "' ");
        try {
            ResultSet result = this.getConnection().executeQuery(sb.toString());
            if (result.next()) {
                return new Smtp(result);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }
    
    public Http getHttp(String id) {
        
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * ");
        sb.append("FROM " + DatabaseFacade.HTTP_TABLE + " ");
        sb.append("WHERE " + DatabaseFacade.HTTP_TABLE_BASE_ID_COLUMN + " = '" + id + "' ");
        try {
            ResultSet result = this.getConnection().executeQuery(sb.toString());
            if (result.next()) {
                return new Http(config,result);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }
    
    public Mllp getMllp(String id) {
        
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * ");
        sb.append("FROM " + DatabaseFacade.MLLP_TABLE + " ");
        sb.append("WHERE " + DatabaseFacade.MLLP_TABLE_BASE_ID_COLUMN + " = '" + id + "' ");
        try {
            ResultSet result = this.getConnection().executeQuery(sb.toString());
            if (result.next()) {
                return new Mllp(result);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }
    
    public Collection<Message> getMessages(String baseId) {
        Collection<Message> messages = new ArrayList<Message>();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * ");
        sb.append("FROM " + DatabaseFacade.MESSAGE_TABLE + " ");
        sb.append("WHERE " + DatabaseFacade.MESSAGE_TABLE_BASE_ID_COLUMN + " = '" + baseId + "' ");
        sb.append("ORDER BY " + DatabaseFacade.MESSAGE_TABLE_COUNTER_COLUMN);
        try {
            ResultSet result = this.getConnection().executeQuery(sb.toString());
            while(result.next()) {
                messages.add(new Message(result));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return messages;
    }
    public Collection<HttpHeader> getHttpHeaders(String baseId) {
        Collection<HttpHeader> messages = new ArrayList<HttpHeader>();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * ");
        sb.append("FROM " + DatabaseFacade.HTTP_HEADER_TABLE + " ");
        sb.append("WHERE " + DatabaseFacade.HTTP_HEADER_TABLE_BASE_ID_COLUMN + " = '" + baseId + "' ");
        
        try {
            ResultSet result = this.getConnection().executeQuery(sb.toString());
            while(result.next()) {
                messages.add(new HttpHeader(result));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return messages;
    }
    
    public Collection<ErrorMessage> getErrorMessages(String baseId) {
        
        Collection<ErrorMessage> errors = new ArrayList<ErrorMessage>();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * ");
        sb.append("FROM " + DatabaseFacade.ERROR_TABLE + " ");
        sb.append("WHERE " + DatabaseFacade.ERROR_TABLE_BASE_ID_COLUMN + " = '" + baseId + "' ");
        
        try {
            ResultSet result = this.getConnection().executeQuery(sb.toString());
            while(result.next()) {
                errors.add(new ErrorMessage(result));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return errors;                
    }
    
    /**
     * Getter for property this.getConnection().
     * @return Value of property this.getConnection().
     */
    public gov.nist.hl7.v3.log.jdbc.DatabaseConnection getConnection() {
        return connection;
    }
    
    /**
     * Setter for property this.getConnection().
     * @param this.getConnection() New value of property this.getConnection().
     */
    public void setConnection(gov.nist.hl7.v3.log.jdbc.DatabaseConnection connection) {
        this.connection = connection;
    }
    
    public Configuration getConfig() {
        return config;
    }
    
    public void setConfig(Configuration config) {
        this.config = config;
    }
    
}
