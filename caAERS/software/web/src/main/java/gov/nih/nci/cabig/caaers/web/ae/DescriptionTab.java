/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.web.ae;

import static gov.nih.nci.cabig.caaers.web.fields.InputFieldFactory.createBooleanSelectField;
import static gov.nih.nci.cabig.caaers.web.fields.InputFieldFactory.createPastDateField;
import static gov.nih.nci.cabig.caaers.web.fields.InputFieldFactory.createSelectField;
import static gov.nih.nci.cabig.caaers.web.fields.InputFieldFactory.createTextArea;
import gov.nih.nci.cabig.caaers.domain.EventStatus;
import gov.nih.nci.cabig.caaers.domain.PostAdverseEventStatus;
import gov.nih.nci.cabig.caaers.domain.expeditedfields.ExpeditedReportSection;
import gov.nih.nci.cabig.caaers.validation.fields.validators.TextSizeValidator;
import gov.nih.nci.cabig.caaers.web.fields.InputField;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldAttributes;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldFactory;
import gov.nih.nci.cabig.caaers.web.utils.WebUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.Errors;

/**
 * @author Rhett Sutphin
 */
public class DescriptionTab extends AeTab {

    public DescriptionTab() {
        super("Event and response description", ExpeditedReportSection.DESCRIPTION_SECTION.getDisplayName(), "ae/description");
        setAutoPopulateHelpKey(true);
/*        addHelpKeyExclusion("presentStatus", "recoveryDate", "retreated");*/
    }


    @Override
    protected void createFieldGroups(AeInputFieldCreator creator, ExpeditedAdverseEventInputCommand command) {
        String baseProp = "responseDescription";

        InputField desc = createTextArea(baseProp + ".eventDescription", "Description & treatment of event(s)", new TextSizeValidator(4000));
        InputFieldAttributes.setColumns(desc, 70);
        InputFieldAttributes.setRows(desc, 8);

        Map<Object, Object> postEventStatusOpts = new LinkedHashMap<Object, Object>();
        postEventStatusOpts.put("", "Please select");
        postEventStatusOpts.putAll(WebUtils.collectOptions(Arrays.asList(PostAdverseEventStatus.values()), null, "displayName"));

        Map<Object, Object> eventStatusOpts = new LinkedHashMap<Object, Object>();
        eventStatusOpts.put("", "Please select");
        eventStatusOpts.putAll(WebUtils.collectOptions(Arrays.asList(EventStatus.values()), null, "displayName"));
        
        InputField removedDateField = createPastDateField(baseProp + ".dateRemovedFromProtocol", "Date removed from protocol", false);
        InputField treatmentTimeField = createTimeField(baseProp +".primaryTreatmentApproximateTime", "Event treatment, approximate time");
        treatmentTimeField.getAttributes().put(InputField.HELP,"ae.treatment.aeReport.treatmentInformation.primaryTreatmentApproximateTime");
        creator.createFieldGroup("desc", desc,
        		treatmentTimeField,
                createSelectField(baseProp + ".presentStatus", "Present status", false, postEventStatusOpts),
                createPastDateField(baseProp + ".recoveryDate", "Date of recovery or death", false),
                createBooleanSelectField(baseProp + ".retreated", "Has the participant been re-treated?", false),
                removedDateField);

        InputField reducedDose = InputFieldFactory.createTextField(baseProp + ".reducedDose", "If reduced, specify: New dose", false);

        creator.createFieldGroup("DCP_INFO",
                createSelectField(baseProp + ".blindBroken", "Was blind broken due to event?", false, createBooleanOptions()),
                createSelectField(baseProp + ".studyDrugInterrupted", "Was study agent stopped/interrupted/reduced in response to event?", false, createBooleanOptions()),
                reducedDose,
                createPastDateField(baseProp + ".reducedDate", "Date dose reduced", false),
                InputFieldFactory.createTextField(baseProp + ".daysNotGiven", "If interrupted, specify total number of days not given", false),
                InputFieldFactory.createCheckboxField(baseProp + ".autopsyPerformed", "Autopsy performed?"),
                InputFieldFactory.createTextField(baseProp + ".causeOfDeath", "Cause of death"),
                createSelectField(baseProp + ".eventAbate", "Did event abate after study drug was stopped or dose reduced?", false, eventStatusOpts),
                createSelectField(baseProp + ".eventReappear", "Did event reappear after study drug was reintroduced?", false, eventStatusOpts));
    }

    private Map<Object, Object> createBooleanOptions() {
        Map<Object, Object> expectedOptions = new LinkedHashMap<Object, Object>();
        expectedOptions.put("", "Please select");
        expectedOptions.put(Boolean.TRUE, "Yes");
        expectedOptions.put(Boolean.FALSE, "No");
        return expectedOptions;
    }

    @Override
    public ExpeditedReportSection[] section() {
        return new ExpeditedReportSection[] { ExpeditedReportSection.DESCRIPTION_SECTION};
    }

    @Override
	public void onBind(HttpServletRequest request, ExpeditedAdverseEventInputCommand command, Errors errors) {
		super.onBind(request, command, errors);
		
		// Check if responseDescription.presentStatus != DEAD and set autopsyPerformed to null if thats the case
		if(command.getAeReport().getResponseDescription().getPresentStatus() != PostAdverseEventStatus.DEAD)
			command.getAeReport().getResponseDescription().setAutopsyPerformed(null);
    }
}
