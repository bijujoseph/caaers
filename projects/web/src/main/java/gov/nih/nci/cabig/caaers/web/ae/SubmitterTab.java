package gov.nih.nci.cabig.caaers.web.ae;

import gov.nih.nci.cabig.caaers.CaaersSystemException;
import gov.nih.nci.cabig.caaers.domain.ReportPerson;
import gov.nih.nci.cabig.caaers.web.fields.DefaultInputFieldGroup;
import gov.nih.nci.cabig.caaers.web.fields.InputField;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroup;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroupMap;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldFactory;
import org.apache.commons.lang.StringUtils;
import gov.nih.nci.cabig.caaers.web.ae.EditExpeditedAdverseEventCommand;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Kulasekaran
 * @author Rhett Sutphin
 * @author Krikor Krumlian
 */
public class SubmitterTab extends AeTab {
   // private EvaluationService evaluationService;

    public SubmitterTab() {
        super("Submitter info", "Submitter", "ae/submitter");
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, InputFieldGroup> createFieldGroups(ExpeditedAdverseEventInputCommand command) {
    	String reportIndex =  ((SubmitExpeditedAdverseEventCommand)command).getReportIndex() ;
    	if ( reportIndex == null ){
    		throw new CaaersSystemException("Report Index Not Defined");
    	}
        InputFieldGroupMap map = new InputFieldGroupMap();
        InputFieldGroup physicianSignoff = new DefaultInputFieldGroup("physicianSignoff");
        physicianSignoff.getFields().add(
				InputFieldFactory.createSelectField("aeReport.reports["
						+ reportIndex + "].physicianSignoff",
						"Physician sign-off", true,createExpectedOptions()));
        map.addInputFieldGroup(physicianSignoff);
        map.addInputFieldGroup(createPersonGroup("reporter",null));
        map.addInputFieldGroup(createPersonGroup("reports["+ reportIndex + "].submitter","submitter"));
        return map;
    }

    private InputFieldGroup createPersonGroup(String person, String name) {
    	String groupName = name == null ? person : name;
        InputFieldGroup group = new DefaultInputFieldGroup(groupName, StringUtils.capitalize(person) + " details");
        String base = "aeReport." + person  + '.';
        group.getFields().add(InputFieldFactory.createTextField(base + "firstName", "First name", true));
        group.getFields().add(InputFieldFactory.createTextField(base + "middleName", "Middle name", false));
        group.getFields().add(InputFieldFactory.createTextField(base + "lastName", "Last name", true));
        group.getFields().add(createContactField(base, ReportPerson.EMAIL, "E-mail address", true));
        group.getFields().add(createContactField(base, ReportPerson.PHONE));
        group.getFields().add(createContactField(base, ReportPerson.FAX));
        return group;
    }
    
    private Map<Object, Object> createExpectedOptions() {
        Map<Object, Object> expectedOptions = new LinkedHashMap<Object, Object>();
        expectedOptions.put("", "Please select");
        expectedOptions.put(Boolean.TRUE, "Yes");
        expectedOptions.put(Boolean.FALSE, "No");
        return expectedOptions;
    }

    private InputField createContactField(String base, String contactType) {
        return createContactField(base, contactType, StringUtils.capitalize(contactType), false);
    }

    private InputField createContactField(
        String base, String contactType, String displayName, boolean required
    ) {
        return InputFieldFactory.createTextField(
            base + "contactMechanisms[" + contactType + ']', displayName, required);
    }
}
