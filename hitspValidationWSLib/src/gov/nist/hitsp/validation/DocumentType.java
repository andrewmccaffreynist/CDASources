/*
 * DocumentType.java
 *
 * Created on November 16, 2007, 2:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package gov.nist.hitsp.validation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author mccaffrey
 */
public class DocumentType {

    private String id = null;
    private String displayName = null;
    private String description = null;
    private int validationType = 0;
    private String validationLocation = null;
    private List<Dependency> dependencies = null;
    private List<String> dependencyNames = null;
    private boolean backendOnly = false;
    public static final int SCHEMA_VALIDATION = 1;
    public static final int SCHEMATRON_VALIDATION = 2;

    /** Creates a new instance of DocumentType */
    public DocumentType() { }

    /*
     * public DocumentType(String id) { // TODO: Implement this! }
     */
    public DocumentType(Element docTypeElement) {
        this.setId(docTypeElement.getAttribute("id"));
        if(docTypeElement.getAttribute("backendOnly") != null) {
            this.setBackendOnly(Boolean.parseBoolean(docTypeElement.getAttribute("backendOnly")));
        }
        NodeList docChildren = docTypeElement.getChildNodes();
        for(int i = 0; i < docChildren.getLength(); i++) {
            Node child = docChildren.item(i);
            if(child instanceof Element) {
                
                Element childElement = (Element) child;
                String childName = childElement.getNodeName();
                
                if("displayName".equals(childName)) {
                    this.setDisplayName(childElement.getTextContent());
                } else if("description".equals(childName)) {
                    this.setDescription(childElement.getTextContent());
                } else if("validation".equals(childName)) {
                    this.setValidation(childElement);
                } else if("dependencies".equals(childName)) {
                    this.setDependencies(childElement);
                }
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getValidationType() {
        return validationType;
    }

    public void setValidationType(int validationType) {
        this.validationType = validationType;
    }

    public String getValidationLocation() {
        return validationLocation;
    }

    public void setValidationLocation(String validationLocation) {
        this.validationLocation = validationLocation;
    }

    public List<Dependency> getDependencies() {
        if(dependencies == null)
            dependencies = new ArrayList<Dependency>();
        return dependencies;
    }

    public void setDependencies(List<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValidation(Element validation) {
        this.setValidationLocation(validation.getTextContent());
        String type = validation.getAttribute("type");
        if("schema".equals(type))
            this.setValidationType(DocumentType.SCHEMA_VALIDATION);
        else
            this.setValidationType(DocumentType.SCHEMATRON_VALIDATION);
    }

    public void setDependencies(Element dependenciesElement) {
        dependencies = new ArrayList<Dependency>();
        // List unordered = new ArrayList<Dependency>();
        NodeList children = dependenciesElement.getElementsByTagName("dependency");
        for(int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if(child instanceof Element) {
                Element childElement = (Element) child;
                Dependency dep = new Dependency(childElement);
                dependencies.add(dep);
            }
        }

        Collections.sort(dependencies, new DependencyComparator());
    }

    public List<String> getDependencyNames(Collection docTypes) {
        if(dependencyNames == null) {
            dependencyNames = new ArrayList<String>();
            List<Dependency> dependencies = this.getDependencies();
            for(int i = 0; i < dependencies.size(); i++) {
                Dependency dependency = dependencies.get(i);
                String id = dependency.getDocumentType();
                DocumentType docType = DocumentType.findDocumentType(id,docTypes);
                dependencyNames.add(docType.getDisplayName());
            }
        }
        return dependencyNames;
    }

    public List<String> getDependencyDescription(Collection docTypes) {
        ArrayList<String> dependencyDescr = new ArrayList<String>();
        List<Dependency> dependencies = this.getDependencies();
        for(int i = 0; i < dependencies.size(); i++) {
            Dependency dependency = dependencies.get(i);
            String id = dependency.getDocumentType();
            DocumentType docType = DocumentType.findDocumentType(id, docTypes);
            dependencyDescr.add(docType.getDescription());
        }
        return dependencyDescr;
    }

    public List<String> getDependencyId(Collection docTypes) {
        ArrayList<String> dependencyId = new ArrayList<String>();
        List<Dependency> dependencies = this.getDependencies();
        for(int i = 0; i < dependencies.size(); i++) {
            Dependency dependency = dependencies.get(i);
            String id = dependency.getDocumentType();
            DocumentType docType = DocumentType.findDocumentType(id, docTypes);
            dependencyId.add(docType.getId());
        }
        return dependencyId;
    }

    public ValidationResults doFullValidation(InputStream is, Document doc,
                                              Collection docTypes, Collection<String> phases,
                                              boolean htmlFormatted, String skeletonLocation) {
        List<Dependency> dependencies = this.getDependencies();
        ValidationResults results = new ValidationResults();
        for(int i = 0; i < dependencies.size(); i++) {
            Dependency dependency = dependencies.get(i);
            String id = dependency.getDocumentType();
            System.out.println("Dependency: " + id);
            DocumentType docType = DocumentType.findDocumentType(id, docTypes);
            ValidationResults individualResults = docType.doSelfValidation(is,doc, phases, htmlFormatted, skeletonLocation);
            results.append(individualResults);
        }
        ValidationResults currentResults = this.doSelfValidation(is, doc,phases, htmlFormatted, skeletonLocation);
        results.append(currentResults);
        return results;
    }

    public ValidationResults doSelfValidation(InputStream is, Document doc,
                                              Collection<String> phases, boolean htmlFormatted,
                                              String skeletonLocation) {
        ValidationResults results = new ValidationResults();
        if(this.getValidationType() == DocumentType.SCHEMA_VALIDATION) {
            results.setSchemaErrors(XmlValidation.validateWithSchema(is, this.getValidationLocation()));
            results.setSchemaName(this.getDisplayName());
        } else if(htmlFormatted)
            results.addSchematronErrors(this.getDisplayName() + ": " + XmlValidation.validateWithSchematron(doc, this.getValidationLocation(), skeletonLocation, phases, htmlFormatted));
        else {
            results.addSchematronErrors(XmlValidation.validateWithSchematron(doc, this.getValidationLocation(), skeletonLocation, phases, htmlFormatted));
        }
        return results;
    }

    public static List<DocumentType> parseDocumentTypes(Document document) {
        NodeList documentTypes = document.getElementsByTagName("documentType");
        List<DocumentType> allDocTypes = new ArrayList<DocumentType>();
        for(int i = 0; i < documentTypes.getLength(); i++) {
            Node docType = documentTypes.item(i);
            if(docType instanceof Element) {
                Element docTypeElement = (Element) docType;
                allDocTypes.add(new DocumentType(docTypeElement));
            }
        }

        return allDocTypes;
    }

    // TODO: Brute force search. Should do something better eventually if this gets
    // larger.
    public static DocumentType findDocumentType(String id, Collection docTypes) {

        Iterator it = docTypes.iterator();
        while(it.hasNext()) {
            Object docObj = it.next();
            // Unfortunately we need this for compatibility with non-generics
            // Java 1.4 in servlet...
            if(docObj instanceof DocumentType) {
                DocumentType docType = (DocumentType) docObj;
                if(docType.getId().equals(id))
                    return docType;
            }
        }
        return null;
    }

    public static List<DocumentType> filterRunnableOnly(List<DocumentType> docTypes) {
        List<DocumentType> runnable = new ArrayList<DocumentType>();
        Iterator<DocumentType> it = docTypes.iterator();
        while(it.hasNext()) {
            DocumentType docType = it.next();
            if(!docType.isBackendOnly())
                runnable.add(docType);
        }
        return runnable;
    }

    public boolean isBackendOnly() {
        return backendOnly;
    }

    public void setBackendOnly(boolean backendOnly) {
        this.backendOnly = backendOnly;
    }
}
