/*
 * DatabaseConnection.java
 *
 * Created on September 14, 2006, 5:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gov.nist.hl7.v3.log.jdbc;


import gov.nist.hl7.v3.misc.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



/**
 *
 * @author  mccaffrey
 */

public class DatabaseConnection {
    
    private Configuration config = null;
    private static Connection con;
    private static Statement stmt;
    private boolean successfulConnection = false;
    
    /*
    public DatabaseConnection(Configuration config) throws SQLException {
        this.setHostname("localhost");
        this.setConfig(config);
        this.initialize();
    }
     */
    /** Creates a new instance of JdbcConnection */
    public DatabaseConnection(Configuration config) throws SQLException {
        this.setConfig(config);
        this.initialize();
    }
    private void initialize() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;            
            
            url = "jdbc:postgresql://" + this.getConfig().getLogDatabaseHostname() + "/" + this.getConfig().getLogDatabaseName();
            
            System.out.println("Connecting to postres on url " + url);
            try {
                if(this.getConfig().getLogDatabaseUsername() != null) {
                    con = DriverManager.getConnection(url, this.getConfig().getLogDatabaseUsername(), this.getConfig().getLogDatabasePassword());
                } else {
                    con = DriverManager.getConnection(url, "hl7applrole", "");                    
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            stmt = con.createStatement();
            successfulConnection = true;
        } catch (ClassNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
            successfulConnection = false;
        }
        
    }
    public void close() throws SQLException {
        con.close();
    }
        
    public ResultSet executeQuery(String sql) throws SQLException {
        ResultSet result = null;
        result = stmt.executeQuery(sql);
        return result;
    }
    
    public int executeUpdate(String sql) throws SQLException {
        int i = 0;
        i = stmt.executeUpdate(sql);
        return i;
    }
    public boolean isAlive() throws SQLException {
        return !con.isClosed();
    }
    
    public static String makeSafe(String input) {
        if(input == null)
            return "";
        String output = input.replaceAll("'", "\"");
        return output;
    }
    
    public Configuration getConfig() {
        return config;
    }
    
    public void setConfig(Configuration config) {
        this.config = config;
    }
}