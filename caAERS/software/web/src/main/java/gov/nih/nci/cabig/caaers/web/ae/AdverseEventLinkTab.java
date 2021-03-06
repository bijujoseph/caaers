/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.web.ae;

import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroup;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroupMap;
import gov.nih.nci.cabig.caaers.web.fields.TabWithFields;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

/**
 * @author: Biju Joseph
 */
public class AdverseEventLinkTab extends TabWithFields<AdverseEventReconciliationCommand>{

    public AdverseEventLinkTab(String longTitle, String shortTitle, String viewName) {
        super(longTitle, shortTitle, viewName);
    }

    @Override
    public Map<String, Object> referenceData(HttpServletRequest request, AdverseEventReconciliationCommand command) {
        Map<String, Object> refData =  super.referenceData(request, command);
        if(command.isNoExternalAes()){
            refData.put("warningMessage", messageSource.getMessage("reconciliation.no.external.ae", new Object[]{}, Locale.getDefault()));
        }

        return refData;
    }

    @Override
    public Map<String, InputFieldGroup> createFieldGroups(AdverseEventReconciliationCommand command) {
        InputFieldGroupMap map = new InputFieldGroupMap();
        return map;
    }

    /**
     * Invoked after successful binding and validation.
     */
    @Override
    public void postProcess(HttpServletRequest request, AdverseEventReconciliationCommand command, Errors errors) {
        if(errors.hasErrors()) return;

        command.processExternalAeRejections();
        command.processUnmappedExternalAes();
        command.processUnmappedInternalAes();
        command.processAeMapping();
        command.seralizeMapping();
    }
}
