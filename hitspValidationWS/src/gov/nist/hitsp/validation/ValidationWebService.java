/*
 * ValidationWebService.java
 *
 * Created on February 14, 2008, 3:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
// ATTN: SEE ERIC POISEAU'S EMAIL AT BOTTOM OF THIS FILE!!!
package gov.nist.hitsp.validation;

import gov.nist.healthcare.mu.core.stat.StatTimer;
import gov.nist.healthcare.mu.core.stat.StatTimer.TestContext;
import gov.nist.hl7.v3.xml.util.MiscXmlUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author mccaffrey
 */
public class ValidationWebService {

    List docTypes = null;
   // String docTypesLocation = "/home/mccaffrey/cvsfiles/hitspValidation/web/WEB-INF/docTypes.xml"; // TODO: This should not be hardcoded.
    //String docTypesLocation = "../webapps/ws/WEB-INF/services/ValidationWebService/conf/docTypes.xml"; // TODO: This should not be hardcoded.

    // String docTypesLocation = "/usr/local/tomcat/apache-tomcat-6.0.33/webapps/axis2/WEB-INF/services/ValidationWebService/conf/docTypes.xml";
    //       String docTypesLocation = "../webapps/axis2/WEB-INF/services/ValidationWebService/conf/docTypes.xml";
    //String docTypesLocation = "./docTypes.xml";
    // 2014/05/13: For some reason the home directory on cda-validation.nist.gov is defaulting to "/" instead of to the bin directory
    // of tomcat, so a relative path to the config will no longer work.  I hate to hardcode in the path, but it's the simpliest solution for now.
      //String SKELETON_LOCATION = "http://localhost:8080/hitspValidation/schematron/schematron-report.xsl";
    
    
    
    
    
    //String SKELETON_LOCATION = "http://localhost:11080/hitspValidation/schematron/schematron-Validator-report.xsl"; // TODO: Update November 2011.  This is still wrong.  Sorry.
    //String docTypesLocation = "/sites/tomcat_andrew/tomcat_cda/webapps/ws/WEB-INF/services/ValidationWebService/conf/docTypes.xml";

        String SKELETON_LOCATION = "http://localhost:8080/hitspValidation/schematron/schematron-Validator-report.xsl"; // TODO: Update November 2011.  This is still wrong.  Sorry.
       String docTypesLocation = "/usr/local/tomcat/apache-tomcat-7.0.53/webapps/ws/WEB-INF/services/ValidationWebService/conf/docTypes.xml";
       
    public WSValidationResults validateDocument(String specificationId, String document) {

        // 2014-06-04: Logging added.
        StatTimer timer = new StatTimer(TestContext.TEST_CONTEXT_FREE, specificationId);
        timer.start();

        System.out.println("docType = " + specificationId);
        Document doc = null;
        try {
            doc = MiscXmlUtil.stringToDom(document);
        } catch (Exception e) {
            WSValidationResults results = ValidationWebService.createSingleError("Unable to parse incoming message.  Verify valid XML.");

            //2014-06-04: Logging added.
            timer.endError();

            return results;
        }

        InputStream is = new ByteArrayInputStream(document.getBytes());
        DocumentType documentType = DocumentType.findDocumentType(specificationId.toString(), this.getDocTypes());
        if (documentType == null) {
            WSValidationResults results = ValidationWebService.createSingleError("Unknown document type. Use getSpecificationList method for finding valid specification IDs.");
            
            //2014-06-04: Logging added.
            timer.endError();            
            
            return results;
        }

        Collection<String> errorDetail = new ArrayList<String>();
        errorDetail.add("errors");
        errorDetail.add("warning");
        errorDetail.add("violation");
        errorDetail.add("note");
        //   ValidationResults validationResults = documentType.doFullValidation(is, doc, this.getDocTypes(), errorDetail, false);
        ValidationResults validationResults = documentType.doFullValidation(is, doc, this.getDocTypes(), errorDetail, false, SKELETON_LOCATION);

        SchemaValidationErrorHandler schemaErrors = validationResults.getSchemaErrors();
        Collection<WSIndividualValidationResult> errorsToReturn = new ArrayList<WSIndividualValidationResult>();

        WSValidationResults results = new WSValidationResults();

        if (schemaErrors.hasErrors()) {
            Vector<String> errors = schemaErrors.getErrors();
            processSchemaIssues(errorsToReturn, errors, "error");
        }
        if (schemaErrors.hasFatalErrors()) {
            Vector<String> errors = schemaErrors.getFatalErrors();
            processSchemaIssues(errorsToReturn, errors, "error");
        }
        if (schemaErrors.hasWarnings()) {
            Vector<String> warnings = schemaErrors.getWarnings();
            processSchemaIssues(errorsToReturn, warnings, "warning");
        }
        results.setIssue(errorsToReturn.toArray(new WSIndividualValidationResult[0]));
        List<String> schematronResults = validationResults.getSchematronErrors();
        if (schematronResults.size() > 0) {
            Iterator<String> it = schematronResults.iterator();
            while (it.hasNext()) {
                String schematronResult = it.next();
                WSIndividualValidationResult[] schematronResultArray = WSIndividualValidationResult.processMultiple(schematronResult, null);
                results.appendIssue(schematronResultArray);
            }
        }

        Arrays.sort(results.getIssue());

        boolean pass = false;
        if (results.getIssue().length == 0) {
            pass = true;
        } else {
            WSIndividualValidationResult result = results.getIssue()[0];
            if (result.getSeverity().equals("error")) {
                pass = false;
            } else {
                pass = true;
            }
        }

        results.setValidationTest(pass);
        
        //2014-06-04: Logging added.
        timer.endSuccess();
            
        return results;
    }

