package gov.nih.nci.cabig.caaers.service;

import gov.nih.nci.cabig.caaers.CaaersTestCase;
import gov.nih.nci.cabig.caaers.domain.report.PlannedEmailNotification;
import gov.nih.nci.cabig.caaers.domain.report.Recipient;
import gov.nih.nci.cabig.caaers.domain.report.Report;
import gov.nih.nci.cabig.caaers.domain.report.ReportDefinition;
import gov.nih.nci.cabig.caaers.domain.report.RoleBasedRecipient;
import gov.nih.nci.cabig.caaers.domain.expeditedfields.ExpeditedReportSection;
import static gov.nih.nci.cabig.caaers.domain.expeditedfields.ExpeditedReportSection.*;
import gov.nih.nci.cabig.caaers.domain.ExpeditedAdverseEventReport;
import gov.nih.nci.cabig.caaers.domain.Fixtures;
import gov.nih.nci.cabig.caaers.domain.Investigator;
import gov.nih.nci.cabig.caaers.domain.OtherCause;
import gov.nih.nci.cabig.caaers.domain.Attribution;
import gov.nih.nci.cabig.caaers.domain.CtcTerm;
import gov.nih.nci.cabig.caaers.domain.CourseAgent;
import gov.nih.nci.cabig.caaers.domain.ConcomitantMedication;
import gov.nih.nci.cabig.caaers.domain.ResearchStaff;
import gov.nih.nci.cabig.caaers.domain.SiteInvestigator;
import gov.nih.nci.cabig.caaers.domain.Study;
import gov.nih.nci.cabig.caaers.domain.StudyInvestigator;
import gov.nih.nci.cabig.caaers.domain.StudyParticipantAssignment;
import gov.nih.nci.cabig.caaers.domain.StudyPersonnel;
import gov.nih.nci.cabig.caaers.domain.StudySite;
import gov.nih.nci.cabig.caaers.domain.SurgeryIntervention;
import gov.nih.nci.cabig.caaers.domain.RadiationIntervention;
import gov.nih.nci.cabig.caaers.domain.attribution.OtherCauseAttribution;
import gov.nih.nci.cabig.caaers.domain.attribution.AdverseEventAttribution;
import gov.nih.nci.cabig.caaers.domain.attribution.CourseAgentAttribution;
import gov.nih.nci.cabig.caaers.domain.attribution.DiseaseAttribution;
import gov.nih.nci.cabig.caaers.domain.attribution.ConcomitantMedicationAttribution;
import gov.nih.nci.cabig.caaers.domain.attribution.SurgeryAttribution;
import gov.nih.nci.cabig.caaers.domain.attribution.RadiationAttribution;
import gov.nih.nci.cabig.ctms.domain.DomainObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Rhett Sutphin
 */
public class ReportServiceTest extends CaaersTestCase {
    private static final Attribution[] SUFFICIENT_ATTRIBUTIONS
        = new Attribution[] { Attribution.POSSIBLE, Attribution.PROBABLE, Attribution.DEFINITE };
    private static final Attribution[] INSUFFICENT_ATTRIBUTIONS
        = new Attribution[] { Attribution.UNLIKELY, Attribution.UNRELATED };
    private static final String TERM = "Auralmonagem";

