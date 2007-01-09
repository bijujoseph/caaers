package gov.nih.nci.cabig.caaers.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Column;

/**
 * @author Rhett Sutphin
 */
@Entity
public class CtcTerm extends AbstractImmutableDomainObject {
    private String term;
    private String select;
    private String ctepTerm;
    private Integer ctepCode;
    private CtcCategory category;

    ////// BEAN PROPERTIES

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    @Column(name = "select_ae")
    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getCtepTerm() {
        return ctepTerm;
    }

    public void setCtepTerm(String ctepTerm) {
        this.ctepTerm = ctepTerm;
    }

    public Integer getCtepCode() {
        return ctepCode;
    }

    public void setCtepCode(Integer ctepCode) {
        this.ctepCode = ctepCode;
    }

    @ManyToOne
    public CtcCategory getCategory() {
        return category;
    }

    public void setCategory(CtcCategory category) {
        this.category = category;
    }
}
