package gov.nih.nci.cabig.caaers.accesscontrol.query.impl;

import gov.nih.nci.cabig.caaers.dao.query.HQLQuery;

import java.util.List;

import com.semanticbits.security.contentfilter.IdFetcher;


public class CaaersResearchStaffIdFetcherImpl extends AbstractIdFetcher implements IdFetcher {
	
	public List fetch(String loginId){
		// get all organizations user has access to .. 
		List<Integer> organizationIds = getAccesibleOrganizationsIncludingStudySites(loginId);
		StringBuilder hql = new StringBuilder("select distinct srs.researchStaff.id from  SiteResearchStaff srs" );
        hql.append(" where srs.organization.id in (:organizationIds) ");
		
        HQLQuery query = new HQLQuery(hql.toString());
        query.setParameterList("organizationIds", organizationIds);
        List resultList = (List<Integer>) search(query);
       
		return resultList;
	}
 
}
