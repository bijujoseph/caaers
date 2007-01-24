package gov.nih.nci.cabig.caaers.dao;


import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;

import edu.nwu.bioinformatics.commons.CollectionUtils;
import gov.nih.nci.cabig.caaers.CaaersSystemException;
import gov.nih.nci.cabig.caaers.domain.DomainObject;
import gov.nih.nci.cabig.caaers.domain.GridIdentifiable;

/**
 * 
 * @author Sujith Vellat Thayyilthodi
 * */
public abstract class GridIdentifiableDao<T extends DomainObject & GridIdentifiable> extends CaaersDao<T>{
    
/*	
	//Implementation taken from Study Calendar code. Needs to know why Study was hardcoded.
    public T getByGridId(String gridId) {
        Study example = new Study();
        example.setGridId(gridId);
        return (T) CollectionUtils.firstElement(getHibernateTemplate().findByExample(example));
    }*/

    @SuppressWarnings("unchecked")
	public T getByGridId(T template) {
    	return (T) CollectionUtils.firstElement(getHibernateTemplate().findByExample(template));
    }
    
    @SuppressWarnings("unchecked")
	public T getByExample(T template, String[] propertyNames, DomainObject[] propertyValues) {
    	Criteria criteria = getSession().createCriteria(template.getClass()).add(Example.create(template));
    	for(int count = 0; count < propertyNames.length; count++) {
    		criteria.createCriteria(propertyNames[count]).add(Example.create(propertyValues[count]));
    	}
    	return (T) CollectionUtils.firstElement(criteria.list());
    }

/*    
  	//Fetches the object using example object if a grid id is specified..
  	public T getByGridId(String gridId) {
    	try {
    		Object obj = domainClass().newInstance();
    		PropertyUtils.setProperty(obj, "gridId", gridId);
    		return getByGridId((T)obj);
    	} catch (Exception e) {
    		throw new CaaersSystemException(e.getMessage(), e);
    	}
    }
*/
    public T getByGridId(String gridId) {
        StringBuilder query = new StringBuilder("from ")
        .append(domainClass().getName()).append(" o where gridId = ?");
        Object[] params = {gridId};
        return (T) CollectionUtils.firstElement(getHibernateTemplate().find(query.toString(), params));
    }    
    
}
