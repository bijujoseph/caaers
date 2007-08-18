package gov.nih.nci.cabig.caaers.web.ae;


import static gov.nih.nci.cabig.caaers.CaaersUseCase.*;
import gov.nih.nci.cabig.caaers.CaaersUseCases;
import static gov.nih.nci.cabig.caaers.domain.Fixtures.*;
import gov.nih.nci.cabig.caaers.domain.report.Report;
import gov.nih.nci.cabig.caaers.domain.report.ReportDefinition;
import gov.nih.nci.cabig.caaers.service.EvaluationService;
import gov.nih.nci.cabig.caaers.service.ReportService;
import static org.easymock.classextension.EasyMock.expect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Rhett Sutphin
 */
@CaaersUseCases({ CREATE_EXPEDITED_REPORT })
public class CheckpointTabTest extends AeTabTestCase {
    private ReportDefinition r1, r2, r3;

    private EvaluationService evaluationService;
    private ReportService reportService;

    @Override
    protected void setUp() throws Exception {
        evaluationService = registerMockFor(EvaluationService.class);
        reportService = registerMockFor(ReportService.class);
        super.setUp();

        r1 = setId(1, createReportDefinition("R1"));
        r2 = setId(2, createReportDefinition("R2"));
        r3 = setId(3, createReportDefinition("R3"));
    }

    @Override
    protected AeTab createTab() {
        CheckpointTab tab = new CheckpointTab();
        tab.setEvaluationService(evaluationService);
        tab.setReportService(reportService);
        return tab;
    }

//TODO: fix the commented testcase URGENT
//    public void testPostProcessAddsOptionalReports() throws Exception {
//        assertEquals(0, command.getAeReport().getReports().size());
//        List<ReportDefinition> reportDefs = new ArrayList<ReportDefinition>();
//        reportDefs.add(r1);
//        reportDefs.add(r2);
//        reportDefs.add(r3);
//
//        command.getOptionalReportDefinitionsMap().put(r1, Boolean.TRUE);
//        command.getOptionalReportDefinitionsMap().put(r2, Boolean.FALSE);
//        command.getOptionalReportDefinitionsMap().put(r3, Boolean.TRUE);
//        evaluationService.addOptionalReports(command.getAeReport(), reportDefs);
//        expect(reportService.createReport(r1, command.getAeReport())).andReturn(null); // DC
//        expect(reportService.createReport(r3, command.getAeReport())).andReturn(null); // DC
//
//        replayMocks();
//
//        getTab().postProcess(request, command, errors);
//        verifyMocks();
//    }

    public void testPostProcessDoesNotInterfereWithExistingRequiredReports() throws Exception {
        command.getAeReport().getReports().add(createRequiredReport(r1));

        command.getOptionalReportDefinitionsMap().put(r2, Boolean.TRUE);
        command.getOptionalReportDefinitionsMap().put(r3, Boolean.FALSE);

        expect(reportService.createReport(r2, command.getAeReport())).andReturn(null); // DC
        getTab().postProcess(request, command, errors);

        List<Report> actualReports = command.getAeReport().getReports();
        // note that IRL, this would be 2, but we mocked out the add of the optional
        // and are only testing that the required report wasn't touched
        assertEquals(1, actualReports.size());
        assertEquals(r1, actualReports.get(0).getReportDefinition());
        assertTrue(actualReports.get(0).isRequired());
    }

    public void testPostProcessRemovesDeselectedOptionalReports() throws Exception {
        command.getAeReport().getReports().add(createRequiredReport(r1));
        command.getAeReport().getReports().add(r2.createReport());

        command.getOptionalReportDefinitionsMap().put(r2, Boolean.FALSE);
        command.getOptionalReportDefinitionsMap().put(r3, Boolean.FALSE);

        // TODO: there will probably be a call to a service in here somewhere
        getTab().postProcess(request, command, errors);

        List<Report> actualReports = command.getAeReport().getReports();
        assertEquals(1, actualReports.size());
        assertEquals(r1, actualReports.get(0).getReportDefinition());
        assertTrue(actualReports.get(0).isRequired());
    }

