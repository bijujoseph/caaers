package gov.nih.nci.cabig.caaers.web.search;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanWrapper;
import org.springframework.validation.Errors;

import gov.nih.nci.cabig.caaers.tools.spring.tabbedflow.WorkFlowTab;
import gov.nih.nci.cabig.caaers.web.ae.CaptureAdverseEventController;
import gov.nih.nci.cabig.caaers.web.ae.CaptureAdverseEventInputCommand;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroup;
import gov.nih.nci.cabig.caaers.web.search.ui.DependentObject;
import gov.nih.nci.cabig.caaers.web.search.ui.UiAttribute;
import gov.nih.nci.cabig.caaers.web.search.ui.ViewColumn;

/**
 * This is the advanced search criteria tab.
 * @author Sameer Sawant
 */
public class AdvancedSearchCriteriaTab<T extends AdvancedSearchCommand> extends WorkFlowTab<T> {
	public static final String AJAX_SUBVIEW_PARAMETER = "subview";
	public static final String AJAX_ACTION_PARAMETER = "ajax_action";
	
	public AdvancedSearchCriteriaTab() {
        super("Enter criteria", "Enter criteria", "search/advancedSearch");
    }
	
	public void onBind(HttpServletRequest request, T command, Errors errors) {
        super.onBind(request, command, errors);
    }
	
	@Override
    public Map<String, Object> referenceData(HttpServletRequest request, AdvancedSearchCommand command) {
		Map<String, Object> refdata = new HashMap<String, Object>();
		
		String ajaxAction = (String) findInRequest(request, AJAX_ACTION_PARAMETER);
		
		// This is for adding a new criteria
		if(ajaxAction != null && ajaxAction.equals("addNewCriteria")){
			String dependentObjectDisplayName = (String) findInRequest(request, "dependentObjectDisplayName");
			DependentObject dependentObject = AdvancedSearchUiUtil.getDependentObjectByDisplayName(command.getSearchTargetObject(), dependentObjectDisplayName);
			refdata.put("dependentObject", dependentObject);
		}
		
		// This is for updating the attribute select element
		if(ajaxAction != null && ajaxAction.equals("updateAttribute")){
			String indexString = (String) findInRequest(request, "index");
			String attributeName = (String) findInRequest(request, "attributeName");
			Integer index = Integer.parseInt(indexString);
			refdata.put("index", index);
			
			//Put the uiAttribute in refdata. Its needed to provide the metadata to the renderValueColumn.tag
			for(DependentObject dObject: command.getSearchTargetObject().getDependentObject()){
				if(dObject.getClassName().equals(command.getCriteriaParameters().get(index).getObjectName())){
					for(UiAttribute uiAttribute: dObject.getUiAttribute())
						if(uiAttribute.getName().equals(attributeName))
							refdata.put("uiAttribute", uiAttribute);
				}
			}
		}
		
		return refdata;
	}
	
	@Override
    public void validate(T command, Errors errors) {
		if(command.getSearchTargetObject() == null)
			errors.rejectValue("searchTargetObject", "ASC_001", "Missing target object");
    }
	
	@Override
	public void postProcess(HttpServletRequest request, AdvancedSearchCommand command, Errors errors) {
		if (findInRequest(request, AJAX_SUBVIEW_PARAMETER) != null || errors.hasErrors())
			return; //ignore if this is an ajax request
		
		//Here we need to setup the view.
		//TODO If the user clicks on a save search and reaches this tab then the following code should not be executed 
		//as the view information should be collected from the saved target object and not based on criteria on this tab.
		//However there is an issue here as the user can reach the criteria tab by clicking on the saved search but change the criteria
		//on the criteria page, then what should the view page show ? the saved view or new view as per the criteria.

		//Reset the value of selected to false for all the dependentObjects and their attributes- 
		for(DependentObject dObject: command.getSearchTargetObject().getDependentObject()){
			dObject.setInView(false);
			for(ViewColumn viewColumn: dObject.getViewColumn())
				viewColumn.setSelected(false);
		}
		//Select the targetObject in the view.
		command.getSearchTargetObject().getDependentObject().get(0).setInView(true);
		for(ViewColumn viewColumn: command.getSearchTargetObject().getDependentObject().get(0).getViewColumn())
			viewColumn.setSelected(true);
		
		//Select all the dependentObjects involved in the criteria
		for(AdvancedSearchCriteriaParameter p: command.getCriteriaParameters()){
			if(p.getAttributeName()!= null && !p.getAttributeName().equals("") && !p.getAttributeName().equals("none") && !p.isDeleted()){
				DependentObject dObject = AdvancedSearchUiUtil.getDependentObjectByName(command.getSearchTargetObject(), p.getObjectName());
				dObject.setInView(true);
				for(ViewColumn viewColumn: dObject.getViewColumn())
					viewColumn.setSelected(true);
			}
		}
	}
	
	/**
     * Returns the value associated with the <code>attributeName</code>, if present in
     * HttpRequest parameter, if not available, will check in HttpRequest attribute map.
     */
    protected Object findInRequest(final ServletRequest request, final String attributName) {

        Object attr = request.getParameter(attributName);
        if (attr == null) {
            attr = request.getAttribute(attributName);
        }
        return attr;
    }
}