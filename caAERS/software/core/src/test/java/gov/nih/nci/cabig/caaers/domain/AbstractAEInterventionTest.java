/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.domain;

import junit.framework.TestCase;

/**
 * @author Biju Joseph
 * @date 7/14/11
 */
public class AbstractAEInterventionTest extends TestCase {
    
    public void testCopy() throws Exception {
        BehavioralIntervention bi = new BehavioralIntervention();
        bi.setDescription("x");
        bi.setId(8);
        OtherIntervention oi = new OtherIntervention();
        oi.setName("n");
        oi.setDescription("x");
        bi.setStudyIntervention(oi);

        BehavioralIntervention bi2 = new BehavioralIntervention();
        bi.copy(bi2);

        assertEquals(8, (int)bi.getId());
        assertEquals("x", bi.getDescription());

        assertEquals("n" , bi2.getStudyIntervention().getName());
    }
}
