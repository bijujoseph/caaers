package gov.nih.nci.cabig.caaers.web.participant;

//java imports
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
// java servlet imports
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// commons imports 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
// spring mvc imports  
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
// caaers imports
import gov.nih.nci.cabig.caaers.dao.ParticipantDao;
import gov.nih.nci.cabig.caaers.dao.StudySiteDao;
import gov.nih.nci.cabig.caaers.domain.Participant;
import gov.nih.nci.cabig.caaers.domain.Study;
import gov.nih.nci.cabig.caaers.domain.Identifier;
import gov.nih.nci.cabig.caaers.domain.StudySite;
import gov.nih.nci.cabig.caaers.service.StudyService;
import gov.nih.nci.cabig.caaers.web.tabbedflow.AbstractTabbedFlowFormController;
import gov.nih.nci.cabig.caaers.web.tabbedflow.Flow;
import gov.nih.nci.cabig.caaers.web.ListValues;
import gov.nih.nci.cabig.caaers.web.ControllerTools;

public class CreateParticipantController extends AbstractTabbedFlowFormController<NewParticipantCommand> {
    private static Log log = LogFactory.getLog(CreateParticipantController.class);
    private StudySiteDao studySiteDao;
    private StudyService studyService;
    private ParticipantDao participantDao;
    private ListValues listValues;
    
    public ListValues getListValues() {
		return listValues;
	}
    
    public void setListValues(ListValues listValues) {
		this.listValues = listValues;
	}
    
    public ParticipantDao getParticipantDao() {
		return participantDao;
	}
    
    public void setParticipantDao(ParticipantDao participantDao) {
		this.participantDao = participantDao;
	}

	public StudyService getStudyService() {
		return studyService;
	}

	public void setStudyService(StudyService studyService) {
		this.studyService = studyService;
	}
    
    public StudySiteDao getStudySiteDao() {
		return studySiteDao;
	}

	public void setStudySiteDao(StudySiteDao studySiteDao) {
		this.studySiteDao = studySiteDao;
	}
    
    public CreateParticipantController() {
        setCommandClass(NewParticipantCommand.class);
        setFlow(new Flow<NewParticipantCommand>("Create Participant"));
        getFlow().addTab(new Tab("Enter Participant Information", "New Participant", "par/par_create_participant") {
            public Map<String, Object> referenceData() {
                Map<String, Object> refdata = super.referenceData();
                refdata.put("genders", listValues.getParticipantGender());
                refdata.put("ethnicity", listValues.getParticipantEthnicity());
                refdata.put("sources", listValues.getParticipantIdentifierSource());
                refdata.put("race", listValues.getParticipantRace());
                refdata.put("action", "New");
                return refdata;
            }
            
            @Override
            public void validate(NewParticipantCommand command, Errors errors) {
                boolean firstName = command.getFirstName() == null || command.getFirstName().equals("");
                boolean lastName = command.getLastName() == null || command.getLastName().equals("");
                boolean dateOfBirth = command.getDateOfBirth() == null;
                if (firstName) errors.rejectValue("firstName", "REQUIRED", "Missing First Name");
                if (lastName) errors.rejectValue("lastName", "REQUIRED", "Missing Last Name");
                if (dateOfBirth) errors.rejectValue("dateOfBirth", "REQUIRED", "Missing Date Of Birth");
            }
            
            @Override
            public boolean isAllowDirtyForward() {
                return false;
            }
        });
        getFlow().addTab(new Tab("Choose Study", "Choose Study", "par/par_choose_study") {
            public Map<String, Object> referenceData() {
                Map<String, Object> refdata = super.referenceData();
                refdata.put("searchType", listValues.getStudySearchType());
                return refdata;
            }
            @Override
            public void validate(NewParticipantCommand command, Errors errors) {
                boolean studySiteArray = command.getStudySiteArray() == null || command.getStudySiteArray().length ==0;
                if (studySiteArray) errors.rejectValue("studySiteArray", "REQUIRED", "Please Select a Study to Continue");
            }
            
            /*
            @Override
            public boolean isAllowDirtyForward() {
                return false;
            }
            */
            
        });
        getFlow().addTab(new Tab("Review and Submit", "Review and Submit", "par/par_confirmation") {
            public Map<String, Object> referenceData() {
                Map<String, Object> refdata = super.referenceData();
                return refdata;
            }
        });
        //getFlow().addTab(new Tab("Confirmation", "Confirmation", "par/par_confirmation"));
    }
    
    protected void initBinder(HttpServletRequest request,
            ServletRequestDataBinder binder) throws Exception {
            binder.registerCustomEditor(Date.class, ControllerTools.getDateEditor(true));
            super.initBinder(request, binder);
        }
    
    
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
    	log.debug("Entering formBackingObject ...");
        NewParticipantCommand participantCommand = new NewParticipantCommand(); 
        for (int i = 0; i < 5; i++) {
        	participantCommand.getIdentifiers().add(new Identifier());
		}
		return participantCommand;
    }
 
    
    protected void onBind(HttpServletRequest request, Object command,BindException errors)throws Exception {
    	log.debug("Entering onBind...");
    	NewParticipantCommand participantCommand = (NewParticipantCommand)command;
    	String searchtext = participantCommand.getSearchText();
    	String type       = participantCommand.getSearchType();
    	List<StudySite> studySites = new ArrayList<StudySite>();
    	// This will happen on page #2
    	if (searchtext != null && type != null && !searchtext.equals(""))
    	{
    		log.debug("Search text : " + searchtext + "Type : " + type);
    		Study  study = new Study();
    		participantCommand.setStudies(new ArrayList<Study>());
    		if ("st".equals(type))
    			study.setShortTitle(searchtext);
    		else if ("lt".equals(type))
    			study.setLongTitle(searchtext);
    		else if ("d".equals(type))
    			study.setDescription(searchtext);
    		else if ("psc".equals(type))
    			study.setPrimarySponsorCode(searchtext);
    		else if ("pc".equals(type))
    			study.setPhaseCode(searchtext);

    		List<Study> studies = studyService.search(study);
    		participantCommand.setStudies(studies);
    		participantCommand.setSearchTypeText("");
    		participantCommand.setSearchType("");
    	}
    	// This will happen everytime studySiteArray is populated
    	if (participantCommand.getStudySiteArray() != null) {
			for (String st : participantCommand.getStudySiteArray()) {
				StudySite studySite = studySiteDao.getById(Integer.parseInt(st));
				studySites.add(studySite);
			}
			participantCommand.setStudySites(studySites);
		}	
    }
    
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
    	log.debug("Entering Process Finish ...");
    	NewParticipantCommand participantCommand = (NewParticipantCommand)command;
    	Participant participant = participantCommand.createParticipant();
        participantDao.save(participant);
        
        ModelAndView modelAndView = new ModelAndView("par/par_confirm");
        modelAndView.addObject("participant", participant);
		modelAndView.addAllObjects(errors.getModel());
		response.sendRedirect("view?participantId=" + participant.getId() + "&type=confirm");
		return null;
		//return modelAndView;
    }

    private static class Tab extends gov.nih.nci.cabig.caaers.web.tabbedflow.Tab<NewParticipantCommand> {
        public Tab(String longTitle, String shortTitle, String viewName) {
            super(longTitle, shortTitle, viewName);
        }
    }
}
