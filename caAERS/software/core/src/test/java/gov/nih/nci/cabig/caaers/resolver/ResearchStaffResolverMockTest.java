/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.resolver;

import edu.duke.cabig.c3pr.esb.Metadata;
import gov.nih.nci.cabig.caaers.AbstractTestCase;
import gov.nih.nci.cabig.caaers.MetadataMatcher;
import gov.nih.nci.cabig.caaers.domain.Organization;
import gov.nih.nci.cabig.caaers.domain.RemoteOrganization;
import gov.nih.nci.cabig.caaers.domain.RemoteResearchStaff;
import gov.nih.nci.cabig.caaers.domain.ResearchStaff;
import gov.nih.nci.cabig.caaers.domain.SiteResearchStaff;
import gov.nih.nci.cabig.caaers.esb.client.MessageBroadcastService;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;

public class ResearchStaffResolverMockTest extends AbstractTestCase {
	
	ResearchStaffResolver researchStaffResolver ;
	MessageBroadcastService messageBroadcastService;
	//ResolverUtils resolverUtils;
	 
	protected void setUp() throws Exception {
		super.setUp();
		
		researchStaffResolver = new ResearchStaffResolver();
		messageBroadcastService = registerMockFor(MessageBroadcastService.class);
		//investigatorResolver.setResolverUtils(resolverUtils);
		researchStaffResolver.setMessageBroadcastService(messageBroadcastService);
	}
	
	public void testSearchByName() throws Exception {
		String xml =  IOUtils.toString(getClass().getResourceAsStream("rs_byname_correlation_coppa_response.xml"));
		Metadata mData = new Metadata("searchCorrelationsWithEntities",  "externalId", "PO_BUSINESS");
		EasyMock.expect(messageBroadcastService.broadcastCOPPA((List<String>)EasyMock.anyObject(), MetadataMatcher.eqMetadata(mData))).andReturn(xml);
		
		mData = new Metadata("getByPlayerIds",  "externalId", "IDENTIFIED_ORGANIZATION");
		xml =  IOUtils.toString(getClass().getResourceAsStream("byname_identifiedorg_coppa_response.xml"));
		EasyMock.expect(messageBroadcastService.broadcastCOPPA((List<String>)EasyMock.anyObject(), MetadataMatcher.eqMetadata(mData))).andReturn(xml);
		
		mData = new Metadata("getByPlayerIds",  "externalId", "IDENTIFIED_PERSON");
		xml =  IOUtils.toString(getClass().getResourceAsStream("byname_identifiedperson_coppa_response.xml"));
		EasyMock.expect(messageBroadcastService.broadcastCOPPA((List<String>)EasyMock.anyObject(), MetadataMatcher.eqMetadata(mData))).andReturn(xml);
		
		replayMocks();
		ResearchStaff example = new RemoteResearchStaff();
		example.setFirstName("abbas");
		List<Object> list = researchStaffResolver.find(example);
		assertEquals(4,list.size());
		
//		for (int i=0 ; i<list.size(); i++) {
//			ResearchStaff obj = (RemoteResearchStaff)list.get(i);
//			System.out.println(obj.getFirstName() + "," + obj.getLastName());
//			System.out.println(obj.getEmailAddress());
//			System.out.println(obj.getExternalId());
//			System.out.println(obj.getNciIdentifier());
//			//System.out.println(obj.getSiteInvestigators().get(0).getOrganization().getNciInstituteCode());
//		}
	}
	
	
	public void testSearchByNciId() throws Exception {
		String xml =  IOUtils.toString(getClass().getResourceAsStream("bynciid_identifiedperson_coppa_response.xml"));
		Metadata mData = new Metadata("search",  "externalId", "IDENTIFIED_PERSON");
		EasyMock.expect(messageBroadcastService.broadcastCOPPA((List<String>)EasyMock.anyObject(), MetadataMatcher.eqMetadata(mData))).andReturn(xml);
		
		mData = new Metadata("searchCorrelationsWithEntities",  "externalId", "PO_BUSINESS");
		xml =  IOUtils.toString(getClass().getResourceAsStream("rs_bynciid_correlation_coppa_response.xml"));
		EasyMock.expect(messageBroadcastService.broadcastCOPPA((List<String>)EasyMock.anyObject(), MetadataMatcher.eqMetadata(mData))).andReturn(xml);
		
		mData = new Metadata("getByPlayerIds",  "externalId", "IDENTIFIED_ORGANIZATION");
		xml =  IOUtils.toString(getClass().getResourceAsStream("bynciid_identifiedorg_coppa_response.xml"));
		EasyMock.expect(messageBroadcastService.broadcastCOPPA((List<String>)EasyMock.anyObject(), MetadataMatcher.eqMetadata(mData))).andReturn(xml);

		mData = new Metadata("getByPlayerIds",  "externalId", "IDENTIFIED_PERSON");
		xml =  IOUtils.toString(getClass().getResourceAsStream("bynciid_identifiedperson_byplids_coppa_response.xml"));
		EasyMock.expect(messageBroadcastService.broadcastCOPPA((List<String>)EasyMock.anyObject(), MetadataMatcher.eqMetadata(mData))).andReturn(xml);
		
		replayMocks();
		ResearchStaff example = new RemoteResearchStaff();
		example.setNciIdentifier("60442");
		List<Object> list = researchStaffResolver.find(example);
		assertEquals(list.size(),1);	
	}
	public void testSearchByOrganization() throws Exception {
		String xml =  IOUtils.toString(getClass().getResourceAsStream("byorg_identifiedorg_coppa_response.xml"));
		Metadata mData = new Metadata("search",  "externalId", "IDENTIFIED_ORGANIZATION");
		EasyMock.expect(messageBroadcastService.broadcastCOPPA((List<String>)EasyMock.anyObject(), MetadataMatcher.eqMetadata(mData))).andReturn(xml);
		
		mData = new Metadata("searchCorrelationsWithEntities",  "externalId", "PO_BUSINESS");
		xml =  IOUtils.toString(getClass().getResourceAsStream("rs_byorg_correlation_coppa_response.xml"));
		EasyMock.expect(messageBroadcastService.broadcastCOPPA((List<String>)EasyMock.anyObject(), MetadataMatcher.eqMetadata(mData))).andReturn(xml);
		
		mData = new Metadata("getByPlayerIds",  "externalId", "IDENTIFIED_PERSON");
		xml =  IOUtils.toString(getClass().getResourceAsStream("byorg_identifiedperson_coppa_response.xml"));
		EasyMock.expect(messageBroadcastService.broadcastCOPPA((List<String>)EasyMock.anyObject(), MetadataMatcher.eqMetadata(mData))).andReturn(xml);
		
		replayMocks();
		ResearchStaff example = new RemoteResearchStaff();
		SiteResearchStaff sr = new SiteResearchStaff();
		Organization org = new RemoteOrganization();
		org.setNciInstituteCode("KY082");
		sr.setOrganization(org);
		example.getSiteResearchStaffs().add(sr);
		List<Object> list = researchStaffResolver.find(example);
		assertEquals(list.size(),3);
		
	}	
	
