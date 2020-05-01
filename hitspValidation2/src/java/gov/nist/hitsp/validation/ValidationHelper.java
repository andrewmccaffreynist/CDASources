/*
 * ValidationHelper.java
 *
 * Created on September 28, 2007, 4:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package gov.nist.hitsp.validation;

import gov.nist.hl7.v3.xml.util.MiscXmlUtil;

import java.io.IOException;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author mccaffrey
 */
public class ValidationHelper {

    HttpServletRequest request = null;
    List docTypes = null;
    private String errorMessage = null;
    Document validDocument = null;
    Document doc = null;
    Document tempDoc = null;
    String fileName = null;
    ValidationResults validationResults = null;
    Collection schematronWarnings = null;
    private String documentType = null;
    private List runnableValidations = null;
    Collection resultDetail = null;
    String detailLevel = null;
    String skeletonLocation = null;
    int indexTest = -1;

    /** Creates a new instance of ValidationHelper */
    public ValidationHelper() { }

    public void processRequest(HttpServletRequest request, boolean upload) {
        this.request = request;
        this.clearTopErrorMessage();
        skeletonLocation = request.getSession().getServletContext().getInitParameter("skeletonLocation");
        try {
            if(docTypes == null) {
                String docTypesLocation = request.getSession().getServletContext().getInitParameter("docTypesLocation");
                // docTypes =
                // DocumentType.parseDocumentTypes(MiscXmlUtil.fileToDom("/home/mccaffrey/cvsfiles/hitspValidation/web/WEB-INF/docTypes.xml"));
                docTypes = DocumentType.parseDocumentTypes(MiscXmlUtil.fileToDom(docTypesLocation));
            }
        } catch(ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch(IOException ex) {
            ex.printStackTrace();
        } catch(SAXException ex) {
            ex.printStackTrace();
        }
        // if(request.getParameter("submit") != null &&
        // !request.getParameter("submit").trim().equals(""))
        if(upload)
            this.upload();
    // boolean successfulUpload = this.upload();

    /*
     * String documentTypeParameter = request.getParameter("documentType");
     * Enumeration enumm = request.getHeaderNames();
     * 
     * while(enumm.hasMoreElements()) { System.out.println((String)
     * enumm.nextElement()); } System.out.println("Doc Type = " +
     * documentTypeParameter);
     * 
     * this.setDocumentType(ValidationHelper.convertDocumentType(documentTypeParameter));
     */

    }

    
    
    public String processRequestXml(HttpServletRequest request) {
/* 2013/7/9 
        System.out.println("LOG: Processing MU Cancer Registry Report - " + Validator.createDateOfTest() + ' ' + Validator.createTimeOfTest() + ' ' + request.getRemoteHost());

        skeletonLocation = request.getSession().getServletContext().getInitParameter("skeletonLocation");
        String schemaLocation = request.getSession().getServletContext().getInitParameter("schemaLocation");
        String ccdSchematronLocation = request.getSession().getServletContext().getInitParameter("ccdSchematronLocation");
        String cda4cdtSchematronLocation = request.getSession().getServletContext().getInitParameter("cda4cdtSchematronLocation");
        String crSchematronLocation = request.getSession().getServletContext().getInitParameter("crSchematronLocation");
        String stylesheetLocation = request.getSession().getServletContext().getInitParameter("stylesheetLocation");

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List items = null;

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
//                String docTypeParameter = fit.getString();
//                indexTest = getIndexOfTest(docTypeParameter);
//                this.setDocumentType(docTypeParameter);
                System.out.println(fit.getString());
            } else if ("resultDetail".equals(fit.getFieldName())) {
//                String resultDetailInput = fit.getString();
//                detailLevel = resultDetailInput;
//                this.processResultDetail(resultDetailInput);
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
            errorHandler = new SchemaValidationErrorHandler();
            doc = ValidationHelper.validateWithSchema(fileItem.getInputStream(), errorHandler, schemaLocation);
        } catch (IOException ex) {
            System.out.println("IO exception HERE!!!!! " + ex.toString());
            ex.printStackTrace();
            // errorHandler = new SchemaValidationErrorHandler();            
            errorHandler.addError("Message is not valid XML.  Possible empty message.", null);
            fatalError = true;
        } catch (Exception ex) {
            System.out.println("Generic exception HERE!!!!! " + ex.toString());
            fatalError = true;
        }
        if (doc == null) {
            fatalError = true;
        }
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
        
        return new String();

    }
    
    public boolean upload() {

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List items = null;

        try {
            items = upload.parseRequest(request);
        } catch(FileUploadException ex) {
            ex.printStackTrace();
            this.setTopErrorMessage("Error on document upload!");
            return false;
        }
        if(items == null || items.size() == 0) {
            this.setTopErrorMessage("No document found!");
            return false;
        }

        Iterator it = items.iterator();
        FileItem fileItem = null;
        while(it.hasNext()) {
            FileItem fit = (FileItem) it.next();
            if("file".equals(fit.getFieldName())) {
                fileItem = fit;
                fileName = fit.getName();
            } else if("documentType".equals(fit.getFieldName())) {
                String docTypeParameter = fit.getString();
                indexTest = getIndexOfTest(docTypeParameter);
                this.setDocumentType(docTypeParameter);
                System.out.println(fit.getString());
            } else if("resultDetail".equals(fit.getFieldName())) {
                String resultDetailInput = fit.getString();
                detailLevel = resultDetailInput;
                this.processResultDetail(resultDetailInput);
            }
        }

        if(fileItem == null || fileItem.getSize() == 0) {
            this.setTopErrorMessage("Empty or no document found!");
            return false;
        }

        doc = null;

        try {
            doc = MiscXmlUtil.inputStreamToDom(fileItem.getInputStream());
            DocumentType documentType = DocumentType.findDocumentType(this.getDocumentType(), docTypes);
            validationResults = documentType.doFullValidation(fileItem.getInputStream(), doc, docTypes, resultDetail, true,
                    skeletonLocation);
        } catch(Exception e) {
            // TODO: Add exception message to error??
            e.printStackTrace();
            this.setTopErrorMessage("Error.  Unable to parse incoming XML.  Check for valid content.  (Also check for extraneous characters at the beginning of the file.)");
            return false;
        }
        return true;
    }

    public String getDetailLevel() {
        if(detailLevel.equals("all"))
            return "Errors, Warnings, Notes";
        else if(detailLevel.equals("errorsWarnings"))
            return "Errors, Warnings";
        else
            return "Errors";
    }

    public Document getDoc() {
        return doc;
    }

    public int getIndexTest() {
        return indexTest;
    }

    public String fileContent(String path, int line) {
        XPathFactory xpfactory = XPathFactory.newInstance();
        XPath xpath = xpfactory.newXPath();
        XPathExpression expr;
        StringWriter output = new StringWriter();
        String code = "qwertyuiopasdfghjkl";
        try {
            if(path != null && !(path.length() == 0) && !path.equals("")) {
                expr = xpath.compile(path);
                Object result = expr.evaluate(doc, XPathConstants.NODESET);
                NodeList nodes = (NodeList) result;
                if(nodes.getLength() > 0) {
                    Node parent = nodes.item(0).getParentNode();
                    Element elem = doc.createElement(code);
                    Node newChild = parent.insertBefore(elem, nodes.item(0));
                    for(int i = 0; i < nodes.getLength(); i++) {
                        Node oldChild = parent.removeChild(nodes.item(i));
                        newChild.appendChild(oldChild);
                    }
                    Transformer tr = TransformerFactory.newInstance().newTransformer();
                    tr.setOutputProperty(OutputKeys.INDENT, "yes");
                    tr.transform(new DOMSource(doc), new StreamResult(output));
                    for(int i = 0; i < nodes.getLength(); i++) {
                        Node n = newChild.removeChild(nodes.item(i));
                        parent.insertBefore(n, newChild);
                    }
                    parent.removeChild(newChild);
                } else
                    TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc),new StreamResult(output));
            } else {
                if(line > -1) {
                    Transformer tr = TransformerFactory.newInstance().newTransformer();
                    tr.setOutputProperty(OutputKeys.INDENT, "yes");
                    tr.transform(new DOMSource(doc), new StreamResult(output));
                    String str = output.toString();
                    int index = 0;
                    int counter = 0;
                    while(str.indexOf('\n', index) > -1 && counter < line) {
                        counter++;
                        index = str.indexOf('\n', index) + 1;
                    }
                    if(counter == line) {
                        str = str.substring(0, index - 1) + "<" + code + ">" + str.substring(index, str.indexOf('\n',
                                index)) + "</" + code + ">" + str.substring(str.indexOf('\n', index) + 1);
                        return deleteBlankLines(design(str, code));
                    }
                } else {
                    Transformer tr = TransformerFactory.newInstance().newTransformer();
                    tr.setOutputProperty(OutputKeys.INDENT, "yes");
                    tr.transform(new DOMSource(doc), new StreamResult(output));
                }
            }

        } catch(DOMException e) {
            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.transform(new DOMSource(doc), new StreamResult(output));
                System.out.println(e.getMessage());
                return deleteBlankLines(design(output.toString(), code));
            } catch(TransformerConfigurationException e1) {
                e1.printStackTrace();
            } catch(TransformerException e1) {
                e1.printStackTrace();
            } catch(TransformerFactoryConfigurationError e1) {
                e1.printStackTrace();
            }
        } catch(XPathExpressionException e) {
            e.printStackTrace();
        } catch(TransformerConfigurationException e) {
            e.printStackTrace();
        } catch(TransformerException e) {
            e.printStackTrace();
            return "Your file's session time is over. Reload it.";
        } catch(TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        }

        return deleteBlankLines(design(output.toString(), code));
    }

    public String design(String txt, String code) {
        StringBuffer res = new StringBuffer("<xmp>" + txt + "</xmp>");
        int i1 = res.indexOf("<" + code + ">");

        if(i1 != -1) {
            res.replace(i1, i1 + code.length() + 2,
                    "</xmp><font color=\"red\"><B><a id=\"error\"></a><xmp>");
            int i2 = res.indexOf("</" + code + ">");
            res.replace(i2, i2 + code.length() + 3, "</xmp></B></font><xmp>");
            if(res.charAt(i1 - 1) == '\n')
                res.deleteCharAt(i1 - 1);
            return res.toString();
        } else {
            res.replace(0, 5,
                    "<font color=\"red\"><B>\n<a id=\"error\"></a><xmp>");
            res.replace(res.indexOf("</xmp>"), res.indexOf("</xmp>") + 5,
                    "</xmp></B></font>\n</xmp>");
        }
        return res.toString();
    }

    public String deleteBlankLines(String input) {
        StringBuffer output = new StringBuffer(input);

        int index = output.indexOf("\n\n");

        while(index > -1) {
            output.deleteCharAt(index);
            index = output.indexOf("\n\n");
        }

        return output.toString();
    }

    public String getSchemaName() {
        if(validationResults != null && validationResults.getSchemaName() != null)
            return validationResults.getSchemaName();
        return "Schema";
    }

    public boolean hasSchemaErrors() {
        if(validationResults == null || validationResults.getSchemaErrors() == null)
            return false;
        if(validationResults.getSchemaErrors().getErrors() == null)
            return false;
        if(validationResults.getSchemaErrors().getErrors().size() == 0)
            return false;
        return true;
    }

    public String getFileName() {
        return fileName;
    }

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
        if(validationResults == null || validationResults.getSchemaErrors() == null)
            return false;
        if(validationResults.getSchemaErrors().getWarnings() == null)
            return false;
        if(validationResults.getSchemaErrors().getWarnings().size() == 0)
            return false;
        return true;
    }

    public boolean hasSchematronErrors() {
        if(validationResults.getSchematronErrors() == null)
            return false;
        if(validationResults.getSchematronErrors().size() == 0)
            return false;
        return true;
    }

    public List getSchematronErrors() {
        return validationResults.getSchematronErrors();
    }

    public boolean hasTopErrorMessage() {
        if(errorMessage == null)
            return false;
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

    // TODO: get rid of these next two methods eventually
	/*
     * public static int convertDocumentType(String docType) {
     * if("cdar2".equalsIgnoreCase(docType)) return ValidationHelper.CDA_R2;
     * if("ccd".equalsIgnoreCase(docType)) return ValidationHelper.CCD;
     * if("c32".equalsIgnoreCase(docType)) return ValidationHelper.C32;
     * if("c37".equalsIgnoreCase(docType)) return ValidationHelper.C37;
     * if("c28nursing".equalsIgnoreCase(docType)) return
     * ValidationHelper.C28_NURSING;
     * if("c28physician".equalsIgnoreCase(docType)) return
     * ValidationHelper.C28_PHYSICIAN; return ValidationHelper.UNKNOWN_TYPE; }
     * public static String convertDocumentType(int docType) { switch (docType) {
     * case ValidationHelper.CDA_R2: return "cdar2"; case ValidationHelper.CCD:
     * return "ccd"; case ValidationHelper.C32: return "c32"; case
     * ValidationHelper.C37: return "c37"; case ValidationHelper.C28_NURSING:
     * return "c28nursing"; case ValidationHelper.C28_PHYSICIAN: return
     * "c28physician"; } return "unknown"; }
     */
    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    /*
     * public static SchemaValidationErrorHandler
     * processStandardCdaR2(InputStream is) { return
     * XmlValidation.validateWithSchema(is,"http://marvel.ncsl.nist.gov/CDA_R2_NormativeWebEdition2005/infrastructure/cda/CDA.xsd"); }
     * 
     */
    public List getRunnableValidations() {
        if(runnableValidations == null && docTypes != null) {
            runnableValidations = DocumentType.filterRunnableOnly(docTypes);
        }
        return runnableValidations;
    }

    public int getNumberOfRunnable() {
        List runnable = this.getRunnableValidations();
        if(runnable == null)
            return 0;
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
        while(i < this.getNumberOfRunnable() && !this.getRunnableDocumentTypeAt(i).getId().equals(name)) {
            i++;
        }
        if(this.getRunnableDocumentTypeAt(i).getId().equals(name))
            return i;
        else
            return -1;
    }

    public void processResultDetail(String resultDetailInput) {
        resultDetail = new ArrayList();
        System.out.println(resultDetailInput);
        if("all".equals(resultDetail))
            resultDetail.add("#ALL");
        else if("errorsWarnings".equals(resultDetailInput)) {
            resultDetail.add("errors");
            resultDetail.add("warning");
            resultDetail.add("violation");
        // resultDetail.add("manual");
        } else if("errors".equals(resultDetailInput)) {
            resultDetail.add("errors");
        // resultDetail.add("manual");
        } else {
            resultDetail.add("#ALL");
        }
    }
}
