/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.dao;

import static gov.nih.nci.cabig.caaers.CaaersUseCase.CREATE_STUDY;
import static gov.nih.nci.cabig.caaers.CaaersUseCase.STUDY_ABSTRACTION;
import gov.nih.nci.cabig.caaers.CaaersUseCases;
import gov.nih.nci.cabig.caaers.DaoNoSecurityTestCase;
import gov.nih.nci.cabig.caaers.dao.query.ResearchStaffQuery;
import gov.nih.nci.cabig.caaers.domain.Address;
import gov.nih.nci.cabig.caaers.domain.LocalResearchStaff;
import gov.nih.nci.cabig.caaers.domain.RemoteResearchStaff;
import gov.nih.nci.cabig.caaers.domain.ResearchStaff;

import java.util.List;

/**
 * @author Kulasekaran
 */
@CaaersUseCases( { CREATE_STUDY, STUDY_ABSTRACTION })
public class ResearchStaffDaoTest extends DaoNoSecurityTestCase<ResearchStaffDao> {
    private OrganizationDao organizationDao = (OrganizationDao) getApplicationContext().getBean("organizationDao");

    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    public void testGetById() throws Exception {
        ResearchStaff researchStaff = getDao().getById(-1000);
        assertNotNull("ResearchStaff not found", researchStaff);
        assertEquals("Wrong last name", "Gates", researchStaff.getLastName());
        assertEquals("Wrong first name", "Bill", researchStaff.getFirstName());
        assertNotNull(researchStaff.getSiteResearchStaffs());
        assertNotNull(researchStaff.getAddress());
        assertEquals("Park Centre Road", researchStaff.getAddress().getStreet());
        assertEquals("Fairfax", researchStaff.getAddress().getCity());
        assertEquals("Virginia", researchStaff.getAddress().getState());
        assertEquals("22033", researchStaff.getAddress().getZip());
    }
    
    public void testGetByLoginId(){
    	ResearchStaff researchStaff = getDao().getByLoginId("abcd");
    	assertNotNull(researchStaff);
    	assertEquals("Bill", researchStaff.getFirstName());
    }

    
    public void testGetByNciIdentifier(){
    	List<ResearchStaff> researchStaffs = getDao().getByNciIdentifier(new String[] {"nci id"}, -1000);
    	assertNotNull(researchStaffs);
        assertEquals(4, researchStaffs.size());
    }

    public void testSaveLocalResearchStaff() throws Exception {
        interruptSession();

        Integer savedId;
        {
            ResearchStaff researchStaff = new LocalResearchStaff();
            researchStaff.setFirstName("Jeff");
            researchStaff.setLastName("Someone");
            researchStaff.setEmailAddress("abc@def.com");
            researchStaff.setPhoneNumber("123-456-789");
            researchStaff.setNciIdentifier("nci id");
            researchStaff.setAddress(new Address());

            getDao().save(researchStaff);

            savedId = researchStaff.getId();
            assertNotNull("The saved researchStaff id", savedId);
        }

        interruptSession();

        {
            ResearchStaff loaded = getDao().getById(savedId);
            assertNotNull("Could not reload researchStaff id " + savedId, loaded);
            assertEquals("Wrong firstname", "Jeff", loaded.getFirstName());
            assertEquals("Wrong lastname", "Someone", loaded.getLastName());
        }
    }
    
    public void testSaveRemoteResearchStaff() throws Exception {
        interruptSession();

        Integer savedId;
        {
            ResearchStaff researchStaff = new RemoteResearchStaff();
            researchStaff.setEmailAddress("abc@def.com");
            researchStaff.setExternalId("externalId");
            researchStaff.setAddress(new Address());
            
            getDao().save(researchStaff);

            savedId = researchStaff.getId();
            assertNotNull("The saved researchStaff id", savedId);
        }

        interruptSession();

        {
            ResearchStaff loaded = getDao().getById(savedId);
            assertNotNull("Could not reload researchStaff id " + savedId, loaded);
            assertEquals("Wrong emailAddress", "abc@def.com", loaded.getEmailAddress());
        }
    }
    
    
    public void testGetByExactLoginId(){
    	ResearchStaffQuery rsQuery = new ResearchStaffQuery();
    	rsQuery.filterByExactLoginId("abcd");
    	List<ResearchStaff> rsList = getDao().searchResearchStaff(rsQuery);
    	assertNotNull(rsList);
    	assertEquals(1, rsList.size());
    	
    	rsQuery = new ResearchStaffQuery();
    	rsQuery.filterByExactLoginId("ab");
    	rsList = getDao().searchResearchStaff(rsQuery);
    	assertNotNull(rsList);
    	assertEquals(0, rsList.size());
    	
    	
    }
}
