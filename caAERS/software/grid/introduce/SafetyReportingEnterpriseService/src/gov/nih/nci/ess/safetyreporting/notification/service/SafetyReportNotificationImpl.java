package gov.nih.nci.ess.safetyreporting.notification.service;

import java.rmi.RemoteException;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.3
 * 
 */
public class SafetyReportNotificationImpl extends SafetyReportNotificationImplBase {

	
	public SafetyReportNotificationImpl() throws RemoteException {
		super();
	}
	
  public gov.nih.nci.ess.safetyreporting.types.SafetyReportDefinitionNotification createSafetyReportDefinitionNotification(gov.nih.nci.ess.safetyreporting.types.SafetyReportDefinitionNotification safetyReportDefinitionNotification,ess.caaers.nci.nih.gov.Id reportDefinitionId) throws RemoteException, gov.nih.nci.ess.safetyreporting.management.stubs.types.SafetyReportingServiceException {
    //TODO: Implement this autogenerated method
    throw new RemoteException("Not yet implemented");
  }

  public gov.nih.nci.ess.safetyreporting.types.SafetyReportDefinitionNotification updateSafetyReportDefinitionNotification(gov.nih.nci.ess.safetyreporting.types.SafetyReportDefinitionNotification safetyReportDefinitionNotification) throws RemoteException, gov.nih.nci.ess.safetyreporting.management.stubs.types.SafetyReportingServiceException {
    //TODO: Implement this autogenerated method
    throw new RemoteException("Not yet implemented");
  }

  public gov.nih.nci.ess.safetyreporting.types.SafetyReportDefinitionNotification deactivateSafetyReportDefinitionNotification(ess.caaers.nci.nih.gov.Id notificationId,_21090.org.iso.ST reasonForDeactivation) throws RemoteException, gov.nih.nci.ess.safetyreporting.management.stubs.types.SafetyReportingServiceException {
    //TODO: Implement this autogenerated method
    throw new RemoteException("Not yet implemented");
  }

}
