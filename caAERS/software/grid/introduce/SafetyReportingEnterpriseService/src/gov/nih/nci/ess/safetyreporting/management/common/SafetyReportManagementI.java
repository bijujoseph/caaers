package gov.nih.nci.ess.safetyreporting.management.common;

import java.rmi.RemoteException;

/** 
 * This class is autogenerated, DO NOT EDIT.
 * 
 * This interface represents the API which is accessable on the grid service from the client. 
 * 
 * @created by Introduce Toolkit version 1.3
 * 
 */
public interface SafetyReportManagementI {

  /**
   * Provides the capability to associate additional information to a safety report.
   *
   * @param safetyReportId
   * @param additionalInformation
   * @throws SafetyReportingServiceException
   *	
   */
  public gov.nih.nci.ess.safetyreporting.types.SafetyReportVersion associateAdditionalInformationToSafetyReport(ess.caaers.nci.nih.gov.Id safetyReportId,ess.caaers.nci.nih.gov.AdditionalInformation additionalInformation) throws RemoteException, gov.nih.nci.ess.safetyreporting.management.stubs.types.SafetyReportingServiceException ;

  /**
   * Enables a client system to create a safety report record in the Safety Reporting System with all fields required for safety report creation.
   *
   * @param studyId
   * @param subjectId
   * @param patientId
   * @param adverseEventIds
   * @param problemIds
   * @param adverseEventReportingPeriod
   * @throws SafetyReportingServiceException
   *	
   */
  public gov.nih.nci.ess.safetyreporting.types.SafetyReportVersion initiateSafetyReport(ess.caaers.nci.nih.gov.Id studyId,ess.caaers.nci.nih.gov.Id subjectId,ess.caaers.nci.nih.gov.Id patientId,_21090.org.iso.DSET_II adverseEventIds,_21090.org.iso.DSET_II problemIds,gov.nih.nci.ess.safetyreporting.types.AdverseEventReportingPeriod adverseEventReportingPeriod) throws RemoteException, gov.nih.nci.ess.safetyreporting.management.stubs.types.SafetyReportingServiceException ;

  /**
   * Provides the capability to associate adverse events to a safety report.
   *
   * @param safetyReportId
   * @param adverseEventIds
   * @throws SafetyReportingServiceException
   *	
   */
  public gov.nih.nci.ess.safetyreporting.types.SafetyReportVersion associateAdverseEventsToSafetyReport(ess.caaers.nci.nih.gov.Id safetyReportId,_21090.org.iso.DSET_II adverseEventIds) throws RemoteException, gov.nih.nci.ess.safetyreporting.management.stubs.types.SafetyReportingServiceException ;

  /**
   * Provides the capability to dissociate an adverse event from a safety report.
   *
   * @param safetyReportId
   * @param adverseEventIds
   * @throws SafetyReportingServiceException
   *	
   */
  public gov.nih.nci.ess.safetyreporting.types.SafetyReportVersion dissociateAdverseEventsFromSafetyReport(ess.caaers.nci.nih.gov.Id safetyReportId,_21090.org.iso.DSET_II adverseEventIds) throws RemoteException, gov.nih.nci.ess.safetyreporting.management.stubs.types.SafetyReportingServiceException ;

  /**
   * Provides the capability to update the adverse event information in the safety report.
   *
   * @param safetyReportId
   * @param adverseEvent
   * @throws SafetyReportingServiceException
   *	
   */
  public gov.nih.nci.ess.safetyreporting.types.SafetyReportVersion updateAdverseEventInformationInSafetyReport(ess.caaers.nci.nih.gov.Id safetyReportId,ess.caaers.nci.nih.gov.AdverseEvent adverseEvent) throws RemoteException, gov.nih.nci.ess.safetyreporting.management.stubs.types.SafetyReportingServiceException ;

  public gov.nih.nci.ess.safetyreporting.types.SafetyReportVersion associateProblemToSafetyReport() throws RemoteException, gov.nih.nci.ess.safetyreporting.management.stubs.types.SafetyReportingServiceException ;

  /**
   * Provides the capability to associate a study to a safety report.
   *
   * @param safetyReportId
   * @param studyId
   * @throws SafetyReportingServiceException
   *	
   */
  public gov.nih.nci.ess.safetyreporting.types.SafetyReportVersion associateStudyToSafetyReport(ess.caaers.nci.nih.gov.Id safetyReportId,ess.caaers.nci.nih.gov.Id studyId) throws RemoteException, gov.nih.nci.ess.safetyreporting.management.stubs.types.SafetyReportingServiceException ;

}

