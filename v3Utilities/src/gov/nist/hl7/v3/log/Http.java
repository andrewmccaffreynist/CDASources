/*
 * Http.java
 *
 * Created on September 15, 2006, 5:29 PM
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
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author mccaffrey
 */
public class Http {
    
    private Configuration config = null;
    
    private String baseId = null;
    private String httpBody = null;
    private String ip = null;
    private String hostname = null;
    private String port = null;
    private Collection<HttpHeader> headers = null;
    
    public Http(Configuration config, ResultSet result) {
        try {
            this.setBaseId(result.getString(DatabaseFacade.HTTP_TABLE_BASE_ID_COLUMN));
            this.setHostname(result.getString(DatabaseFacade.HTTP_TABLE_HOSTNAME_COLUMN));
            this.setHttpBody(result.getString(DatabaseFacade.HTTP_TABLE_HTTP_BODY_COLUMN));
            this.setIp(result.getString(DatabaseFacade.HTTP_TABLE_IP_COLUMN));
            this.setPort(result.getString(DatabaseFacade.HTTP_TABLE_PORT_COLUMN));            
            this.setHeaders(DatabaseFacade.getInstance(config).getHttpHeaders(this.getBaseId()));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    /** Creates a new instance of Http */
    public Http(Configuration config, String baseId) {
        this.setConfig(config);
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
    
    public String getHttpBody() {
        return httpBody;
    }
    
    public void setHttpBody(String httpBody) {
        this.httpBody = httpBody;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String getHostname() {
        return hostname;
    }
    
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    
    public String getPort() {
        return port;
    }
    
    public void setPort(String port) {
        this.port = port;
    }
    
    public Collection<HttpHeader> getHeaders() {
        if (headers == null)
            headers = new ArrayList<HttpHeader>();
        return headers;
    }
    public void setHeaders(Collection<HttpHeader> headers) {
        this.headers = headers;
    }
    public void addHeader(HttpHeader header) {
        this.getHeaders().add(header);
    }
    public void addHeader(String name, String value) {
        this.getHeaders().add(new HttpHeader(name, value));
    }
    
    public boolean save() {
        try {
            return DatabaseFacade.getInstance(getConfig()).addHttp(this);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }
    public static Http createFromId(Configuration config, String baseId) {
        try {
            return DatabaseFacade.getInstance(config).getHttp(baseId);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }
    public Collection<String> getPrintableHttpHeaders() {
        Iterator<HttpHeader> it = this.getHeaders().iterator();
        Collection<String> headers = new ArrayList<String>();
        while (it.hasNext()) {
            headers.add(it.next().toString());
        }
        return headers;
    }
}
