package gov.nih.nci.cabig.caaers.accesscontrol;

import gov.nih.nci.cabig.caaers.dao.ResearchStaffDao;
import gov.nih.nci.cabig.caaers.dao.StudyDao;
import gov.nih.nci.cabig.caaers.dao.query.ResearchStaffQuery;
import gov.nih.nci.cabig.caaers.domain.Organization;
import gov.nih.nci.cabig.caaers.domain.ResearchStaff;
import gov.nih.nci.cabig.caaers.domain.Study;
import gov.nih.nci.cabig.caaers.domain.StudyOrganization;
import gov.nih.nci.cabig.caaers.domain.StudyPersonnel;
import gov.nih.nci.cabig.caaers.domain.StudySite;
import gov.nih.nci.cabig.caaers.domain.ajax.StudySearchableAjaxableDomainObject;
import gov.nih.nci.cabig.caaers.domain.ajax.StudySiteAjaxableDomainObject;

import java.util.Iterator;
import java.util.List;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.User;

public class StudySiteSecurityFilterer implements DomainObjectSecurityFilterer {
	
	private ResearchStaffDao researchStaffDao;
	private StudyDao studyDao;


	public Object filter(Authentication authentication, String permission, Object returnObject) {
		
		System.out.println("filtering from new classes ");
		
		//get user
		User user = (User)authentication.getPrincipal();
		
		//no filtering if super user
        GrantedAuthority[] grantedAuthorities = user.getAuthorities();
        for (int i=0; i<grantedAuthorities.length; i++) {
        	GrantedAuthority grantedAuthority = (GrantedAuthority)grantedAuthorities[i];
        	if ( grantedAuthority.getAuthority().equals("ROLE_caaers_super_user")) {
        		if (returnObject instanceof Filterer) {
        			return ((Filterer)returnObject).getFilteredObject();
        		} else {
        			return returnObject;
        		}
        		
        	}
        }
             
        
        // get research staff and associated organization.
		ResearchStaffQuery rsQuery = new ResearchStaffQuery();
    	rsQuery.filterByLoginId(user.getUsername());
        List<ResearchStaff> rsList = researchStaffDao.searchResearchStaff(rsQuery);
        
        ResearchStaff researchStaff = rsList.get(0);
        Organization organization = researchStaff.getOrganization();
        
		StudySiteAjaxableDomainObject studySite = new StudySiteAjaxableDomainObject();
		studySite.setNciInstituteCode(organization.getNciInstituteCode());
		
		boolean studyFilteringRequired = false ; 
		// study level restricted roles(SLRR) - AE Coordinator or Subject Coordinator 
        //check if user is  SLRR
        for (int i=0; i<grantedAuthorities.length; i++) {
        	GrantedAuthority grantedAuthority = (GrantedAuthority)grantedAuthorities[i];
        	if ( grantedAuthority.getAuthority().equals("ROLE_caaers_study_cd") || grantedAuthority.getAuthority().equals("ROLE_caaers_ae_cd")) {
        		studyFilteringRequired = true;
        		break;
        	}
        }
        
		boolean isAuthorizedOnThisStudy = true;
		
		if (returnObject instanceof StudySearchableAjaxableDomainObject) {			
			// study level filtering for SLRR
			if (studyFilteringRequired) {
				if (!isAuthorized(researchStaff.getId(),(StudySearchableAjaxableDomainObject)returnObject)) {
					isAuthorizedOnThisStudy=false;
				}
			}
			// if not SLRR , or SLRR is authorized , then apply site level filtering.
			if (isAuthorized(studySite,(StudySearchableAjaxableDomainObject)returnObject) && isAuthorizedOnThisStudy) {
				return returnObject;
			} else {
				return null;
			}
		}
		Filterer filterer = (Filterer)returnObject;
		Iterator collectionIter = filterer.iterator();
		
		while (collectionIter.hasNext()) {
        	Object domainObject = collectionIter.next();
        	StudySearchableAjaxableDomainObject study = (StudySearchableAjaxableDomainObject)domainObject;
        	isAuthorizedOnThisStudy = true;
        	// study level filtering for SLRR
			if (studyFilteringRequired) {
				if (!isAuthorized(researchStaff.getId(),study)) {
					isAuthorizedOnThisStudy=false;
				}
			}
			//if not SLRR , or SLRR is authorized , then apply site level filtering.
        	if (!isAuthorized(studySite,study) || !isAuthorizedOnThisStudy) {
        		filterer.remove(study);
        	}
        }
		
		return filterer.getFilteredObject();
	}
	private boolean isAuthorized(StudySiteAjaxableDomainObject studySite, StudySearchableAjaxableDomainObject study) {
		// check if user is part of co-ordinating center 
		if (studySite.getNciInstituteCode().equals(study.getCoordinatingCenterCode())) return true;
		
		//if not co-ordinating center check for study sites.
		//if (study.getStudySites().contains(studySite)) return true;
		// Query is not doing outer join if particpant id is passed , need to fix the query. 
		// temp fix is getting study object for DAO.		
		Study s = studyDao.getById(study.getId()) ;
		List<StudyOrganization> soList = s.getStudyOrganizations();
		for (StudyOrganization so:soList) {
			if (so instanceof StudySite) {
				if (studySite.getNciInstituteCode().equals(so.getOrganization().getNciInstituteCode())) {
					return true;
				}
			}			
		}
		return false;
	}
	private boolean isAuthorized(Integer researchStaffId, StudySearchableAjaxableDomainObject study) {
		// TODO
		// Query is not doing outer join on research staff , need to fix the query. 
		// temp fix is getting study object for DAO.
		//if (study.getStudyPersonnelIds().contains(researchStaffId)) return true;
		
		Study s = studyDao.getById(study.getId()) ;
		List<StudyOrganization> soList = s.getStudyOrganizations();
		for (StudyOrganization so:soList) {
			List<StudyPersonnel> spList = so.getStudyPersonnels();
			for (StudyPersonnel sp:spList) {
				if (sp.getResearchStaff().getId().equals(researchStaffId)) {
					return true;
				}
			}
		}
		return false;
	}
	public void setResearchStaffDao(ResearchStaffDao researchStaffDao) {
		this.researchStaffDao = researchStaffDao;
	}
	public void setStudyDao(StudyDao studyDao) {
		this.studyDao = studyDao;
	}

}
