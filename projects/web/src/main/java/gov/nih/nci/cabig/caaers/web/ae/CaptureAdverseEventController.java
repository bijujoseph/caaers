package gov.nih.nci.cabig.caaers.web.ae;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import gov.nih.nci.cabig.caaers.dao.AdverseEventDao;
import gov.nih.nci.cabig.caaers.dao.CtcCategoryDao;
import gov.nih.nci.cabig.caaers.dao.CtcTermDao;
import gov.nih.nci.cabig.caaers.dao.ParticipantDao;
import gov.nih.nci.cabig.caaers.dao.AdverseEventReportingPeriodDao;
import gov.nih.nci.cabig.caaers.dao.StudyDao;
import gov.nih.nci.cabig.caaers.dao.StudyParticipantAssignmentDao;
import gov.nih.nci.cabig.caaers.dao.TreatmentAssignmentDao;
import gov.nih.nci.cabig.caaers.dao.meddra.LowLevelTermDao;
import gov.nih.nci.cabig.caaers.domain.AdverseEvent;
import gov.nih.nci.cabig.caaers.domain.AdverseEventCtcTerm;
import gov.nih.nci.cabig.caaers.domain.AdverseEventReportingPeriod;
import gov.nih.nci.cabig.caaers.domain.Arm;
import gov.nih.nci.cabig.caaers.domain.Attribution;
import gov.nih.nci.cabig.caaers.domain.CodedGrade;
import gov.nih.nci.cabig.caaers.domain.Grade;
import gov.nih.nci.cabig.caaers.domain.Hospitalization;
import gov.nih.nci.cabig.caaers.domain.ResearchStaff;
import gov.nih.nci.cabig.caaers.domain.SolicitedAdverseEvent;
import gov.nih.nci.cabig.caaers.domain.Term;
import gov.nih.nci.cabig.caaers.domain.TreatmentAssignment;
import gov.nih.nci.cabig.caaers.domain.UserGroupType;
import gov.nih.nci.cabig.caaers.tools.spring.tabbedflow.AutomaticSaveAjaxableFormController;
import gov.nih.nci.cabig.caaers.web.ControllerTools;
import gov.nih.nci.cabig.caaers.web.study.EmptyStudyTab;
import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.FlowFactory;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;

public class CaptureAdverseEventController extends AutomaticSaveAjaxableFormController<CaptureAdverseEventInputCommand, AdverseEventReportingPeriod, AdverseEventReportingPeriodDao> {
	
	private static final String AJAX_SUBVIEW_PARAMETER = "subview";
	private ParticipantDao participantDao;
	private StudyDao studyDao;
	private StudyParticipantAssignmentDao assignmentDao;
	private TreatmentAssignmentDao treatmentAssignmentDao;
	private CtcTermDao ctcTermDao;
	private CtcCategoryDao ctcCategoryDao;
	private LowLevelTermDao lowLevelTermDao;
	private AdverseEventDao adverseEventDao;
	private AdverseEventReportingPeriodDao adverseEventReportingPeriodDao;
	
	
	public CaptureAdverseEventController(){
		setBindOnNewForm(true);
		setCommandClass(CaptureAdverseEventInputCommand.class);
	}
	
    /*@Override
    protected void onBindAndValidate(HttpServletRequest request, Object command,
                    BindException errors, int page) throws Exception {
    	String action = (String) findInRequest(request, "_action");
		if(org.apache.commons.lang.StringUtils.isEmpty(action))
			super.onBindAndValidate(request, command, errors, page);
    }*/
	
	@Override
	protected AdverseEventReportingPeriodDao getDao() {
		return null;
	}
	
	/**
	 *  createBinder method is over-ridden. In this use-case, we need to bind only the adverseEventReportingPeriod to the command object
	 *  incase the submit occurs on change in the Select(reporting period) dropdown. So a hidden attribute "_action" is checked (which is 
	 *  set in the onchange handler of the select dropdown. Incase the submit occurs due to "Save" then the entire form alongwith the adverse
	 *  events will be bound to the command object.
	 */
	
