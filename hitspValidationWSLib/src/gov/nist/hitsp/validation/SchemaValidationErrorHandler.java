/*
 * SchemaValidationErrorHandler.java
 *
 * Created on July 13, 2006, 12:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package gov.nist.hitsp.validation;

import java.util.Iterator;
import java.util.Vector;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 * @author mccaffrey
 */
public class SchemaValidationErrorHandler implements ErrorHandler {

    private Vector<String> warnings = null;
    private Vector<String> linesWarnings = null;
    private Vector<String> errors = null;
    private Vector<String> linesErrors = null;
    private Vector<String> fatalErrors = null;
    private Vector<String> linesFatalErrors = null;

    /**
     * Creates a new instance of SchemaValidationErrorHandler
     */
    public SchemaValidationErrorHandler() {
    }

    public void warning(SAXParseException exception) throws SAXException {
        this.addWarning(exception.getMessage(), Integer.toString(exception.getLineNumber()));
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        this.addFatalError(exception.getMessage(), Integer.toString(exception.getLineNumber()));
    }

    public void error(SAXParseException exception) throws SAXException {
        this.addError(exception.getMessage(), Integer.toString(exception.getLineNumber()));
    }

    public boolean hasWarnings() {
        if(getWarnings() == null)
            return false;
        if(getWarnings().isEmpty())
            return false;
        return true;
    }

    public boolean hasErrors() {
        if(getErrors() == null)
            return false;
        if(getErrors().isEmpty())
            return false;
        return true;
    }

    public boolean hasFatalErrors() {
        if(getFatalErrors() == null)
            return false;
        if(getFatalErrors().isEmpty())
            return false;
        return true;
    }

    public String getPrintableWarnings() {
        if(getWarnings() == null)
            return "";
        Iterator it = getWarnings().iterator();
        StringBuffer sb = new StringBuffer();
        while(it.hasNext()) {
            sb.append("Warning: " + (String) it.next() + "\n");
        }
        return sb.toString();
    }

    public String getPrintableErrors() {
        if(getErrors() == null)
            return "";
        Iterator it = getErrors().iterator();
        StringBuffer sb = new StringBuffer();
        while(it.hasNext()) {
            sb.append("Error: " + (String) it.next() + "\n");
        }
        return sb.toString();

    }

    public String getPrintableFatalErrors() {
        if(getFatalErrors() == null)
            return "";
        Iterator it = getFatalErrors().iterator();
        StringBuffer sb = new StringBuffer();
        while(it.hasNext()) {
            sb.append("Fatal Error: " + (String) it.next() + "\n");
        }
        return sb.toString();
    }

    public boolean addWarning(String warning, String lineNumber) {
        if(getWarnings() == null) {
            setWarnings(new Vector<String>());
            setLinesWarnings(new Vector<String>());
        }
        return (getWarnings().add(warning) && getLinesWarnings().add(lineNumber));
    }

    public boolean addError(String error, String lineNumber) {
        if(getErrors() == null) {
            setErrors(new Vector<String>());
            setLinesErrors(new Vector<String>());
        }
        return (getErrors().add(error) && getLinesErrors().add(lineNumber));
    }

    public boolean addFatalError(String fatalError, String lineNumber) {
        if(getFatalErrors() == null) {
            setFatalErrors(new Vector<String>());
            setLinesErrors(new Vector<String>());
        }
        return (getFatalErrors().add(fatalError) && getLinesErrors().add(lineNumber));
    }

    public Vector<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(Vector<String> warnings) {
        this.warnings = warnings;
    }

    public Vector<String> getLinesWarnings() {
        return linesWarnings;
    }

    public void setLinesWarnings(Vector<String> linesWarnings) {
        this.linesWarnings = linesWarnings;
    }

    public Vector<String> getErrors() {
        return errors;
    }

    public void setErrors(Vector<String> errors) {
        this.errors = errors;
    }

    public Vector<String> getLinesErrors() {
        return linesErrors;
    }

    public void setLinesErrors(Vector<String> linesErrors) {
        this.linesErrors = linesErrors;
    }

    public Vector<String> getFatalErrors() {
        return fatalErrors;
    }

    public void setFatalErrors(Vector<String> fatalErrors) {
        this.fatalErrors = fatalErrors;
    }

    public Vector<String> getLinesFatalErrors() {
        return linesFatalErrors;
    }

    public void setLinesFatalErrors(Vector<String> linesFatalErrors) {
        this.linesFatalErrors = linesFatalErrors;
    }

    public int getNumberErrors() {
        try {
            return linesErrors.size();
        } catch(Exception e) {
            return 0;
        }
    }

    public int getNumberWarnings() {
        try {
            return linesWarnings.size();
        } catch(Exception e) {
            return 0;
        }
    }

    public int getNumberFatalErrors() {
        try {
            return linesFatalErrors.size();
        } catch(Exception e) {
            return 0;
        }
    }
}
