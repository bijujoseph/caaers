/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.dao;

import static gov.nih.nci.cabig.caaers.CaaersUseCase.MAPPING_VOCAB;

import java.util.List;

import gov.nih.nci.cabig.caaers.CaaersUseCases;
import gov.nih.nci.cabig.caaers.DaoTestCase;
import gov.nih.nci.cabig.caaers.domain.CtcCategory;
import gov.nih.nci.cabig.caaers.domain.DiseaseCategory;

/**
 * @author Sameer Sawant
 */
@CaaersUseCases( { MAPPING_VOCAB })
public class CtcCategoryDaoTest extends DaoTestCase<CtcCategoryDao> {
	
	public void testDomainClass() throws Exception {
        Class<CtcCategory> dcc = getDao().domainClass();
        assertNotNull(dcc);
    }
	
	public void testGetAll() throws Exception{
		List<CtcCategory> ctcCategoryList = getDao().getAll();
		assertEquals("Incorrect number of CtcCategories fetched by getAll method in the dao", 4, ctcCategoryList.size());
	}
	
	public void testGetByCtcVersion() throws Exception{
		List<CtcCategory> ctcCategoryList = getDao().getByCtcVersion(1);
		assertEquals("Incorrect number of CtcCategories fetched by getByCtcVersion method in the dao", 2, ctcCategoryList.size());
	}
}
