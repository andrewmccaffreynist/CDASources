/*
 * DependencyComparator.java
 *
 * Created on November 19, 2007, 2:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gov.nist.hitsp.validation;

import java.util.Comparator;

/**
 *
 * @author mccaffrey
 */
public class DependencyComparator implements Comparator<Dependency> {
    
    /** Creates a new instance of DependencyComparator */
    public DependencyComparator() {
    }
    
    public int compare(Dependency d1, Dependency d2) {
        if(d1.getSequenceNumber() == d2.getSequenceNumber()) return 0;
        if(d1.getSequenceNumber() < d2.getSequenceNumber()) return -1;
        
        return 1;
    }
    
}
