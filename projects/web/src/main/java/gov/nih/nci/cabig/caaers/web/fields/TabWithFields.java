package gov.nih.nci.cabig.caaers.web.fields;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.validation.Errors;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The central class in the <code>fields</code> package.  Subclasses
 * may provide a map of {@link InputFieldGroup}s.  This map will be provided to the view
 * (in the request attribute <code>fieldGroups</code>).  It will also be used to to
 * basic (required/not required) field validation.
 *
 * @see InputField
 * @see InputFieldGroup
 * @author Rhett Sutphin
 */
public abstract class TabWithFields<C> extends Tab<C> {
	private boolean autoPopulateHelpKey;
	
    public TabWithFields(String longTitle, String shortTitle, String viewName) {
        super(longTitle, shortTitle, viewName);
    }

    /**
     * Return the field groups needed for the given command.
     * {@link InputFieldGroupMap} can be helpful for this, but is not required.
     *
     * @param command
     * @see RepeatingFieldGroupFactory
     */
    public abstract Map<String, InputFieldGroup> createFieldGroups(C command);

    @Override
    public Map<String, Object> referenceData(C command) {
        Map<String, Object> refdata = referenceData();
        Map<String, InputFieldGroup> groupMap = createFieldGroups(command);
        if(isAutoPopulateHelpKey()) populateHelpAttributeOnFields(groupMap); //to populate the help keys
        refdata.put("fieldGroups", groupMap); 
        return refdata;
    }

    @Override
    public final void validate(C command, Errors errors) {
        super.validate(command, errors);
        BeanWrapper commandBean = new BeanWrapperImpl(command);
        Map<String, InputFieldGroup> fieldGroups = createFieldGroups(command);
        for (InputFieldGroup fieldGroup : fieldGroups.values()) {
            for (InputField field : fieldGroup.getFields()) {
                field.validate(commandBean, errors);
            }
        }
        validate(command, commandBean, fieldGroups, errors);
    }

    /**
     * Template method for subclasses to provide additional non-field self-validation.
     */
    protected void validate(
        C command, BeanWrapper commandBean,
        Map<String, InputFieldGroup> fieldGroups, Errors errors
    ) {
    }
    
    /**
     * This functions sets the HELP key in the attribute map associated to an InputField.
     * The default logic of deriving HELP key is "viewName" + "." + propertyName, where
     * <code>/</code> character in viewName is replaced with <code>. (dot) </code> and 
     * array notations<code>[x]</code> in propertyName removed .  
     */
    protected void populateHelpAttributeOnFields(Map<String, InputFieldGroup> groupMap){
    	String helpKeyPrefix = (getViewName() != null) ? getViewName().replaceAll("/", ".") : "";
    	if(groupMap == null || groupMap.isEmpty()) return;
    	for(InputFieldGroup group : groupMap.values()){
    		for(InputField field : group.getFields()){
    			field.getAttributes().put(InputField.HELP, 
    					helpKeyPrefix + "." + field.getPropertyName().replaceAll("(\\[\\d+\\])",""));
    		}
    	}
    }
    
    ///BEAN PROPERTIES
    
	public boolean isAutoPopulateHelpKey() {
		return autoPopulateHelpKey;
	}

	public void setAutoPopulateHelpKey(boolean autoPopulateHelpKey) {
		this.autoPopulateHelpKey = autoPopulateHelpKey;
	}

    
}
