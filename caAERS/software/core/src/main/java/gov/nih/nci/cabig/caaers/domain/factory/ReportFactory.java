/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.domain.factory;

import gov.nih.nci.cabig.caaers.domain.ExpeditedAdverseEventReport;
import gov.nih.nci.cabig.caaers.domain.report.PlannedEmailNotification;
import gov.nih.nci.cabig.caaers.domain.report.PlannedNotification;
import gov.nih.nci.cabig.caaers.domain.report.Report;
import gov.nih.nci.cabig.caaers.domain.report.ReportDefinition;
import gov.nih.nci.cabig.caaers.domain.report.ScheduledEmailNotification;
import gov.nih.nci.cabig.caaers.domain.report.ScheduledNotification;
import gov.nih.nci.cabig.caaers.utils.DateUtils;
import gov.nih.nci.cabig.ctms.lang.NowFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.collections.CollectionUtils;

 
/**
 * A factory for creating Report objects.
 *
 * @author Biju Joseph
 */
public class ReportFactory {
	
	/** The now factory. */
	private NowFactory nowFactory;
    
    /**
     * Creates a new Report object.
     *
     * @param reportDefinition the report definition
     * @param aeReport the ae report
     * @param baseDate the base date
     * @return the report
     */
    public Report createReport(final ReportDefinition reportDefinition, final ExpeditedAdverseEventReport aeReport, Date baseDate) {
        assert reportDefinition != null : "ReportDefinition must be not null. Unable to create a Report";
        assert aeReport != null : "ExpeditedAdverseEventReport should not be null. Unable to create a Report";
        
        Date now = nowFactory.getNow();
        
        Report report = reportDefinition.createReport();
        report.setCreatedOn(now);
        Date dueDate = reportDefinition.getExpectedDueDate(baseDate == null ? now : baseDate);
        report.setDueOn(dueDate);
        report.getLastVersion().setReportVersionId("0"); //default
        report.getLastVersion().setAmendmentNumber(new Integer(0));
        //attach the aeReport to report
        aeReport.addReport(report);

        addScheduledNotifications(reportDefinition, report);
        return report;
    }
    
    
    /**
     * Adds the scheduled notifications.
     *
     * @param reportDefinition the report definition
     * @param report the report
     */
    public void addScheduledNotifications(ReportDefinition reportDefinition, Report report){
    	//Note : there is a change in busineess requirement, that at firing time only we generate the message/recipients
    	//So only one Scheduled Notification per Planned Notification. 
    	if(CollectionUtils.isEmpty(reportDefinition.getPlannedNotifications())) return;
    	
    	Date now = nowFactory.getNow();
    	Calendar cal = GregorianCalendar.getInstance();
    	
    	for (PlannedNotification plannedNotification : reportDefinition.getPlannedNotifications()) {
    		ScheduledNotification scheduledNotification = plannedNotification.createScheduledNotification("dummy");
    		
    		if(plannedNotification instanceof PlannedEmailNotification){
    			ScheduledEmailNotification scheduledEmailNotification = (ScheduledEmailNotification)scheduledNotification;
    			scheduledEmailNotification.setBody("dummy");
    			scheduledEmailNotification.setSubjectLine("dummy");
    		}
    		
            //set the scheduled dates
            if(reportDefinition.getBaseDate() != null)
            	cal.setTime(reportDefinition.getBaseDate());
            else
            	cal.setTime(now);
            cal.add(reportDefinition.getTimeScaleUnitType().getCalendarTypeCode(), plannedNotification.getIndexOnTimeScale());
            scheduledNotification.setScheduledOn(cal.getTime());
            if(plannedNotification.getIndexOnTimeScale() == 0 || DateUtils.compateDateAndTime(now, cal.getTime()) < 0)
            	report.addScheduledNotification(scheduledNotification);
    	}
    
    	
    }
  


    /**
     * Sets the now factory.
     *
     * @param nowFactory the new now factory
     */
    public void setNowFactory(final NowFactory nowFactory) {
        this.nowFactory = nowFactory;
    }
    
}
