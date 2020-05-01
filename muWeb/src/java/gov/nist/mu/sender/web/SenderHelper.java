/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.nist.mu.sender.web;

import gov.nist.mu.sender.Sender;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;


/**
 * @author mccaffrey
 */
public class SenderHelper {

    String message = null;

    public void processRequest(HttpServletRequest request) {

        String hostname = request.getParameter("hostname");
        String port = request.getParameter("port");
        String path = request.getParameter("path");

        String protocolString = request.getParameter("protocol");
        int protocol = 0;
        if(protocolString != null && protocolString.equals("soap")) {
            protocol = Sender.SOAP;
        } else {
            protocol = Sender.REST;
        }
        try {
            Sender.send(hostname, port, path, protocol);
            message = "Success.";
        } catch(UnknownHostException ex) {
            ex.printStackTrace();
            message = "Unknown Host: " + ex.getMessage();
        } catch(IOException ex) {
            ex.printStackTrace();
            message = "I/O Issue: " + ex.getMessage();
        }
    }

    public String getMessage() {
        return message;
    }
}
