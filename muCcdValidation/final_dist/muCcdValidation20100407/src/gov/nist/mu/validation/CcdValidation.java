/*
Favorable outcome in the use of the test materials on this site does not imply
conformance recognition by NIST or HL7.

Links to non-Federal Government Web sites do not imply NIST endorsement of any
particular product, service, organization, company, information provider, or
content.

This software was developed at the National Institute of Standards and Technology
by employees of the Federal Government in the course of their official duties.
Pursuant to title 17 Section 105 of the United States Code this software is not
subject to copyright protection and is in the public domain.

The CDA Guideline Validator is an experimental system. NIST assumes no responsibility
whatsoever for its use by other parties, and makes no guarantees, expressed or implied,
about its quality, reliability, or any other characteristic. We would appreciate
acknowledgment if the software is used. This software can be redistributed and/or
modified freely provided that any derivative works bear some notice that they are
derived from it, and any modified versions bear some notice that they have been
modified.

 */
package gov.nist.mu.validation;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author mccaffrey
 */
public class CcdValidation {
  
    public static String schemaLocation = "./cdar2/infrastructure/cda/CDA.xsd";
    public static String schematronLocation = "./schematron/ccd.sch";
    public static String skeletonLocation = "./schematron/schematron-Validator-report.xsl";
    public static TransformerFactory factory = null;

