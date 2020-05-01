/*
 * Smtp.java
 *
 * Created on September 14, 2006, 3:26 PM
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
public class Smtp {
    
    private Configuration config = null;
    
    private String baseId = null;
    private String from = null;
    private String content = null;
    private String subject = null;
    private String destination = null;
    private int size = 0;
    
    public Smtp(Configuration config) {
        this.config = config;
    }
    
    // This assumes the ResultSet has already had next() run on it.
    public Smtp(ResultSet result) {
        try {
            this.setBaseId(result.getString(DatabaseFacade.SMTP_TABLE_BASE_ID_COLUMN));
            this.setContent(result.getString(DatabaseFacade.SMTP_TABLE_CONTENT_COLUMN));
            this.setDestination(result.getString(DatabaseFacade.SMTP_TABLE_DESTINATION_COLUMN));
            this.setFrom(result.getString(DatabaseFacade.SMTP_TABLE_FROM_COLUMN));
            this.setSubject(result.getString(DatabaseFacade.SMTP_TABLE_SUBJECT_COLUMN));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    /** Creates a new instance of Smtp */
    public Smtp(Configuration config, String baseId) {
        this.config = config;
        this.setBaseId(baseId);
    }
    
    public String getBaseId() {
        return baseId;
    }
    
    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }
    
    public String getFrom() {
        return from;
    }
    
    public void setFrom(String from) {
        this.from = from;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Configuration getConfig() {
        return config;
    }
    
    public void setConfig(Configuration config) {
        this.config = config;
    }
    
    public void loadById(String id) {
        try {
            Smtp smtp = DatabaseFacade.getInstance(this.getConfig()).getSmtp(id);
            this.setBaseId(smtp.getBaseId());
            this.setContent(smtp.getContent());
            this.setDestination(smtp.getDestination());
            this.setFrom(smtp.getFrom());
            this.setSubject(smtp.getSubject());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    public boolean save() {
        try {
            return DatabaseFacade.getInstance(getConfig()).addSmtp(this);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }
    
    public static Smtp createFromId(Configuration config, String id) {
        try {
            return DatabaseFacade.getInstance(config).getSmtp(id);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }
            
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
}
