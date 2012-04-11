package gov.nih.nci.cabig.caaers.service;

import gov.nih.nci.cabig.caaers.dao.AdverseEventDao;
import gov.nih.nci.cabig.caaers.dao.NotificationDao;
import gov.nih.nci.cabig.caaers.dao.ObservedAdverseEventProfileDao;
import gov.nih.nci.cabig.caaers.dao.RuleSetDao;
import gov.nih.nci.cabig.caaers.dao.StudyParticipantAssignmentDao;
import gov.nih.nci.cabig.caaers.dao.query.NotificationQuery;
import gov.nih.nci.cabig.caaers.domain.AbstractStudyInterventionExpectedAE;
import gov.nih.nci.cabig.caaers.domain.AdverseEvent;
import gov.nih.nci.cabig.caaers.domain.CodedGrade;
import gov.nih.nci.cabig.caaers.domain.CtcTerm;
import gov.nih.nci.cabig.caaers.domain.Grade;
import gov.nih.nci.cabig.caaers.domain.Notification;
import gov.nih.nci.cabig.caaers.domain.NotificationStatus;
import gov.nih.nci.cabig.caaers.domain.ObservedAdverseEventProfile;
import gov.nih.nci.cabig.caaers.domain.RuleSet;
import gov.nih.nci.cabig.caaers.domain.Study;
import gov.nih.nci.cabig.caaers.domain.TreatmentAssignment;
import gov.nih.nci.cabig.caaers.domain.dto.SafetyRuleEvaluationResultDTO;
import gov.nih.nci.cabig.caaers.tools.mail.CaaersJavaMailSender;
import gov.nih.nci.cabig.ctms.domain.DomainObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SafetyMonitoringServiceImpl implements SafetyMonitoringService {
	
	private AdverseEventDao adverseEventDao;
	
	private ObservedAdverseEventProfileDao observedAdverseEventProfileDao;
	
	private EvaluationService evaluationService;
	
	private FreeMarkerService freeMarkerService;
	
	private CaaersJavaMailSender mailer;
	
	private NotificationDao notificationDao;
	
	private RuleSetDao ruleSetDao;
	
	private StudyParticipantAssignmentDao studyParticipantAssignmentDao;

	public void generateSafetyAlerts() {
		List<Study> studies = getStudiesForSafetySignalling();
		
		for(Study study: studies){
			generateSafetyAlerts(study);
		}
	}
	
	private void generateSafetyAlerts(Study study){
		List<ObservedAdverseEventProfile> notifiables = new ArrayList<ObservedAdverseEventProfile>();
		SafetySignallingAEHelper safetySignallingAEHelper = new SafetySignallingAEHelper(study);
		for(TreatmentAssignment treatmentAssignment : safetySignallingAEHelper.getTACs()){
			for(DomainObject term : safetySignallingAEHelper.getTerms(treatmentAssignment)){
				for (Grade grade: getValidGrades(term)){
					ObservedAdverseEventProfile observedAdverseEventProfile = getObservedAdverseEventProfile(safetySignallingAEHelper, treatmentAssignment, term, grade);
					if(isNotifiable(observedAdverseEventProfile)){
						notifiables.add(observedAdverseEventProfile);
					}
				}
				ObservedAdverseEventProfile observedAdverseEventProfile = getObservedAdverseEventProfile(safetySignallingAEHelper, treatmentAssignment, term, null);
				if(isNotifiable(observedAdverseEventProfile)){
					notifiables.add(observedAdverseEventProfile);
				}
			}
		}
		sendSafetySignalNitifcation(study, notifiables);
	}
	
	private boolean isNotifiable(ObservedAdverseEventProfile observedAdverseEventProfile){
		boolean notify = false;
		SafetyRuleEvaluationResultDTO dto = evaluationService.evaluateSafetySignallingRules(observedAdverseEventProfile);
		if(dto.getNotificationStatus() == NotificationStatus.NOTIFY){
			notify = true;
		}
		observedAdverseEventProfile.setNotificationStatus(dto.getNotificationStatus());
		observedAdverseEventProfileDao.save(observedAdverseEventProfile);
		return notify;
	}
	
	private ObservedAdverseEventProfile getObservedAdverseEventProfile(SafetySignallingAEHelper safetySignallingAEHelper, TreatmentAssignment ta, DomainObject term, Grade grade){
		ObservedAdverseEventProfile observedAdverseEventProfile = safetySignallingAEHelper.getObservedAdverseEventProfile(ta, term, grade);
		if(observedAdverseEventProfile == null){
			observedAdverseEventProfile = new ObservedAdverseEventProfile(ta, term, grade);
		}
		AbstractStudyInterventionExpectedAE expectedProfile = safetySignallingAEHelper.getExpectedAdverseEventProfile(ta, term);
		observedAdverseEventProfile.setExpectedFrequency(expectedProfile.getFrequency(grade));
		observedAdverseEventProfile.setObservedNoOfAE(safetySignallingAEHelper.getFrequency(ta, term, grade));
		observedAdverseEventProfile.setTotalNoOfRegistrations(safetySignallingAEHelper.getFrequency(ta));
		observedAdverseEventProfile.calculateStatistics();
		return observedAdverseEventProfile;
	}
	
	private void sendSafetySignalNitifcation(Study study, List<ObservedAdverseEventProfile> notifiables){
		NotificationQuery query = new NotificationQuery();
		query.filterByStudyId(study.getId());
		List<Notification> notifications = (List<Notification>) notificationDao.search(query);
		if(notifiables.size() > 0 && notifications.size() > 0){
			String rawTemplate = notifications.get(0).getContent();
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("observed", notifiables);
			String notificationString = freeMarkerService.applyRuntimeReplacementsForReport(rawTemplate, map);
			mailer.sendMail((String[])notifications.get(0).getRecipientEmails().toArray(), notifications.get(0).getSubject(), notificationString, new String[]{});
		}
	}
	
	private Grade[] getValidGrades(DomainObject term){
		if (term instanceof CtcTerm){
			List<CodedGrade> grades1 = ((CtcTerm)term).getGrades();
			List<Grade> grades = new ArrayList<Grade>();
			for(CodedGrade grade: grades1){
				if(grade.getCode() >= 1 && grade.getCode() <=5){
					grades.add((Grade)grade);
				}
			}
			return grades.toArray(new Grade[0]);
			
		}else {
			return new Grade[]{Grade.getByCode(1), Grade.getByCode(2), Grade.getByCode(3), Grade.getByCode(4), Grade.getByCode(5)};
		}
	}
	
	private List<Study> getStudiesForSafetySignalling(){
		List<RuleSet> ruleSets= ruleSetDao.getRuleSetForSafetySignalling();
		Set<Study> studies = new HashSet<Study>();
		for(RuleSet ruleSet : ruleSets){
			studies.add(ruleSet.getStudy());
		}
		return new ArrayList<Study>(studies); 
	}
	
	class SafetySignallingAEHelper{
		
		Map<String, TreatmentAssignment> treatmentAssignmentsMap = new HashMap<String, TreatmentAssignment>();
		Map<String, Set<DomainObject>> treatmentAssignmentsTermsMap = new HashMap<String, Set<DomainObject>>();
		Map<String, ObservedAdverseEventProfile> observedAEProfileMap = new HashMap<String, ObservedAdverseEventProfile>();
		Map<String, AbstractStudyInterventionExpectedAE> expectedAEProfileMap = new HashMap<String, AbstractStudyInterventionExpectedAE>();
		Map<String, Integer> aeFrequencyMap = new HashMap<String, Integer>();
		Map<String, Integer> tacRegistrationCountMap = new HashMap<String, Integer>();

		public SafetySignallingAEHelper(Study s) {
			super();
			setupAEs(adverseEventDao.getAllAEsForSafetySignaling(s));
			setupObservedAEs();
			setupTACRegistrationCount();
		}
		
		private void setupAEs(List aeList){
			for(Object item: aeList){
				AdverseEvent ae = (AdverseEvent)((Object[])item)[0];
				AbstractStudyInterventionExpectedAE expectedProfile = (AbstractStudyInterventionExpectedAE)((Object[])item)[1];
				TreatmentAssignment tac = (TreatmentAssignment)((Object[])item)[2];
				if (treatmentAssignmentsMap.get("TAC_"+tac.getId()) == null){
					treatmentAssignmentsMap.put("TAC_"+tac.getId(), tac);
					treatmentAssignmentsTermsMap.put("TAC_"+tac.getId(), new HashSet<DomainObject>());
				}
				treatmentAssignmentsTermsMap.get("TAC_"+tac.getId()).add(expectedProfile.getTerm());
				if(expectedAEProfileMap.get(key(tac, expectedProfile.getTerm(), null)) == null){
					expectedAEProfileMap.put(key(tac, expectedProfile.getTerm(), null), expectedProfile);
				}
				if(aeFrequencyMap.get(key(tac, expectedProfile.getTerm(), ae.getGrade())) == null){
					aeFrequencyMap.put(key(tac, expectedProfile.getTerm(), ae.getGrade()), 0);
				}
				if(aeFrequencyMap.get(key(tac, expectedProfile.getTerm(), null)) == null){
					aeFrequencyMap.put(key(tac, expectedProfile.getTerm(), null), 0);
				}
				Integer i = aeFrequencyMap.get(key(tac, expectedProfile.getTerm(), ae.getGrade())) + 1;
				aeFrequencyMap.put(key(tac, expectedProfile.getTerm(), ae.getGrade()), i);
				i = aeFrequencyMap.get(key(tac, expectedProfile.getTerm(), null)) + 1;
				aeFrequencyMap.put(key(tac, expectedProfile.getTerm(), null), i);
			}
		}
		
		private void setupObservedAEs(){
			List<ObservedAdverseEventProfile> observedProfiles = observedAdverseEventProfileDao.getByTACs(getTACs());
			for(ObservedAdverseEventProfile observedProfile: observedProfiles){
				observedAEProfileMap.put(key(observedProfile), observedProfile);
			}
		}
		
		private void setupTACRegistrationCount(){
			for(TreatmentAssignment tac: getTACs()){
				if(tacRegistrationCountMap.get("TAC_"+tac.getId()) == null){
					tacRegistrationCountMap.put("TAC_"+tac.getId(), studyParticipantAssignmentDao.getCountByTAC(tac.getId()));
				}
			}
		}
		
		public TreatmentAssignment[] getTACs(){
			return treatmentAssignmentsMap.values().toArray(new TreatmentAssignment[0]);
		}
		
		public DomainObject[] getTerms(TreatmentAssignment tac){
			return treatmentAssignmentsTermsMap.get("TAC_"+tac.getId()).toArray(new DomainObject[0]);
		}
		
		public ObservedAdverseEventProfile getObservedAdverseEventProfile(TreatmentAssignment tac, DomainObject term, Grade grade){
			return observedAEProfileMap.get(key(tac, term, grade));
		}
		
		public AbstractStudyInterventionExpectedAE getExpectedAdverseEventProfile(TreatmentAssignment tac, DomainObject term){
			return expectedAEProfileMap.get(key(tac, term, null));
		}
		
		public Integer getFrequency(TreatmentAssignment tac, DomainObject term, Grade grade){
			return aeFrequencyMap.get(key(tac, term, grade));
		}
		
		public Integer getFrequency(TreatmentAssignment tac){
			return tacRegistrationCountMap.get("TAC_"+tac.getId());
		}
		
		private String getTermLabel(DomainObject term){
			if (term instanceof CtcTerm) return "CTC";
			else return "MEDRA";
		}
		
		private String key(ObservedAdverseEventProfile observedProfile){
			return key(observedProfile.getTreatmentAssignment(), observedProfile.getTerm(), observedProfile.getGrade());
		}
		private String key(TreatmentAssignment tac, DomainObject term, Grade grade){
			String key = "TAC_"+tac.getId()+"_"+getTermLabel(term)+"TERM_"+term.getId();
			if(grade != null){
				key+= "_"+grade.getCode();
			}
			return key;
		}
	}

	public void setAdverseEventDao(AdverseEventDao adverseEventDao) {
		this.adverseEventDao = adverseEventDao;
	}

	public void setObservedAdverseEventProfileDao(
			ObservedAdverseEventProfileDao observedAdverseEventProfileDao) {
		this.observedAdverseEventProfileDao = observedAdverseEventProfileDao;
	}

	public void setEvaluationService(EvaluationService evaluationService) {
		this.evaluationService = evaluationService;
	}

	public void setFreeMarkerService(FreeMarkerService freeMarkerService) {
		this.freeMarkerService = freeMarkerService;
	}

	public void setMailer(CaaersJavaMailSender mailer) {
		this.mailer = mailer;
	}

	public void setNotificationDao(NotificationDao notificationDao) {
		this.notificationDao = notificationDao;
	}

	public void setRuleSetDao(RuleSetDao ruleSetDao) {
		this.ruleSetDao = ruleSetDao;
	}

	public void setStudyParticipantAssignmentDao(
			StudyParticipantAssignmentDao studyParticipantAssignmentDao) {
		this.studyParticipantAssignmentDao = studyParticipantAssignmentDao;
	}
	
	

}