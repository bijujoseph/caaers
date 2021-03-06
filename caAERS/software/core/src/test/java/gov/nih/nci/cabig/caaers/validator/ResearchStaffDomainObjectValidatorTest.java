/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.validator;

import gov.nih.nci.cabig.caaers.CaaersDbTestCase;
import gov.nih.nci.cabig.caaers.domain.Fixtures;
import gov.nih.nci.cabig.caaers.domain.LocalResearchStaff;
import gov.nih.nci.cabig.caaers.domain.Organization;
import gov.nih.nci.cabig.caaers.domain.ResearchStaff;
import gov.nih.nci.cabig.caaers.validation.validator.DomainObjectValidator;

import java.util.ArrayList;
import java.util.List;

public class ResearchStaffDomainObjectValidatorTest extends CaaersDbTestCase{
	ResearchStaff rStaff;
	Organization organization;
	List<String> errors;
	DomainObjectValidator domainObjectValidator;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		rStaff = new LocalResearchStaff();
		organization = Fixtures.createOrganization("NCI");
		errors = new ArrayList<String>();
		domainObjectValidator = (DomainObjectValidator)getDeployedApplicationContext().getBean("domainObjectValidator");
	}
	
	public void testResearchStaffValid(){
		
		rStaff.setFirstName("John");
		rStaff.setLastName("Doe");
		rStaff.setEmailAddress("john.doe@semanticbits.com");
		rStaff.setPhoneNumber("111-111-1111");
		rStaff.setFaxNumber("111-111-1112");
		rStaff.setNciIdentifier("JOHN-D2");

		errors = domainObjectValidator.validate(rStaff);
		
		assertEquals(0,errors.size());
	}
	
	

}
