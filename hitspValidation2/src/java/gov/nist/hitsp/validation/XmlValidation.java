/*
 * XmlValidation.java
 *
 * Created on October 1, 2007, 11:25 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gov.nist.hitsp.validation;

import gov.nist.hl7.v3.xml.util.MiscXmlUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author mccaffrey
 */
public class XmlValidation {
    
    /** Creates a new instance of XmlValidation */
    private XmlValidation() {
    }
    
    public static SchemaValidationErrorHandler validateWithSchema(InputStream xml, String schemaLocation) {                
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
        SchemaValidationErrorHandler handler = new SchemaValidationErrorHandler();
        builder.setErrorHandler(handler);
        
        try {
            Document doc = builder.parse(xml);            
        } catch (SAXException e) {
            System.out.println("Message is not valid XML.");
            handler.addError("Message is not valid XML.", null);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Message is not valid XML.  Possible empty message.");
            handler.addError("Message is not valid XML.  Possible empty message.", null);
            e.printStackTrace();
        }
        
        return handler;
        
    }
    //  public static String validateWithSchematron(InputStream xml, String schematronLocation) {
    public static String validateWithSchematron(Document xml, String schematronLocation, String skeletonLocation, Collection<String> phases, boolean htmlFormatted) {
        if(phases == null)
            phases = XmlValidation.emptyPhases();
        try {
            
            StringBuilder result = new StringBuilder();
            
            // TODO: Not the most elegent solution, but...
            if(!htmlFormatted)
                result.append("<result>");
            Iterator<String> it = phases.iterator();
            while(it.hasNext()) {
                
                // TODO: check this. probably not the most efficient way of doing this...
                System.out.println("skeletonLocation = " + skeletonLocation);
                URL skeletonUrl = new URL(skeletonLocation);
                /*
                if(htmlFormatted)
                    skeletonUrl = new URL("http://localhost:8080/hitspValidation/schematron/schematron-report.xsl");                
                else
                    skeletonUrl = new URL("http://localhost:8080/hitspValidation/schematron/schematron-Validator-report.xsl");
                 */
                InputStream skeleton = skeletonUrl.openStream();
                URL schematronUrl = new URL(schematronLocation);
                InputStream schematron = schematronUrl.openStream();
                
                String phase = it.next();
                System.out.println(phase);
                Node schematronTransform = XmlValidation.doTransform(schematron,skeleton,phase);
                if(htmlFormatted)
                    result.append(XmlValidation.doTransform(xml,schematronTransform));
                else 
                    result.append(MiscXmlUtil.removeXmlHeader(XmlValidation.doTransform(xml,schematronTransform)));                                    
            }
            if(!htmlFormatted)
                result.append("</result>");
            return result.toString();
            
//            return MiscXmlUtil.xmlToString(result);
            
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
            return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }
    
    
    // TODO: Clean up this and following methods (refactor most of it)
    // TODO: This setProperty, clearProperty stuff is not ideal.  At some point make this cleaner...
    
    public static Node doTransform(InputStream originalXml, InputStream transform,String phase) {
        
        System.out.println("javax.xml.transform.TransformerFactory = " + System.getProperty("javax.xml.transform.TransformerFactory"));
        
        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        DOMResult result = new DOMResult();
        try {
            Source xmlSource = new StreamSource(originalXml);
            Source xsltSource = new StreamSource(transform);
            
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer(xsltSource);
            transformer.setParameter("phase",phase);
            transformer.transform(xmlSource, result);
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
            return null;
        } catch (TransformerException te) {
            te.printStackTrace();
            return null;
        } finally {
            System.clearProperty("javax.xml.transform.TransformerFactory");
        }
        return result.getNode();
    }
    
    public static String doTransform(Document originalXml, Node transform) {
        System.out.println("javax.xml.transform.TransformerFactory = " + System.getProperty("javax.xml.transform.TransformerFactory"));
        
        
        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        StreamResult result = new StreamResult(os);
        try {
            Source xmlSource = new DOMSource(originalXml);
            Source xsltSource = new DOMSource(transform);
            
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer(xsltSource);
            transformer.transform(xmlSource, result);
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
            return null;
        } catch (TransformerException te) {
            te.printStackTrace();
            return null;
        } finally {
            System.clearProperty("javax.xml.transform.TransformerFactory");
        }
        return os.toString();
        
    }
    
    public static String doTransform(InputStream originalXml, Node transform) {
        System.out.println("javax.xml.transform.TransformerFactory = " + System.getProperty("javax.xml.transform.TransformerFactory"));
        
        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        StreamResult result = new StreamResult(os);
        try {
            Source xmlSource = new StreamSource(originalXml);
            Source xsltSource = new DOMSource(transform);
            
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer(xsltSource);
            transformer.transform(xmlSource, result);
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
            return null;
        } catch (TransformerException te) {
            te.printStackTrace();
            return null;
        } finally {
            System.clearProperty("javax.xml.transform.TransformerFactory");
        }
        return os.toString();
        
    }
    
    
    // TODO: Should this throw the transformer exceptions up the chain?
    public static Node doTransform(InputStream originalXml, String transformLocation) {
        System.out.println("javax.xml.transform.TransformerFactory = " + System.getProperty("javax.xml.transform.TransformerFactory"));
        
        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        DOMResult result = new DOMResult();
        try {
            Source xmlSource = new StreamSource(originalXml);
            Source xsltSource = new StreamSource(transformLocation);
            
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer(xsltSource);
            transformer.transform(xmlSource, result);
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
            return null;
        } catch (TransformerException te) {
            te.printStackTrace();
            return null;
        } finally {        
            System.clearProperty("javax.xml.transform.TransformerFactory");
        }
        return result.getNode();
    }
    
    public static void main(String[] args) {
        // try {
            /*
            System.out.println("javax.xml.transform.TransformerFactory = " + System.getProperty("javax.xml.transform.TransformerFactory"));
             
            System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
            TransformerFactory tf = TransformerFactory.newInstance();
             
            //tf.setAttribute("javax.xml.transform.TransformerFactory","org.apache.xalan.xsltc.trax.TransformerFactoryImpl");
             
            Source xmlSource = new StreamSource("http://localhost:8084/hitspValidation/schematron/ccd/ccd.sch");
            Source xsltSource = new StreamSource("http://localhost:8084/hitspValidation/schematron/skeleton1-5Mod_add_br.xsl");
            DOMResult result = new DOMResult();
            //SAXResult saxResult = new SAXResult();
            Transformer transformer = tf.newTemplates(xsltSource).newTransformer(); // .newTransformer(xsltSource);
             
             
            //transformer.transform(xmlSource, result);
            transformer.transform(xmlSource,result);
//            System.out.println(MiscXmlUtil.xmlToString(result.getNode()));
             
            Source xmlSource2 = new StreamSource("http://localhost:8084/hitspValidation/schematron/SampleCCDDocument.xml");
            Source schematronSource = new DOMSource(result.getNode());
             
            //Source schematronSource = new SAXSource(saxResult.);
             
//            Source schematronSource = new StreamSource("http://localhost:8084/hitspValidation/schematron/ccd/temp.xsl");
            //Document doc = MiscXmlUtil.fileToDom("/home/mccaffrey/cvsfiles/hitspValidation/web/schematron/ccd/temp.xsl");
             
            System.out.println("Here!1");
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            System.out.println("Here!2");
             
            StreamResult result2 = new StreamResult(os);
            System.out.println("Here!3");
             
            Transformer transformer2 = tf.newTemplates(schematronSource).newTransformer(); // newTransformer(schematronSource);
            System.out.println("Here!4");
             
            transformer2.transform(xmlSource2,result2);
            System.out.println("Here!5");
             
            System.out.println("Output = " + os.toString());
             
             */
        
        
        
            /*
             
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
            //return null;
        } catch (TransformerException te) {
            te.printStackTrace();
            //return null;
        } /* /* catch (SAXException saxe) {
            saxe.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } */
    }
    public static Collection<String> emptyPhases() {
        Collection<String> phases = new ArrayList<String>();
        phases.add("#ALL");
        return phases;
    }
}