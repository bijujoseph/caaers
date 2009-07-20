package gov.nih.nci.cabig.caaers.domain;

import gov.nih.nci.cabig.ctms.domain.DomainObject;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author Krikor Krumlian
 * @author Biju Joseph
 */

@Entity
@Table(name = "study_diseases")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "term_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("ABSTRACT_TERM")
// should be ignored
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_study_diseases_id") })
public abstract class AbstractStudyDisease<T extends DomainObject> extends AbstractMutableRetireableDomainObject implements Serializable {
  
	private static final long serialVersionUID = 4312939165182797807L;
	private T term;
    private Study study;

    // //// BEAN PROPERTIES

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    @Cascade(value = {CascadeType.EVICT})
    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

   
    /*
     * this is only transient here -- subclasses need to override it and specify what it refers to
     * This should work: @ManyToOne @JoinColumn(name = "cause_id", nullable = false)
     */
    @Transient
    public T getTerm() {
        return term;
    }

    public void setTerm(T term) {
        this.term = term;
    }

    @Transient
    public abstract String getTermName();

    @Transient
    public void setTermName(String name) {
    }
}
