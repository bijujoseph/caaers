/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.esb.client;

import gov.nih.nci.cabig.caaers.CaaersTestCase;
/**
 * 
 * @author Biju Joseph
 *
 */
public class MessageNotificationServiceIntegrationTest extends CaaersTestCase {
	MessageNotificationService service;
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testBeanLoading(){
		service = (MessageNotificationService)getDeployedApplicationContext().getBean("messageNotificationService");
		assertNotNull(service);
	}
}
