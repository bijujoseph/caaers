/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.resolver;

import gov.nih.nci.cabig.caaers.domain.Organization;
import gov.nih.nci.cabig.caaers.domain.RemoteOrganization;
import gov.nih.nci.cabig.caaers.utils.XMLUtil;
import gov.nih.nci.coppa.po.IdentifiedOrganization;
import gov.nih.nci.coppa.po.Person;
import gov.nih.nci.security.util.StringUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.iso._21090.II;

import com.semanticbits.coppa.infrastructure.service.RemoteResolver;
import com.semanticbits.coppasimulator.util.CoppaObjectFactory;

public class OrganizationResolver extends BaseResolver implements RemoteResolver{ 
	private static Logger logger = Logger.getLogger(OrganizationResolver.class);
	private String ORGANIZATION_ROOT = "2.16.840.1.113883.3.26.4.2";
	
	public Object getRemoteEntityByUniqueId(String externalId) {
		logger.info("Entering OrganizationResolver.getRemoteEntityByUniqueId(externalId : " + externalId + ")"  );
		return this.getRemoteOrganizationsByExternalId(externalId);
		
	}
	/**
	 * Organization Search By Example . 
	 */
	@SuppressWarnings("unchecked")
	public List<Object> find(Object example) {	
		logger.info("Entering OrganizationResolver.find()");
		List<Object> remoteOrganizations = new ArrayList<Object>();
		Organization remoteOrgExample = (RemoteOrganization)example;
		remoteOrganizations = getRemoteOrganizationsByExample(remoteOrgExample);
		logger.info("Exiting OrganizationResolver.find()");
		return remoteOrganizations;
	}
	
	/**
	 * Organization Search By Example 
	 * By Name,City,Country,NCI-Id
	 * @param remoteOrgExample
	 * @return
	 */
	public List<Object> getRemoteOrganizationsByExample(Organization remoteOrgExample){
		List<Object> remoteOrganizations = new ArrayList<Object>();
		// List<gov.nih.nci.coppa.po.CorrelationNode> correlationNodes = new ArrayList<gov.nih.nci.coppa.po.CorrelationNode>();
		// get by name
		gov.nih.nci.coppa.po.Organization coppaOrganization = 
			CoppaObjectFactory.getCoppaOrganization(remoteOrgExample.getName(), null, remoteOrgExample.getCity(), null, null, remoteOrgExample.getCountry(), null);
		if (!StringUtilities.isBlank(remoteOrgExample.getExternalId())) {
			II ii = new II();
			ii.setExtension(remoteOrgExample.getExternalId());
			ii.setRoot(ORGANIZATION_ROOT);
			coppaOrganization.setIdentifier(ii);
		}
		gov.nih.nci.coppa.po.IdentifiedOrganization identifiedOrganization = new gov.nih.nci.coppa.po.IdentifiedOrganization();
		if (!StringUtilities.isBlank(remoteOrgExample.getNciInstituteCode())) {
			II ii = new II();
			ii.setExtension(remoteOrgExample.getNciInstituteCode());//setNullFlavor(NullFlavor.NI);
			identifiedOrganization.setAssignedId(ii);
		}
		String correlationNodeXmlPayload = CoppaObjectFactory.getCorrelationNodePayload(identifiedOrganization, coppaOrganization, null);

		String resultXml = broadcastSearchCorrelationsWithEntities(correlationNodeXmlPayload, true, false);
		remoteOrganizations = buildOrganizationsFromResults(resultXml);
	
		return remoteOrganizations;
	}
	
	/**
	 * Organization Search By external Id (COPPA id)
	 * @param externalId
	 * @return
	 */
	public Object getRemoteOrganizationsByExternalId(String externalId){
		Organization remoteOrgExample = new RemoteOrganization();
		remoteOrgExample.setExternalId(externalId);
		List<Object> objects = find(remoteOrgExample);
		if (objects.size()>0) {
			return objects.get(0);
		}
		return null;
	}
	
	private List<Object> buildOrganizationsFromResults(String resultXml) {
		List<String> results = XMLUtil.getObjectsFromCoppaResponse(resultXml);
		List<Object> remoteOrganizations = new ArrayList<Object>();

		Organization remoteOrganization = null;
		for (String result:results) {
			gov.nih.nci.coppa.po.CorrelationNode correlationNode = CoppaObjectFactory.getCorrelationNodeObjectFromXml(result);
			remoteOrganization = populateRemoteOrganization(correlationNode);
			remoteOrganizations.add(remoteOrganization);
		}
		return remoteOrganizations;
	}
	/**
	 * Populate caAERS organization with COPPA organization . 
	 * @param correlationNode
	 * @return
	 */
	private Organization populateRemoteOrganization(gov.nih.nci.coppa.po.CorrelationNode correlationNode){
		Organization remoteOrganization = new RemoteOrganization();
		gov.nih.nci.coppa.po.EntityType entityType = correlationNode.getPlayer();
		gov.nih.nci.coppa.po.Organization coppaOrganization = (gov.nih.nci.coppa.po.Organization)entityType.getContent().get(0);
		
		gov.nih.nci.coppa.po.CorrelationType correlationType = correlationNode.getCorrelation();
		gov.nih.nci.coppa.po.IdentifiedOrganization identifiedOrganization =(gov.nih.nci.coppa.po.IdentifiedOrganization)correlationType.getContent().get(0);
		
		remoteOrganization.setNciInstituteCode(identifiedOrganization.getAssignedId().getExtension());	
		remoteOrganization.setName(CoppaObjectFactory.getName(coppaOrganization.getName()));
		remoteOrganization.setCity(CoppaObjectFactory.getCity(coppaOrganization.getPostalAddress()));
		remoteOrganization.setCountry(CoppaObjectFactory.getCountry(coppaOrganization.getPostalAddress()));
		remoteOrganization.setExternalId(coppaOrganization.getIdentifier().getExtension());
		return remoteOrganization;
	}

	public Object saveOrUpdate(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object populateRole(Person coppaPerson, String staffAssignedIdentifier, List<gov.nih.nci.coppa.po.Organization> coppaOrganizationList, Map<String, IdentifiedOrganization> organizationIdToIdentifiedOrganizationsMap) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object populateRole(Person coppaPerson, String staffAssignedIdentifier, IdentifiedOrganization identifiedOrganization) {
		// TODO Auto-generated method stub
		return null;
	}

}
