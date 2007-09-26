package gov.nih.nci.cabig.caaers.service;

import gov.nih.nci.cabig.caaers.domain.AdverseEvent;
import gov.nih.nci.cabig.caaers.domain.ExpeditedAdverseEventReport;
import gov.nih.nci.cabig.caaers.domain.StudyParticipantAssignment;
import gov.nih.nci.cabig.caaers.domain.report.Report;
import gov.nih.nci.cabig.caaers.domain.report.ReportDefinition;


import java.util.List;

/**
 * @author Rhett Sutphin
 */
public interface EvaluationService {
    /**
     * @return true if the given adverse event is severe in the context of the provided
     *  study, site, and participant
     */
    boolean isSevere(StudyParticipantAssignment assignment, AdverseEvent adverseEvent);

    /**
     * Checks whether all the mandatory fields, are duly filled. If the report is complete, the
     * ErrorMessages will be empty
     * @param report - {@link Report}
     * @return {@link ErrorMessages}
     */
    //return type based on the method name, is misleading,need to find a better name.
    public ErrorMessages isSubmitable(Report report);

    /**
     * Will return the ReportDefinition that are marked required at rules engine.
     *
     * @param expeditedData - The expedited adverse event report
     * @return - A list of {@link ReportDefinition} objects.
     */
    List<ReportDefinition> findRequiredReportDefinitions(ExpeditedAdverseEventReport expeditedData);

    /**
     * Evaluates the provided data and associates new {@link Report}
     * instances with the given {@link ExpeditedAdverseEventReport}.
     * <p>
     * This method may be called multiple times for the same expedited data.  Implementors must be
     * sure not to add multiple {@link Report}s for the same
     * {@link ReportDefinition}.  Implementors must also <em>not</em> remove
     * {@link gov.nih.nci.cabig.caaers.domain.report.Report}s if they don't evaluate as required
     * (e.g., some reports may have been directly selected by the user).  Instead, implementors
     * should update the {@link Report#setRequired} flag.
     *
     * @param expeditedData
     * @return the report definitions which the evaluation indicated were required.
     */
    void addRequiredReports(ExpeditedAdverseEventReport expeditedData);

    /**
     * This method will instantiate and saves the optional reports.
     * @param expeditedData
     * @param reportDefs - A list of ReportDefinitions
     */
    void addOptionalReports(ExpeditedAdverseEventReport expeditedData, List<ReportDefinition> reportDefs);


    /**
     *
     * @param expeditedData
     * @return All the mandatory sections for a given expedited report.
     */
    List<String> mandatorySections(ExpeditedAdverseEventReport expeditedData);

    /**
     * @return All the report definitions which might apply to the given
     *  study, site, and participant
     */
    // TODO: it might more sense for this to go in ReportService
    List<ReportDefinition> applicableReportDefinitions(StudyParticipantAssignment assignment);



}
