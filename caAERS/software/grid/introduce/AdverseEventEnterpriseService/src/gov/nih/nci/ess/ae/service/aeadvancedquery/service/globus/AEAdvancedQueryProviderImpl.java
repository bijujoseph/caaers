package gov.nih.nci.ess.ae.service.aeadvancedquery.service.globus;

import gov.nih.nci.ess.ae.service.aeadvancedquery.service.AEAdvancedQueryImpl;

import java.rmi.RemoteException;

/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This class implements each method in the portType of the service.  Each method call represented
 * in the port type will be then mapped into the unwrapped implementation which the user provides
 * in the AdverseEventEnterpriseServiceImpl class.  This class handles the boxing and unboxing of each method call
 * so that it can be correclty mapped in the unboxed interface that the developer has designed and 
 * has implemented.  Authorization callbacks are automatically made for each method based
 * on each methods authorization requirements.
 * 
 * @created by Introduce Toolkit version 1.3
 * 
 */
public class AEAdvancedQueryProviderImpl{
	
	AEAdvancedQueryImpl impl;
	
	public AEAdvancedQueryProviderImpl() throws RemoteException {
		impl = new AEAdvancedQueryImpl();
	}
	

    public gov.nih.nci.ess.ae.service.aeadvancedquery.stubs.GetAuditTrialOfAdverseEventResponse getAuditTrialOfAdverseEvent(gov.nih.nci.ess.ae.service.aeadvancedquery.stubs.GetAuditTrialOfAdverseEventRequest params) throws RemoteException, gov.nih.nci.ess.ae.service.management.stubs.types.AdverseEventServiceException {
    gov.nih.nci.ess.ae.service.aeadvancedquery.stubs.GetAuditTrialOfAdverseEventResponse boxedResult = new gov.nih.nci.ess.ae.service.aeadvancedquery.stubs.GetAuditTrialOfAdverseEventResponse();
    boxedResult.setAuditTrial(impl.getAuditTrialOfAdverseEvent(params.getAdverseEventIdentifier().getId(),params.getMinDate().getTsDateTime(),params.getMaxDate().getTsDateTime()));
    return boxedResult;
  }

    public gov.nih.nci.ess.ae.service.aeadvancedquery.stubs.FindAdverseEventsResponse findAdverseEvents(gov.nih.nci.ess.ae.service.aeadvancedquery.stubs.FindAdverseEventsRequest params) throws RemoteException, gov.nih.nci.ess.ae.service.management.stubs.types.AdverseEventServiceException {
    gov.nih.nci.ess.ae.service.aeadvancedquery.stubs.FindAdverseEventsResponse boxedResult = new gov.nih.nci.ess.ae.service.aeadvancedquery.stubs.FindAdverseEventsResponse();
    boxedResult.setAdverseEvent(impl.findAdverseEvents(params.getAdverseEventQuery().getAdverseEventQuery(),params.getLimitOffset().getLimitOffset()));
    return boxedResult;
  }

}
