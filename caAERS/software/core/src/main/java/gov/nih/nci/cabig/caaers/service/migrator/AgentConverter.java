/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.service.migrator;

import gov.nih.nci.cabig.caaers.domain.Agent;
import gov.nih.nci.cabig.caaers.integration.schema.common.ActiveInactiveStatusType;
import gov.nih.nci.cabig.caaers.integration.schema.common.AgentType;

/**
 * @author Ion C. Olaru
 *         Date: 4/3/12 -11:49 AM
 */
public class AgentConverter{

    public static Agent convert(AgentType at) {
        Agent a = new Agent();
        a.setName(at.getName());
        a.setNscNumber(at.getNscNumber());
        a.setDescription(at.getDescriptionText());
        if (at.getStatus() != null) {
            a.setRetiredIndicator(at.getStatus() == ActiveInactiveStatusType.INACTIVE);
        }
        return a;
    }

    public static AgentType convert(Agent a) {
        AgentType at = new AgentType();
        at.setName(a.getName());
        at.setNscNumber(a.getNscNumber());
        at.setDescriptionText(a.getDescription());
        at.setStatus(a.getRetiredIndicator() ? ActiveInactiveStatusType.INACTIVE : ActiveInactiveStatusType.ACTIVE);
        return at;
    }

}
