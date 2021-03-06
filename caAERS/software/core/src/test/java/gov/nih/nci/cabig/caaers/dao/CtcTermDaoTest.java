/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.dao;

import static gov.nih.nci.cabig.caaers.CaaersUseCase.MAPPING_VOCAB;
import gov.nih.nci.cabig.caaers.CaaersUseCases;
import gov.nih.nci.cabig.caaers.DaoTestCase;
import gov.nih.nci.cabig.caaers.domain.CtcGrade;
import gov.nih.nci.cabig.caaers.domain.CtcTerm;
import gov.nih.nci.cabig.caaers.domain.Grade;
import org.dbunit.operation.DatabaseOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rhett Sutphin
 */

@CaaersUseCases( { MAPPING_VOCAB })
public class CtcTermDaoTest extends DaoTestCase<CtcTermDao> {
    public void testGetById() throws Exception {
        CtcTerm loaded = getDao().getById(3001);
        assertNotNull(loaded);
        assertEquals("Wrong category", 301, (int) loaded.getCategory().getId());
        assertEquals("Wrong CTC version", 3, (int) loaded.getCategory().getCtc().getId());
        assertTrue("Other is required", loaded.isOtherRequired());
    }

    public void testGetBySubnameMatchesTermSubstring() throws Exception {
        List<CtcTerm> matches = getDao().getBySubname(new String[] { "dry" }, 2, null);
        assertEquals("Wrong number of matches", 1, matches.size());
        assertEquals("Wrong match", 2002, (int) matches.get(0).getId());
    }

    public void testGetBySubnameMatchesCtepTermSubstring() throws Exception {
        List<CtcTerm> matches = getDao().getBySubname(new String[] { "thermal" }, 2, null);
        assertEquals("Wrong number of matches", 1, matches.size());
        assertEquals("Wrong match", 2001, (int) matches.get(0).getId());
    }

    public void testGetBySubnameMatchesSelectSubstring() throws Exception {
        List<CtcTerm> matches = getDao().getBySubname(new String[] { "chemorad" }, 3, null);
        assertEquals("Wrong number of matches", 1, matches.size());
        assertEquals("Wrong match", 3004, (int) matches.get(0).getId());
    }

    public void testGetBySubnameMatchesFiltersByCategory() throws Exception {
        List<CtcTerm> unfilteredMatches = getDao().getBySubname(new String[] { "skin" }, 3, null);
        assertEquals("Test setup invalid: wrong number of matches w/o category filter", 2,
                        unfilteredMatches.size());

        List<CtcTerm> matches = getDao().getBySubname(new String[] { "skin" }, 3, 302);
        assertEquals("Wrong number of matches", 1, matches.size());
        assertEquals("Wrong match", 3010, (int) matches.get(0).getId());
    }

    public void testGetContextualGrades() throws Exception {
        CtcTerm term = getDao().getById(3010);
        assertEquals("Wrong number of contextual grades", 2, term.getContextualGrades().size());
        assertEquals("Wrong 0th grade", 30103, (int) term.getContextualGrades().get(0).getId());
        CtcGrade actual = term.getContextualGrades().get(1);
        assertEquals("Wrong 1st grade", 30104, (int) actual.getId());
        assertEquals("Wrong grade", Grade.LIFE_THREATENING, actual.getGrade());
        assertEquals("Wrong text", "On fire", actual.getText());
    }
    
    public void testGetCtcTerm(){
    	CtcTerm ctcTerm = getDao().getCtcTerm(new String[] { "Rash: dermatitis associated with radiation" });
    	assertNotNull(ctcTerm);
    }
    
    public void testGetCtcTerm_1a(){
    	CtcTerm ctcTerm = getDao().getCtcTerm(new String[] { "1a_Rash: dermatitis associated with radiation" });
    	assertNull(ctcTerm);
    }
    
    public void testGetAll(){
    	List<CtcTerm> ctcTerms = getDao().getAll();
    	assertNotNull(ctcTerms);
    	assertTrue(ctcTerms.size() > 0);
    }
    
    public void testGetByCtepCodeandVersion() throws Exception {
        List<CtcTerm> ctcTerms = getDao().getByCtepCodeandVersion("10016241","3.0");
        assertEquals(1, ctcTerms.size());
        assertEquals("Atrophy, subcutaneous fat", ctcTerms.get(0).getTerm());
    }
    
    public void testGetCtcTerm_1(){
    	CtcTerm ctcTerm = getDao().getCtcTerm(new String[] { "Rash: dermatitis associated with radiation" },null,null);
    	assertNotNull(ctcTerm);
    	assertEquals("Rash: dermatitis associated with radiation", ctcTerm.getTerm());
    }
    public void testGetCtcTerm_2(){
    	CtcTerm ctcTerm = getDao().getCtcTerm(new String[] { "Rash: dermatitis associated with radiation" },new Integer(3),null);
    	assertNotNull(ctcTerm);
    	assertEquals("Rash: dermatitis associated with radiation", ctcTerm.getTerm());
    }
    public void testGetCtcTerm_3(){
    	CtcTerm ctcTerm = getDao().getCtcTerm(new String[] { "Rash: dermatitis associated with radiation" },new Integer(3),new Integer(301));
    	assertNotNull(ctcTerm);
    	assertEquals("Rash: dermatitis associated with radiation", ctcTerm.getTerm());
    }
    
    public void testGetCtcTerm_4() { 
    	CtcTerm ctcTerm = getDao().getCtcTerm(new String[] { "1_Rash: dermatitis associated with radiation" },new Integer(3),new Integer(301));
    	assertNull(ctcTerm);
    }

/*
    * Get the list of Ctc Term providing term name, category name and ctcae version
    * */

    public void testGetCtcTermByNameCategoryCtcVersion() {
        List<CtcTerm> terms = getDao().getCtcTerm("DERMATOLOGY/SKIN", "2.0", "Burn");
        assertEquals(1, terms.size());
        assertEquals("Burn", terms.get(0).getTerm());

    }

    
    public void testGetByTermIds(){
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(3001);
        ids.add(3002);
        ids.add(-99809);
        List<CtcTerm> terms = getDao().findByIds(ids);
        assertEquals(2, terms.size());
        assertTrue(terms.get(0).getId().equals(3001) || terms.get(0).getId().equals(3002)  );
    }
}