    public WSSpecification[] getAvailableValidations() {
        System.out.println("NOTE! getAvailableValidations() called!");
        List localDocTypes = this.getDocTypes();
        return WSSpecification.convert(localDocTypes);
    }

    private static void processSchemaIssues(final Collection<WSIndividualValidationResult> errorsToReturn, final Vector<String> errors, String severity) {
        Iterator<String> it = errors.iterator();
        while (it.hasNext()) {
            String message = it.next();
            WSIndividualValidationResult result = new WSIndividualValidationResult();
            // TODO: If we ever expand beyond only CDA's: fix this
            result.setSpecification("CDA R2");
            result.setMessage(message);
            result.setSeverity(severity);
            errorsToReturn.add(result);
        }
    }

    //TODO: Is here the best place for this function?
    protected static WSValidationResults createSingleError(String errorMessage) {
        WSValidationResults results = new WSValidationResults();
        results.setValidationTest(false);
        WSIndividualValidationResult error = new WSIndividualValidationResult();
        error.setSeverity("error");
        error.setMessage(errorMessage);
        Collection<WSIndividualValidationResult> issue = new ArrayList<WSIndividualValidationResult>();
        issue.add(error);
        results.setIssue(issue.toArray(new WSIndividualValidationResult[0]));
        return results;
    }

    protected List getDocTypes() {
        if (docTypes == null) {
            try {
                docTypes = DocumentType.parseDocumentTypes(MiscXmlUtil.fileToDom(docTypesLocation));
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace();
            } catch (SAXException ex) {
                ex.printStackTrace();
            }
        }
        return docTypes;
    }

    public static final void main(String[] args) {
        ValidationWebService ws = new ValidationWebService();
        WSValidationResults results = ws.validateDocument("crs", "<ClinicalDocument />");

        System.out.println(results.toString());
    }

}

/*
 * 
 * Dear Andrew

 I'd like to make a request for  modification of your CDA validation  
 service. Please let me know if this is something you can do or not !

 Currently the webservice offers a method that returns the list of  
 available validations. Each validations has a Description, a Name and  
 a SpecificationId. Would it be possible to
 include the templateId as well

 I think that having the  templateId  as an identifier of the  
 schematron might be very usefull for the validation process.
 Indeed, when I have to validate a document, I need to validate it  
 against all the templateId that the document claims to support.

 Could we have the templateId returned as well when the  
 getAvailableValidations method is called ?

 <xs:element name="getAvailableValidationsResponse">
 <xs:complexType>
 <xs:sequence>
 <xs:element maxOccurs="unbounded"  
 minOccurs="0" name="return" nillable="true" type="ns0:WSSpecification"/>
 </xs:sequence>
 </xs:complexType>
 </xs:element>
 Could we specify the templateId  when we call the validateDocument  
 method ? You may keep the existing method and create a new one that is  
 called  validateDocumentBasedOnTemplateId for example.

 <xs:element name="validateDocumentBasedOnTemplateId">
 <xs:complexType>
 <xs:sequence>
 <xs:element minOccurs="0" name="templateId"  
 nillable="false" type="xs:string"/>
 <xs:element minOccurs="0" name="document"  
 nillable="false" type="xs:string"/>
 </xs:sequence>
 </xs:complexType>
 </xs:element>

 Another method that would be interesting to have is :  
 isTemplateIdAvailable with a templateId as input and a boolean as  
 returned value.

 <xs:element name="isTemplateIdAvailable">
 <xs:complexType>
 <xs:sequence>
 <xs:element minOccurs="0" name="templateId"  
 nillable="false" type="xs:string"/>
 </xs:sequence>
 </xs:complexType>
 </xs:element>

 Finally another method that would be VERY VERY nice to have in the  
 service :
 autoValidateDocument : (you may find a better name for this method)  
 input parameter the document and nothing else

 <xs:element name="autoValidateDocument">
 <xs:complexType>
 <xs:sequence>
 <xs:element minOccurs="0" name="document"  
 nillable="false" type="xs:string"/>
 </xs:sequence>
 </xs:complexType>
 </xs:element>


 The method will parse the document, search for the templateId's and  
 return the result of the validation for each of the templateId the  
 document claims to support.
 The method could return that the templateId is unknown in the report

 If we had this last method the process could be very simple !

 Please let me know what you think about this ?

 Best regards

 * */
