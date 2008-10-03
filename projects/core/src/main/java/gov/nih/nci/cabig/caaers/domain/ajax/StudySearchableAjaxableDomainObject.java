package gov.nih.nci.cabig.caaers.domain.ajax;

import gov.nih.nci.cabig.caaers.domain.StudySite;

import java.util.List;
import java.util.ArrayList;


/**
 *
 */
public class StudySearchableAjaxableDomainObject extends StudyAjaxableDomainObject {


    private String shortTitle;
    private String primaryIdentifierValue;
    private String primarySponsorCode;

    private String status;
    private String phaseCode;

    List<StudySiteAjaxableDomainObject> studySites = new ArrayList<StudySiteAjaxableDomainObject>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhaseCode() {
        return phaseCode;
    }

    public void setPhaseCode(String phaseCode) {
        this.phaseCode = phaseCode;
    }

    public String getPrimarySponsorCode() {
        return primarySponsorCode;
    }

    public void setPrimarySponsorCode(String primarySponsorCode) {
        this.primarySponsorCode = primarySponsorCode;
    }

    protected AbstractAjaxableDomainObject getObjectById(List<? extends AbstractAjaxableDomainObject> ajaxableDomainObjects,
                                                         Integer id) {

        for (AbstractAjaxableDomainObject object : ajaxableDomainObjects) {
            if (object.getId().equals(id)) {
                return object;
            }
        }

        return null;
    }

    public void addStudySite(StudySiteAjaxableDomainObject studySiteAjaxableDomainObject) {
        if (getObjectById(this.getStudySites(), studySiteAjaxableDomainObject.getId()) == null) {
            getStudySites().add(studySiteAjaxableDomainObject);
        }

    }

    public List<StudySiteAjaxableDomainObject> getStudySites() {
        return studySites;
    }

    public String getDisplayName() {

        String primaryIdentifier = this.getPrimaryIdentifierValue() == null ? "" :
                " ( " + this.getPrimaryIdentifierValue() + " ) ";
        return this.getShortTitle() + primaryIdentifier;

    }


    public String getPrimaryIdentifierValue() {
        return primaryIdentifierValue;
    }

    public void setPrimaryIdentifierValue(String primaryIdentifierValue) {
        this.primaryIdentifierValue = primaryIdentifierValue;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }
}