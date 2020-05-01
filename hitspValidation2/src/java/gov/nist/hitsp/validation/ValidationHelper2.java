/*
 * ValidationHelper.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package gov.nist.hitsp.validation;

import gov.nist.hl7.v3.xml.util.MiscXmlUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author mccaffrey
 */
public class ValidationHelper2 {

    // HttpServletRequest request = null;
    List docTypes = null;
    private String errorMessage = null;
    //  Document validDocument = null;
    //  Document tempDoc = null;
    //  String fileName = null;
    ValidationResults validationResults = null;
    //  Collection schematronWarnings = null;
    private String documentType = null;
    private List runnableValidations = null;
    Collection resultDetail = null;
    String detailLevel = null;
    String skeletonLocation = null;
    // int indexTest = -1;

    /**
     * Creates a new instance of ValidationHelper
     */
    public ValidationHelper2() {
    }

    public void processRequest(HttpServletRequest request) {  //, boolean upload) {

        System.out.println("LOG: Processing MU Cancer Registry Report");

        //  this.request = request;
        this.clearTopErrorMessage();
        skeletonLocation = request.getSession().getServletContext().getInitParameter("skeletonLocation");
        try {
            if (docTypes == null) {
                String docTypesLocation = request.getSession().getServletContext().getInitParameter("docTypesLocation");
                docTypes = DocumentType.parseDocumentTypes(MiscXmlUtil.fileToDom(docTypesLocation));

            }
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        }
        //  if(upload)
        //      this.upload();

    }

    public String processRequestXml(HttpServletRequest request) {

        System.out.println("LOG: Processing Regular CDA-based document - " + Validator.createDateOfTest() + ' ' + Validator.createTimeOfTest() + ' ' + request.getRemoteHost());

        skeletonLocation = request.getSession().getServletContext().getInitParameter("skeletonLocation");        
        String stylesheetLocation = request.getSession().getServletContext().getInitParameter("stylesheetLocation");

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List items = null;

        this.processDocumentTypes(request);

        try {
            items = upload.parseRequest(request);
        } catch (FileUploadException ex) {
            ex.printStackTrace();
            this.setTopErrorMessage("Error on document upload!");
            //   return false;
        }
        if (items == null || items.size() == 0) {
            this.setTopErrorMessage("No document found!");
//            return false;
        }



        Iterator it = items.iterator();
        FileItem fileItem = null;
        while (it.hasNext()) {
            FileItem fit = (FileItem) it.next();
            if ("file".equals(fit.getFieldName())) {
                fileItem = fit;
                //               fileName = fit.getName();
            } else if ("documentType".equals(fit.getFieldName())) {
                String docTypeParameter = fit.getString();
//                indexTest = getIndexOfTest(docTypeParameter);
                this.setDocumentType(docTypeParameter);
                System.out.println(fit.getString());
            } else if ("resultDetail".equals(fit.getFieldName())) {
                String resultDetailInput = fit.getString();
                detailLevel = resultDetailInput;
                this.processResultDetail(resultDetailInput);
            }
        }

        if (fileItem == null || fileItem.getSize() == 0) {
            this.setTopErrorMessage("Empty or no document found!");
//            return false;

            // TODO!!!!
        }



        //SchemaValidationErrorHandler errorHandler = null;

        SchemaValidationErrorHandler errorHandler = new SchemaValidationErrorHandler();
        Document doc = null;
        boolean fatalError = false;

        try {
            //    errorHandler = new SchemaValidationErrorHandler();
            //    doc = ValidationHelper2.validateWithSchema(fileItem.getInputStream(), errorHandler, schemaLocation);                        
            //    DocumentType docType = DocumentType.findDocumentType(this.getDocumentType(), docTypes);

            doc = MiscXmlUtil.inputStreamToDom(fileItem.getInputStream());
            DocumentType documentType = DocumentType.findDocumentType(this.getDocumentType(), docTypes);
            validationResults = documentType.doFullValidation(fileItem.getInputStream(), doc, docTypes, resultDetail, false,
                    skeletonLocation);
            System.out.println("HERE!!!!!" + validationResults.getSchemaErrors().getPrintableErrors());

            String validationReport = this.convertValidationResults(validationResults, stylesheetLocation);

            return validationReport;

        } catch (IOException ex) {
            System.out.println("IO exception HERE!!!!! " + ex.toString());
            ex.printStackTrace();
            // errorHandler = new SchemaValidationErrorHandler();            
            errorHandler.addError("Message is not valid XML.  Possible empty message.", null);
            fatalError = true;
        } catch (Exception ex) {
            System.out.println("Generic exception HERE!!!!! " + ex.toString());
            ex.printStackTrace();
            fatalError = true;
            StringBuilder sb = new StringBuilder();
            
            
            String fatalErrorMessage = this.createFatalError("Fatal Error.  Cannot Validate.  Empty or non-XML document found.", stylesheetLocation);
                    
            return fatalErrorMessage;
            /*
             * 
             * <!--<%@page contentType="text/xml"%>-->
             * 
*/
            
            

   

        }
        if (doc == null) {
            fatalError = true;
        }

        return "No.";
        /*
         String ccdErrorsString = null;
         String cda4cdtErrorsString = null;
         String crErrorsString = null;

         Node ccdErrors = null;
         Node cda4cdtErrors = null;
         Node crErrors = null;
                
         if (!fatalError) {

         ccdErrorsString = Validator.validateWithSchematron(doc, ccdSchematronLocation, skeletonLocation, "errors");
         cda4cdtErrorsString = Validator.validateWithSchematron(doc, cda4cdtSchematronLocation, skeletonLocation, "errors");
         crErrorsString = Validator.validateWithSchematron(doc, crSchematronLocation, skeletonLocation, "errors");

         try {
         ccdErrors = Validator.stringToDom(ccdErrorsString);
         cda4cdtErrors = Validator.stringToDom(cda4cdtErrorsString);
         crErrors = Validator.stringToDom(crErrorsString);
         } catch (SAXException ex) {
         // TODO
         } catch (ParserConfigurationException ex) {
         // TODO
         } catch (IOException ex) {
         // TODO
         }
         }
         Node[] messageList = { ccdErrors, cda4cdtErrors, crErrors } ;
                
         Document result = null;
         if(!fatalError) 
         result = Validator.generateReport(doc, errorHandler, messageList);
         else
         result = Validator.generateReport(doc, errorHandler, null);
        
         String resultString = Validator.xmlToString(result);        
         
         resultString = resultString.substring(resultString.indexOf("?>") + 2);
         resultString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
         // + "<?xml-stylesheet type=\"text/xsl\" href=\"http://localhost:8084/muValidation/report.xsl\"?>"
         + "<?xml-stylesheet type=\"text/xsl\" href=\"" + stylesheetLocation + "\"?>"
         + resultString;

         return resultString;
         */

    }

