/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.esb.client.impl;

import gov.nih.nci.cabig.caaers.CaaersSystemException;
import gov.nih.nci.cabig.caaers.domain.report.Report;
import gov.nih.nci.cabig.caaers.domain.report.ReportDelivery;
import gov.nih.nci.cabig.caaers.domain.report.ReportDeliveryDefinition;
import gov.nih.nci.cabig.caaers.esb.client.ResponseMessageProcessor;
import gov.nih.nci.cabig.caaers.tools.configuration.Configuration;

import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.transaction.annotation.Transactional;
/**
 * Will handle the responses related to report submission.
 * 
 * 
 * @author Srini
 * @author Biju Joseph
 *
 */
/*
 * BJ : Messages read from resource files. 
 * BJ : TODO copy the testcases checked in at r10219
 */
public class AdeersSubmissionResponseMessageProcessor extends ResponseMessageProcessor{

	protected final Log log = LogFactory.getLog(getClass());
	private Configuration configuration;

	@Override
	@Transactional
	public void processMessage(String message) throws CaaersSystemException {
        log.debug("AdeersSubmissionResponseMessageProcessor - message recieved");

        
        Element jobInfo = this.getResponseElement(message,"submitAEDataXMLAsAttachmentResponse","AEReportJobInfo");
        Namespace emptyNS=null;
        for (Object obj:jobInfo.getChildren()) {
				Element e = (Element)obj;
				if (e.getName().equals("CAEERS_AEREPORT_ID")) {
					emptyNS = e.getNamespace();
				}
		}
        
        log.debug("got JobInfo");
        
        String caaersAeReportId = jobInfo.getChild("CAEERS_AEREPORT_ID",emptyNS).getValue();
        log.debug("Data Colleciton ID : " + caaersAeReportId);
        String reportId = jobInfo.getChild("CAAERSRID",emptyNS).getValue();
        log.debug("Report ID : " + reportId);
        String submitterEmail = jobInfo.getChild("SUBMITTER_EMAIL",emptyNS).getValue();
        log.debug("Submitter email : " + submitterEmail);
        
        Report r = reportDao.getById(Integer.parseInt(reportId));
        
        //FIXME: When updating Caaers to send to multiple systems the below must also be changed.
        //Can just use the first system as that is the only one that is used.
        String sysName = "UNKNOWN";
        if(r != null) {
	        List<ReportDelivery> list = r.getExternalSystemDeliveries();
            ReportDeliveryDefinition deliveryDef = list.isEmpty() ? null : list.get(0).getReportDeliveryDefinition();
            sysName = deliveryDef != null ? deliveryDef.getEntityName() : "UNKNOWN";
        }
        // build error messages
        StringBuffer sb = new StringBuffer();

        boolean success = true;
        boolean communicationError = false;
        String ticketNumber = "";
        String url = "";

        try {

            if (jobInfo.getChild("reportStatus").getValue().equals("SUCCESS")) {
            	
            	 ticketNumber = jobInfo.getChild("ticketNumber").getValue();
                 url = jobInfo.getChild("reportURL").getValue();
                 
        		 String submissionMessage = messageSource.getMessage("successful.reportSubmission.message",
        				 new Object[]{String.valueOf(r.getLastVersion().getId()), ticketNumber,  url}, Locale.getDefault());
        		 
        		sb.append(submissionMessage);
        		
        		// append additional report information
            	String reportDetails = messageSource.getMessage("additional.successful.reportSubmission.information",  new Object[] {r.getSubmitter().getFullName(), 
       				 r.getSubmitter().getEmailAddress(), r.getAeReport().getStudy().getPrimaryIdentifier().getValue(), r.getAeReport()
    				 .getParticipant().getPrimaryIdentifierValue(), r.getCaseNumber(),String.valueOf(r.getId()),ticketNumber, configuration.get(Configuration.SYSTEM_NAME)}, Locale.getDefault());
            	sb.append(reportDetails);
            }else{
            	 success = false;
            	 @SuppressWarnings("unchecked")
				List<Element> exceptions = jobInfo.getChildren("jobExceptions");
            	 //find the exception elements
            	 if(CollectionUtils.isNotEmpty(exceptions)){
            		 StringBuffer exceptionMsgBuffer = new StringBuffer();
            		 for (Element ex : exceptions) {
            			 exceptionMsgBuffer.append(ex.getChild("code").getValue()).append( "  -  ").append(ex.getChild("description").getValue()).append("\n");

            			 if (ex.getChild("code").getValue().equals("caAERS-adEERS : COMM_ERR")) {
                     		communicationError=true;
                         }
                     }
            		 
            		 String submissionMessage = messageSource.getMessage("failed.reportSubmission.message", new Object[]{String.valueOf(r.getLastVersion().getId()),
            				 exceptionMsgBuffer.toString(), r.getSubmitter().getFullName(), r.getSubmitter().getEmailAddress(), r.getAeReport().getStudy()
            				 .getPrimaryIdentifier().getValue(), r.getAeReport().getParticipant().getPrimaryIdentifierValue(), r.getCaseNumber(),String.valueOf(r.getId()),
            				 configuration.get(Configuration.SYSTEM_NAME), sysName}, Locale.getDefault());
            		 sb.append(submissionMessage);
            		 
            	 }//if exceptions
            	 
            }
            
            if (jobInfo.getChild("comments") != null) {
           	 	String commentsMessage = messageSource.getMessage("comments.reportSubmission.message", new Object[]{jobInfo.getChild("comments").getValue()}, Locale.getDefault());
           	 	sb.append("\n"); // Move comments section to NextLine.
                sb.append(commentsMessage);
            }
            
         	// append Help Text message
        	String helpText = messageSource.getMessage("helptext.submission.message", new Object[]{}, Locale.getDefault());
        	sb.append(helpText);

            

        } catch (Exception e) {
            log.error("Error while generating email body", e);
        }

        // Notify submitter
        try {
        	 String messages = sb.toString();
            log.debug("Calling notfication service ..");
            this.getMessageNotificationService().sendNotificationToReporter(submitterEmail, messages,
                            caaersAeReportId, reportId, success, ticketNumber, url,communicationError);
        } catch (Exception e) {
            log.error("Error while sending out  email", e);
        }
		
	}
	
	  public void setConfiguration(Configuration configuration) {
	        this.configuration = configuration;
	    }

}
