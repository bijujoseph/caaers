/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.dao;

import gov.nih.nci.cabig.caaers.dao.query.ResearchStaffQuery;
import gov.nih.nci.cabig.caaers.dao.query.SiteResearchStaffQuery;
import gov.nih.nci.cabig.caaers.domain.LocalResearchStaff;
import gov.nih.nci.cabig.caaers.domain.RemoteResearchStaff;
import gov.nih.nci.cabig.caaers.domain.ResearchStaff;
import gov.nih.nci.cabig.caaers.domain.SiteResearchStaff;
import gov.nih.nci.cabig.ctms.dao.MutableDomainObjectDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.semanticbits.coppa.infrastructure.RemoteSession;

/**
 * This class implements the Data access related operations for the ResearchStaff domain object.
 * 
 * @author Kulasekaran
 */
@Transactional(readOnly = true)
public class ResearchStaffDao extends GridIdentifiableDao<ResearchStaff> implements MutableDomainObjectDao<ResearchStaff> {

    private static final List<String> SUBSTRING_MATCH_PROPERTIES = Arrays.asList("firstName", "lastName");
    private static final List<String> NCIIDENTIFIER_MATCH_PROPERTIES = Arrays.asList("nciIdentifier");
    private static final List<String> EXACT_MATCH_PROPERTIES = Collections.emptyList();
    private static final List<Object> EXTRA_PARAMS = Collections.emptyList();
    private RemoteSession remoteSession;
    
    /**
     * Get the Class representation of the domain object that this DAO is representing.
     * 
     * @return Class representation of the domain object that this DAO is representing.
     */
    @Override
    @Transactional(readOnly = true, propagation= Propagation.NOT_SUPPORTED)
    public Class<ResearchStaff> domainClass() {
        return ResearchStaff.class;
    }

    /**
     * Save or update the research staff in the db.
     * 
     * @param The
     *                research staff.
     */
    @Transactional(readOnly = false)
    public void save(ResearchStaff researchStaff) {
    	if(researchStaff.getId() == null && researchStaff.getNciIdentifier() != null  && researchStaff instanceof LocalResearchStaff){
    		ResearchStaff rs = new RemoteResearchStaff();
    		rs.setNciIdentifier(researchStaff.getNciIdentifier());
    		List<ResearchStaff> remoteResearchStaffs = getRemoteResearchStaff(rs);
    		if(remoteResearchStaffs != null && remoteResearchStaffs.size() > 0){
    			logger.error("ResearchStaff exists in external system");
    			throw new RuntimeException("ResearchStaff exists in external system");
    		}
    	}
    	getHibernateTemplate().saveOrUpdate(researchStaff);
    }
    
    @Override
    public ResearchStaff merge(ResearchStaff staff) {
    	ResearchStaff mergedStaff =  super.merge(staff);
    	//copy the user groups
    	//mergedStaff.setUserGroupTypes(staff.getUserGroupTypes());
    	return mergedStaff;
    }
    
    /**
     * This method queries the caAERS DB to get all the matching ResearchStaff for the given query.
     * @param query
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = false)
	public List<ResearchStaff> getLocalResearchStaff(final ResearchStaffQuery query){
    	String queryString = query.getQueryString();
        log.debug("::: " + queryString.toString());
        List<ResearchStaff> researchStaffs = (List<ResearchStaff>) super.search(query);
        return researchStaffs;
    }
    
    /**
     * This method queries the caAERS DB to get all the matching SiteResearchStaff for the given query.
     * @param query
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = false)
	public List<SiteResearchStaff> getSiteResearchStaff(final SiteResearchStaffQuery query){
    	String queryString = query.getQueryString();
        log.debug("::: " + queryString.toString());
        List<SiteResearchStaff> siteResearchStaffs = (List<SiteResearchStaff>) super.search(query);
        return siteResearchStaffs;
    }

    /**
     * This method queries the external system to fetch all the matching ResearchStaff
     * @param researchStaff
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<ResearchStaff> getRemoteResearchStaff(final ResearchStaff researchStaff){
    	List<ResearchStaff> remoteResearchStaffs = new ArrayList<ResearchStaff>();
    	try {
			remoteResearchStaffs = (List)remoteSession.find(researchStaff);
		} catch (Exception e) {
			log.warn("Error while invoking COPPA", e);
		}
    	return remoteResearchStaffs;
    }
    
    /**
     * Search for research staffs using query.
     * 
     * @param query
     *                The query used to search for research staffs
     * @return The list of research staffs.
     */
    @SuppressWarnings( { "unchecked" })
    public List<ResearchStaff> searchResearchStaff(final ResearchStaffQuery query) {
       String queryString = query.getQueryString();
        log.debug("::: " + queryString.toString());
        List<ResearchStaff> researchStaffs = (List<ResearchStaff>) super.search(query);
        return researchStaffs;
    }
    
