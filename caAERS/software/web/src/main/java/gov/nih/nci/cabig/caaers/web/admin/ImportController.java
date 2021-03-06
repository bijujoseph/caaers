/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.web.admin;

import gov.nih.nci.cabig.caaers.CaaersSystemException;
import gov.nih.nci.cabig.caaers.dao.AgentDao;
import gov.nih.nci.cabig.caaers.dao.CtcDao;
import gov.nih.nci.cabig.caaers.dao.ExpeditedAdverseEventReportDao;
import gov.nih.nci.cabig.caaers.dao.MedDRADao;
import gov.nih.nci.cabig.caaers.dao.OrganizationDao;
import gov.nih.nci.cabig.caaers.dao.StudyDao;
import gov.nih.nci.cabig.caaers.domain.AdverseEvent;
import gov.nih.nci.cabig.caaers.domain.ExpeditedAdverseEventReport;
import gov.nih.nci.cabig.caaers.domain.Study;
import gov.nih.nci.cabig.caaers.rules.business.service.AdverseEventEvaluationService;
import gov.nih.nci.cabig.caaers.rules.business.service.AdverseEventEvaluationServiceImpl;
import gov.nih.nci.cabig.caaers.web.ControllerTools;
import gov.nih.nci.cabig.ctms.lang.NowFactory;
import gov.nih.nci.cabig.ctms.web.tabs.AbstractTabbedFlowFormController;
import gov.nih.nci.cabig.ctms.web.tabs.Flow;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Krikor Krumlian
 */
public class ImportController extends AbstractTabbedFlowFormController<ImportCommand> {

    private static Log log = LogFactory.getLog(ImportController.class);

    private NowFactory nowFactory;

    private StudyDao studyDao;

    private OrganizationDao organizationDao;

    private ExpeditedAdverseEventReportDao expeditedAdverseEventReportDao;

    private AgentDao agentDao;

    private MedDRADao meddraDao;

    private CtcDao ctcDao;
    
    private AdverseEventEvaluationService adverseEventEvaluationService = new AdverseEventEvaluationServiceImpl();

    
    public ImportController() {

        setCommandClass(ImportCommand.class);
        setAllowDirtyForward(false);
        setAllowDirtyBack(false);

        Flow<ImportCommand> flow = new Flow<ImportCommand>("Import Data");
        flow.addTab(new ImportTab("Import ", "Import ", "admin/import"));
        flow.addTab(new ImportReviewTab("Review & Submit", "Review & Submit","admin/import_review_submit"));
        flow.addTab(new ImportSummaryTab("Import summary", "Import summary", "admin/import_summary"));
        setFlow(flow);
    }

    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        binder.registerCustomEditor(Date.class, ControllerTools.getDateEditor(true));
    }

    /**
     * 
     * @param request -
     *                HttpServletRequest
     * @throws ServletException
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        return createCommandObject();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish
     *      (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        String redirectTo = "redirectToImportTab";
        return new ModelAndView(redirectTo);
    }



    public String getXSDLocation(String type) {
        if ("study".equals(type)) {
            //return "classpath:gov/nih/nci/cabig/caaers/studyXSD.xsd";
        	return "classpath:schema/integration/StudySchema.xsd";
        }
        if ("participant".equals(type)) {
            //return "classpath:gov/nih/nci/cabig/caaers/participantXSD.xsd";
        	return "classpath:schema/integration/ParticipantSchema.xsd";
        }
        if ("investigator".equals(type)) {
            return "classpath:schema/integration/Investigator.xsd";
        }  
        if ("researchStaff".equals(type)) {
            return "classpath:schema/integration/ResearchStaff.xsd";
        }        
        return null;
    }
    
    
    /**
	 * Supress the validation in the following cases.
	 *   1 - When the current page is the final summary page.
	 */
	
	@Override
    protected boolean suppressValidation(final HttpServletRequest request) {

        //check current page and next page
        int currPage = getCurrentPage(request);

        return currPage == 2;

    }
    

    private ImportCommand createCommandObject() {
        ImportCommand ic = new ImportCommand();
        return ic;
    }
    
    public StudyDao getStudyDao() {
        return studyDao;
    }

    public void setStudyDao(StudyDao studyDao) {
        this.studyDao = studyDao;
    }

    public OrganizationDao getOrganizationDao() {
        return organizationDao;
    }

    public void setOrganizationDao(final OrganizationDao organizationDao) {
        this.organizationDao = organizationDao;
    }

    public AgentDao getAgentDao() {
        return agentDao;
    }

    public void setAgentDao(AgentDao agentDao) {
        this.agentDao = agentDao;
    }

    public MedDRADao getMeddraDao() {
        return meddraDao;
    }

    public void setMeddraDao(MedDRADao meddraDao) {
        this.meddraDao = meddraDao;
    }

    public CtcDao getCtcDao() {
        return ctcDao;
    }

    public void setCtcDao(CtcDao ctcDao) {
        this.ctcDao = ctcDao;
    }

    public NowFactory getNowFactory() {
        return nowFactory;
    }

    public void setNowFactory(NowFactory nowFactory) {
        this.nowFactory = nowFactory;
    }

    public ExpeditedAdverseEventReportDao getExpeditedAdverseEventReportDao() {
        return expeditedAdverseEventReportDao;
    }

    public void setExpeditedAdverseEventReportDao(
                    ExpeditedAdverseEventReportDao expeditedAdverseEventReportDao) {
        this.expeditedAdverseEventReportDao = expeditedAdverseEventReportDao;
    }

}