	@Override
	protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object command) throws Exception{
		ServletRequestDataBinder binder = super.createBinder(request, command);
		binder.setDisallowedFields(new String[]{"adverseEventReportingPeriod"});
		prepareBinder(binder);
		initBinder(request,binder);
		return binder;
	}

	@Override
	protected AdverseEventReportingPeriod getPrimaryDomainObject(CaptureAdverseEventInputCommand cmd) {
		//TODO should be refined.
		return new AdverseEventReportingPeriod();
	}

	@Override
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object oCommand, BindException errors) throws Exception {
		CaptureAdverseEventInputCommand command = (CaptureAdverseEventInputCommand) oCommand;
		adverseEventReportingPeriodDao.save(command.getAdverseEventReportingPeriod());
		
		ModelAndView mv = new ModelAndView("forward:view?type=confirm", errors.getModel());

        return mv;
	}
	
	@Override
	protected void initBinder(HttpServletRequest request,ServletRequestDataBinder binder) throws Exception {
		ControllerTools.registerDomainObjectEditor(binder, "participant", participantDao);
        ControllerTools.registerDomainObjectEditor(binder, "study", studyDao);
        //ControllerTools.registerDomainObjectEditor(binder, "aeReport", reportDao);
        //ControllerTools.registerDomainObjectEditor(binder, "aeRoutineReport", routineReportDao);
        ControllerTools.registerDomainObjectEditor(binder, "adverseEvent", adverseEventDao);
        //ControllerTools.registerDomainObjectEditor(binder, "adverseEventReportingPeriod", adverseEventReportingPeriodDao);
        ControllerTools.registerDomainObjectEditor(binder, ctcTermDao);
        ControllerTools.registerDomainObjectEditor(binder, ctcCategoryDao);
        ControllerTools.registerDomainObjectEditor(binder, lowLevelTermDao);
        binder.registerCustomEditor(Date.class, ControllerTools.getDateEditor(false));
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        ControllerTools.registerEnumEditor(binder, Grade.class);
        ControllerTools.registerEnumEditor(binder, Hospitalization.class);
        ControllerTools.registerEnumEditor(binder, Attribution.class);
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)	throws Exception {
		CaptureAdverseEventInputCommand cmd = new CaptureAdverseEventInputCommand(assignmentDao);
		
		return cmd;
	}
	
	@Override
	public FlowFactory<CaptureAdverseEventInputCommand> getFlowFactory() {
		return new FlowFactory<CaptureAdverseEventInputCommand>() {
			public Flow<CaptureAdverseEventInputCommand> createFlow(CaptureAdverseEventInputCommand cmd) {
				Flow<CaptureAdverseEventInputCommand> flow = new Flow<CaptureAdverseEventInputCommand>("Capture Adverse Event Flow");
				flow.addTab(new BeginTab<CaptureAdverseEventInputCommand>());
				flow.addTab(new AdverseEventCaptureTab());
				flow.addTab(new AdverseEventConfirmTab("Overview", "Overview", "ae/ae_reviewsummary"));
				return flow;
			}
		};
	}
	
	@Override
    protected boolean suppressValidation(final HttpServletRequest request) {

        Object isAjax = findInRequest(request, "_isAjax");
        if (isAjax != null) {
            return true;
        }
        String action = (String) findInRequest(request, "_action");
        if (org.apache.commons.lang.StringUtils.isNotEmpty(action)) {
            return true;
        }
        return true;
        //return super.suppressValidation(request);
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
    
    /**
     * Adds ajax sub-page view capability. TODO: factor this into main tabbed flow controller.
     */
    @Override
    protected String getViewName(HttpServletRequest request, Object command, int page) {
        String subviewName = request.getParameter(AJAX_SUBVIEW_PARAMETER);
        if (subviewName != null) {
            // for side-effects:
            super.getViewName(request, command, page);
            return "ae/ajax/" + subviewName;
        } else {
            return super.getViewName(request, command, page);
        }
    }
	
	@Override
	protected boolean shouldSave(HttpServletRequest request,CaptureAdverseEventInputCommand command,Tab<CaptureAdverseEventInputCommand> tab) {
		return false;
	}

	public ParticipantDao getParticipantDao() {
		return participantDao;
	}

	public void setParticipantDao(ParticipantDao participantDao) {
		this.participantDao = participantDao;
	}

	public StudyDao getStudyDao() {
		return studyDao;
	}

	public void setStudyDao(StudyDao studyDao) {
		this.studyDao = studyDao;
	}
	public void setAssignmentDao(StudyParticipantAssignmentDao assignmentDao){
		this.assignmentDao = assignmentDao;
	}
	
	public void setTreatmentAssignmentDao(
			TreatmentAssignmentDao treatmentAssignmentDao) {
		this.treatmentAssignmentDao = treatmentAssignmentDao;
	}
	
	public TreatmentAssignmentDao getTreatmentAssignmentDao() {
		return treatmentAssignmentDao;
	}
	
	public CtcTermDao getCtcTermDao() {
		return ctcTermDao;
	}
	
	public void setCtcTermDao(CtcTermDao ctcTermDao) {
		this.ctcTermDao = ctcTermDao;
	}
	
	public CtcCategoryDao getCtcCategoryDao() {
		return ctcCategoryDao;
	}
	
	public void setCtcCategoryDao(CtcCategoryDao ctcCategoryDao) {
		this.ctcCategoryDao = ctcCategoryDao;
	}
	
	public LowLevelTermDao getLowLevelTermDao() {
		return lowLevelTermDao;
	}
	
	public void setLowLevelTermDao(LowLevelTermDao lowLevelTermDao) {
		this.lowLevelTermDao = lowLevelTermDao;
	}
	
	public AdverseEventReportingPeriodDao getAdverseEventReportingPeriodDao() {
		return adverseEventReportingPeriodDao;
	}
	
	public void setAdverseEventReportingPeriodDao(
			AdverseEventReportingPeriodDao adverseEventReportingPeriodDao) {
		this.adverseEventReportingPeriodDao = adverseEventReportingPeriodDao;
	}
	
	public void setAdverseEventDao(AdverseEventDao adverseEventDao) {
		this.adverseEventDao = adverseEventDao;
	}
	
	public AdverseEventDao getAdverseEventDao() {
		return adverseEventDao;
	}
}
