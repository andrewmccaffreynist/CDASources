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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;

/**
 *
 * @author mccaffrey
 */
public class WSSpecification {
    
    private String specificationId = null;
    private String name = null;
    private String description = null;
    //  private String[] dependencies = null;
    
    /**
     * Creates a new instance of WSSpecification
     */
    public WSSpecification() {
    }
    
    public WSSpecification(OMElement element) {
        Iterator it = element.getChildElements();
        while(it.hasNext()) {
            OMElement child = (OMElement) it.next();
            if(child.getLocalName().equals("specificationId"))
                this.setSpecificationId(child.getText());
            else if(child.getLocalName().equals("name"))
                this.setName(child.getText());
            else if(child.getLocalName().equals("description"))
                this.setDescription(child.getText());
        }
    }
    
    public WSSpecification(DocumentType specification) {
        this.specificationId = specification.getId();
        this.name = specification.getDisplayName();
        this.description = specification.getDescription();
        //    this.dependencies = specification.getDependencies();
    }
    
    public static WSSpecification[] processMultiple(OMElement element, String elementName) {
        Collection<WSSpecification> documentTypes = new ArrayList<WSSpecification>();
        QName qname = new QName(Constants.NIST_WEB_SERVICE_NAMESPACE,elementName);
        Iterator it = element.getChildrenWithName(qname);
        while(it.hasNext()) {
            OMElement child = (OMElement) it.next();
            WSSpecification specification = new WSSpecification(child);
            documentTypes.add(specification);
        }
        return documentTypes.toArray(new WSSpecification[0]);
    }
    
    public static WSSpecification[] convert(List specifications) {
        Collection<WSSpecification> converted = new ArrayList<WSSpecification>();
        Iterator it = specifications.iterator();
        while(it.hasNext()) {
            DocumentType dt = (DocumentType) it.next();
            if(!dt.isBackendOnly()) {
                WSSpecification specification = new WSSpecification(dt);
                converted.add(specification);
            }
        }
        
        
        return converted.toArray(new WSSpecification[0]);
    }
    
    public String getSpecificationId() {
        return specificationId;
    }
    
    public void setSpecificationId(String specificationId) {
        this.specificationId = specificationId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: " + this.getSpecificationId() + '\n');
        sb.append("Name: " + this.getName() + '\n');
        sb.append("Description: " + this.getDescription() + '\n');
        return sb.toString();
    }
    
/*
    public String[] getDependencies() {
        return dependencies;
    }
 
    public void setDependencies(String[] dependencies) {
        this.dependencies = dependencies;
    }
 */
}
