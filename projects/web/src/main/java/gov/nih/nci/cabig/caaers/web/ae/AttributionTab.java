package gov.nih.nci.cabig.caaers.web.ae;

import gov.nih.nci.cabig.caaers.domain.ExpeditedAdverseEventReport;
import gov.nih.nci.cabig.caaers.domain.Attribution;
import gov.nih.nci.cabig.caaers.domain.attribution.AdverseEventAttribution;
import gov.nih.nci.cabig.caaers.domain.expeditedfields.ExpeditedReportSection;
import gov.nih.nci.cabig.caaers.web.fields.DefaultInputFieldGroup;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroup;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroupMap;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldFactory;
import gov.nih.nci.cabig.caaers.web.fields.InputField;
import gov.nih.nci.cabig.ctms.domain.DomainObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rhett Sutphin
 */
public class AttributionTab extends AeTab {
    private static final Map<Object,Object> ATTRIBUTION_OPTIONS;
    static{
    	ATTRIBUTION_OPTIONS = collectAttributionOptions();
    }
    protected AttributionTab() {
        super("Attribution", "Attribution", "ae/attribution");

    }

    @Override
    public InputFieldGroupMap createFieldGroups(ExpeditedAdverseEventInputCommand command) {
        InputFieldGroupMap map = new InputFieldGroupMap();
        List<AttributionBlock> blocks = createBlocks(command.getAeReport());
        for (AttributionBlock block : blocks) {
            for (InputFieldGroup group : block.getRows()) {
                map.addInputFieldGroup(group);
            }
        }
        return map;
    }

    private List<AttributionBlock> createBlocks(ExpeditedAdverseEventReport report) {
        List<AttributionBlock> blocks = new ArrayList<AttributionBlock>();
        blocks.add(new AttributionBlock("Disease",
            createGroups(CauseAndAttributionAccessor.DISEASE, report)));
        blocks.add(new AttributionBlock("Course", "Course",
            createGroups(CauseAndAttributionAccessor.COURSE_AGENT, report)));
        blocks.add(new AttributionBlock("Surgery intervention",
            createGroups(CauseAndAttributionAccessor.SURGERY, report)));
        blocks.add(new AttributionBlock("Radiation intervention",
            createGroups(CauseAndAttributionAccessor.RADIATION, report)));
        blocks.add(new AttributionBlock("Concomitant medication",
            createGroups(CauseAndAttributionAccessor.CONCOMITANT_MEDICATION, report)));
        blocks.add(new AttributionBlock("Other cause",
            createGroups(CauseAndAttributionAccessor.OTHER_CAUSE, report)));
        return blocks;
    }

    @Override
    public Map<String, Object> referenceData(ExpeditedAdverseEventInputCommand command) {
        Map<String, Object> refdata = super.referenceData(command);
        refdata.put("blocks", createBlocks(command.getAeReport()));
        return refdata;
    }

    public class AttributionBlock {
        private String displayName;
        private List<InputFieldGroup> rows;

        public AttributionBlock(String singularName, String pluralName, List<InputFieldGroup> rows) {
            this.displayName = rows.size() == 1 ? singularName : pluralName;
            this.rows = rows;
        }

        public AttributionBlock(String displayName, List<InputFieldGroup> rows) {
            this(displayName, displayName + 's', rows);
        }

        public String getDisplayName() {
            return displayName;
        }

        public List<InputFieldGroup> getRows() {
            return rows;
        }
    }

    private static <D extends DomainObject, A extends AdverseEventAttribution<D>>
    List<InputFieldGroup> createGroups(
        CauseAndAttributionAccessor<D, A> accessor, ExpeditedAdverseEventReport report
    ) {
        List<InputFieldGroup> groups = new ArrayList<InputFieldGroup>();
        List<D> causes = accessor.getCauseList(report);
        for (int c = 0 ; c < causes.size(); c++) {
            DefaultInputFieldGroup newGroup
                = new DefaultInputFieldGroup(accessor.getKey() + c);
            newGroup.setDisplayName(accessor.getDisplayName(causes.get(c)));
            for (int a = 0; a < report.getAdverseEvents().size(); a++) {
                newGroup.getFields().add(
                    createAttributionField(accessor.getKey(), a, c));
            }
            groups.add(newGroup);
        }
        return groups;
    }

    private static InputField createAttributionField(String groupKey, int aeIndex, int causeIndex) {
        String propertyName = new StringBuilder()
                .append("attributionMap[").append(groupKey).append("][")
                .append(aeIndex).append("][")
                .append(causeIndex).append(']').toString();
        return InputFieldFactory.createSelectField(propertyName, null, true, ATTRIBUTION_OPTIONS);
    }

    private static Map<Object, Object> collectAttributionOptions(){
    	Map<Object, Object> map = new LinkedHashMap<Object, Object>();
    	map.put("", "Please select");
    	map.putAll(InputFieldFactory.collectOptions(Arrays.asList(Attribution.values()), "name", null));
    	return map;
    }

    @Override
    public ExpeditedReportSection section() {
    	return ExpeditedReportSection.ATTRIBUTION_SECTION;
    }
}
