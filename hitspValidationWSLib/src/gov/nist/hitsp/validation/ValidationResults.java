/*
 * ValidationResults.java
 *
 * Created on November 20, 2007, 3:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package gov.nist.hitsp.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author mccaffrey
 */
public class ValidationResults {

    private String schemaName = null;
    private SchemaValidationErrorHandler schemaErrors = null;
    private List<String> schematronErrors = null;

    /** Creates a new instance of ValidationResults */
    public ValidationResults() { }

    public SchemaValidationErrorHandler getSchemaErrors() {
        return schemaErrors;
    }

    public void setSchemaErrors(SchemaValidationErrorHandler schemaErrors) {
        this.schemaErrors = schemaErrors;
    }

    public List<String> getSchematronErrors() {
        if(schematronErrors == null)
            schematronErrors = new ArrayList<String>();
        return schematronErrors;
    }

    /*
     * public void setSchematronErrors(List<String> schematronErrors) {
     * this.schematronErrors = schematronErrors; }
     */
    public void addSchematronErrors(String schematronError) {
        /*
        
        2014/05/30
        System.out.println("schematron error!!!" + schematronError);
        int count = errorsCount(schematronError, "Location:");
        int ind = schematronError.indexOf(':');
        String res = "<h2><font color=\"red\">" + schematronError.substring(0, ind + 1) + "</font></h2>\n <h3>Errors: " + count + "</h3>\n" + schematronError.substring(ind + 1);

        this.getSchematronErrors().add(res);
                */
        this.getSchematronErrors().add(schematronError);
        
        
    }

    public static int errorsCount(String text, String word) {
        int count = 0;
        int fromIndex = text.indexOf(word);
        while(fromIndex >= 0) {
            count++;
            fromIndex = text.indexOf(word, fromIndex + 1);
        }
        return count;
    }

    // TODO: Right now we are assuming only one schema result. FIX THIS!!
    public void append(ValidationResults newResult) {
        if(newResult.getSchemaName() != null)
            this.setSchemaName(newResult.getSchemaName());
        if(newResult.getSchemaErrors() != null)
            this.setSchemaErrors(newResult.getSchemaErrors());
        if(newResult.getSchematronErrors() != null)
            this.getSchematronErrors().addAll(newResult.getSchematronErrors());
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
}
