/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.domain;

import gov.nih.nci.cabig.caaers.utils.DateUtils;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

 
/**
 * This class represents the StudyPersonnel domain object associated with the Adverse event report.
 * 
 * @author Kulasekaran
 * @author Biju Joseph
 * @author Ion C. Olaru
 */
@Entity
@Table(name = "study_personnel")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_study_personnel_id") })
public class StudyPersonnel extends AbstractMutableRetireableDomainObject implements StudyOrganizationChild {

    /** The role code. */
    private String roleCode;
    
    /** The site research staff. */
    private SiteResearchStaff siteResearchStaff;
    
    /** The study organization. */
    private StudyOrganization studyOrganization;
    
    /** The start date. */
    private Date startDate;
    
    /** The end date. */
    private Date endDate;
    
    /**
     * This method will deactivate a user, by setting the termEndDate to a past date.
     */
    public void deactivate() {
    	this.endDate = DateUtils.yesterday();
    }
    
    /**
     *  This method will activate a {@link StudyPersonnel}
     *  This will check the active date of the associated {@link gov.nih.nci.cabig.caaers.domain.SiteResearchStaff}
     */
    public void activate(){
        if (getSiteResearchStaff().getStartDate().compareTo(DateUtils.today()) > 0) {
            this.setStartDate(getSiteResearchStaff().getStartDate());
        } else {
            this.setStartDate(DateUtils.today());
        }
        this.setEndDate(getSiteResearchStaff().getEndDate());
        this.endDate = null;
    }
    
    /**
     * Gets the site research staff.
     *
     * @return the site research staff
     */
    @ManyToOne
    @JoinColumn(name = "site_research_staffs_id")
	public SiteResearchStaff getSiteResearchStaff() {
		return siteResearchStaff;
	}

	/**
	 * Sets the site research staff.
	 *
	 * @param siteResearchStaff the new site research staff
	 */
	public void setSiteResearchStaff(SiteResearchStaff siteResearchStaff) {
		this.siteResearchStaff = siteResearchStaff;
	}

    /* (non-Javadoc)
     * @see gov.nih.nci.cabig.caaers.domain.StudyOrganizationChild#getStudyOrganization()
     */
    @ManyToOne
    @JoinColumn(name = "study_sites_id")
    public StudyOrganization getStudyOrganization() {
        return studyOrganization;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cabig.caaers.domain.StudyOrganizationChild#setStudyOrganization(gov.nih.nci.cabig.caaers.domain.StudyOrganization)
     */
    public void setStudyOrganization(StudyOrganization studyOrganization) {
        this.studyOrganization = studyOrganization;
    }

    /**
     * Gets the role code.
     *
     * @return the role code
     */
    @Column(name = "role_code")
    public String getRoleCode() {
        return roleCode;
    }

    /**
     * Sets the role code.
     *
     * @param roleCode the new role code
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    /**
     * Gets the start date.
     *
     * @return the start date
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="start_date")
    public Date getStartDate() {
		return startDate;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cabig.caaers.domain.StudyOrganizationChild#setStartDate(java.util.Date)
	 */
	public void setStartDate(Date termStartDate) {
		this.startDate = termStartDate;
	}
	
	/**
	 * Gets the end date.
	 *
	 * @return the end date
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="end_date")
	public Date getEndDate() {
		return endDate;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cabig.caaers.domain.StudyOrganizationChild#setEndDate(java.util.Date)
	 */
	public void setEndDate(Date termEndDate) {
		this.endDate = termEndDate;
	}

	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	@Transient
    public boolean isActive(){
    	return (startDate != null && DateUtils.between(new Date(), startDate, endDate));
    }
    

    /**
     * Checks if is in active.
     *
     * @return true, if is in active
     */
    @Transient
    public boolean isInActive(){
    	return (startDate == null || !DateUtils.between(new Date(), startDate, endDate));
    }
    
    /**
     * Returns the email address associtated to {@link SiteResearchStaff}.
     * If email addres not found in {@link SiteResearchStaff}, {@link ResearchStaff}'s one is returned.
     *
     * @return the email address
     */
    @Transient
    public String getEmailAddress(){
    	String email = null;
    	if(getSiteResearchStaff() != null){
    		email = siteResearchStaff.getEmailAddress();
    		if(email == null) {
    			email = siteResearchStaff.getResearchStaff().getEmailAddress();
    		}
    	}
    	return email;
    }
    
    /**
     * Returns the phone number associated to {@link SiteResearchStaff}.
     * If the phone number not found in {@link SiteResearchStaff}, {@link ResearchStaff}'s one is returned.
     *
     * @return the phone number
     */
    @Transient
    public String getPhoneNumber(){
    	String phoneNumber = null;
    	if(getSiteResearchStaff() != null){
    		phoneNumber = siteResearchStaff.getPhoneNumber();
    		if(phoneNumber == null){
    			phoneNumber = siteResearchStaff.getResearchStaff().getPhoneNumber();
    		}
    	}
    	return phoneNumber;
    }
    
    /**
     * Returns the fax number associated to {@link SiteResearchStaff}
     * If the fax number not found in {@link SiteResearchStaff}, {@link ResearchStaff}'s one is returned.
     *
     * @return the fax number
     */
    @Transient
    public String getFaxNumber(){
    	String faxNumber = null;
    	if(getSiteResearchStaff() != null){
    		faxNumber = siteResearchStaff.getFaxNumber();
    		if(faxNumber == null){
    			faxNumber = siteResearchStaff.getResearchStaff().getFaxNumber();
    		}
    	}
    	return faxNumber;
    }

    // /OBJECT METHODS
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((siteResearchStaff == null) ? 0 : siteResearchStaff.hashCode());
        result = prime * result + ((roleCode == null) ? 0 : roleCode.hashCode());
        result = prime * result + ((studyOrganization == null) ? 0 : studyOrganization.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        
        final StudyPersonnel other = (StudyPersonnel) obj;
        
        if(this.isRetired() || other.isRetired()) return false;
        if (siteResearchStaff == null) {
            if (other.siteResearchStaff != null) return false;
        } else if (!siteResearchStaff.equals(other.siteResearchStaff)) return false;
        if (roleCode == null) {
            if (other.roleCode != null) return false;
        } else if (!roleCode.equals(other.roleCode)) return false;
        if (studyOrganization == null) {
            if (other.studyOrganization != null) return false;
        } else if (!studyOrganization.equals(other.studyOrganization)) return false;
        return true;
    }
    
    
    /**
     * This method will take care of synchronizing the start and end dates.
     *  By copying it from {@link SiteResearchStaff}
     */
    public void syncDates(){
    	if(siteResearchStaff == null) return;
    	
    	startDate = siteResearchStaff.getStartDate();
    	endDate = siteResearchStaff.getEndDate();
    }    
}
