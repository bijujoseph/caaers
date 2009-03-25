package gov.nih.nci.cabig.caaers.dao;

import gov.nih.nci.cabig.caaers.dao.query.OrganizationQuery;
import gov.nih.nci.cabig.caaers.domain.Organization;
import gov.nih.nci.cabig.ctms.dao.MutableDomainObjectDao;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class implements the Data access related operations for the Organization domain object.
 * 
 * @author Padmaja Vedula
 * @author Rhett Sutphin
 */
@Transactional(readOnly = true)
public class OrganizationDao extends GridIdentifiableDao<Organization> implements
                MutableDomainObjectDao<Organization> {

    private static final List<String> SUBSTRING_MATCH_PROPERTIES = Arrays.asList("name","nciInstituteCode");

    private static final List<String> EXACT_MATCH_PROPERTIES = Collections.emptyList();
    
    public void initialize(Organization org){
    	getHibernateTemplate().initialize(org);
    }
    public void lock(Organization org){
    	getHibernateTemplate().lock(org, LockMode.NONE);
    }
    /**
     * Get the Class representation of the domain object that this DAO is representing.
     * 
     * @return Class representation of the domain object that this DAO is representing.
     */
    @Override
    public Class<Organization> domainClass() {
        return Organization.class;
    }

    /**
     * Get the default organization.
     * 
     * @return The default organization.
     */
    public Organization getDefaultOrganization() {
        List<Organization> results = getHibernateTemplate().find("from Organization where name=?",
                        Organization.DEFAULT_SITE_NAME);
        if (results.size() > 0) {
        	return results.get(0);
        }
        return null;
    }

    /**
     * Get the list of all organizations.
     * 
     * @return return the list of organizations.
     */
    public List<Organization> getAll() {
        return getHibernateTemplate().find("from Organization");
    }

    /**
     * Get organization given organization name.
     * 
     * @param name
     *                The name of the organization.
     * @return The organization.
     */
    public Organization getByName(final String name) {
        List<Organization> results = getHibernateTemplate().find("from Organization where name= ?",
                        name);
        return results.size() > 0 ? results.get(0) : null;
    }
    
    /**
     * Get organization given organization name.
     * 
     * @param name
     *                The name of the organization.
     * @return The organization.
     */
    public Organization getByNCIcode(final String code) {
        List<Organization> results = getHibernateTemplate().find("from Organization where nci_institute_code = ?",
                        code);
        return results.size() > 0 ? results.get(0) : null;
    }
    
    /**
     * Get the list of organizations matching the name fragments.
     * 
     * @param subnames
     *                the name fragments to search on.
     * @return List of matching organizations.
     */
    public List<Organization> getBySubnames(final String[] subnames) {
        return findBySubname(subnames, SUBSTRING_MATCH_PROPERTIES, EXACT_MATCH_PROPERTIES);
    }

    /**
     * Get the list of organizations matching the name fragments.
     * 
     * @param subnames
     *                the name fragments to search on.
     * @return List of matching organizations.
     */
    public List<Organization> restrictBySubnames(final String[] subnames) {
    	return getBySubnames(subnames);
        //return findBySubname(subnames, SUBSTRING_MATCH_PROPERTIES, EXACT_MATCH_PROPERTIES);
    }
    
    /**
     * Save or update the organization in the db.
     * 
     * @param organization
     *                the organization
     */
    @Transactional(readOnly = false)
    public void save(final Organization organization) {
        getHibernateTemplate().saveOrUpdate(organization);
    }

    /**
     * Get list of organizations having study sites.
     * 
     * @return The list of organizations.
     */
    @SuppressWarnings("unchecked")
    public List<Organization> getOrganizationsHavingStudySites() {

        return getHibernateTemplate().find("select distinct ss.organization from StudySite ss order by ss.organization.name");
    }

    /**
     * Search for organizations using query
     * 
     * @param query
     *                The query for finding organizations.
     * @return The list of organizations.
     */
    @SuppressWarnings( { "unchecked" })
    public List<Organization> searchOrganization(final OrganizationQuery query) {
        String queryString = query.getQueryString();
        log.debug("::: " + queryString);
        return (List<Organization>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(final Session session) throws HibernateException,
                            SQLException {
                org.hibernate.Query hiberanteQuery = session.createQuery(query.getQueryString());
                Map<String, Object> queryParameterMap = query.getParameterMap();
                for (String key : queryParameterMap.keySet()) {
                    Object value = queryParameterMap.get(key);
                    hiberanteQuery.setParameter(key, value);

                }
                return hiberanteQuery.list();
            }

        });

    }

}