    private ReportService service;
    private ExpeditedAdverseEventReport expeditedData;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        service = new ReportServiceImpl();
        expeditedData = new ExpeditedAdverseEventReport();
        CtcTerm ctcTerm = new CtcTerm();
        ctcTerm.setTerm(TERM);
        expeditedData.getAdverseEvents().get(0).getAdverseEventCtcTerm().setCtcTerm(ctcTerm);
    }

    public void testValidateChecksForNoAttribution() throws Exception {
        ReportSubmittability actual = validateForAttribution();
        assertInsuffientAttributionMessage("No attributions should not be sufficent", actual);
    }

    public void testValidateAcceptsGrade3Through5ForOtherCause() throws Exception {
        OtherCause cause = new OtherCause();
        expeditedData.addOtherCause(cause);
        OtherCauseAttribution attr = createAttribution(cause, Attribution.POSSIBLE, OtherCauseAttribution.class);
        expeditedData.getAdverseEvents().get(0).getOtherCauseAttributions().add(attr);

        assertSufficientAttributionsAreSufficent(attr);
    }
    
    public void testValidateAcceptsGrade3Through5ForCourseAgent() throws Exception {
        CourseAgent cause = new CourseAgent();
        expeditedData.getTreatmentInformation().addCourseAgent(cause);
        CourseAgentAttribution attr = createAttribution(cause, Attribution.POSSIBLE, CourseAgentAttribution.class);
        expeditedData.getAdverseEvents().get(0).getCourseAgentAttributions().add(attr);

        assertSufficientAttributionsAreSufficent(attr);
    }

    public void testValidateAcceptsGrade3Through5ForConcomitantMedication() throws Exception {
        ConcomitantMedication cause = new ConcomitantMedication();
        expeditedData.addConcomitantMedication(cause);
        ConcomitantMedicationAttribution attr = createAttribution(cause, Attribution.UNRELATED, ConcomitantMedicationAttribution.class);
        expeditedData.getAdverseEvents().get(0).getConcomitantMedicationAttributions().add(attr);

        assertSufficientAttributionsAreSufficent(attr);
    }

    public void testValidateAcceptsGrade3Through5ForDisease() throws Exception {
        expeditedData.getDiseaseHistory().setOtherPrimaryDisease("Hearing loss");

        DiseaseAttribution attr = createAttribution(expeditedData.getDiseaseHistory(), Attribution.UNRELATED, DiseaseAttribution.class);
        expeditedData.getAdverseEvents().get(0).getDiseaseAttributions().add(attr);

        assertSufficientAttributionsAreSufficent(attr);
    }

    public void testValidateDoesNotAcceptGrade3Through5ForSurgery() throws Exception {
        SurgeryIntervention cause = new SurgeryIntervention();
        expeditedData.getSurgeryInterventions().add(cause);

        SurgeryAttribution attr = createAttribution(cause, Attribution.UNRELATED, SurgeryAttribution.class);
        expeditedData.getAdverseEvents().get(0).getSurgeryAttributions().add(attr);

        assertNoAttributionsAreSufficent(attr);
    }

    public void testValidateDoesNotAcceptGrade3Through5ForRadiation() throws Exception {
        RadiationIntervention cause = new RadiationIntervention();
        expeditedData.getRadiationInterventions().add(cause);

        RadiationAttribution attr = createAttribution(cause, Attribution.UNRELATED, RadiationAttribution.class);
        expeditedData.getAdverseEvents().get(0).getRadiationAttributions().add(attr);

        assertNoAttributionsAreSufficent(attr);
    }
    
    public void testFindToAddress(){
    	
    	StudyParticipantAssignment assignment = Fixtures.createAssignment();
    	StudySite site = assignment.getStudySite();
    	StudyInvestigator studyInvestigator = new StudyInvestigator();
    	studyInvestigator.setRoleCode("Site Principal Investigator");
    	SiteInvestigator siteInvestigator = new SiteInvestigator();
    	Investigator investigator = new Investigator();
    	investigator.setEmailAddress("biju@kk.com");
    	siteInvestigator.setInvestigator(investigator);
    	studyInvestigator.setSiteInvestigator(siteInvestigator);
    	site.addStudyInvestigators(studyInvestigator);
    	expeditedData.setAssignment(assignment);
    	
    	PlannedEmailNotification penf = new PlannedEmailNotification();
    	RoleBasedRecipient recipient = new RoleBasedRecipient();
    	recipient.setRoleName("SPI");
    	
    	List<Recipient> recipients = new ArrayList<Recipient>();
    	recipients.add(recipient);
    	penf.setRecipients(recipients);
    	
    	
    	ReportDefinition reportDef = Fixtures.createReportDefinition("test");
    	reportDef.addPlannedNotification(penf);
    	
    	Report report = new Report();
    	report.setReportDefinition(reportDef);
    	report.setAeReport(expeditedData);
    	
    	ReportServiceImpl impl = (ReportServiceImpl) service;
    	List<String> addresses = impl.findToAddresses(penf, report);
    	assertEquals(1,addresses.size());
    	
    }
    
    public void testFindEmailAddress(){
    	StudyParticipantAssignment assignment = Fixtures.createAssignment();
    	StudySite site = assignment.getStudySite();
    	StudyInvestigator studyInvestigator = new StudyInvestigator();
    	studyInvestigator.setRoleCode("Site Principal Investigator");
    	SiteInvestigator siteInvestigator = new SiteInvestigator();
    	Investigator investigator = new Investigator();
    	investigator.setEmailAddress("biju@kk.com");
    	siteInvestigator.setInvestigator(investigator);
    	studyInvestigator.setSiteInvestigator(siteInvestigator);
    	site.addStudyInvestigators(studyInvestigator);
    	
    	ResearchStaff staff = new ResearchStaff();
    	staff.setEmailAddress("aa@kk.com");
    	StudyPersonnel studyPersonnel = new StudyPersonnel();
    	studyPersonnel.setRoleCode("Participant Coordinator");
    	studyPersonnel.setResearchStaff(staff);
    	site.addStudyPersonnel(studyPersonnel);
    	
    	expeditedData.setAssignment(assignment);
    	
    	ReportServiceImpl impl = (ReportServiceImpl) service;
    	List<String> addresses = impl.findEmailAddress("SPI", expeditedData);
    	assertEquals(1,addresses.size());
    	List<String> addresses2 = impl.findEmailAddress("PC", expeditedData);
    	assertEquals(1,addresses2.size());
    }

    private void assertNoAttributionsAreSufficent(AdverseEventAttribution attr) {
        for (Attribution attribution : Attribution.values()) {
            attr.setAttribution(attribution);
            ReportSubmittability actual = validateForAttribution();
            assertInsuffientAttributionMessage(attribution + " should not be sufficient", actual);
        }
    }

    private void assertSufficientAttributionsAreSufficent(AdverseEventAttribution attr) {
        for (Attribution attribution : SUFFICIENT_ATTRIBUTIONS) {
            attr.setAttribution(attribution);
            ReportSubmittability actual = validateForAttribution();
            assertTrue(attribution + " should be sufficent", actual.isSubmittable());
        }

        for (Attribution attribution : INSUFFICENT_ATTRIBUTIONS) {
            attr.setAttribution(attribution);
            ReportSubmittability actual = validateForAttribution();
            assertInsuffientAttributionMessage(attribution + " should not be sufficent", actual);
        }
    }

    private Report createAttributionMandatoryReport() {
        Report report = new Report();
        report.setReportDefinition(new ReportDefinition());
        report.setAeReport(expeditedData);
        // TODO:
        // report.setAttributionMandatory(true);
        return report;
    }

    private ReportSubmittability validateForAttribution() {
        return service.validate(createAttributionMandatoryReport(), Collections.<ExpeditedReportSection>emptySet());
    }

    private <C extends DomainObject, A extends AdverseEventAttribution<C>> A createAttribution(C cause, Attribution level, Class<A> klass) throws IllegalAccessException, InstantiationException {
        A attr = klass.newInstance();
        attr.setAttribution(level);
        attr.setCause(cause);
        return attr;
    }

    private void assertInsuffientAttributionMessage(String assertionMessage, ReportSubmittability container) {
        assertTrue(assertionMessage + ": No attribution section messages",
            container.getMessages().containsKey(ATTRIBUTION_SECTION));
        assertTrue(assertionMessage + ": No attribution section messages",
            container.getMessages().get(ATTRIBUTION_SECTION).size() > 0);
        assertEquals(
            assertionMessage + ": Wrong message",
            "The adverse event " + TERM + " is not attributed to a cause. An attribution of possible or higher must be selected for at least one of the causes.",
            container.getMessages().get(ATTRIBUTION_SECTION).get(0).getText()
        );
    }
    
  
}
