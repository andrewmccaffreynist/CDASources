/*
 * Dependency.java
 *
 * Created on November 19, 2007, 2:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gov.nist.hitsp.validation;

import org.w3c.dom.Element;

/**
 *
 * @author mccaffrey
 */
public class Dependency {
    
    private int sequenceNumber = 0;
    private String documentType = null;
    
    /** Creates a new instance of Dependency */
    public Dependency(Element dependency) {
        this.setSequenceNumber(Integer.parseInt(dependency.getAttribute("sequenceNumber")));
        this.setDocumentType(dependency.getTextContent());
    }
    
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    public String getDocumentType() {
        return documentType;
    }
    
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
    
}
