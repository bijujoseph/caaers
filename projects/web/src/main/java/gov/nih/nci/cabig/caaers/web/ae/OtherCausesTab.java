package gov.nih.nci.cabig.caaers.web.ae;

import gov.nih.nci.cabig.caaers.domain.expeditedfields.ExpeditedReportSection;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroup;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroupMap;
import gov.nih.nci.cabig.caaers.web.fields.RepeatingFieldGroupFactory;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldFactory;

import java.util.Map;

/**
 * @author Rhett Sutphin
 */
public class OtherCausesTab extends AeTab {
    public OtherCausesTab() {
        super("Other contributing causes", "Other Causes", "ae/other");
    }

    @Override
    public Map<String, InputFieldGroup> createFieldGroups(ExpeditedAdverseEventInputCommand command) {
        RepeatingFieldGroupFactory factory = new RepeatingFieldGroupFactory("otherCause",
            "aeReport.otherCauses");
        factory.addField(InputFieldFactory.createTextArea("text", "Cause", false));

        InputFieldGroupMap map = new InputFieldGroupMap();
        map.addRepeatingFieldGroupFactory(factory, command.getAeReport().getOtherCauses().size());
        return map;
    }

    @Override
    public ExpeditedReportSection section() {
    	return ExpeditedReportSection.OTHER_CAUSE_SECTION;
    }
}
