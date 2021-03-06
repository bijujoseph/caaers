/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.ess.ae.service.aeassociate.service;

import gov.nih.nci.ess.ae.service.aeassociate.service.globus.resource.AEAssociateResource;
import  gov.nih.nci.ess.ae.service.service.AdverseEventEnterpriseServiceConfiguration;

import java.rmi.RemoteException;

import javax.naming.InitialContext;
import javax.xml.namespace.QName;

import org.apache.axis.MessageContext;
import org.globus.wsrf.Constants;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceContextException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceHome;
import org.globus.wsrf.ResourceProperty;
import org.globus.wsrf.ResourcePropertySet;


/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * Provides some simple accessors for the Impl.
 * 
 * @created by Introduce Toolkit version 1.3
 * 
 */
public abstract class AEAssociateImplBase {
	
	public AEAssociateImplBase() throws RemoteException {
	
	}
	
	public AdverseEventEnterpriseServiceConfiguration getConfiguration() throws Exception {
		return AdverseEventEnterpriseServiceConfiguration.getConfiguration();
	}
	
	
	public gov.nih.nci.ess.ae.service.aeassociate.service.globus.resource.AEAssociateResourceHome getResourceHome() throws Exception {
		ResourceHome resource = getResourceHome("home");
		return (gov.nih.nci.ess.ae.service.aeassociate.service.globus.resource.AEAssociateResourceHome)resource;
	}

	
	
	
	public gov.nih.nci.ess.ae.service.service.globus.resource.AdverseEventEnterpriseServiceResourceHome getAdverseEventEnterpriseServiceResourceHome() throws Exception {
		ResourceHome resource = getResourceHome("adverseEventEnterpriseServiceHome");
		return (gov.nih.nci.ess.ae.service.service.globus.resource.AdverseEventEnterpriseServiceResourceHome)resource;
	}
	
	public gov.nih.nci.ess.ae.service.management.service.globus.resource.ManagementResourceHome getManagementResourceHome() throws Exception {
		ResourceHome resource = getResourceHome("managementHome");
		return (gov.nih.nci.ess.ae.service.management.service.globus.resource.ManagementResourceHome)resource;
	}
	
	public gov.nih.nci.ess.ae.service.query.service.globus.resource.QueryResourceHome getQueryResourceHome() throws Exception {
		ResourceHome resource = getResourceHome("queryHome");
		return (gov.nih.nci.ess.ae.service.query.service.globus.resource.QueryResourceHome)resource;
	}
	
	public gov.nih.nci.ess.ae.service.aeadvancedquery.service.globus.resource.AEAdvancedQueryResourceHome getAEAdvancedQueryResourceHome() throws Exception {
		ResourceHome resource = getResourceHome("aEAdvancedQueryHome");
		return (gov.nih.nci.ess.ae.service.aeadvancedquery.service.globus.resource.AEAdvancedQueryResourceHome)resource;
	}
	
	public gov.nih.nci.ess.ae.service.protocol.service.globus.resource.AEProtocolResourceHome getAEProtocolResourceHome() throws Exception {
		ResourceHome resource = getResourceHome("aEProtocolHome");
		return (gov.nih.nci.ess.ae.service.protocol.service.globus.resource.AEProtocolResourceHome)resource;
	}
	
	
	protected ResourceHome getResourceHome(String resourceKey) throws Exception {
		MessageContext ctx = MessageContext.getCurrentContext();

		ResourceHome resourceHome = null;
		
		String servicePath = ctx.getTargetService();

		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/" + resourceKey;
		try {
			javax.naming.Context initialContext = new InitialContext();
			resourceHome = (ResourceHome) initialContext.lookup(jndiName);
		} catch (Exception e) {
			throw new Exception("Unable to instantiate resource home. : " + resourceKey, e);
		}

		return resourceHome;
	}


}

