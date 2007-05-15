package gov.nih.nci.cabig.caaers.web;

import gov.nih.nci.cabig.caaers.CaaersSystemException;
import gov.nih.nci.cabig.caaers.dao.AdverseEventReportDao;
import gov.nih.nci.cabig.caaers.dao.CtcDao;
import gov.nih.nci.cabig.caaers.dao.CtcTermDao;
import gov.nih.nci.cabig.caaers.dao.ParticipantDao;
import gov.nih.nci.cabig.caaers.dao.StudyDao;
import gov.nih.nci.cabig.caaers.domain.AdverseEventReport;
import gov.nih.nci.cabig.caaers.domain.Ctc;
import gov.nih.nci.cabig.caaers.domain.CtcCategory;
import gov.nih.nci.cabig.caaers.domain.CtcTerm;
import static gov.nih.nci.cabig.caaers.domain.Fixtures.*;
import gov.nih.nci.cabig.caaers.domain.Participant;
import gov.nih.nci.cabig.caaers.domain.Site;
import gov.nih.nci.cabig.caaers.domain.Study;
import gov.nih.nci.cabig.caaers.domain.Grade;
import gov.nih.nci.cabig.caaers.domain.CodedGrade;
import gov.nih.nci.cabig.caaers.domain.CtcGrade;
import gov.nih.nci.cabig.caaers.service.InteroperationService;
import gov.nih.nci.cabig.caaers.web.ae.CreateAdverseEventAjaxFacade;
import static org.easymock.classextension.EasyMock.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Rhett Sutphin
 */
public class CreateAdverseEventAjaxFacadeTest extends DwrFacadeTestCase {
    private CreateAdverseEventAjaxFacade facade;
    private StudyDao studyDao;
    private ParticipantDao participantDao;
    private CtcDao ctcDao;
    private CtcTermDao ctcTermDao;
    private AdverseEventReportDao aeReportDao;
    private InteroperationService interoperationService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        studyDao = registerDaoMockFor(StudyDao.class);
        participantDao = registerDaoMockFor(ParticipantDao.class);
        ctcDao = registerDaoMockFor(CtcDao.class);
        ctcTermDao = registerDaoMockFor(CtcTermDao.class);
        aeReportDao = registerDaoMockFor(AdverseEventReportDao.class);
        interoperationService = registerMockFor(InteroperationService.class);

