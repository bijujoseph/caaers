package gov.nih.nci.ess.ae.service.globus;

import gov.nih.nci.ess.ae.service.AdverseEventEnterpriseServiceImpl;

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
public class AdverseEventEnterpriseServiceProviderImpl{
	
	AdverseEventEnterpriseServiceImpl impl;
	
	public AdverseEventEnterpriseServiceProviderImpl() throws RemoteException {
		impl = new AdverseEventEnterpriseServiceImpl();
	}
	

}