    /**
     * Get the list of research staffs matching the name fragments and belonging to specified site.
     * 
     * @param subnames
     *                the name fragments to search on.
     * @param site
     *                The organization ID of the site.
     * @return List of matching research staffs.
     */
    @Transactional(readOnly = false)
    public List<ResearchStaff> getBySubnames(final String[] subnames, final int site) {
    	List<ResearchStaff> researchStaffs =  findBySubname(subnames, SUBSTRING_MATCH_PROPERTIES, EXACT_MATCH_PROPERTIES);
    	return researchStaffs;
    }
    
    /**
     * Get the list of research staffs matching the NciIdentifier and belonging to specified site.
     * 
     * @param subnames
     *                the name fragments to search on.
     * @param site
     *                The organization ID of the site.
     * @return List of matching research staffs.
     */
    @Transactional(readOnly = false)
    public List<ResearchStaff> getByNciIdentifier(final String[] subnames, final int site) {
    	List<ResearchStaff> researchStaffs = findBySubname(subnames, NCIIDENTIFIER_MATCH_PROPERTIES, EXACT_MATCH_PROPERTIES);
    	return researchStaffs;
    }

    /**
     * Get the user who has specified email address.
     * 
     * @param loginId
     *                The loginId of the user.
     * @return The user.
     */
    @SuppressWarnings("unchecked")
	public ResearchStaff getByLoginId(String loginId) {
        List<ResearchStaff> results = getHibernateTemplate().find("from ResearchStaff rs where rs.caaersUser.loginName= ?", loginId);
        return results.size() > 0 ? results.get(0) : null;
    }

    @SuppressWarnings("unchecked")
	public ResearchStaff getByEmailAddress(String email) {
        List<ResearchStaff> results = getHibernateTemplate().find("from ResearchStaff where emailAddress= ?", email);
        return results.size() > 0 ? results.get(0) : null;
    }
    
    @SuppressWarnings("unchecked")
	public ResearchStaff getByNciIdentfier(String nciId) {
    	List<ResearchStaff> results = getHibernateTemplate().find("from ResearchStaff where lower(nciIdentifier)= ?", nciId.toLowerCase());
        return results.size() > 0 ? results.get(0) : null;
    }
    
    @SuppressWarnings("unchecked")
	public ResearchStaff getByExternalId(String externalId) {
    	List<ResearchStaff> results = getHibernateTemplate().find("from ResearchStaff where lower(externalId)= ?", externalId.toLowerCase());
        return results.size() > 0 ? results.get(0) : null;
    }

    /**
     * Will deactivate all the StudyPersonnel associated with this SiteResearchStaff
     * @param siteResearchStaff
     */
    @Transactional(readOnly = false)
    public void deactivateStudyPersonnel(SiteResearchStaff siteResearchStaff){

        if(siteResearchStaff.getStartDate() == null){
            getHibernateTemplate().bulkUpdate("update StudyPersonnel sp set sp.startDate = null where sp.siteResearchStaff.id = ?", new Object[]{
                 siteResearchStaff.getId()
            });
        }else{
            getHibernateTemplate().bulkUpdate("update StudyPersonnel sp set sp.startDate = ? where sp.siteResearchStaff.id = ?", new Object[]{
                siteResearchStaff.getStartDate(), siteResearchStaff.getId()
            });
        }

        if(siteResearchStaff.getEndDate() == null){
           getHibernateTemplate().bulkUpdate("update StudyPersonnel sp set sp.endDate = null where sp.siteResearchStaff.id = ?", new Object[]{
                 siteResearchStaff.getId()
           });
        }else{
           getHibernateTemplate().bulkUpdate("update StudyPersonnel sp set sp.endDate = ? where sp.siteResearchStaff.id = ? and sp.endDate > ?", new Object[]{
                siteResearchStaff.getEndDate(), siteResearchStaff.getId(), siteResearchStaff.getEndDate()
           });
        }

        
    }
    
    
	public void setRemoteSession(RemoteSession remoteSession) {
		this.remoteSession = remoteSession;
	}

}
