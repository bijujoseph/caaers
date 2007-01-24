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
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
// caaers imports
import gov.nih.nci.cabig.caaers.dao.ParticipantDao;
import gov.nih.nci.cabig.caaers.dao.StudySiteDao;
import gov.nih.nci.cabig.caaers.dao.StudyDao;
import gov.nih.nci.cabig.caaers.domain.Participant;
import gov.nih.nci.cabig.caaers.domain.Study;
import gov.nih.nci.cabig.caaers.domain.Identifier;
import gov.nih.nci.cabig.caaers.domain.StudyParticipantAssignment;
import gov.nih.nci.cabig.caaers.domain.StudySite;
import gov.nih.nci.cabig.caaers.service.ParticipantService;
import gov.nih.nci.cabig.caaers.service.StudyService;
import gov.nih.nci.cabig.caaers.web.tabbedflow.AbstractTabbedFlowFormController;
import gov.nih.nci.cabig.caaers.web.tabbedflow.Flow;
import gov.nih.nci.cabig.caaers.web.tabbedflow.Tab;
import gov.nih.nci.cabig.caaers.web.ListValues;
import gov.nih.nci.cabig.caaers.web.ControllerTools;

public class AssignParticipantStudyController extends AbstractTabbedFlowFormController<AssignParticipantStudyCommand> {
    private static Log log = LogFactory.getLog(AssignParticipantStudyController.class);
    private StudySiteDao studySiteDao;
    private StudyDao studyDao;
    private StudyService studyService;
    private ParticipantService participantService;
    private ParticipantDao participantDao;
    protected ListValues listValues;
    
    
    public StudyDao getStudyDao() {
		return studyDao;
	}
    
    public void setStudyDao(StudyDao studyDao) {
		this.studyDao = studyDao;
	}
    
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
	
	public ParticipantService getParticipantService() {
		return participantService;
	}
	
	public void setParticipantService(ParticipantService participantService) {
		this.participantService = participantService;
	}
    
    public StudySiteDao getStudySiteDao() {
		return studySiteDao;
	}

	public void setStudySiteDao(StudySiteDao studySiteDao) {
		this.studySiteDao = studySiteDao;
	}
    
    public AssignParticipantStudyController() {
    	/*
        setCommandClass(AssignParticipantStudyCommand.class);
        setFlow(new Flow<AssignParticipantStudyCommand>("Assign Participant to Study"));
        getFlow().addTab(new Tab("Search for Studies", "Search Study", "par/reg_protocol_search") {
            public Map<String, Object> referenceData() {
                Map<String, Object> refdata = super.referenceData();
                refdata.put("studySearchType", listValues.getStudySearchType());
                return refdata;
            }
        });
        getFlow().addTab(new Tab("Choose Study", "Search Participant", "par/reg_participant_search") {
            public Map<String, Object> referenceData() {
                Map<String, Object> refdata = super.referenceData();
                refdata.put("participantSearchType", listValues.getParticipantSearchType());
                return refdata;
            }
        });
        getFlow().addTab(new Tab("Review and Submit", "Review and Submit", "par/reg_review_submit") {
            public Map<String, Object> referenceData() {
                Map<String, Object> refdata = super.referenceData();
                return refdata;
            }
        });
        */
    }
    
