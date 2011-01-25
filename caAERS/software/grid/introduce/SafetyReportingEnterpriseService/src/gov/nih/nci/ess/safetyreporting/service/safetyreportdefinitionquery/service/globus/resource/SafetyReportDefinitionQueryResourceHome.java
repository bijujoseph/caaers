package gov.nih.nci.ess.safetyreporting.service.safetyreportdefinitionquery.service.globus.resource;

import gov.nih.nci.ess.safetyreporting.service.safetyreportdefinitionquery.common.SafetyReportDefinitionQueryConstants;
import gov.nih.nci.ess.safetyreporting.service.safetyreportdefinitionquery.stubs.SafetyReportDefinitionQueryResourceProperties;

import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.ResourceHomeImpl;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.MessageContext;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.utils.AddressingUtils;
import org.apache.axis.components.uuid.UUIDGen;
import org.apache.axis.components.uuid.UUIDGenFactory;

/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This class implements the resource home for the resource type represented
 * by this service.
 * 
 * @created by Introduce Toolkit version 1.4
 * 
 */
public class SafetyReportDefinitionQueryResourceHome extends ResourceHomeImpl {
        private static final UUIDGen UUIDGEN = UUIDGenFactory.getUUIDGen();


	/**
 	* Creates a new Resource, adds it to the list of resources managed by this resource home,
 	* and returns the key to the resource.
 	*/
	public ResourceKey createResource() throws Exception {
		// Create a resource and initialize it
		SafetyReportDefinitionQueryResource resource = (SafetyReportDefinitionQueryResource) createNewInstance();
		// Create the resource properties bean so that the resource can use it to hold the  resource property values
		SafetyReportDefinitionQueryResourceProperties props = new SafetyReportDefinitionQueryResourceProperties();
        
        // Get a unique id for the resource
        Object id = UUIDGEN.nextUUID();
        
        // Create the resource key set it on the resource
		// this key is used for index service registration
		ResourceKey key = new SimpleResourceKey(getKeyTypeName(), id);
		resource.setResourceKey(key);
		
        resource.initialize(props, SafetyReportDefinitionQueryConstants.RESOURCE_PROPERTY_SET, id);

		// Add the resource to the list of resources in this home
		add(key, resource);
		return key;
	}
	
	/**
 	* Take a resource key managed by this resource, locates the resource, and created a typed EPR for the resource.
 	*/
	public gov.nih.nci.ess.safetyreporting.service.safetyreportdefinitionquery.stubs.types.SafetyReportDefinitionQueryReference getResourceReference(ResourceKey key) throws Exception {
		MessageContext ctx = MessageContext.getCurrentContext();
		String transportURL = (String) ctx.getProperty(org.apache.axis.MessageContext.TRANS_URL);
		org.apache.axis.message.addressing.AttributedURI uri = new org.apache.axis.message.addressing.AttributedURI(transportURL);
		java.net.URL baseURL = org.globus.wsrf.container.ServiceHost.getBaseURL();
		String correctHost = baseURL.getHost();
		uri.setHost(correctHost);
		int correctPort = baseURL.getPort();
		uri.setPort(correctPort);
		transportURL = uri.toString();
		transportURL = transportURL.substring(0,transportURL.lastIndexOf('/') +1 );
		transportURL += "SafetyReportDefinitionQuery";
		EndpointReferenceType epr = AddressingUtils.createEndpointReference(transportURL,key);
		gov.nih.nci.ess.safetyreporting.service.safetyreportdefinitionquery.stubs.types.SafetyReportDefinitionQueryReference ref = new gov.nih.nci.ess.safetyreporting.service.safetyreportdefinitionquery.stubs.types.SafetyReportDefinitionQueryReference();
		ref.setEndpointReference(epr);
		return ref;
	}

	/**
 	* Given the key of a resource managed by this resource home, a type resource will be returned.
 	*/	
	public SafetyReportDefinitionQueryResource getResource(ResourceKey key) throws ResourceException {
		SafetyReportDefinitionQueryResource thisResource = (SafetyReportDefinitionQueryResource)find(key);
		return thisResource;
	}
	
    /**
     * Get the resouce that is being addressed in this current context
     */
    public SafetyReportDefinitionQueryResource getAddressedResource() throws Exception {
        SafetyReportDefinitionQueryResource thisResource;
        thisResource = (SafetyReportDefinitionQueryResource) ResourceContext.getResourceContext().getResource();
        return thisResource;
    }

	
}