	public void testSearchByExternalId() throws Exception {
		String xml =  IOUtils.toString(getClass().getResourceAsStream("rs_byextid_correlation_coppa_response.xml"));
		Metadata mData = new Metadata("searchCorrelationsWithEntities",  "externalId", "PO_BUSINESS");
		EasyMock.expect(messageBroadcastService.broadcastCOPPA((List<String>)EasyMock.anyObject(), MetadataMatcher.eqMetadata(mData))).andReturn(xml);
		
		mData = new Metadata("getByPlayerIds",  "externalId", "IDENTIFIED_ORGANIZATION");
		xml =  IOUtils.toString(getClass().getResourceAsStream("byextid_identifiedorg_coppa_response.xml"));
		EasyMock.expect(messageBroadcastService.broadcastCOPPA((List<String>)EasyMock.anyObject(), MetadataMatcher.eqMetadata(mData))).andReturn(xml);
		
		mData = new Metadata("getByPlayerIds",  "externalId", "IDENTIFIED_PERSON");
		xml =  IOUtils.toString(getClass().getResourceAsStream("byextid_identifiedperson_coppa_response.xml"));
		EasyMock.expect(messageBroadcastService.broadcastCOPPA((List<String>)EasyMock.anyObject(), MetadataMatcher.eqMetadata(mData))).andReturn(xml);
		
		replayMocks();
		Object obj1 = researchStaffResolver.getRemoteEntityByUniqueId("1167533");
		ResearchStaff obj = (RemoteResearchStaff)obj1;
		assertEquals(obj.getExternalId(),"1167533");	
		
	}

}