    public void processDocumentTypes(HttpServletRequest request) {

        try {
            if (docTypes == null) {
                String docTypesLocation = request.getSession().getServletContext().getInitParameter("docTypesLocation");
                docTypes = DocumentType.parseDocumentTypes(MiscXmlUtil.fileToDom(docTypesLocation));
            }
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        }

    }

    public String getSchemaName() {
        if (validationResults != null && validationResults.getSchemaName() != null) {
            return validationResults.getSchemaName();
        }
        return "Schema";
    }

    public boolean hasSchemaErrors() {
        if (validationResults == null || validationResults.getSchemaErrors() == null) {
            return false;
        }
        if (validationResults.getSchemaErrors().getErrors() == null) {
            return false;
        }
        if (validationResults.getSchemaErrors().getErrors().size() == 0) {
            return false;
        }
        return true;
    }
    /*
     * public String getFileName() { return fileName; }
     */

    public List getLinesSchemaErrors() {
        return validationResults.getSchemaErrors().getLinesErrors();
    }

    public List getSchemaErrors() {
        return validationResults.getSchemaErrors().getErrors();
    }

    public int getNumberSchemaErrors() {
        return validationResults.getSchemaErrors().getNumberErrors();
    }

    public List getSchemaWarnings() {
        return validationResults.getSchemaErrors().getWarnings();
    }

    public List getLinesSchemaWarnings() {
        return validationResults.getSchemaErrors().getLinesWarnings();
    }

    public int getNumberSchemaWarnings() {
        return validationResults.getSchemaErrors().getNumberWarnings();
    }

    public boolean hasSchemaWarnings() {
        if (validationResults == null || validationResults.getSchemaErrors() == null) {
            return false;
        }
        if (validationResults.getSchemaErrors().getWarnings() == null) {
            return false;
        }
        if (validationResults.getSchemaErrors().getWarnings().size() == 0) {
            return false;
        }
        return true;
    }

