package gov.nih.nci.cabig.caaers.dao;

import gov.nih.nci.cabig.caaers.domain.DomainObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * @author Rhett Sutphin
 */
/* TODO: much of this class is shared with PSC.  Refactor into a shared library. */
public abstract class CaaersDao<T extends DomainObject> extends HibernateDaoSupport {
    protected final Log log = LogFactory.getLog(getClass());

    @SuppressWarnings("unchecked")
    public T getById(int id) {
        return (T) getHibernateTemplate().get(domainClass(), id);
    }

    protected List<T> findBySubname(
        String[] subnames, List<String> substringMatchProperties, List<String> exactMatchProperties
    ) {
        return findBySubname(subnames, null, null, substringMatchProperties, exactMatchProperties);
    }

    @SuppressWarnings("unchecked")
    protected List<T> findBySubname(
        String[] subnames, String extraConditions, List<Object> extraParameters,
        List<String> substringMatchProperties, List<String> exactMatchProperties
    ) {
        StringBuilder query = new StringBuilder("from ")
            .append(domainClass().getName()).append(" o where ");
        if (extraConditions != null) query.append(extraConditions).append(" and ");
        List<Object> params = new LinkedList<Object>();
        if (extraParameters != null) params.addAll(extraParameters);

        for (int i = 0; i < subnames.length; i++) {
            buildSubnameQuery(subnames[i], query, params,
                substringMatchProperties, exactMatchProperties);
            if (i < subnames.length - 1) query.append(" and ");
        }
        
        return getHibernateTemplate().find(query.toString(), params.toArray());
    }

    private void buildSubnameQuery(
        String subname, StringBuilder query, List<Object> params,
        List<String> substringMatchProperties, List<String> exactMatchProperties
    ) {
        query.append('(');
        if (hasAny(substringMatchProperties)) {
            for (Iterator<String> it = substringMatchProperties.iterator(); it.hasNext();) {
                String prop = it.next();
                query.append("LOWER(o.").append(prop).append(") LIKE ?");
                params.add('%' + subname.toLowerCase() + '%');
                if (it.hasNext()) query.append(" or ");
            }
            if (hasAny(exactMatchProperties)) {
                query.append(" or ");
            }
        }
        if (hasAny(exactMatchProperties)) {
            for (Iterator<String> it = exactMatchProperties.iterator(); it.hasNext();) {
                String prop = it.next();
                query.append("LOWER(o.").append(prop).append(") = ?");
                params.add(subname.toLowerCase());
                if (it.hasNext()) query.append(" or ");
            }
        }
        query.append(')');
    }

    private boolean hasAny(List<String> substringMatchProperties) {
        return substringMatchProperties != null && substringMatchProperties.size() > 0;
    }

    public abstract Class<T> domainClass();
}
