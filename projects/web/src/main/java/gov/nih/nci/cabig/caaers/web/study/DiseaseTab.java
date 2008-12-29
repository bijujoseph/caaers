package gov.nih.nci.cabig.caaers.web.study;

import gov.nih.nci.cabig.caaers.dao.DiseaseTermDao;
import gov.nih.nci.cabig.caaers.dao.MeddraVersionDao;
import gov.nih.nci.cabig.caaers.dao.ConditionDao;
import gov.nih.nci.cabig.caaers.dao.meddra.LowLevelTermDao;
import gov.nih.nci.cabig.caaers.domain.*;
import gov.nih.nci.cabig.caaers.domain.meddra.LowLevelTerm;
import gov.nih.nci.cabig.caaers.web.fields.InputFieldGroup;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.validation.Errors;

/**
 * @author Rhett Sutphin
 * @author Krikor Krumlian
 */
public class DiseaseTab extends StudyTab {
    private static Log log = LogFactory.getLog(DiseaseTab.class);

    private DiseaseTermDao diseaseTermDao;
    private LowLevelTermDao lowLevelTermDao;
    private MeddraVersionDao meddraVersionDao;
    private ConditionDao conditionDao;
    private HashMap<String, Condition> conditionMap;
    
    public DiseaseTab() {
        super("Disease", "Disease", "study/study_diseases");
    }

    /*
     * If CTEP Study Disease 1. Check if the existing CTEP disease ID is mentioned in
     * study.getDiseaseTermIds() 2. Throw error, saying that the selected disease term already
     * exists.
     * 
     * If Medra Study Disease 1. Check if the existing MEDRA disease (LowLevelTerm) is mentioned in
     * study.getDiseaseLlt() 2. Throw error, saying that the selected disease already present.
     */
    @Override
    protected void validate(StudyCommand command, BeanWrapper commandBean, Map<String, InputFieldGroup> fieldGroups, Errors errors) {

        HashMap<String, DiseaseTerm> ctepTermMap = new HashMap<String, DiseaseTerm>();
        for (CtepStudyDisease ctepDisease : command.getStudy().getCtepStudyDiseases()) {
            ctepTermMap.put(ctepDisease.getTerm().getId().toString(), ctepDisease.getDiseaseTerm());
        }

        String[] newCTEPTermIds = command.getDiseaseTermIds();
        if (newCTEPTermIds != null) {
            for (String newCTEPTermId : newCTEPTermIds) {
                if (ctepTermMap.containsKey(newCTEPTermId)) {
                    errors.reject("DUPLICATE", "'" + ctepTermMap.get(newCTEPTermId).getFullName() + "' is already associated to this study");
                }
            }
        }

        HashMap<String, LowLevelTerm> medraTermMap = new HashMap<String, LowLevelTerm>();
        for (MeddraStudyDisease meddraStudyDisease : command.getStudy().getMeddraStudyDiseases()) {
            medraTermMap.put(meddraStudyDisease.getTerm().getId().toString(), meddraStudyDisease.getTerm());
        }

        if (command.getDiseaseLlt() != null) {
            if (medraTermMap.containsKey(command.getDiseaseLlt())) {
                errors.reject("DUPLICATE", "'" + medraTermMap.get(command.getDiseaseLlt()).getFullName() + "' is already associated to this study");
                command.setDiseaseLlt(null);
            }
        }

        if (checkDuplicateConditionById(conditionMap, command.getCondition())) {
            errors.reject("DUPLICATE_STUDY_CONDITION", new Object[] {(conditionMap.get(command.getCondition())).getConditionName()}, "");
        }
    }

    @Override
    public void postProcess(HttpServletRequest request, StudyCommand command, Errors errors) {
        super.postProcess(request, command, errors);
        if (!errors.hasErrors()) {
            handleStudyDiseaseAction(errors, command.getStudy(), request.getParameter("_action"), request.getParameter("_selected"), request);
            command.getStudy().setDiseaseLlt(null);
        }
    }