        facade = new CreateAdverseEventAjaxFacade();
        facade.setParticipantDao(participantDao);
        facade.setStudyDao(studyDao);
        facade.setCtcDao(ctcDao);
        facade.setCtcTermDao(ctcTermDao);
        facade.setAeReportDao(aeReportDao);
        facade.setInteroperationService(interoperationService);
    }

    public void testMatchParticipants() throws Exception {
        Participant expectedMatch = setId(3, createParticipant("Foo", "B"));
        expectedMatch.setDateOfBirth(new Date());  // set not null so we can be sure it isn't copied
        expect(participantDao.getBySubnames(aryEq(new String[] { "foo" })))
            .andReturn(Arrays.asList(expectedMatch));

        replayMocks();
        List<Participant> actualList = facade.matchParticipants("foo", null);
        verifyMocks();

        assertEquals("Wrong number of results", 1, actualList.size());
        Participant actualMatch = actualList.get(0);
        assertNotSame("Returned match is not copy", expectedMatch, actualMatch);
        assertEquals("id not copied", 3, (int) actualMatch.getId());
        assertEquals("first not copied", "Foo", actualMatch.getFirstName());
        assertEquals("last not copied", "B", actualMatch.getLastName());
        assertNull("other field incorrectly copied", actualMatch.getDateOfBirth());
    }
    
    public void testMatchParticipantsMultipleSubnames() throws Exception {
        Participant expectedMatch = setId(5, new Participant());
        expect(participantDao.getBySubnames(aryEq(new String[] { "foo", "zappa" })))
            .andReturn(Arrays.asList(expectedMatch));

        replayMocks();
        List<Participant> actualList = facade.matchParticipants("foo zappa", null);
        verifyMocks();

        assertEquals("Result not forwarded", 1, actualList.size());
    }

    public void testMatchParticipantsFiltersByStudyId() throws Exception {
        List<Participant> expectedList = new ArrayList<Participant>();
        expectedList.add(createParticipant("Joe", "One"));
        expectedList.add(createParticipant("Joe", "Two"));
        Study study = setId(4, new Study());
        assignParticipant(expectedList.get(1), study, new Site());

        expect(participantDao.getBySubnames(aryEq(new String[] { "joe" })))
            .andReturn(expectedList);

        replayMocks();
        List<Participant> actualList = facade.matchParticipants("joe", 4);
        verifyMocks();

        assertEquals("Wrong number of participants returned", 1, actualList.size());
        assertEquals("Wrong participant included", "Two", actualList.get(0).getLastName());
    }

    public void testMatchParticipantsWithBlankToken() throws Exception {
        expect(participantDao.getBySubnames(aryEq(new String[] { })))
            .andReturn(Collections.<Participant>emptyList());

        replayMocks();
        List<Participant> actualList = facade.matchParticipants(" ", null);
        verifyMocks();

        assertEquals("Should return nothing", 0, actualList.size());
    }

    public void testMatchStudies() throws Exception {
        Study expectedMatch = setId(22, createStudy("Jim's Study"));
        expect(studyDao.getBySubnames(aryEq(new String[] { "jim" })))
            .andReturn(Arrays.asList(expectedMatch));

        replayMocks();
        List<Study> actualList = facade.matchStudies("jim", null);
        verifyMocks();

        assertEquals("Result not forwarded", 1, actualList.size());
        Study actualMatch = actualList.get(0);
        assertNotSame("Returned match is not copy", expectedMatch, actualMatch);
        assertEquals("id not copied", 22, (int) actualMatch.getId());
        assertEquals("shortTitle not copied", "Jim's Study", actualMatch.getShortTitle());
        assertNull("extra field incorrectly copied", actualMatch.getLongTitle());
    }

    public void testMatchStudiesMultipleSubnames() throws Exception {
        Study expectedMatch = setId(22, createStudy("Jim's Study"));
        expect(studyDao.getBySubnames(aryEq(new String[] { "jules", "jim" })))
            .andReturn(Arrays.asList(expectedMatch));

        replayMocks();
        List<Study> actualList = facade.matchStudies("jules jim", null);
        verifyMocks();

        assertEquals("Result not forwarded", 1, actualList.size());
    }

    public void testMatchStudiesFiltersByParticipantId() throws Exception {
        List<Study> expectedList = new ArrayList<Study>();
        expectedList.add(createStudy("Happy"));
        expectedList.add(createStudy("Joyful"));
        Participant p = setId(7, createParticipant("Sad", "Man"));
        assignParticipant(p, expectedList.get(1), new Site());

        expect(studyDao.getBySubnames(aryEq(new String[] { "y" })))
            .andReturn(expectedList);

        replayMocks();
        List<Study> actualList = facade.matchStudies("y", 7);
        verifyMocks();

        assertEquals("Wrong number of studies returned", 1, actualList.size());
        assertEquals("Wrong study included", "Joyful", actualList.get(0).getShortTitle());
    }

    public void testMatchTermsNoCategory() throws Exception {
        List<CtcTerm> expected = new LinkedList<CtcTerm>();
        expect(ctcTermDao.getBySubname(aryEq(new String[] { "what" }), eq(12), (Integer) isNull()))
            .andReturn(expected);

        replayMocks();
        List<CtcTerm> actual = facade.matchTerms("what", 12, null, 10);
        verifyMocks();

        assertSame("Wrong list", expected, actual);
    }

    public void testMatchTermsWithCategory() throws Exception {
        List<CtcTerm> expected = new LinkedList<CtcTerm>();
        expect(ctcTermDao.getBySubname(aryEq(new String[] { "what" }), eq(12), eq(7)))
            .andReturn(expected);

        replayMocks();
        List<CtcTerm> actual = facade.matchTerms("what", 12, 7, 10);
        verifyMocks();

        assertSame("Wrong list", expected, actual);
    }

    public void testMatchTermsMultipleSubnames() throws Exception {
        List<CtcTerm> expected = new LinkedList<CtcTerm>();
        expect(ctcTermDao.getBySubname(aryEq(new String[] { "what", "happ" }), eq(2), eq(205)))
            .andReturn(expected);

        replayMocks();
        List<CtcTerm> actual = facade.matchTerms("what happ", 2, 205, 10);
        verifyMocks();

        assertSame("Wrong list", expected, actual);
    }

    public void testGetCategories() throws Exception {
        int expectedId = 55;
        Ctc ctc = setId(expectedId, new Ctc());
        ctc.setCategories(new ArrayList<CtcCategory>());
        expect(ctcDao.getById(expectedId)).andReturn(ctc);

        replayMocks();
        List<CtcCategory> actual = facade.getCategories(expectedId);
        verifyMocks();

        assertSame("Wrong list", ctc.getCategories(), actual);
    }

    public void testPushToPsc() throws Exception {
        int expectedId = 510;
        AdverseEventReport report = setId(expectedId, new AdverseEventReport());
        expect(aeReportDao.getById(510)).andReturn(report);
        interoperationService.pushToStudyCalendar(report);

        replayMocks();
        assertTrue(facade.pushAdverseEventToStudyCalendar(expectedId));
        verifyMocks();
    }

    public void testPushToPscAndFail() throws Exception {
        int expectedId = 510;
        AdverseEventReport report = setId(expectedId, new AdverseEventReport());
        expect(aeReportDao.getById(510)).andReturn(report);
        interoperationService.pushToStudyCalendar(report);
        expectLastCall().andThrow(new CaaersSystemException("Turbo bad"));

        replayMocks();
        assertFalse(facade.pushAdverseEventToStudyCalendar(expectedId));
        verifyMocks();
    }
    
    public void testPushToPscAndFailWithArbitraryException() throws Exception {
        int expectedId = 510;
        AdverseEventReport report = setId(expectedId, new AdverseEventReport());
        expect(aeReportDao.getById(510)).andReturn(report);
        interoperationService.pushToStudyCalendar(report);
        expectLastCall().andThrow(new RuntimeException("Turbo bad"));

        replayMocks();
        assertFalse(facade.pushAdverseEventToStudyCalendar(expectedId));
        verifyMocks();
    }

    public void testAddConcomitantMedications() throws Exception {
        expect(webContext.getCurrentPage()).andReturn("/pages/ae/edit");
        expect(webContext.forwardToString("/pages/ae/edit?index=4&aeReport=12&subview=conMedFormSection"))
            .andReturn("The HTML");

        replayMocks();
        assertEquals("The HTML", facade.addConcomitantMedication(4, 12));
        verifyMocks();
    }

    public void testAddConcomitantMedicationsNoReport() throws Exception {
        expect(webContext.getCurrentPage()).andReturn("/pages/ae/create");
        expect(webContext.forwardToString("/pages/ae/create?index=4&subview=conMedFormSection"))
            .andReturn("The HTML");

        replayMocks();
        assertEquals("The HTML", facade.addConcomitantMedication(4, null));
        verifyMocks();
    }

    public void testAddConcomitantMedicationsRemovesContextPath() throws Exception {
        request.setContextPath("/caaers");
        expect(webContext.getCurrentPage()).andReturn("/caaers/pages/ae/edit");
        expect(webContext.forwardToString("/pages/ae/edit?index=4&aeReport=12&subview=conMedFormSection"))
            .andReturn("The HTML");

        replayMocks();
        assertEquals("The HTML", facade.addConcomitantMedication(4, 12));
        verifyMocks();
    }

    public void testAddConcomitantMedicationsDoesNotRemoveContextPathIfNotThere() throws Exception {
        request.setContextPath("/caaers");
        expect(webContext.getCurrentPage()).andReturn("/pages/ae/edit");
        expect(webContext.forwardToString("/pages/ae/edit?index=4&aeReport=12&subview=conMedFormSection"))
            .andReturn("The HTML");

        replayMocks();
        assertEquals("The HTML", facade.addConcomitantMedication(4, 12));
        verifyMocks();
    }

    public void testGetGradesForEnumGrades() throws Exception {
        CtcTerm term = registerMockFor(CtcTerm.class);
        expect(ctcTermDao.getById(5)).andReturn(term);
        expect(term.getGrades()).andReturn(Arrays.<CodedGrade>asList(Grade.SEVERE));
        replayMocks();

        List<? extends CodedGrade> actual = facade.getTermGrades(5);
        verifyMocks();
        assertEquals(1, actual.size());
        assertEquals(3, actual.get(0).getCode());
        assertEquals("Severe", actual.get(0).getDisplayName());
    }

    public void testGetGradesForCtcGrades() throws Exception {
        CtcTerm term = registerMockFor(CtcTerm.class);
        expect(ctcTermDao.getById(5)).andReturn(term);
        CtcGrade grade = new CtcGrade();
        grade.setText("Oh, so severe");
        grade.setGrade(Grade.SEVERE);
        grade.setTerm(term);
        expect(term.getGrades()).andReturn(Arrays.<CodedGrade>asList(grade));
        replayMocks();

        List<? extends CodedGrade> actual = facade.getTermGrades(5);
        verifyMocks();
        assertEquals(1, actual.size());
        assertEquals(3, actual.get(0).getCode());
        assertEquals("Oh, so severe", actual.get(0).getDisplayName());
    }
}
