/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.domain.meddra;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The Class SystemOrganClass.
 */
@Entity
@Table(name = "meddra_soc")
public class SystemOrganClass extends AbstractMeddraDomainObject {

    /*
     * public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() !=
     * o.getClass()) return false;
     * 
     * final DiseaseTerm that = (DiseaseTerm) o;
     * 
     * if (ctepTerm != null ? !ctepTerm.equals(that.ctepTerm) : that.ctepTerm != null) return false;
     * if (medraCode != null ? !medraCode.equals(that.medraCode) : that.medraCode != null) return
     * false;
     * 
     * return true; }
     * 
     * public int hashCode() { int result; result = (ctepTerm != null ? ctepTerm.hashCode() : 0);
     * result = 29 * result + (medraCode != null ? medraCode.hashCode() : 0); return result; }
     */
}
