/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.domain.repository;

import gov.nih.nci.cabig.caaers.domain.ConfigProperty;
import gov.nih.nci.cabig.caaers.domain.ConfigPropertyType;
import java.util.List;

 
/**
 * User: Ion
 * Date: Jun 19, 2009
 * Time: 12:04:40 PM.
 */
public interface ConfigPropertyRepository {
    
    /**
     * Gets the by type and code.
     *
     * @param cpType the cp type
     * @param cpCode the cp code
     * @return the by type and code
     */
    public ConfigProperty getByTypeAndCode(ConfigPropertyType cpType, String cpCode);
    
    /**
     * Gets the by type.
     *
     * @param cpType the cp type
     * @return the by type
     */
    public List<ConfigProperty> getByType(ConfigPropertyType cpType);

    public void saveOrUpdate(ConfigProperty configProperty);

    public void removeAll(ConfigPropertyType cpType);
}
