package gov.nih.nci.cabig.caaers.web.ae;

import gov.nih.nci.cabig.caaers.domain.expeditedfields.ExpeditedReportSection;
import gov.nih.nci.cabig.caaers.web.fields.InputField;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldAttributes;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroupMap;
import gov.nih.nci.cabig.caaers.web.fields.RepeatingFieldGroupFactory;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldFactory;

/**
 * @author Rhett Sutphin
 */
public class ConcomitantMedicationsTab extends AeTab {

    public ConcomitantMedicationsTab() {
        super("Concomitant medications", "Conmeds", "ae/conMed");
    }

    @Override
    @SuppressWarnings("unchecked")
    public InputFieldGroupMap createFieldGroups(ExpeditedAdverseEventInputCommand command) {
    	//-
        RepeatingFieldGroupFactory fieldFactory = new RepeatingFieldGroupFactory("conmed", "aeReport.concomitantMedications");
        fieldFactory.setDisplayNameCreator(new RepeatingFieldGroupFactory.DisplayNameCreator() {
            public String createDisplayName(int index) {
                return "Medication " + (index + 1);
            }
        });
        InputField agentNameField = InputFieldFactory.createTextField("agentName", "Information about concomitant medication", false);
        InputFieldAttributes.setSize(agentNameField, 50);
        fieldFactory.addField(agentNameField);

    	//-
        InputFieldGroupMap groups = new InputFieldGroupMap();
        groups.addRepeatingFieldGroupFactory(fieldFactory, command.getAeReport().getConcomitantMedications().size());
        return groups;
    }


    @Override
    public ExpeditedReportSection section() {
    	return ExpeditedReportSection.CONCOMITANT_MEDICATION_SECTION;
    }
}
