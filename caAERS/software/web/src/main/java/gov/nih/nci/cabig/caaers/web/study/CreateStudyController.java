/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.web.study;

import gov.nih.nci.cabig.caaers.domain.*;
import gov.nih.nci.cabig.caaers.security.SecurityUtils;
import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.FlowFactory;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang.StringUtils;

/**
 * Study Controller for 'Create' Workflow
 * 
 */
public class CreateStudyController extends StudyController<StudyCommand> {

    /**
     * Flow pages build
     * */
    @Override
    public FlowFactory<StudyCommand> getFlowFactory() {
        return new FlowFactory<StudyCommand>() {
            public Flow<StudyCommand> createFlow(StudyCommand cmd) {
                Flow<StudyCommand> flow = new Flow<StudyCommand>("Create Study");

                if (cmd.getStudyCreator())
                    flow.addTab(new DetailsTab());

                if(cmd.getSupplementalInfoManager()){
                    flow.addTab(new AgentsTab());
                    flow.addTab(new TreatmentAssignmentTab());
                    flow.addTab(new DiseaseTab());
                    flow.addTab(new SolicitedAdverseEventTab());
                    flow.addTab(new ExpectedAEsTab());
                }
                
                if(SecurityUtils.checkAuthorization(UserGroupType.study_site_participation_administrator)){
                    flow.addTab(new SitesTab());
                }

                if(SecurityUtils.checkAuthorization(UserGroupType.study_team_administrator)){
                    flow.addTab(new InvestigatorsTab());
                    flow.addTab(new PersonnelTab());
                }
                
                if(cmd.getSupplementalInfoManager()){
                    flow.addTab(new IdentifiersTab());
                }

                flow.addTab(new EmptyStudyTab("Overview", "Overview", "study/study_reviewsummary"));
                return flow;
            }
        };

    }

    /**
     * Creates an Study(empty study), with a empty Sponsor,CoordinatingCenter and Identifiers.
     */
    @Override
    protected Object formBackingObject(final HttpServletRequest request) throws ServletException {

        request.getSession().removeAttribute(getReplacedCommandSessionAttributeName(request));
        request.getSession().removeAttribute(CreateStudyAjaxFacade.CREATE_STUDY_FORM_NAME);
        
        StudyCommand command = new StudyCommand(studyDao, investigationalNewDrugDao);
        command.setMustFireEvent(true);
        Study study = new LocalStudy(); 
        study.setDataEntryStatus(false);
        study.setAeTermUnique(null);
        command.setStudy(study);

        StudyFundingSponsor sponsor = new StudyFundingSponsor();
        sponsor.setPrimary(true);
        study.addStudyFundingSponsor(sponsor);

        StudyCoordinatingCenter cordinatCenter = new StudyCoordinatingCenter();
        study.addStudyOrganization(cordinatCenter);

        OrganizationAssignedIdentifier sponsorIdentifier = new OrganizationAssignedIdentifier();
        sponsorIdentifier.setType(OrganizationAssignedIdentifier.SPONSOR_IDENTIFIER_TYPE);
        study.addIdentifier(sponsorIdentifier);
        sponsorIdentifier.setPrimaryIndicator(true);

        OrganizationAssignedIdentifier ccIdentifier = new OrganizationAssignedIdentifier();
        ccIdentifier.setPrimaryIndicator(false);
        ccIdentifier.setType(OrganizationAssignedIdentifier.COORDINATING_CENTER_IDENTIFIER_TYPE);
        study.addIdentifier(ccIdentifier);
        study.initializeEpocsIfNecessary();
        
        command.setWorkflowEnabled(getConfiguration().get(getConfiguration().ENABLE_WORKFLOW));
        command.setAllPersonnelRoles(configPropertyRepository.getByType(ConfigPropertyType.RESEARCH_STAFF_ROLE_TYPE));
        command.setAllInvestigatorRoles(configPropertyRepository.getByType(ConfigPropertyType.INVESTIGATOR_ROLE_TYPE));
        command.populateRoleNamesMap();
        command.setStudyRepository(this.getStudyRepository());

        command.setPrevFS(command.getStudy().getPrimaryFundingSponsor());
        command.setPrevCC(command.getStudy().getStudyCoordinatingCenter());
        
        return command;
    }

    @Override
    protected ModelAndView processFinish(final HttpServletRequest request, final HttpServletResponse response, final Object command, final BindException errors) throws Exception {

        StudyCommand studyCommand = (StudyCommand) command;
        studyCommand.save();

        //fire the modification event
        getEventFactory().publishEntityModifiedEvent(studyCommand.getStudy());

        ModelAndView mv = new ModelAndView("forward:view?type=confirm", errors.getModel());

        return mv;
    }

    @Override
    protected StudyCommand save(StudyCommand command, Errors errors) {
        StudyCommand c = super.save(command, errors);
        if(errors.hasErrors()) return c;
        // Fire the event for re-indexing
        if (command.isMustFireEvent()) {
            if (getEventFactory() != null) getEventFactory().publishEntityModifiedEvent(command.getStudy());
            command.setMustFireEvent(false);
        }
        return c;
    }
}