    public boolean hasSchematronErrors() {
        if (validationResults.getSchematronErrors() == null) {
            return false;
        }
        if (validationResults.getSchematronErrors().size() == 0) {
            return false;
        }
        return true;
    }

    public List getSchematronErrors() {
        return validationResults.getSchematronErrors();
    }

    public boolean hasTopErrorMessage() {
        if (errorMessage == null) {
            return false;
        }
        return true;
    }

    public String getTopErrorMessage() {
        return errorMessage;
    }

    public void setTopErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void clearTopErrorMessage() {
        this.errorMessage = null;
    }

    protected static Document validateWithSchema(InputStream xml, SchemaValidationErrorHandler handler, String schemaLocation) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                "http://www.w3.org/2001/XMLSchema");

        factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource",
                schemaLocation);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            return null;
        }

        builder.setErrorHandler(handler);
        Document doc = null;
        try {
            doc = builder.parse(xml);
        } catch (SAXException e) {
            System.out.println("File is not valid XML. " + e.getMessage() + ' ' + e.toString());
            handler.addError("File is not valid XML.  " + e.getMessage() + ' ' + e.toString(), null);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File is not valid XML.  Possible empty file.");
            handler.addError("File is not valid XML.  Possible empty file.", null);
            e.printStackTrace();
        }
        return doc;
    }

    public List getRunnableValidations() {
        if (runnableValidations == null && docTypes != null) {
            runnableValidations = DocumentType.filterRunnableOnly(docTypes);
        }
        return runnableValidations;
    }

    public int getNumberOfRunnable() {
        List runnable = this.getRunnableValidations();
        if (runnable == null) {
            return 0;
        }
        return runnable.size();
    }

    public DocumentType getRunnableDocumentTypeAt(int i) {
        return (DocumentType) this.getRunnableValidations().get(i);
    }

    public int getNumberOfDependenciesAt(int i) {
        return this.getRunnableDocumentTypeAt(i).getDependencyNames(docTypes).size();
    }

    public String getDependenciesNameAt(int i, int j) {
        return (String) this.getRunnableDocumentTypeAt(i).getDependencyNames(docTypes).get(j);
    }

    public String getDependenciesIdAt(int i, int j) {
        return (String) this.getRunnableDocumentTypeAt(i).getDependencyId(docTypes).get(j);
    }

    public String getDependenciesDescriptionAt(int i, int j) {
        return (String) this.getRunnableDocumentTypeAt(i).getDependencyDescription(docTypes).get(j);
    }

    public int getIndexOfTest(String name) {
        int i = 0;
        while (i < this.getNumberOfRunnable() && !this.getRunnableDocumentTypeAt(i).getId().equals(name)) {
            i++;
        }
        if (this.getRunnableDocumentTypeAt(i).getId().equals(name)) {
            return i;
        } else {
            return -1;
        }
    }

    public void processResultDetail(String resultDetailInput) {
        resultDetail = new ArrayList();
        System.out.println(resultDetailInput);
        if ("all".equals(resultDetail)) {
            resultDetail.add("errors");
            resultDetail.add("warning");
            resultDetail.add("notes");
        } else if ("errorsWarnings".equals(resultDetailInput)) {
            resultDetail.add("errors");
            resultDetail.add("warning");
        } else if ("errors".equals(resultDetailInput)) {
            resultDetail.add("errors");
        } else {
            resultDetail.add("errors");
            resultDetail.add("warning");
            resultDetail.add("notes");
        }
    }

    /*
     * public void processResultDetail(String resultDetailInput) {
     *
     */
    /*
     * resultDetail = new ArrayList(); System.out.println(resultDetailInput);
     * if("all".equals(resultDetail)) resultDetail.add("#ALL"); else
     * if("errorsWarnings".equals(resultDetailInput)) {
     * resultDetail.add("errors"); resultDetail.add("warning");
     * resultDetail.add("violation"); // resultDetail.add("manual"); } else
     * if("errors".equals(resultDetailInput)) { resultDetail.add("errors"); //
     * resultDetail.add("manual"); } else { resultDetail.add("#ALL"); }
     */
    /*
     * resultDetail = new ArrayList(); resultDetail.add("checks");
     * resultDetail.add("errors"); resultDetail.add("warning");
     *
     *
     * }
     *
     */
    /**
     * @return the documentType
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * @param documentType the documentType to set
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    // TODO: Yes, this is messy.  Will fix later, maybe.
    public String convertValidationResults(ValidationResults validationResults, String stylesheetLocation) {

        Document result = null;
        StringBuilder sb = new StringBuilder();
        try {
            result = MiscXmlUtil.createDomDocument();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            return null;
        }

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<?xml-stylesheet type=\"text/xsl\" href=\"" + stylesheetLocation + "\"?>\n");

        sb.append("<Report>");
        Element header = ValidationHelper2.createHeader(result, 0); // TODO
        sb.append(MiscXmlUtil.removeXmlHeader(MiscXmlUtil.xmlToString(header)));

        Element schemaErrors = ValidationHelper2.createSchemaErrorReport(result, validationResults.getSchemaErrors());
        sb.append(MiscXmlUtil.removeXmlHeader(MiscXmlUtil.xmlToString(schemaErrors)));
        System.out.println("Schematron errors!" + validationResults.getSchematronErrors());
        for (int i = 0; i < validationResults.getSchematronErrors().size(); i++) {
            String results = validationResults.getSchematronErrors().get(i);

            if (results.startsWith("<result>")) {
                results = results.substring(8, results.length() - 9);

            }

            sb.append(results);
        }
        sb.append("</Report>");
        return sb.toString();
    }

    private static Element createHeader(Document result, int errorCountInt) {

        Element reportHeader = result.createElement("ReportHeader");

        Element validationStatus = result.createElement("ValidationStatus");
        validationStatus.setTextContent("Complete");
        reportHeader.appendChild(validationStatus);
/*
        Element serviceName = result.createElement("ServiceName");
        serviceName.setTextContent("Meaningful Use HITSP/C32 v2.5 Validation");
        reportHeader.appendChild(serviceName);
*/
        Element dateOfTest = result.createElement("DateOfTest");
        dateOfTest.setTextContent(Validator.createDateOfTest());
        reportHeader.appendChild(dateOfTest);

        Element timeOfTest = result.createElement("TimeOfTest");
        timeOfTest.setTextContent(Validator.createTimeOfTest());
        reportHeader.appendChild(timeOfTest);
