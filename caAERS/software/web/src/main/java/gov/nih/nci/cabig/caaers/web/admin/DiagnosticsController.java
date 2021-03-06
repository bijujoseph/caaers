/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.web.admin;

import gov.nih.nci.cabig.caaers.esb.client.impl.CaaersAdeersMessageBroadcastServiceImpl;
import gov.nih.nci.cabig.caaers.tools.configuration.Configuration;
import gov.nih.nci.cabig.caaers.tools.mail.CaaersJavaMailSender;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import gov.nih.nci.cabig.caaers.web.listener.EventMonitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.servlet.mvc.SimpleFormController;

import java.io.File;

public class DiagnosticsController extends SimpleFormController{
	
	private Configuration configuration;
	protected CaaersJavaMailSender caaersJavaMailSender;
	protected final Log log = LogFactory.getLog(getClass());
	protected CaaersAdeersMessageBroadcastServiceImpl messageBroadcastService;
    protected EventMonitor eventMonitor;
	
    public DiagnosticsController() {
    	setCommandClass(DiagnosticsCommand.class);
        setFormView("admin/happy");
    }
    
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
    	DiagnosticsCommand diagnosticsCommand =  new DiagnosticsCommand(configuration);
    	diagnosticsCommand.setSmtpTestResult(testSmtp(diagnosticsCommand));
    	diagnosticsCommand.setServiceMixUp(testServiceMix());
        diagnosticsCommand.setEvents(eventMonitor.getAllEvents());
    	return diagnosticsCommand;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
    
    public void setCaaersJavaMailSender(CaaersJavaMailSender caaersJavaMailSender) {
		this.caaersJavaMailSender = caaersJavaMailSender;
	}

	private boolean testSmtp(DiagnosticsCommand diagnosticsCommand) {
    	boolean testResult = false;
	    try {
		    MimeMessage message = caaersJavaMailSender.createMimeMessage();
		    message.setSubject("Test mail from caAERS Diagnostics");
		    message.setFrom(new InternetAddress("caaers.app@gmail.com"));
		
		    // use the true flag to indicate you need a multipart message
		    MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String fromEmail = configuration.get(Configuration.SYSTEM_FROM_EMAIL);
            if(fromEmail == null) fromEmail = "admin@semanticbits.com" ;
		    helper.setTo(fromEmail);
		    caaersJavaMailSender.send(message);
		    testResult = true;
		} catch (Exception e) {
			//log.error(" Error in sending email , please check the confiuration " + e);
			diagnosticsCommand.setSmtpException(e);
			testResult = false;
		}
		return testResult;
	}
	private boolean testServiceMix() {
    	boolean testResult = false;
	    try {
	    	messageBroadcastService.initialize();
		    testResult = true;
		} catch (Exception e) {
			testResult = false;
		}
		return testResult;
	}

	public void setMessageBroadcastService(
			CaaersAdeersMessageBroadcastServiceImpl messageBroadcastService) {
		this.messageBroadcastService = messageBroadcastService;
	}

    public void setEventMonitor(EventMonitor eventMonitor) {
        this.eventMonitor = eventMonitor;
    }
}