    protected void initBinder(HttpServletRequest request,
            ServletRequestDataBinder binder) throws Exception {
            binder.registerCustomEditor(Date.class, ControllerTools.getDateEditor(true));
            super.initBinder(request, binder);
        }
    
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
    	log.debug("Entering formBackingObject ...");
    	AssignParticipantStudyCommand participantCommand = new AssignParticipantStudyCommand(); 
        StudySite studySite=null;
        for (int i = 0; i < 5; i++) {
        	participantCommand.getIdentifiers().add(new Identifier());
		}
        if(request.getParameter("studySiteId")!=null){
    
			log.debug("Parameters found as.."+request.getParameter("studySiteId"));
			studySite=studySiteDao.getById(Integer.parseInt(request.getParameter("studySiteId")));
			Study study = studySite.getStudy();
			study.getStudySites().get(0).getId(); // this code is to initialize object 
			
			String[ ] studySiteArray = new String[1];
			studySiteArray[0] = request.getParameter("studySiteId");
			participantCommand.setStudySiteArray(studySiteArray);
			participantCommand.getStudies().add(study);
			//participantCommand.getStudyParticipantAssignment().setStudySite(studySite);
			//participantCommand.getStudyParticipantAssignment().setDateOfEnrollment(new Date());
		}
		return participantCommand;
    }
 
    
    protected void onBind(HttpServletRequest request, Object command,BindException errors)throws Exception {
    	log.debug("Entering onBind...");
    	AssignParticipantStudyCommand apsCommand = (AssignParticipantStudyCommand)command;
    	String searchtext = apsCommand.getStudyText();
    	String type       = apsCommand.getStudyType();
    	String psearchtext= apsCommand.getParticipantText();
    	String ptype      = apsCommand.getParticipantType();
    	List<StudySite> studySites = new ArrayList<StudySite>();
    	
    	
    	// PAGE 1
    	if (searchtext != null && type != null && !searchtext.equals(""))
    	{
    		log.debug("Search text : " + searchtext + "Type : " + type);
    		Study  study = new Study();
    		apsCommand.setStudies(new ArrayList<Study>());
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
    		apsCommand.setStudies(studies);
    		apsCommand.setStudyText("");
    	}
    	
    	// PAGE 2
    	if (apsCommand.getStudyId() != null && !apsCommand.getStudyId().equals(""))
    	{
    		log.debug("The StudyId is : " + apsCommand.getStudyId());
    		Study study = studyDao.getById(Integer.parseInt(apsCommand.getStudyId()));
			studySites.add(study.getStudySites().get(0));
			apsCommand.setStudySites(studySites);
			//apsCommand.setStudyId("");
    		
    	}
    	// PAGE 2
    	if (psearchtext != null && ptype != null && !psearchtext.equals("")) {
			
    		log.debug("Search text : " + searchtext + "Type : " + type);
    		Participant participant = new Participant();
    		
    		if ("fn".equals(ptype))
				participant.setFirstName(psearchtext);
			else if ("ln".equals(ptype))
				participant.setLastName(psearchtext);
			else if ("g".equals(ptype))
				participant.setGender(psearchtext);
			else if ("r".equals(ptype))
				participant.setRace(psearchtext);
			else if ("e".equals(ptype))
				participant.setEthnicity(psearchtext);
    		
    		List<Participant> participants = participantService.search(participant);
    		apsCommand.setParticipantSearchResults(participants);
    		apsCommand.setParticipantText("");
		}
    	// PAGE 3
    	if (apsCommand.getParticipantId() != null && !apsCommand.getParticipantId().equals(""))
    	{
    		log.debug("The ParticipantId is : " + apsCommand.getParticipantId());
    		Participant participant = participantDao.getById(Integer.parseInt(apsCommand.getParticipantId()));
    		participant.getAssignments().size();
    		List<Participant> participants = new ArrayList<Participant>();
    		participants.add(participant);
			apsCommand.setParticipants(participants);
			//apsCommand.setParticipantId("");
    		
    	}
    }
    
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
    	log.debug("Entering Process Finish ...");
    	AssignParticipantStudyCommand apsCommand = (AssignParticipantStudyCommand)command;
    	
    	StudyParticipantAssignment spa = new StudyParticipantAssignment();
    	spa.setDateOfEnrollment(new Date());
    	spa.setParticipant(apsCommand.getParticipants().get(0));
    	spa.setStudySite(apsCommand.getStudySites().get(0));
    	//Participant participant = participantCommand.createParticipant();
        Participant participant = apsCommand.getParticipants().get(0);
        participant.addAssignment(spa);
    	participantDao.save(participant);
        
        ModelAndView modelAndView = new ModelAndView("par/par_confirm");
        modelAndView.addObject("participant", participant);
		modelAndView.addAllObjects(errors.getModel());
		response.sendRedirect("view?participantId=" + participant.getId() + "&type=confirm");
		return null;
		//return modelAndView;
    }

    protected static class Tab extends gov.nih.nci.cabig.caaers.web.tabbedflow.Tab<AssignParticipantStudyCommand> {
        public Tab(String longTitle, String shortTitle, String viewName) {
            super(longTitle, shortTitle, viewName);
        }
    }
}
