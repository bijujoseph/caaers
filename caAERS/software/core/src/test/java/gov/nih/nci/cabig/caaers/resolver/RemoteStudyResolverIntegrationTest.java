/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.resolver;

import gov.nih.nci.cabig.caaers.CaaersTestCase;
/**
 * This test will execute on the actual bean objects loaded from "applicationContext-coppa.xml"
 * @author Biju Joseph
 *
 */
public class RemoteStudyResolverIntegrationTest extends CaaersTestCase {
	private RemoteStudyResolver studyResolver;
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testLoading(){
		studyResolver = (RemoteStudyResolver)getDeployedApplicationContext().getBean("remoteStudyResolver");
		assertNotNull(studyResolver);
		assertNotNull(studyResolver.getMessageBroadcastService());
	}

}
