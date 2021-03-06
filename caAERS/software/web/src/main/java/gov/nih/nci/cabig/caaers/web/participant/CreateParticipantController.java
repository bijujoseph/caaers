/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.web.participant;

//java imports

import gov.nih.nci.cabig.caaers.dao.*;
import gov.nih.nci.cabig.caaers.domain.Participant;
import gov.nih.nci.cabig.caaers.domain.repository.OrganizationRepository;
import gov.nih.nci.cabig.caaers.domain.repository.ParticipantRepository;
import gov.nih.nci.cabig.caaers.event.EventFactory;
import gov.nih.nci.cabig.caaers.security.SecurityUtils;
import gov.nih.nci.cabig.caaers.tools.spring.tabbedflow.AutomaticSaveAjaxableFormController;
import gov.nih.nci.cabig.caaers.tools.configuration.Configuration;
import gov.nih.nci.cabig.caaers.utils.ConfigProperty;
import gov.nih.nci.cabig.caaers.web.utils.WebUtils;
import gov.nih.nci.cabig.caaers.web.validation.validator.WebControllerValidator;
import gov.nih.nci.cabig.caaers.web.ControllerTools;
import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.FlowFactory;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

/**
 * Participant Creation Flow Controller
 * */
public class CreateParticipantController extends AutomaticSaveAjaxableFormController<ParticipantInputCommand, Participant, ParticipantDao> {

    private static Log log = LogFactory.getLog(CreateParticipantController.class);

    private StudyDao studyDao;
    private OrganizationDao organizationDao;
    private StudySiteDao studySiteDao;
    private ParticipantDao participantDao;
    private ResearchStaffDao rsDao;
    private InvestigatorDao investigatorDao;

    private ConfigProperty configurationProperty;
    protected WebControllerValidator webControllerValidator;

    OrganizationRepository organizationRepository;
    ParticipantRepository participantRepository;

    protected PriorTherapyDao priorTherapyDao;
    protected AnatomicSiteDao anatomicSiteDao;
    protected PreExistingConditionDao preExistingConditionDao;
    protected AbstractStudyDiseaseDao abstractStudyDiseaseDao;
    protected ChemoAgentDao chemoAgentDao;
    protected AgentDao agentDao;
    private Configuration configuration;

    private EventFactory eventFactory;

    public CreateParticipantController() {
    }

    /**
     * Determines whether the subject acts in deidentified mode.
     * This mode doesn't show subject names.
     * */
    public boolean getUnidentifiedMode(){
        boolean unidentifiedMode;
        if (configuration.get(Configuration.UNIDENTIFIED_MODE) == null) unidentifiedMode = false;
        else unidentifiedMode =  configuration.get(Configuration.UNIDENTIFIED_MODE);
        return unidentifiedMode;
    }

    /**
     * Building Flow pages.
     * */
    @Override
    public FlowFactory<ParticipantInputCommand> getFlowFactory() {
        return new FlowFactory<ParticipantInputCommand>() {
            public Flow<ParticipantInputCommand> createFlow(ParticipantInputCommand cmd) {
                Flow<ParticipantInputCommand> flow = new Flow<ParticipantInputCommand>("Enter Subject");
                
                flow.addTab(new CreateParticipantTab());
                flow.addTab(new SelectStudyForParticipantTab());
                flow.addTab(new CreateSubjectMedHistoryTab());
                flow.addTab(new CreateParticipantReviewParticipantTab());
                
                return flow;
            }
        };
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ParticipantInputCommand participantCommand = (ParticipantInputCommand) command;
        Participant participant = participantCommand.getParticipant();
        participantDao.save(participant);
        if (eventFactory != null) eventFactory.publishEntityModifiedEvent(participant);
        response.sendRedirect("view?participantId=" + participant.getId() + "&type=create");
        return null;
    }

    @Override
    protected Object formBackingObject(final HttpServletRequest request) throws Exception {
        ParticipantInputCommand c = new ParticipantInputCommand();
        c.setUnidentifiedMode(getUnidentifiedMode());

        c.init(configurationProperty.getMap().get("participantIdentifiersType").get(2).getCode()); //initialise the command

/*
        c.setLoggedinResearchStaff(rsDao.getByLoginId(SecurityUtils.getUserLoginName()));
        c.setLoggedinInvestigator(investigatorDao.getByLoginId(SecurityUtils.getUserLoginName()));
*/

        return c;
    }
    
    @Override
    protected Object currentFormObject(HttpServletRequest request,
    		Object oCommand) throws Exception {
    	((ParticipantInputCommand)oCommand).setTargetPage(WebUtils.getTargetPage(request));
    	return super.currentFormObject(request, oCommand);
    }

    protected void initBinder(HttpServletRequest httpServletRequest, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(httpServletRequest, binder);
        ControllerTools.registerDomainObjectEditor(binder, organizationDao);
        ControllerTools.registerDomainObjectEditor(binder, priorTherapyDao);
        ControllerTools.registerDomainObjectEditor(binder, anatomicSiteDao);
        ControllerTools.registerDomainObjectEditor(binder, preExistingConditionDao);
        ControllerTools.registerDomainObjectEditor(binder, studyDao);
        ControllerTools.registerDomainObjectEditor(binder, "assignment.diseaseHistory.abstractStudyDisease", abstractStudyDiseaseDao);
        ControllerTools.registerDomainObjectEditor(binder, chemoAgentDao);
        ControllerTools.registerDomainObjectEditor(binder, agentDao);
    }

