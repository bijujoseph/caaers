/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.domain;

import javax.persistence.*;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.BeanUtils;

 
/**
 * This class represents the SAEReportPreExistingCondition domain object associated with the Adverse
 * event report.
 *
 * @author Rhett Sutphin
 * @author Biju Joseph
 */
@Entity
@Table(name = "ae_pre_existing_conds")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "SEQ_ae_pre_existing_conds_ID")})
public class SAEReportPreExistingCondition extends AbstractExpeditedReportCollectionElementChild {
    
    /** The pre existing condition. */
    private PreExistingCondition preExistingCondition;

    /** The other. */
    private String other;

    private Boolean linkedToOtherCause = false;

    // //// LOGIC

    /**
     * Gets the name.
     *
     * @return the name
     */
    @Transient
    public String getName() {
        if (getPreExistingCondition() != null) {
            return getPreExistingCondition().getText();
        } else if (getOther() != null) {
            return "Other: " + getOther();
        } else {
            return null;
        }
    }

    // //// BOUND PROPERTIES

    /**
     * Gets the pre existing condition.
     *
     * @return the pre existing condition
     */
    @ManyToOne
    public PreExistingCondition getPreExistingCondition() {
        return preExistingCondition;
    }

    /**
     * Sets the pre existing condition.
     *
     * @param preExistingCondition the new pre existing condition
     */
    public void setPreExistingCondition(PreExistingCondition preExistingCondition) {
        this.preExistingCondition = preExistingCondition;
    }

    /**
     * Gets the other.
     *
     * @return the other
     */
    public String getOther() {
        return other;
    }

    /**
     * Sets the other.
     *
     * @param other the new other
     */
    public void setOther(String other) {
        this.other = other;
    }
    
    @Column(name="synced_to_cause")
    public Boolean getLinkedToOtherCause() {
        return linkedToOtherCause;
    }

    public void setLinkedToOtherCause(Boolean linkedToOtherCause) {
        this.linkedToOtherCause = linkedToOtherCause;
    }

    /**
     * Will return true, if this object has the same 'other' and 'preexistingcondition'.
     *
     * @param cond the cond
     * @param other the other
     * @return true, if successful
     */
    public boolean equals(PreExistingCondition cond, String other){
    	return StringUtils.equals(this.other, other) && ObjectUtils.equals(cond, this.preExistingCondition); 
    }


    ///OBJECT METHODS
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((other == null) ? 0 : other.hashCode());
        result = prime
                * result
                + ((preExistingCondition == null) ? 0 : preExistingCondition
                .hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SAEReportPreExistingCondition other = (SAEReportPreExistingCondition) obj;
        if (this.other == null) {
            if (other.other != null)
                return false;
        } else if (!this.other.equals(other.other))
            return false;
        if (preExistingCondition == null) {
            if (other.preExistingCondition != null)
                return false;
        } else if (!preExistingCondition.equals(other.preExistingCondition))
            return false;
        return true;
    }

    /**
     * Creates the sae report pre existing condition.
     *
     * @param studyParticipantPreExistingCondition the study participant pre existing condition
     * @return the sAE report pre existing condition
     */
    public static SAEReportPreExistingCondition createSAEReportPreExistingCondition(StudyParticipantPreExistingCondition studyParticipantPreExistingCondition) {

        if (studyParticipantPreExistingCondition != null) {
            SAEReportPreExistingCondition saeReportPreExistingCondition = copy(studyParticipantPreExistingCondition);


            return saeReportPreExistingCondition;

        }
        return null;

    }

    /**
     * Copy.
     *
     * @param object the object
     * @return the sAE report pre existing condition
     */
    private static SAEReportPreExistingCondition copy(Object object) {
        SAEReportPreExistingCondition saeReportPreExistingCondition = new SAEReportPreExistingCondition();
        BeanUtils.copyProperties(object, saeReportPreExistingCondition, new String[]{"id", "gridId",
                "version", "report"});
        return saeReportPreExistingCondition;
    }


    /**
     * Copy.
     *
     * @return the sAE report pre existing condition
     */
    public SAEReportPreExistingCondition copy() {
        return copy(this);
    }
}
