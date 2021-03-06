/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.dao;

import gov.nih.nci.cabig.caaers.domain.PreExistingCondition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class implements the Data access related operations for the PreExistingCondition domain
 * object.
 * 
 * @author Krikor Krumlian
 */
@Transactional(readOnly=true)
public class PreExistingConditionDao extends CaaersDao<PreExistingCondition> {
    private static final List<String> SUBSTRING_MATCH_PROPERTIES = Arrays.asList("text",
                    "meddraLlt");

    private static final List<String> EXACT_MATCH_PROPERTIES = Collections.emptyList();

    /**
     * Get the Class representation of the domain object that this DAO is representing.
     * 
     * @return Class representation of the domain object that this DAO is representing.
     */
    @Transactional(readOnly = true, propagation= Propagation.NOT_SUPPORTED)
    public Class<PreExistingCondition> domainClass() {
        return PreExistingCondition.class;
    }

    /**
     * Get the list of pre-existing conditions matching the name fragments.
     * 
     * @param subnames
     *                the name fragments to search on.
     * @return List of matching pre-existing conditions.
     */
    public List<PreExistingCondition> getBySubnames(String[] subnames) {
        return findBySubname(subnames, SUBSTRING_MATCH_PROPERTIES, EXACT_MATCH_PROPERTIES);
    }

    /**
     * Get the list of all pre-existing conditions.
     * 
     * @return return the list of pre-existing conditions.
     */
    public List<PreExistingCondition> getAll() {
        return getHibernateTemplate().find("from PreExistingCondition pc order by lower(pc.text)");
    }
}
