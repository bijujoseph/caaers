package gov.nih.nci.ess.safetyreporting.service.safetyreportrulesmanagement.common;

import java.rmi.RemoteException;

/** 
 * This class is autogenerated, DO NOT EDIT.
 * 
 * This interface represents the API which is accessible on the grid service from the client. 
 * 
 * @created by Introduce Toolkit version 1.4
 * 
 */
public interface SafetyReportRulesManagementI {

  public org.oasis.wsrf.lifetime.DestroyResponse destroy(org.oasis.wsrf.lifetime.Destroy params) throws RemoteException ;

  public org.oasis.wsrf.lifetime.SetTerminationTimeResponse setTerminationTime(org.oasis.wsrf.lifetime.SetTerminationTime params) throws RemoteException ;

}