/*
        Element resultOfTest = result.createElement("ResultOfTest");
        if (errorCountInt == 0) {
            resultOfTest.setTextContent("Passed");
        } else {
            resultOfTest.setTextContent("Failed");
        }
        reportHeader.appendChild(resultOfTest);

        Element errorCount = result.createElement("ErrorCount");
        errorCount.setTextContent(String.valueOf(errorCountInt));
        reportHeader.appendChild(errorCount);
*/
        return reportHeader;
    }

    private static Element createSchemaErrorReport(Document doc, SchemaValidationErrorHandler errorHandler) {

        Element result = doc.createElement("Results");
        result.setAttribute("severity", "schemaViolation");
        result.setAttribute("specification", "cda_r2");
        if (errorHandler.hasErrors()) {
            Iterator<String> it = errorHandler.getErrors().iterator();
            while (it.hasNext()) {
                Element issue = doc.createElement("issue");
                result.appendChild(issue);

                Element message = doc.createElement("message");
                message.setTextContent(it.next());
                issue.appendChild(message);
            }
        }

        return result;
    }
    
    
    
    // TODO:  Yes, this is terrible.  No, I don't care.  I'll do it properly later
    // if I have time, which I won't.
    private String createFatalError(String errorMessage, String stylesheetLocation) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<?xml-stylesheet type=\"text/xsl\" href=\"" + stylesheetLocation + "\"?>\n");
        sb.append("<Report>\n");
        sb.append("<ReportHeader>\n");
        sb.append("<ValidationStatus>Complete</ValidationStatus>\n");        
        sb.append("<DateOfTest>" + Validator.createDateOfTest() +"</DateOfTest>\n");
        sb.append("<TimeOfTest>" + Validator.createTimeOfTest() + "</TimeOfTest>\n");
        sb.append("<ResultOfTest>Failed</ResultOfTest>\n");
        sb.append("<ErrorCount>1</ErrorCount>\n");
        sb.append("</ReportHeader>\n");
        sb.append("<Results severity=\"fatalError\">\n");
        sb.append("<issue>\n");
        sb.append("<message>" + errorMessage + "</message>\n");
        sb.append("</issue>\n");                
        sb.append("</Results>\n");
        sb.append("</Report>\n");
                
        return sb.toString();
        
    }
}