    /**
     * Looking if the request contains the needed attribute.
     * @param   attributeName needed attribute name
     * @return  The needed attribute if found, if the attribute is not found returns null.
     * */
    // ToDo this should be moved to a super Controller
    protected Object findInRequest(final HttpServletRequest request, final String attributeName) {
        Object attr = request.getParameter(attributeName);
        if (attr == null) {
            attr = request.getAttribute(attributeName);
        }
        return attr;
    }

    @Override
    protected boolean suppressValidation(HttpServletRequest request, Object cmd) {
    	ParticipantInputCommand command = (ParticipantInputCommand) cmd;
    	
        // supress validation when target page is less than current page.
        int curPage = getCurrentPage(request);
        int targetPage = getTargetPage(request, curPage);
        if (targetPage < curPage) return true;
        
        
        // supress for ajax and delete requests
        if(isAjaxRequest(request) && !StringUtils.equals("save",command.getTask())) return true;
        return super.suppressValidation(request, command);
    }

    @Required
    public void setStudySiteDao(final StudySiteDao studySiteDao) {
        this.studySiteDao = studySiteDao;
    }

    @Required
    public void setStudyDao(final StudyDao studyDao) {
        this.studyDao = studyDao;
    }

    public ConfigProperty getConfigurationProperty() {
        return configurationProperty;
    }

    @Required
    public void setConfigurationProperty(ConfigProperty configurationProperty) {
        this.configurationProperty = configurationProperty;
    }

    public WebControllerValidator getWebControllerValidator() {
        return webControllerValidator;
    }

    public void setWebControllerValidator(WebControllerValidator webControllerValidator) {
        this.webControllerValidator = webControllerValidator;
    }

    public OrganizationDao getOrganizationDao() {
        return organizationDao;
    }

    public void setOrganizationDao(OrganizationDao organizationDao) {
        this.organizationDao = organizationDao;
    }

    public OrganizationRepository getOrganizationRepository() {
        return organizationRepository;
    }

    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public ParticipantDao getParticipantDao() {
        return participantDao;
    }

    public void setParticipantDao(ParticipantDao participantDao) {
        this.participantDao = participantDao;
    }

    protected ParticipantDao getDao() {
        return participantDao;
    }

    protected Participant getPrimaryDomainObject(ParticipantInputCommand command) {
        return command.getParticipant();
    }
    
    
    @Required
    public void setPriorTherapyDao(PriorTherapyDao priorTherapyDao) {
		this.priorTherapyDao = priorTherapyDao;
	}
    @Required
    public void setAnatomicSiteDao(AnatomicSiteDao anatomicSiteDao) {
		this.anatomicSiteDao = anatomicSiteDao;
	}
    @Required
    public void setPreExistingConditionDao(
			PreExistingConditionDao preExistingConditionDao) {
		this.preExistingConditionDao = preExistingConditionDao;
	}
    @Required
    public void setAbstractStudyDiseaseDao(
			AbstractStudyDiseaseDao abstractStudyDiseaseDao) {
		this.abstractStudyDiseaseDao = abstractStudyDiseaseDao;
	}
    @Required
    public void setChemoAgentDao(ChemoAgentDao chemoAgentDao) {
		this.chemoAgentDao = chemoAgentDao;
	}

    @Required
    public void setAgentDao(AgentDao agentDao) {
        this.agentDao = agentDao;
    }

    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
        super.onBindAndValidate(request, command, errors, page);
        ParticipantInputCommand cmd = (ParticipantInputCommand) command;

        if (isAjaxRequest(request) || cmd.getOrganization() == null) return;
        
        // if the target tab is not the next to the crrent one
        if (getTargetPage(request, command, errors, page) - page > 1) {
            // if the assisgnment object needed by SubjectMedHistoryTab is not in the command 
            if (cmd.assignment == null || cmd.assignment.getId() == null)
                if (getTab((ParticipantInputCommand) command, getTargetPage(request, command, errors, page)).getClass() == SubjectMedHistoryTab.class)
                    errors.reject("ERR_SELECT_STUDY_FROM_DETAILS", "Please select a study first.");
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    protected Map referenceData(final HttpServletRequest request, final Object command, final Errors errors, final int page) throws Exception {
        Map<String, Object> refdata = super.referenceData(request, command, errors, page);
        refdata.put("unidentifiedMode", getUnidentifiedMode());
        ParticipantInputCommand cmd = (ParticipantInputCommand) command;
        if (cmd.getParticipant() == null || cmd.getParticipant().getId() == null) {
            refdata.remove("flashMessage");
        }
        return refdata;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public ResearchStaffDao getRsDao() {
        return rsDao;
    }

    public void setRsDao(ResearchStaffDao rsDao) {
        this.rsDao = rsDao;
    }

    public InvestigatorDao getInvestigatorDao() {
        return investigatorDao;
    }

    public void setInvestigatorDao(InvestigatorDao investigatorDao) {
        this.investigatorDao = investigatorDao;
    }

    @Override
    protected boolean shouldSave(HttpServletRequest request, ParticipantInputCommand command, Tab<ParticipantInputCommand> participantInputCommandTab) {
        //if (isAjaxRequest(request)) return false;
        //return (getCurrentPage(request) > 0 && getTargetPage(request, getCurrentPage(request)) > 0);
    	return false;
    }

	public EventFactory getEventFactory() {
		return eventFactory;
	}

	public void setEventFactory(EventFactory eventFactory) {
		this.eventFactory = eventFactory;
	}
}
