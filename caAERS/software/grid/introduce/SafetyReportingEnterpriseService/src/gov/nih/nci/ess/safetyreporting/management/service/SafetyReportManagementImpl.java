package gov.nih.nci.ess.safetyreporting.management.service;

import gov.nih.nci.ess.safetyreporting.management.common.SafetyReportManagementI;
import gov.nih.nci.ess.safetyreporting.misc.SpringContextCreator;

import java.rmi.RemoteException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.3
 * 
 */
public class SafetyReportManagementImpl extends SafetyReportManagementImplBase {
	
	private static final String BEAN_NAME = "safetyReportManagementImpl";
	
	private SafetyReportManagementI safetyReportManagementI; 
	
	public SafetyReportManagementImpl() throws RemoteException {
		super();
	}
	
	/**
	 * @throws BeansException
	 */
	private void initialize() throws BeansException {
		if (safetyReportManagementI == null) {
			ApplicationContext ctx = SpringContextCreator.getApplicationContext();
			safetyReportManagementI = (SafetyReportManagementI) ctx.getBean(BEAN_NAME);
		}
	}

	
  public gov.nih.nci.ess.safetyreporting.types.SafetyReportVersion initiateSafetyReport(ess.caaers.nci.nih.gov.Id studyId,ess.caaers.nci.nih.gov.Id subjectId,ess.caaers.nci.nih.gov.Id patientId,_21090.org.iso.DSET_II adverseEventIds,_21090.org.iso.DSET_II problemIds,gov.nih.nci.ess.safetyreporting.types.AdverseEventReportingPeriod adverseEventReportingPeriod) throws RemoteException, gov.nih.nci.ess.safetyreporting.management.stubs.types.SafetyReportingServiceException {
	initialize();
	return safetyReportManagementI.initiateSafetyReport(studyId, subjectId, patientId, adverseEventIds, problemIds, adverseEventReportingPeriod);
  }

  public gov.nih.nci.ess.safetyreporting.types.SafetyReportVersion associateAdverseEventsToSafetyReport(ess.caaers.nci.nih.gov.Id safetyReportId,_21090.org.iso.DSET_II adverseEventIds) throws RemoteException, gov.nih.nci.ess.safetyreporting.management.stubs.types.SafetyReportingServiceException {
	initialize();
	return safetyReportManagementI.associateAdverseEventsToSafetyReport(safetyReportId, adverseEventIds);
  }

  public gov.nih.nci.ess.safetyreporting.types.SafetyReportVersion dissociateAdverseEventsFromSafetyReport(ess.caaers.nci.nih.gov.Id safetyReportId,_21090.org.iso.DSET_II adverseEventIds) throws RemoteException, gov.nih.nci.ess.safetyreporting.management.stubs.types.SafetyReportingServiceException {
	initialize();
	return safetyReportManagementI.dissociateAdverseEventsFromSafetyReport(safetyReportId, adverseEventIds);
  }

  public gov.nih.nci.ess.safetyreporting.types.SafetyReportVersion updateAdverseEventInformationInSafetyReport(ess.caaers.nci.nih.gov.Id safetyReportId,ess.caaers.nci.nih.gov.AdverseEvent adverseEvent) throws RemoteException, gov.nih.nci.ess.safetyreporting.management.stubs.types.SafetyReportingServiceException {
	initialize();
    return safetyReportManagementI.updateAdverseEventInformationInSafetyReport(safetyReportId, adverseEvent);
  }

}

