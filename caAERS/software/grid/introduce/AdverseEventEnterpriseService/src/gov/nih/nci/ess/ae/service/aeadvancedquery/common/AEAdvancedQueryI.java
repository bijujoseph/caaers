package gov.nih.nci.ess.ae.service.aeadvancedquery.common;

import java.rmi.RemoteException;

/** 
 * This class is autogenerated, DO NOT EDIT.
 * 
 * This interface represents the API which is accessable on the grid service from the client. 
 * 
 * @created by Introduce Toolkit version 1.3
 * 
 */
public interface AEAdvancedQueryI {

  public ess.caaers.nci.nih.gov.AdverseEvent[] findAdverseEvents(ess.caaers.nci.nih.gov.AdverseEventQuery adverseEventQuery,ess.caaers.nci.nih.gov.LimitOffset limitOffset) throws RemoteException, gov.nih.nci.ess.ae.service.management.stubs.types.AdverseEventServiceException ;

}
