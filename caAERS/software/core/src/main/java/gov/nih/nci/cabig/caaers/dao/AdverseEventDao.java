/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.dao;

import gov.nih.nci.cabig.caaers.dao.query.AdverseEventQuery;
import gov.nih.nci.cabig.caaers.dao.query.SafetySignalingQuery;
import gov.nih.nci.cabig.caaers.domain.*;
import gov.nih.nci.cabig.caaers.domain.report.Report;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class implements the Data access related operations for the AdverseEvent domain object.
 * 
 * @author Rhett Sutphin
 */
@Transactional(readOnly = true)
public class AdverseEventDao extends CaaersDao<AdverseEvent> {
	
	private StudyParticipantAssignmentDao studyParticipantAssignmentDao;

    @Override
    @Transactional(readOnly = true, propagation= Propagation.NOT_SUPPORTED)
    public Class<AdverseEvent> domainClass() {
        return AdverseEvent.class;
    }

   
    /**
     * Save the Adverse Event.
     * 
     * @param event
     *                The event to be saved.
     */
    @Transactional(readOnly = false)
    public void save(final AdverseEvent event) {
        getHibernateTemplate().saveOrUpdate(event);
    }


    public List<AdverseEvent> getByParticipant(Participant participant) {		
		DetachedCriteria criteria = DetachedCriteria.forClass(AdverseEvent.class);		
		criteria.createCriteria("reportingPeriod").createCriteria("assignment")
		.createCriteria("participant").add(getParticipantExample(participant));

		//.createCriteria("identifiers").add(getIdentifierExample(participant));
		return getHibernateTemplate().findByCriteria(criteria);	
	}

    public List<AdverseEvent> getByParticipant(Participant participant , AdverseEvent adverseEvent) {		
		DetachedCriteria criteria = DetachedCriteria.forClass(AdverseEvent.class);		
		criteria.add(getAdverseEventExample(adverseEvent)).createCriteria("reportingPeriod").createCriteria("assignment")
		.createCriteria("participant").add(getParticipantExample(participant));

		return getHibernateTemplate().findByCriteria(criteria);	
	}
    