    public void testPostProcessDoesNotRemoveRequiredReportsEver() throws Exception {
        command.getAeReport().getReports().add(createRequiredReport(r1));

        // other code should prevent this situation from occurring, but just in case:
        command.getOptionalReportDefinitionsMap().put(r1, Boolean.FALSE);
        command.getOptionalReportDefinitionsMap().put(r2, Boolean.FALSE);
        command.getOptionalReportDefinitionsMap().put(r3, Boolean.FALSE);

        // TODO: there will probably be a call to a service in here somewhere
        getTab().postProcess(request, command, errors);

        List<Report> actualReports = command.getAeReport().getReports();
        assertEquals(1, actualReports.size());
        assertEquals(r1, actualReports.get(0).getReportDefinition());
        assertTrue(actualReports.get(0).isRequired());
    }

    private Report createRequiredReport(ReportDefinition def) {
        Report reqd = def.createReport();
        reqd.setRequired(true);
        return reqd;
    }

    public void testPostProcessSavesWhenThereAreAnyReports() throws Exception {
        command.getAeReport().getReports().add(createRequiredReport(r2));
        reportDao.save(command.getAeReport());

        replayMocks();
        getTab().postProcess(request, command, errors);
        verifyMocks();
    }

    public void testPostProcessDoesNotSaveWhenThereAreNoReports() throws Exception {
        command.getAeReport().getReports().clear();

        replayMocks();
        getTab().postProcess(request, command, errors);
        verifyMocks();
    }

    public void testPreProcessEvaluates() throws Exception {
        evaluationService.addRequiredReports(command.getAeReport());
        expect(evaluationService.applicableReportDefinitions(command.getAssignment()))
            .andReturn(Collections.<ReportDefinition>emptyList());

        replayMocks();
        getTab().onDisplay(request, command);
        verifyMocks();
    }

    public void testPreProcessSetsUpOptionalDefList() throws Exception {
        command.getAeReport().getReports().clear();
        command.getAeReport().addReport(r1.createReport());
        command.getAeReport().addReport(createRequiredReport(r2));

        evaluationService.addRequiredReports(command.getAeReport());
        expect(evaluationService.applicableReportDefinitions(command.getAssignment()))
            .andReturn(new ArrayList<ReportDefinition>(Arrays.asList(r1, r2, r3)));

        replayMocks();
        getTab().onDisplay(request, command);
        verifyMocks();

        Map<ReportDefinition,Boolean> map = command.getOptionalReportDefinitionsMap();

        assertEquals("Wrong number of optional defs", 2, map.size());
        assertTrue("Optional defs does not include r1", map.containsKey(r1));
        assertTrue("Optional defs does not include r3", map.containsKey(r3));
    }

    public void testFieldsPresentForOptionalReports() throws Exception {
        command.getAeReport().addReport(r2.createReport());
        command.setOptionalReportDefinitions(Arrays.asList(r1, r2, r3));
        assertFieldProperties("optionalReports",
            "optionalReportDefinitionsMap[1]",
            "optionalReportDefinitionsMap[2]",
            "optionalReportDefinitionsMap[3]"
        );
    }

    public void testNoReportsIsError() throws Exception {
        command.getAeReport().getReports().clear();

        replayMocks();
        getTab().validate(command, errors);
        verifyMocks();

        assertEquals(1, errors.getErrorCount());
        assertEquals(1, errors.getGlobalErrorCount());
        assertEquals("At least one expedited report must be selected to proceed",
            errors.getGlobalError().getDefaultMessage());
    }

    public void testAtLeastOneSelectedOptionalReportIsNotError() throws Exception {
        command.getAeReport().getReports().clear();
        command.getOptionalReportDefinitionsMap().put(r1, Boolean.FALSE);
        command.getOptionalReportDefinitionsMap().put(r2, Boolean.TRUE);
        command.getOptionalReportDefinitionsMap().put(r3, Boolean.FALSE);

        replayMocks();
        getTab().validate(command, errors);
        verifyMocks();

        assertEquals(0, errors.getErrorCount());
    }
}
