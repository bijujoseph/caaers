package gov.nih.nci.cabig.caaers.web.ae;

import org.springframework.beans.BeanWrapper;
import org.springframework.validation.Errors;

import java.util.Map;
import java.util.ListIterator;

import gov.nih.nci.cabig.caaers.domain.AdverseEventPriorTherapy;
import gov.nih.nci.cabig.caaers.domain.expeditedfields.ExpeditedReportSection;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroup;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroupMap;
import gov.nih.nci.cabig.caaers.web.fields.RepeatingFieldGroupFactory;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldFactory;

/**
 * @author Rhett Sutphin
 */
public class PriorTherapyTab extends AeTab {

    public PriorTherapyTab() {
        super("Prior Therapies", "Prior Therapies", "ae/priorTherapies");

    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, InputFieldGroup> createFieldGroups(ExpeditedAdverseEventInputCommand command) {
    	//-
    	RepeatingFieldGroupFactory fieldFactory = new RepeatingFieldGroupFactory("priorTherapy", "aeReport.adverseEventPriorTherapies");
        fieldFactory.setDisplayNameCreator(new RepeatingFieldGroupFactory.DisplayNameCreator() {
            public String createDisplayName(int index) {
                return "Prior Therpy " + (index + 1);
            }
        });
        fieldFactory.addField(InputFieldFactory.createAutocompleterField("priorTherapy", "Therapy", false));
        fieldFactory.addField(InputFieldFactory.createTextField("other", "Other", false));
        fieldFactory.addField(InputFieldFactory.createDateField("startDate", "Start Date", false));
        fieldFactory.addField(InputFieldFactory.createDateField("endDate", "End Date", false));
        fieldFactory.addField(InputFieldFactory.createAutocompleterField("priorTherapyAgents.agent", "Agent", false));

        RepeatingFieldGroupFactory agentFactory = new RepeatingFieldGroupFactory("ptAgent", "aeReport.adverseEventPriorTherapies");
        //agentFactory.addField(new AutocompleterField("agent", "Agent", false));

    	//-
        InputFieldGroupMap groups = new InputFieldGroupMap();
        groups.addRepeatingFieldGroupFactory(fieldFactory, command.getAeReport().getAdverseEventPriorTherapies().size());
        groups.addRepeatingFieldGroupFactory(agentFactory);
        return groups;
    }

    @Override
    protected void validate(
        ExpeditedAdverseEventInputCommand command, BeanWrapper commandBean,
        Map<String, InputFieldGroup> fieldGroups, Errors errors
    ) {
        for (ListIterator<AdverseEventPriorTherapy> it = command.getAeReport().getAdverseEventPriorTherapies().listIterator(); it.hasNext();) {
            AdverseEventPriorTherapy aePriorTherapy = it.next();
            validatePriorTherapy(aePriorTherapy, it.previousIndex(), errors);
        }
    }

    private void validatePriorTherapy(AdverseEventPriorTherapy aePriorTherapy, int index, Errors errors) {
        if (aePriorTherapy.getPriorTherapy() == null && aePriorTherapy.getOther() == null) {
            errors.rejectValue(
                String.format("aeReport.adverseEventPriorTherapies[%d]", index),
                "REQUIRED",
                "Either Prior Therapy or Other is required"
            );
        }
    }
    @Override
    public ExpeditedReportSection section() {
    	return ExpeditedReportSection.PRIOR_THERAPIES_SECTION;
    }
}
