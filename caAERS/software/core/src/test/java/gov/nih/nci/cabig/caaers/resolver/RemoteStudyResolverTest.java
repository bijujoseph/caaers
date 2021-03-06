/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.resolver;

import edu.duke.cabig.c3pr.esb.Metadata;
import gov.nih.nci.cabig.caaers.AbstractTestCase;
import gov.nih.nci.cabig.caaers.domain.RemoteStudy;
import gov.nih.nci.cabig.caaers.domain.StudyTherapy;
import gov.nih.nci.cabig.caaers.domain.StudyTherapyType;
import gov.nih.nci.cabig.caaers.esb.client.MessageBroadcastService;
import gov.nih.nci.coppa.services.pa.StudyProtocol;
import gov.nih.nci.cabig.caaers.domain.InvestigationalNewDrug;
import gov.nih.nci.cabig.caaers.domain.OrganizationHeldIND;
import gov.nih.nci.cabig.caaers.domain.InvestigatorHeldIND;


import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.iso._21090.II;
/**
 * 
 * @author Biju Joseph
 *
 */
public class RemoteStudyResolverTest extends AbstractTestCase {
	RemoteStudyResolver studyResolver;
	MessageBroadcastService messageBroadcastService;
	StudyProtocol studyProtocol;
	II identifier;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		
		//initilize the study protocol
		studyProtocol = new StudyProtocol();
		identifier = new II();
		identifier.setExtension("tester");
		studyProtocol.setIdentifier(identifier);
		
		 
		studyResolver = new RemoteStudyResolver();
		messageBroadcastService = registerMockFor(MessageBroadcastService.class);
		studyResolver.setMessageBroadcastService(messageBroadcastService);
	}
	
	public void testPopulateIND_NIH_NCI_CoppaResponse() throws Exception{
		String xml =  IOUtils.toString(getClass().getResourceAsStream("indide_with_nci_nih_coppa_response.xml"));
		EasyMock.expect(messageBroadcastService.broadcastCOPPA((List<String>)EasyMock.anyObject(), (Metadata)EasyMock.anyObject())).andReturn(xml);
		replayMocks();
		RemoteStudy remoteStudy = new RemoteStudy();
		studyResolver.populateIND(studyProtocol, remoteStudy);
		assertNotNull(remoteStudy.getInvestigationalNewDrugList());
		assertEquals(2, remoteStudy.getInvestigationalNewDrugList().size());
		assertTrue(remoteStudy.getInvestigationalNewDrugList().get(0).getINDHolder() instanceof OrganizationHeldIND); 
		assertTrue(remoteStudy.getInvestigationalNewDrugList().get(1).getINDHolder() instanceof OrganizationHeldIND);
		assertTrue(!StringUtils.equals(((OrganizationHeldIND)remoteStudy.getInvestigationalNewDrugList().get(0).getINDHolder()).getOrganization().getNciInstituteCode(), "DUMMY"));
		assertTrue(!StringUtils.equals(((OrganizationHeldIND)remoteStudy.getInvestigationalNewDrugList().get(1).getINDHolder()).getOrganization().getNciInstituteCode(), "DUMMY"));
		assertEquals(new Integer("46211"), remoteStudy.getInvestigationalNewDrugList().get(0).getIndNumber());
		assertEquals(new Integer("98765"), remoteStudy.getInvestigationalNewDrugList().get(1).getIndNumber());
		
		verifyMocks();
	}
	
	public void testPopulateIND_Industry_Organization_CoppaResponse() throws Exception{
		String xml =  IOUtils.toString(getClass().getResourceAsStream("indide_with_industry_org_coppa_response.xml"));
		EasyMock.expect(messageBroadcastService.broadcastCOPPA((List<String>)EasyMock.anyObject(), (Metadata)EasyMock.anyObject())).andReturn(xml);
		replayMocks();
		RemoteStudy remoteStudy = new RemoteStudy();
		studyResolver.populateIND(studyProtocol, remoteStudy);
		assertNotNull(remoteStudy.getInvestigationalNewDrugList());
		assertEquals(2, remoteStudy.getInvestigationalNewDrugList().size());
		assertTrue(remoteStudy.getInvestigationalNewDrugList().get(0).getINDHolder() instanceof OrganizationHeldIND); 
		assertTrue(remoteStudy.getInvestigationalNewDrugList().get(1).getINDHolder() instanceof OrganizationHeldIND);
		assertTrue(StringUtils.equals(((OrganizationHeldIND)remoteStudy.getInvestigationalNewDrugList().get(0).getINDHolder()).getOrganization().getNciInstituteCode(), "DUMMY"));
		assertTrue(StringUtils.equals(((OrganizationHeldIND)remoteStudy.getInvestigationalNewDrugList().get(1).getINDHolder()).getOrganization().getNciInstituteCode(), "DUMMY"));
		assertEquals(new Integer("76543"), remoteStudy.getInvestigationalNewDrugList().get(0).getIndNumber());
		assertEquals(new Integer("871234"), remoteStudy.getInvestigationalNewDrugList().get(1).getIndNumber());

		verifyMocks();
	}
	
	public void testPopulateIND_Investigator_CoppaResponse() throws Exception{
		String xml =  IOUtils.toString(getClass().getResourceAsStream("indide_with_investigator_coppa_response.xml"));
		EasyMock.expect(messageBroadcastService.broadcastCOPPA((List<String>)EasyMock.anyObject(), (Metadata)EasyMock.anyObject())).andReturn(xml);
		replayMocks();
		RemoteStudy remoteStudy = new RemoteStudy();
		studyResolver.populateIND(studyProtocol, remoteStudy);
		assertNotNull(remoteStudy.getInvestigationalNewDrugList());
		assertEquals(1, remoteStudy.getInvestigationalNewDrugList().size());
		assertTrue(remoteStudy.getInvestigationalNewDrugList().get(0).getINDHolder() instanceof InvestigatorHeldIND);
		assertTrue(StringUtils.equals(((InvestigatorHeldIND)remoteStudy.getInvestigationalNewDrugList().get(0).getINDHolder()).getInvestigator().getNciIdentifier(), "-1111"));
		assertEquals(new Integer("53893"), remoteStudy.getInvestigationalNewDrugList().get(0).getIndNumber());
		
		verifyMocks();
	}
}