	public List<AdverseEvent> getByStudy(Study study) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AdverseEvent.class);		
		criteria.createCriteria("reportingPeriod").createCriteria("assignment").createCriteria("studySite")
		.createCriteria("study").add(getStudyExample(study));

		return getHibernateTemplate().findByCriteria(criteria);	
	}

	public List<AdverseEvent> getByStudy(Study study, AdverseEvent adverseEvent) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AdverseEvent.class);		
		criteria.add(getAdverseEventExample(adverseEvent)).createCriteria("reportingPeriod").createCriteria("assignment").createCriteria("studySite")
		.createCriteria("study").add(getStudyExample(study));
		//if (study.getIdentifiers().size() > 0) {
			//criteria = criteria.createCriteria("identifiers").add(getIdentifierExample(study.getIdentifiers().get(0)));
		//}
		return getHibernateTemplate().findByCriteria(criteria);	
	}
	
	public List<AdverseEvent> getByAssignment(StudyParticipantAssignment assignment) { 
		DetachedCriteria criteria = DetachedCriteria.forClass(AdverseEvent.class);		
		criteria.createCriteria("reportingPeriod").createCriteria("assignment").add(getAssignmentExample(assignment));
		return getHibernateTemplate().findByCriteria(criteria);		
	}
	public List<AdverseEvent> getByStudyParticipant(Study study, Participant participant, AdverseEvent adverseEvent) {
		
		StudyParticipantAssignment assignment = studyParticipantAssignmentDao.getAssignment(participant, study);		
		DetachedCriteria criteria = DetachedCriteria.forClass(AdverseEvent.class);		
		criteria.add(getAdverseEventExample(adverseEvent)).createCriteria("reportingPeriod").createCriteria("assignment").add(getAssignmentExample(assignment));
		//if (study.getIdentifiers().size() > 0) {
			//criteria = criteria.createCriteria("identifiers").add(getIdentifierExample(study.getIdentifiers().get(0)));
		//}
		return getHibernateTemplate().findByCriteria(criteria);			
	}
	
	public List<AdverseEvent> getByStudyParticipant(Study study, Participant participant) {
		
		StudyParticipantAssignment assignment = studyParticipantAssignmentDao.getAssignment(participant, study);		
		DetachedCriteria criteria = DetachedCriteria.forClass(AdverseEvent.class);		
		criteria.createCriteria("reportingPeriod").createCriteria("assignment").add(getAssignmentExample(assignment));
		//if (study.getIdentifiers().size() > 0) {
			//criteria = criteria.createCriteria("identifiers").add(getIdentifierExample(study.getIdentifiers().get(0)));
		//}
		return getHibernateTemplate().findByCriteria(criteria);			
	}
	
	public List<AdverseEvent> getByAdverseEventReportingPeriod(AdverseEventReportingPeriod adverseEventReportingPeriod, Study study, Participant participant){
		StudyParticipantAssignment assignment = studyParticipantAssignmentDao.getAssignment(participant, study);
		DetachedCriteria criteria = DetachedCriteria.forClass(AdverseEvent.class);
		criteria.createCriteria("reportingPeriod").add(getReportingPeriodExample(adverseEventReportingPeriod)).createCriteria("assignment").add(getAssignmentExample(assignment));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List<AdverseEvent> getByAdverseEventReportingPeriod(AdverseEventReportingPeriod adverseEventReportingPeriod, Study study, Participant participant, AdverseEvent adverseEvent){
		StudyParticipantAssignment assignment = studyParticipantAssignmentDao.getAssignment(participant, study);
		DetachedCriteria criteria = DetachedCriteria.forClass(AdverseEvent.class);
		criteria.add(getAdverseEventExample(adverseEvent)).createCriteria("reportingPeriod").add(getReportingPeriodExample(adverseEventReportingPeriod)).createCriteria("assignment").add(getAssignmentExample(assignment));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List<AdverseEvent> getByReport(Report report){
		DetachedCriteria criteria = DetachedCriteria.forClass(AdverseEvent.class);
		criteria.createCriteria("reportingPeriod").createCriteria("aeReport").createCriteria("report").add(getReportExample(report));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List<AdverseEvent> getByReport(Report report, AdverseEvent adverseEvent){
		DetachedCriteria criteria = DetachedCriteria.forClass(AdverseEvent.class);
		criteria.add(getAdverseEventExample(adverseEvent)).createCriteria("reportingPeriod").createCriteria("aeReport").createCriteria("report").add(getReportExample(report));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}

    public List<AdverseEvent> getByAeReport(ExpeditedAdverseEventReport aeReport){
        AdverseEventQuery query = new AdverseEventQuery();
        query.filterByAeReportId(aeReport.getId());
        return (List<AdverseEvent> )search(query);
    }


	public List<AdverseEvent> findByExample(AdverseEvent caaersAe) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AdverseEvent.class);
		//AbstractAdverseEventTerm aeTerm = caaersAe.getAdverseEventCtcTerm();
		//CtcTerm term = null; 
		//if (aeTerm instanceof AdverseEventCtcTerm) {
			//term = ((AdverseEventCtcTerm)aeTerm).getCtcTerm();
		//}
		
		criteria.add(getAdverseEventExample(caaersAe));
		
		//if (caaersAe.getLowLevelTerm() != null) {
			//criteria.createCriteria("lowLevelTerm");
		//}
		
		return getHibernateTemplate().findByCriteria(criteria);
	}

	
	private Example getParticipantExample(Participant participant) {
		return addOptions(Example.create(participant));
	}
	
	private Example getStudyExample(Study study) {
		return addOptions(Example.create(study));
	}
	
	private Example getAdverseEventExample(AdverseEvent adverseEvent) {
		Example ex = Example.create(adverseEvent).excludeProperty("eventApproximateTime");
		if (adverseEvent.getEventApproximateTime().getHour() == null) {
//			System.out.println("excluding ....");
			ex.excludeProperty("eventApproximateTime");
		}
		ex.excludeProperty("retiredIndicator");
		ex.excludeProperty("solicited");
		//ex.excludeProperty("adverseEventTerm.term");
		return addOptions(ex);
	}
	
	private Example getAssignmentExample( StudyParticipantAssignment assignment) {
		return addOptions(Example.create(assignment));
	}
	
	private Example getReportingPeriodExample(AdverseEventReportingPeriod reportingPeriod){
		return addOptions(Example.create(reportingPeriod));
	}
	
	private Example getReportExample(Report report){
		return addOptions(Example.create(report));
	}
	
	private Example addOptions(Example example) {
		example.enableLike();
		example.ignoreCase();
		return example;
	}

	public void setStudyParticipantAssignmentDao(
			StudyParticipantAssignmentDao studyParticipantAssignmentDao) {
		this.studyParticipantAssignmentDao = studyParticipantAssignmentDao;
	}

	public List getAllAEsForSafetySignaling(Study study){
		SafetySignalingQuery safetyQuery = new SafetySignalingQuery(SafetySignalingQuery.TAC_EXPECTED_AE_PROFILE, SafetySignalingQuery.TAC, SafetySignalingQuery.STUDY_PARTICIPANT_ALIAS);
    	safetyQuery.joinStudy();
    	safetyQuery.joinStudyParticipantAssignment();
    	safetyQuery.joinTreatmentAssignmentExpectedAEProfile();
    	safetyQuery.joinAdverseEventTerm();
    	safetyQuery.filterByStudy(study);
    	safetyQuery.filterByMatchingTermsOnExpectedAEProfileAndReportedAE();
//    	System.out.println(safetyQuery.getQueryString());
    	return search(safetyQuery);
	}
   
}
