/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.service;

import gov.nih.nci.cabig.caaers.dao.AdverseEventRecommendedReportDao;
import gov.nih.nci.cabig.caaers.domain.AdverseEventRecommendedReport;
import gov.nih.nci.cabig.caaers.domain.report.ContactMechanismBasedRecipient;
import gov.nih.nci.cabig.caaers.domain.report.PlannedEmailNotification;
import gov.nih.nci.cabig.caaers.domain.report.PlannedNotification;
import gov.nih.nci.cabig.caaers.domain.report.ReportDefinition;
import gov.nih.nci.cabig.caaers.tools.mail.CaaersJavaMailSender;
import gov.nih.nci.cabig.caaers.utils.DateUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.Transactional;

public class UnreportedSAENotificationProcessService {
	
	private FreeMarkerService freeMarkerService;
	private CaaersJavaMailSender caaersJavaMailSender;
	private AdverseEventRecommendedReportDao adverseEventRecommendedReportDao;
	protected static final Log logger = LogFactory.getLog(UnreportedSAENotificationProcessService.class);
	
	public void setFreeMarkerService(FreeMarkerService freeMarkerService) {
		this.freeMarkerService = freeMarkerService;
	}

	public void setCaaersJavaMailSender(CaaersJavaMailSender caaersJavaMailSender) {
		this.caaersJavaMailSender = caaersJavaMailSender;
	}

	public void setAdverseEventRecommendedReportDao(
			AdverseEventRecommendedReportDao adverseEventRecommendedReportDao) {
		this.adverseEventRecommendedReportDao = adverseEventRecommendedReportDao;
	}

	@Transactional
	public void process(){
		
		// get all the distinct report definitions
		List<ReportDefinition> rds = adverseEventRecommendedReportDao.getAllRecommendedReportsNotReported();
		
		// get planned notifications associated with the report definitions
		for(ReportDefinition rd1 : rds){
			for(PlannedNotification plannedNotification : rd1.getUnreportedAePlannedNotification()){
					Set<String> emailAddresses = new HashSet<String>();
		            //find emails of direct recipients
		            if(CollectionUtils.isNotEmpty(plannedNotification.getContactMechanismBasedRecipients())){
		          	  for(ContactMechanismBasedRecipient recipient : plannedNotification.getContactMechanismBasedRecipients()){
		          		  String contact = recipient.getContact();
		          		  if(GenericValidator.isEmail(contact)) emailAddresses.add(contact);  
		    	      }
		            }
		            
	            	 //now process the notifications. 
	        		PlannedEmailNotification plannedemailNotification = (PlannedEmailNotification) plannedNotification;
	                String rawSubjectLine = plannedemailNotification.getSubjectLine();
	                String rawBody = plannedNotification.getNotificationBodyContent().getBody();
	                Integer dayOfNotification = plannedemailNotification.getIndexOnTimeScale();
	                List<AdverseEventRecommendedReport> aeRecomReports = adverseEventRecommendedReportDao.getAllAdverseEventsGivenReportDefinition(rd1);
	                for(AdverseEventRecommendedReport aeRecomReport : aeRecomReports){
	                	if(aeRecomReport.getAdverseEvent().getGradedDate() != null){
	                		long daysPassedSinceGradedDate = DateUtils.differenceInDays(new Date(),aeRecomReport.getAdverseEvent().getGradedDate());
	                		if(daysPassedSinceGradedDate != dayOfNotification){
	                			continue;
	                		}
	                	}
	                	// add adverse event reporter email for email notification
	                	if(aeRecomReport.getAdverseEvent().getReporterEmail() != null){
	                		emailAddresses.add(aeRecomReport.getAdverseEvent().getReporterEmail());
	                	}
	                	// get the graded date and compare with the day of notification to check if notificatino is configured on this day
	                	
	                	
	                	  Map<Object, Object> contextVariableMap = aeRecomReport.getAdverseEvent().getContextVariables();
			                //get the AE reporting deadline
			                contextVariableMap.put("aeReportingDeadline", aeRecomReport.getDueDate().toString());
			                
			                //apply the replacements. 
			                String subjectLine = freeMarkerService.applyRuntimeReplacementsForReport(rawSubjectLine, contextVariableMap);
			                String body = freeMarkerService.applyRuntimeReplacementsForReport(rawBody, contextVariableMap);
			                
			                //create the message
			                SimpleMailMessage mailMsg = new SimpleMailMessage();
			                mailMsg.setSentDate(new Date());
			                mailMsg.setSubject(subjectLine);
			                mailMsg.setText(body);
			                
			                //send email to each recipient
			                for(String email : emailAddresses){
			                	mailMsg.setTo(email);
			                	
			                	try{
			                		caaersJavaMailSender.send(mailMsg);
			                	}catch(Exception e){
			                		//no need to throw and rollback
			                		logger.warn("Error while emailing to [" + email + "]", e);
			                	}
			                }
	                }
	                
			}
		
		}
		
	}

}