    public static final void main(String[] args) {

        String workingDirectory = System.getProperty("user.dir");        
        schemaLocation = workingDirectory + '/' + schemaLocation;
        schematronLocation = workingDirectory + '/' + schematronLocation;
        skeletonLocation = workingDirectory + '/' + skeletonLocation;

        org.apache.commons.cli.Options cliOptions = CcdValidation.setCliOptions();
        CommandLineParser parser = new GnuParser();
        CommandLine line = null;
        try {
            line = parser.parse(cliOptions, args);
        } catch(ParseException ex) {
            ex.printStackTrace();
            return;
        }

        HelpFormatter formatter = new HelpFormatter();
        if(line.hasOption("help") || args.length == 0 || !line.hasOption("input")) {
            formatter.printHelp("java -jar CcdValidation.jar", cliOptions);

            System.out.println("\n\nExample: username@localhost:~/ccdfiles> java -jar muCcdValidation.jar -input CCD.xml -output report.xml\n");

            return;
        }

        String input = null;        
        input = line.getOptionValue("input");
        File file = new File(input);

        SchemaValidationErrorHandler errorHandler = new SchemaValidationErrorHandler();
        Document doc = CcdValidation.validateWithSchema(file, errorHandler, schemaLocation);
        String checksString = CcdValidation.validateWithSchematron(doc, schematronLocation, skeletonLocation, "checks");
        String errorsString = CcdValidation.validateWithSchematron(doc, schematronLocation, skeletonLocation, "errors");
        String warningsString = CcdValidation.validateWithSchematron(doc, schematronLocation, skeletonLocation, "warning");

        Node checks = null;
        Node errors = null;
        Node warnings = null;

        try {
            checks = CcdValidation.stringToDom(checksString);
            errors = CcdValidation.stringToDom(errorsString);
            warnings = CcdValidation.stringToDom(warningsString);
        } catch(SAXException ex) {
            ex.printStackTrace();
            return;
        } catch(ParserConfigurationException ex) {
            ex.printStackTrace();
            return;
        } catch(IOException ex) {
            ex.printStackTrace();
            return;
        }

        Document result = CcdValidation.generateReport(errorHandler, doc, checks, errors, warnings);

        String output = CcdValidation.xmlToString(result);

        String outputfilename = null;
        
        if (line.hasOption("output")) {
            outputfilename = line.getOptionValue("output");
        } else {
            String date = CcdValidation.createDateOfTest();
            String time = CcdValidation.createTimeOfTest().substring(0,5);
            outputfilename = "ccdValidationResult" + date + time + ".xml";
        }
        FileWriter outputStream = null;
        try {
            outputStream = new FileWriter(outputfilename);
            outputStream.write(output);
        } catch(IOException ex) {
            ex.printStackTrace();
            return;
        } finally {
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch(IOException ex) {
                    ex.printStackTrace();
                    return;
                }
            }
        }


    }

    public static Document generateReport(SchemaValidationErrorHandler errorHandler, Document doc, Node checks, Node errors, Node warnings) {

        Document result = null;
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            result = builder.newDocument();
        } catch(ParserConfigurationException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        Element report = result.createElement("Report");
        result.appendChild(report);

        int errorCount = errorHandler.getNumberErrors() + errors.getFirstChild().getChildNodes().getLength();
        int warningCount = warnings.getFirstChild().getChildNodes().getLength();

        
        Element header = CcdValidation.createHeader(result, errorCount, warningCount);
        report.appendChild(header);

        if(errorHandler.hasErrors()) {
            Element schemaErrorReport = CcdValidation.createSchemaErrorReport(result, errorHandler);
            report.appendChild(schemaErrorReport);
        }

        report.appendChild(result.importNode(errors.getFirstChild(), true));
        report.appendChild(result.importNode(warnings.getFirstChild(), true));
        report.appendChild(result.importNode(checks.getFirstChild(), true));

        Element testObject = result.createElement("TestObject");

         //   if(doc != null) {
                testObject.appendChild(result.importNode(doc.getDocumentElement(), true));
         //   } else {
         //       testObject.setTextContent(CcdValidation.readFile(path));
         //   }

        report.appendChild(testObject);
        return result;
    }

    private static Element createHeader(Document result, int errorCountInt, int warningCountInt) {

        Element reportHeader = result.createElement("ReportHeader");

        Element validationStatus = result.createElement("ValidationStatus");
        validationStatus.setTextContent("Complete");
        reportHeader.appendChild(validationStatus);

        Element serviceName = result.createElement("ServiceName");
        serviceName.setTextContent("CCD Validation");
        reportHeader.appendChild(serviceName);

        Element serviceProvider = result.createElement("ServiceProvider");
        serviceProvider.setTextContent("NIST");
        reportHeader.appendChild(serviceProvider);

        Element standardType = result.createElement("StandardType");
        standardType.setTextContent("CDA R2");
        reportHeader.appendChild(standardType);

        Element standardVersion = result.createElement("StandardVersion");
        standardVersion.setTextContent("N/A");
        reportHeader.appendChild(standardVersion);

        Element dateOfTest = result.createElement("DateOfTest");
        dateOfTest.setTextContent(CcdValidation.createDateOfTest());
        reportHeader.appendChild(dateOfTest);

        Element timeOfTest = result.createElement("TimeOfTest");
        timeOfTest.setTextContent(CcdValidation.createTimeOfTest());
        reportHeader.appendChild(timeOfTest);

        Element reportPositiveIndicator = result.createElement("ReportPositiveIndicator");
        reportPositiveIndicator.setTextContent("True");
        reportHeader.appendChild(reportPositiveIndicator);

        Element resultOfTest = result.createElement("ResultOfTest");
        if(errorCountInt == 0)
            resultOfTest.setTextContent("Passed");
        else
            resultOfTest.setTextContent("Failed");
        reportHeader.appendChild(resultOfTest);

        Element errorCount = result.createElement("ErrorCount");
        errorCount.setTextContent(String.valueOf(errorCountInt));
        reportHeader.appendChild(errorCount);

        Element warningCount = result.createElement("WarningCount");
        warningCount.setTextContent(String.valueOf(warningCountInt));
        reportHeader.appendChild(warningCount);

        return reportHeader;
    }

    public static String createDateOfTest() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String createTimeOfTest() {
        DateFormat dateFormat = new SimpleDateFormat("HHmmss.SSSS ZZZZ");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String readFile(String path) throws FileNotFoundException, IOException {
        BufferedReader input = new BufferedReader(new FileReader(new File(path)));
        String line = null;
        StringBuilder xml = new StringBuilder();
        while((line = input.readLine()) != null)
            xml.append(line);
        return xml.toString();
    }

    public static Document validateWithSchema(File xml, SchemaValidationErrorHandler handler, String schemaLocation) {

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
        } catch(ParserConfigurationException pce) {
            pce.printStackTrace();
            return null;
        }

        builder.setErrorHandler(handler);
        Document doc = null;
        try {
            doc = builder.parse(xml);
        } catch(SAXException e) {
            System.out.println("Message is not valid XML.");
            handler.addError("Message is not valid XML.", null);
            e.printStackTrace();
        } catch(IOException e) {
            System.out.println("Message is not valid XML.  Possible empty message.");
            handler.addError("Message is not valid XML.  Possible empty message.", null);
            e.printStackTrace();
        }

        return doc;

    }

    public static String validateWithSchematron(Document xml, String schematronLocation, String skeletonLocation, String phase) {

        StringBuilder result = new StringBuilder();

        File schematron = new File(schematronLocation);
        File skeleton = new File(skeletonLocation);

        Node schematronTransform = CcdValidation.doTransform(schematron, skeleton, phase);

        result.append(CcdValidation.doTransform(xml, schematronTransform));
        return result.toString();

       

    }

    public static Node doTransform(File originalXml, File transform, String phase) {

        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        DOMResult result = new DOMResult();
        try {
            Source xmlSource = new StreamSource(originalXml);
            Source xsltSource = new StreamSource(transform);

            Transformer transformer = CcdValidation.getTransformerFactory().newTransformer(xsltSource);
            transformer.setParameter("phase", phase);
            transformer.transform(xmlSource, result);
        } catch(TransformerConfigurationException tce) {
            tce.printStackTrace();
            return null;
        } catch(TransformerException te) {
            te.printStackTrace();
            return null;
        } finally {
            System.clearProperty("javax.xml.transform.TransformerFactory");
        }
        return result.getNode();
    }

    public static String doTransform(Document originalXml, Node transform) {

        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        StreamResult result = new StreamResult(os);
        try {
            Source xmlSource = new DOMSource(originalXml);
            Source xsltSource = new DOMSource(transform);

            Transformer transformer = CcdValidation.getTransformerFactory().newTransformer(xsltSource);
            transformer.transform(xmlSource, result);
        } catch(TransformerConfigurationException tce) {
            tce.printStackTrace();
            return null;
        } catch(TransformerException te) {
            te.printStackTrace();
            return null;
        } finally {
            System.clearProperty("javax.xml.transform.TransformerFactory");
        }
        return os.toString();
    }

    public static String xmlToString(Node inputNode) {
        try {
            Source source = new DOMSource(inputNode);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            Transformer transformer = CcdValidation.getTransformerFactory().newTransformer();
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Document stringToDom(String xmlSource) throws SAXException, ParserConfigurationException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlSource)));
    }

    private static Element createSchemaErrorReport(Document doc, SchemaValidationErrorHandler errorHandler) {

        Element result = doc.createElement("Results");
        result.setAttribute("severity", "schemaViolation");

        Iterator<String> it = errorHandler.getErrors().iterator();
        while(it.hasNext()) {
            Element issue = doc.createElement("issue");
            result.appendChild(issue);

            Element message = doc.createElement("message");
            message.setTextContent(it.next());
            issue.appendChild(message);          
        }


        return result;
    }

    public static TransformerFactory getTransformerFactory() {
        if(factory == null)
            factory = TransformerFactory.newInstance();
        return factory;
    }

    private static org.apache.commons.cli.Options setCliOptions() throws IllegalArgumentException {
        Option help = new Option("help", "display this message");

        Option input = OptionBuilder.withArgName("input").hasArg().withDescription("input filename").create("input");
        Option output = OptionBuilder.withArgName("output").hasArg().withDescription("output filename (without this option, the default filename is ccdValidationResult[timestamp].xml)").create("output");

        org.apache.commons.cli.Options cliOptions = new org.apache.commons.cli.Options();

        cliOptions.addOption(help);
        cliOptions.addOption(input);
        cliOptions.addOption(output);

        return cliOptions;
    }

}