    @Override
    public Map<String, Object> referenceData(HttpServletRequest request, StudyCommand command) {
        Map<String, Object> refdata = super.referenceData(command);
        Study study = command.getStudy();

        // this will hold the Study Conditions' IDs as keys
        conditionMap = new HashMap<String, Condition>();
        for (StudyCondition studyCondition : study.getStudyConditions()) {
            conditionMap.put(studyCondition.getTerm().getId().toString(), studyCondition.getTerm());
        }

        String diseaseTerminology = "MEDDRA";
        if (study.getDiseaseTerminology().getDiseaseCodeTerm() == DiseaseCodeTerm.CTEP) diseaseTerminology = "CTEP";
        else if (study.getDiseaseTerminology().getDiseaseCodeTerm() == DiseaseCodeTerm.OTHER) diseaseTerminology = "OTHER";
        refdata.put("diseaseTerminology", diseaseTerminology);

        if(study.getDiseaseTerminology().getDiseaseCodeTerm().equals(DiseaseCodeTerm.MEDDRA)) {
        	refdata.put("meddraVersionId", study.getDiseaseTerminology().getMeddraVersion().getId());
        	refdata.put("meddraVersion", study.getDiseaseTerminology().getMeddraVersion().getName());
        }
        return refdata;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, InputFieldGroup> createFieldGroups(StudyCommand command) {
        return super.createFieldGroups(command);
    }

    private boolean checkDuplicateConditionById(Map conditions, String conditionName) {
        if (conditionMap == null) return false;
        return (conditionMap.containsKey(conditionName));
    }

    private boolean checkDuplicateConditionByText(Map conditions, String conditionName) {
        Iterator it = conditionMap.keySet().iterator();
        while (it.hasNext()) {
            Condition c = (Condition)conditionMap.get(it.next());
            if (conditionName.equals(c.getConditionName())) return true;
        }
        return false;
    }

    private void handleStudyDiseaseAction(Errors errors, Study study, String action, String selected, HttpServletRequest request) {

        if ("addMeddraStudyDisease".equals(action) && study.getDiseaseLlt().length() > 0 && study.getDiseaseTerminology().getDiseaseCodeTerm() == DiseaseCodeTerm.MEDDRA) {
            String diseaseCode = study.getDiseaseLlt();
            MeddraStudyDisease meddraStudyDisease = new MeddraStudyDisease();
            // meddraStudyDisease.setMeddraCode(diseaseCode);
            meddraStudyDisease.setTerm(lowLevelTermDao.getById(Integer.parseInt(diseaseCode)) == null ? lowLevelTermDao.getById(1): lowLevelTermDao.getById(Integer.parseInt(diseaseCode)));
            study.addMeddraStudyDisease(meddraStudyDisease);
        }

        if ("removeMeddraStudyDisease".equals(action)) {
            study.getMeddraStudyDiseases().remove(Integer.parseInt(selected));
        }

        if (action.equals("addOtherCondition")) {

            Condition condition = null;
            int _c = 0;

            try {
                _c = Integer.parseInt(study.getCondition());
            } catch (NumberFormatException e) {
                log.warn("Incorrect ID for the Condition Object.");
                e.printStackTrace();
            }
            StudyCondition studyCondition = new StudyCondition();

            if (_c > 0) {
                condition = conditionDao.getById(_c);
                studyCondition.setTerm(condition);
            } else {

                Condition newCondition = new Condition();
                if (StringUtils.isNotBlank(request.getParameter("condition-input")))
                    newCondition.setConditionName(StringEscapeUtils.escapeHtml(request.getParameter("condition-input")));

                if (checkDuplicateConditionByText(conditionMap, newCondition.getConditionName())) {
                    errors.reject("DUPLICATE_STUDY_CONDITION", new Object[] {newCondition.getConditionName()}, "");
                } else {
                    conditionDao.save(newCondition);
                    studyCondition.setTerm(newCondition);
                }
                
            }
            if (!errors.hasErrors()) study.addStudyCondition(studyCondition);
        }

        if (action.equals("removeOtherCondition")) {
            try {
                study.getStudyConditions().remove(Integer.parseInt(selected));
                System.out.println("Removing a Condition.");
            } catch (IndexOutOfBoundsException e) {
                log.warn("No <StudyCondition> at the position: " + selected);
            }
        }

        if ("addStudyDisease".equals(action) && study.getDiseaseTerminology().getDiseaseCodeTerm() == DiseaseCodeTerm.CTEP) {
            String[] diseases = study.getDiseaseTermIds();
            log.debug("Study Diseases Size : " + study.getCtepStudyDiseases().size());
            for (String diseaseId : diseases) {
                log.debug("Disease Id : " + diseaseId);
                CtepStudyDisease ctepStudyDisease = new CtepStudyDisease();
                ctepStudyDisease.setTerm(diseaseTermDao.getById(Integer.parseInt(diseaseId)));
                study.addCtepStudyDisease(ctepStudyDisease);

            }
        } else if ("removeStudyDisease".equals(action)) {
            study.getCtepStudyDiseases().remove(Integer.parseInt(selected));
        }
    }

    public void setDiseaseTermDao(DiseaseTermDao diseaseTermDao) {
        this.diseaseTermDao = diseaseTermDao;
    }

    public void setLowLevelTermDao(LowLevelTermDao lowLevelTermDao) {
        this.lowLevelTermDao = lowLevelTermDao;
    }

    public void setMeddraVersionDao(MeddraVersionDao meddraVersionDao) {
        this.meddraVersionDao = meddraVersionDao;
    }

    public ConditionDao getConditionDao() {
        return conditionDao;
    }

    public void setConditionDao(ConditionDao conditionDao) {
        this.conditionDao = conditionDao;
    }
}
