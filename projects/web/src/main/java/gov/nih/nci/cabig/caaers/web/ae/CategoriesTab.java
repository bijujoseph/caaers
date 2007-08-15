package gov.nih.nci.cabig.caaers.web.ae;

import gov.nih.nci.cabig.caaers.dao.CtcDao;
import gov.nih.nci.cabig.caaers.domain.AdverseEvent;
import gov.nih.nci.cabig.caaers.domain.CtcCategory;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroupMap;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroup;


import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;

/**
 * @author Rhett Sutphin
 */
public class CategoriesTab extends AeRoutTab {
    private CtcDao ctcDao;

    public CategoriesTab() {
        super("Select CTC Categories", "Categories", "ae/categories");
    }
    
    @Override
    public Map<String, Object> referenceData(RoutineAdverseEventInputCommand command) {
        Map<String, Object> refdata = super.referenceData();
        refdata.put("ctcCats" , getCategories(command));
        return refdata;
    }


    @Override
    @SuppressWarnings("unchecked")
    public Map<String, InputFieldGroup> createFieldGroups(RoutineAdverseEventInputCommand command) {
    	InputFieldGroupMap map = new InputFieldGroupMap();
        //groups.addRepeatingFieldGroupFactory(fieldFactory,command.getCategories().size());
        return map;
    }
    
    private List<CtcCategory> getCategories(RoutineAdverseEventInputCommand command) {
        List<CtcCategory> categories = command.getStudy().getTerminology().getCtcVersion().getCategories();
        // cut down objects for serialization
        for (CtcCategory category : categories) {
            category.setTerms(null);
        }
        return categories;
    }
    
    @Override
    protected void validate(
        RoutineAdverseEventInputCommand command, BeanWrapper commandBean,
        Map<String, InputFieldGroup> fieldGroups, Errors errors
    ) {
       
			if (command.getAeRoutineReport().getStartDate() == null){
				errors.rejectValue("aeRoutineReport.startDate", "REQUIRED", "Missing From");
			}
			if (command.getAeRoutineReport().getEndDate() == null){
				errors.rejectValue("aeRoutineReport.endDate", "REQUIRED", "Missing To");
			}
    }

    @Required
	public CtcDao getCtcDao() {
		return ctcDao;
	}

	public void setCtcDao(CtcDao ctcDao) {
		this.ctcDao = ctcDao;
	}
    
    
}
