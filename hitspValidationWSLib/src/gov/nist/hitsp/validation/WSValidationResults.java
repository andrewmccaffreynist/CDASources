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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import org.apache.axiom.om.OMElement;

/**
 *
 * @author mccaffrey
 */
public class WSValidationResults {
        
    private Boolean validationTest = null;
    private String validationDate = null;
    private String validationTime = null;
    
    private WSIndividualValidationResult[] issue;
    
    
    
    /**
     * Creates a new instance of WSValidationResults
     */
    public WSValidationResults() {
        this.setCurrentTime();
    }
    public WSValidationResults(OMElement element) {         
        Iterator it = element.getChildElements();
        Collection<WSIndividualValidationResult> issues = new ArrayList<WSIndividualValidationResult>();
        while(it.hasNext()) {
            OMElement elem = (OMElement) it.next();
            if(elem.getLocalName().equals("validationDate")) {
                this.setValidationDate(elem.getText());
            } else if (elem.getLocalName().equals("validationTime")) {
                this.setValidationTime(elem.getText());
            } else if (elem.getLocalName().equals("validationTest")) {
                this.validationTest = Boolean.parseBoolean(elem.getText());
            } else if (elem.getLocalName().equals("issue")) {
                issues.add(new WSIndividualValidationResult(elem));
            }
        }
        this.setIssue(issues.toArray(new WSIndividualValidationResult[0]));

    }
    public WSValidationResults(boolean passed) {
        this.validationTest = passed;
        this.setCurrentTime();
    }
    
    private void setCurrentTime() {
        Calendar now = Calendar.getInstance();
        setValidationDate(String.valueOf(now.get(Calendar.YEAR)) + WSValidationResults.ensureDoubleDigits(now.get(Calendar.MONTH))
        + WSValidationResults.ensureDoubleDigits(now.get(Calendar.DATE)));
        setValidationTime(now.get(Calendar.HOUR_OF_DAY) + WSValidationResults.ensureDoubleDigits(now.get(Calendar.MINUTE))
        + WSValidationResults.ensureDoubleDigits(now.get(Calendar.SECOND)));
    }
        
    // Only for two or one digit numbers
    private static String ensureDoubleDigits(int i) {
        if(i < 10)
            return "0" + String.valueOf(i);
        return String.valueOf(i);
    }

    public void setValidationTest(boolean pass) {
        this.validationTest = pass;
    }
    public boolean isValidationTest() {
        return validationTest;
    }

    public String getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(String validationDate) {
        this.validationDate = validationDate;
    }

    public String getValidationTime() {
        return validationTime;
    }

    public void setValidationTime(String validationTime) {
        this.validationTime = validationTime;
    }

    public WSIndividualValidationResult[] getIssue() {
        return issue;
    }

    public void setIssue(WSIndividualValidationResult[] issue) {
        this.issue = issue;
    }

    public void appendIssue(WSIndividualValidationResult[] issues) {
        if(this.issue == null)
            this.setIssue(new WSIndividualValidationResult[0]);
        WSIndividualValidationResult[] temp = new WSIndividualValidationResult[this.issue.length + issues.length];
        System.arraycopy(issues, 0, temp, 0, issues.length);        
        System.arraycopy(this.issue, 0, temp, issues.length, this.issue.length);
        this.setIssue(temp);              
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Validation Date: " + this.getValidationDate() + '\n');
        sb.append("Validation Time: " + this.getValidationTime() + '\n');
        sb.append("Validation Test Result: " + this.validationTest + "\n");        
        for(int i = 0; i < this.issue.length; i++) {
            sb.append('\n');
            sb.append(issue[i].toString());
        }                        
        return sb.toString();
    }
}
