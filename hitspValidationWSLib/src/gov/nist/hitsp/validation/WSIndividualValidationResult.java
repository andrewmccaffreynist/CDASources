/*
 *
 NOTICE OF SOFTWARE ACKNOWLEDGMENT AND REDISTRIBUTION
 *
 The software provided herein is released by the National Institute of Standards
 and Technology (NIST), an agency of the U.S. Department of Commerce,
 Gaithersburg MD 20899, USA. The software presented here is intended to be
 utilized for research purposes only and bear no warranty, either express or
 implied. NIST does not assume legal liability nor responsibility for a USER's
 use of a NIST-derived software product or the results of such use.
 *
 Please note that within the United States, copyright protection, under Section
 105 of the United States Code, Title 17, is not available for any work of the
 United States Government and/or for any works created by United States
 Government employees. USER acknowledges that this software contains work which
 was created by NIST employees and is therefore in the public domain and is not
 subject to copyright. The USER may use, distribute, or incorporate this code or
 any part of it provided the USER acknowledges this via an explicit
 acknowledgment of NIST-related contributions to the USER's work. USER also
 agrees to acknowledge, via an explicit acknowledgment, that modifications or
 alterations have been made to this software by USER before redistribution.
 *
 **/

package gov.nist.hitsp.validation;

import com.sun.org.apache.xpath.internal.XPathAPI;

import gov.nist.hl7.v3.xml.util.MiscXmlUtil;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.axiom.om.OMElement;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

/**
 *
 * @author mccaffrey
 */
public class WSIndividualValidationResult implements Comparable {
    
    private String severity = null;
    private String message = null;
    private String context = null;
    private String test = null;
    private String specification = null;
    
    /**
     * Creates a new instance of WSIndividualValidationResult
     */
    public WSIndividualValidationResult() {
        /*
        this.setSeverity("severity");
        this.setMessage("message");
        this.setContext("context");
        this.setTest("test");
         */
    }
    
    public WSIndividualValidationResult(Element returnElement) {
        this.populateByElement(returnElement);
    }
    public WSIndividualValidationResult(OMElement returnElement) {
        this.populateByElement(returnElement);
    }
    
    public String getSeverity() {
        return severity;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getContext() {
        return context;
    }
    
    public String getTest() {
        return test;
    }
    private void populateByElement(OMElement returnElement) {
        Iterator it = returnElement.getChildElements();
        while(it.hasNext()) {
            OMElement element = (OMElement) it.next();                       
            if(element.getLocalName().equals("severity"))
                this.setSeverity(element.getText());
            else if(element.getLocalName().equals("message"))
                this.setMessage(element.getText());
            else if(element.getLocalName().equals("context"))
                this.setContext(element.getText());
            else if(element.getLocalName().equals("test"))
                this.setTest(element.getText());
            else if(element.getLocalName().equals("specification"))
                this.setSpecification(element.getText());
        }
    }
    private void populateByElement(Element element) {
        String severity = element.getAttribute("severity");
        
        // Done for consistency...  (Patterns in schematron are errors/warning/note.  Let's make 
        // them all singular here.)
        if("errors".equals(severity))
            severity = "error";
        this.setSeverity(severity);
        try {
            Element messageElement = (Element) XPathAPI.selectSingleNode(element,"message");
            if(messageElement != null)
                this.setMessage(messageElement.getTextContent());
            Element contextElement = (Element) XPathAPI.selectSingleNode(element,"context");
            if(contextElement != null)
                this.setContext(contextElement.getTextContent());
            Element testElement = (Element) XPathAPI.selectSingleNode(element,"test");
            if(testElement != null)
                this.setTest(testElement.getTextContent());
            Element specElement = (Element) XPathAPI.selectSingleNode(element,"specification");
            if(specElement != null)
                this.setSpecification(specElement.getTextContent());            
        } catch (TransformerException te) {
            // TODO: Do something here.
            te.printStackTrace();
        }
        
    }
    public static WSIndividualValidationResult[] processMultiple(String xml,String severity) {
        Document doc = null;
        
        System.out.println("WSIndividualValidationResult[] processMultiple");
        System.out.println(xml);
        
        try {
            doc = MiscXmlUtil.stringToDom(xml);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
        NodeList issues = doc.getElementsByTagName("issue");
        Collection<WSIndividualValidationResult> result = new ArrayList<WSIndividualValidationResult>();
        for(int i = 0; i < issues.getLength(); i++) {
            Element issue = (Element) issues.item(i);
            WSIndividualValidationResult individualResult = new WSIndividualValidationResult(issue);
            
            // This check is to get around the CCD schematron which reports an empty pattern...
            if(!individualResult.getMessage().trim().equals("")) {
                if(severity != null)
                    individualResult.setSeverity(severity);
                result.add(individualResult);
            }
        }
        return result.toArray(new WSIndividualValidationResult[0]);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Severity: " + this.getSeverity() + "\n");
        sb.append("Specification: " + this.getSpecification() + '\n');
        sb.append("Message: " + this.getMessage() + "\n");
        sb.append("Context: " + this.getContext() + "\n");
        sb.append("Test: " + this.getTest() + "\n");
        return sb.toString();
    }
    
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setContext(String context) {
        this.context = context;
    }
    
    public void setTest(String test) {
        this.test = test;
    }
    
    public String getSpecification() {
        return specification;
    }
    
    public void setSpecification(String specification) {
        this.specification = specification;
    }
    
    public int compareTo(Object o2) {
        //WSIndividualValidationResult r1 = (WSIndividualValidationResult) o1;
        WSIndividualValidationResult r1 = this;        
        WSIndividualValidationResult r2 = (WSIndividualValidationResult) o2;
        
        int compareSeverity = r1.getSeverity().compareTo(r2.getSeverity());
        if(compareSeverity != 0)
            return compareSeverity;
        return r1.getSpecification().compareTo(r2.getSpecification());
    }
}
