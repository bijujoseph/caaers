package gov.nih.nci.ess.safetyreporting.service.safetyreportreview.service.globus.resource;

import gov.nih.nci.ess.safetyreporting.service.safetyreportreview.common.SafetyReportReviewConstants;
import gov.nih.nci.ess.safetyreporting.service.safetyreportreview.stubs.SafetyReportReviewResourceProperties;

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
public class SafetyReportReviewResourceHome extends ResourceHomeImpl {
        private static final UUIDGen UUIDGEN = UUIDGenFactory.getUUIDGen();


	/**
 	* Creates a new Resource, adds it to the list of resources managed by this resource home,
 	* and returns the key to the resource.
 	*/
	public ResourceKey createResource() throws Exception {
		// Create a resource and initialize it
		SafetyReportReviewResource resource = (SafetyReportReviewResource) createNewInstance();
		// Create the resource properties bean so that the resource can use it to hold the  resource property values
		SafetyReportReviewResourceProperties props = new SafetyReportReviewResourceProperties();
        
        // Get a unique id for the resource
        Object id = UUIDGEN.nextUUID();
        
        // Create the resource key set it on the resource
		// this key is used for index service registration
		ResourceKey key = new SimpleResourceKey(getKeyTypeName(), id);
		resource.setResourceKey(key);
		
        resource.initialize(props, SafetyReportReviewConstants.RESOURCE_PROPERTY_SET, id);

		// Add the resource to the list of resources in this home
		add(key, resource);
		return key;
	}
	
	/**
 	* Take a resource key managed by this resource, locates the resource, and created a typed EPR for the resource.
 	*/
	public gov.nih.nci.ess.safetyreporting.service.safetyreportreview.stubs.types.SafetyReportReviewReference getResourceReference(ResourceKey key) throws Exception {
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
		transportURL += "SafetyReportReview";
		EndpointReferenceType epr = AddressingUtils.createEndpointReference(transportURL,key);
		gov.nih.nci.ess.safetyreporting.service.safetyreportreview.stubs.types.SafetyReportReviewReference ref = new gov.nih.nci.ess.safetyreporting.service.safetyreportreview.stubs.types.SafetyReportReviewReference();
		ref.setEndpointReference(epr);
		return ref;
	}

	/**
 	* Given the key of a resource managed by this resource home, a type resource will be returned.
 	*/	
	public SafetyReportReviewResource getResource(ResourceKey key) throws ResourceException {
		SafetyReportReviewResource thisResource = (SafetyReportReviewResource)find(key);
		return thisResource;
	}
	
    /**
     * Get the resouce that is being addressed in this current context
     */
    public SafetyReportReviewResource getAddressedResource() throws Exception {
        SafetyReportReviewResource thisResource;
        thisResource = (SafetyReportReviewResource) ResourceContext.getResourceContext().getResource();
        return thisResource;
    }

	
}