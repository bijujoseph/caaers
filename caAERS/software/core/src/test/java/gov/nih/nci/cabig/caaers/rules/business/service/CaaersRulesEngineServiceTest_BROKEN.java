/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.rules.business.service;

import com.semanticbits.rules.api.RulesEngineService;
import com.semanticbits.rules.brxml.Rule;
import com.semanticbits.rules.brxml.RuleSet;
import gov.nih.nci.cabig.caaers.AbstractTestCase;
import gov.nih.nci.cabig.caaers.domain.Fixtures;
import gov.nih.nci.cabig.caaers.domain.Organization;
import gov.nih.nci.cabig.caaers.domain.Study;
import gov.nih.nci.cabig.caaers.domain.expeditedfields.ExpeditedReportSection;
import gov.nih.nci.cabig.caaers.rules.common.RuleType;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CaaersRulesEngineService Tester.
 *
 * @author Biju Joseph
 * @since <pre>03/08/2010</pre>
 * 
 */
public class CaaersRulesEngineServiceTest_BROKEN extends AbstractTestCase {
    

    CaaersRulesEngineService service;
    RulesEngineService ruleEngineService;

    public void setUp() throws Exception {
        super.setUp();
        service = new CaaersRulesEngineService();
        ruleEngineService = registerMockFor(RulesEngineService.class);
        service.setRuleEngineService(ruleEngineService);
    }


    //checks that non ui related conditions are filtered off. 
    public void testCleanRuleSet(){
        RuleSet rs = new RuleSet();

        ArrayList<Rule> rules = new ArrayList<Rule>();
        rs.setRule(rules);
        rules.add(Fixtures.createRule(Fixtures.createCondition("abcd", "studySDO", "hello", "organizationSDO", "jay", "factResolver", "adverseEventEvaluationResult", "thankyou")));

        service.cleanRuleSet(rs);

        assertEquals("abcd", rules.get(0).getCondition().getColumn().get(0).getIdentifier());
        assertEquals("hello", rules.get(0).getCondition().getColumn().get(1).getIdentifier());
        assertEquals("jay", rules.get(0).getCondition().getColumn().get(2).getIdentifier());
        assertEquals("thankyou", rules.get(0).getCondition().getColumn().get(3).getIdentifier());
    }


    //pouplates redable attribute in rules.
    // BJ - tests for 3 types of action. 
    public void testMakeRuleSetReadable(){
       RuleSet rs = new RuleSet();

       ArrayList<Rule> rules = new ArrayList<Rule>();
       rs.setRule(rules);
       Rule r1 = Fixtures.createRule(Fixtures.createCondition("abc", "def"));
       Rule r2  = Fixtures.createRule(Fixtures.createCondition("ghi"));
       Rule r3 = Fixtures.createRule(Fixtures.createCondition("field"));
       rules.add(r1);
       rules.add(r2);
       rules.add(r3);

       r1.setAction(Arrays.asList("Hello", "Below"));
       r2.setAction(Arrays.asList("NA"));
       r3.setAction(Arrays.asList(ExpeditedReportSection.ADDITIONAL_INFO_SECTION.name()));
        
       {
           RuleSet rs1 = new RuleSet();
           rs1.setDescription("SAE Reporting Rules");
           rs1.setRule(Arrays.asList(r1,r2, r3));

           service.makeRuleSetReadable(rs1);

           assertCorrectValuesInList(r1.getReadableRule().getLine(), "If", "\t &nbsp;&nbsp;&nbsp; prefix  is  'B' ", "And","\t &nbsp;&nbsp;&nbsp; prefix  is  'B' ");
           assertCorrectValuesInList(r1.getReadableAction(), "Hello", "Below");
           assertEquals("Rule-1", r1.getMetaData().getName());
           assertEquals("Rule-3", r3.getMetaData().getName());
       }
        

       {
           RuleSet rs1 = new RuleSet();
           rs1.setDescription("Mandatory Sections Rules");
           rs1.setRule(Arrays.asList(r3));

           service.makeRuleSetReadable(rs1);

           assertCorrectValuesInList(r2.getReadableRule().getLine(), "If", "\t &nbsp;&nbsp;&nbsp; prefix  is  'B' ");
           assertCorrectValuesInList(r2.getReadableAction(), "NA");

       }


       {
           RuleSet rs1 = new RuleSet();
           rs1.setDescription("Field Rules");
           rs1.setRule(Arrays.asList(r2));

           service.makeRuleSetReadable(rs1);

           assertCorrectValuesInList(r3.getReadableRule().getLine(), "If", "\t &nbsp;&nbsp;&nbsp; prefix  is  'B' ");
           assertCorrectValuesInList(r3.getReadableAction(), "Additional Info");
           assertEquals("Rule-1", r3.getMetaData().getName());

       }

    }



    public void testGeneratePath() throws Exception{
        String path = service.generatePath("junk", null, null, null, null);
        assertEquals("/CAAERS_BASE", path);

        Organization org = Fixtures.createOrganization("abcd",1);
        assertEquals("/CAAERS_BASE/SPONSOR/ORG_1/sae_reporting_rules",
                service.generatePath(CaaersRulesEngineService.SPONSOR_LEVEL, RuleType.REPORT_SCHEDULING_RULES.getName(), org, null, null));
        assertEquals("/CAAERS_BASE/SPONSOR/ORG_1",
                        service.generatePath(CaaersRulesEngineService.SPONSOR_LEVEL, null, org, null, null));
        assertEquals("/CAAERS_BASE/INSTITUTION/ORG_1/sae_reporting_rules",
                service.generatePath(CaaersRulesEngineService.INSTITUTIONAL_LEVEL, RuleType.REPORT_SCHEDULING_RULES.getName(), org, org, null));

        Study s = Fixtures.createStudy("test");
        s.setId(1);
        assertEquals("/CAAERS_BASE/INSTITUTION_DEFINED_STUDY/ORG_1/STU_1/sae_reporting_rules",
                service.generatePath(CaaersRulesEngineService.INSTITUTION_DEFINED_STUDY_LEVEL, RuleType.REPORT_SCHEDULING_RULES.getName(), org, org, s));
       assertEquals("/CAAERS_BASE/SPONSOR_DEFINED_STUDY/ORG_1/STU_1/sae_reporting_rules",
                service.generatePath(CaaersRulesEngineService.SPONSOR_DEFINED_STUDY_LEVEL, RuleType.REPORT_SCHEDULING_RULES.getName(), org, org, s));
    }

    public void testPopulateCategoryBasedColumns() throws Exception{
        Rule r1 = Fixtures.createRule(Fixtures.createCondition("abc", "def"));
        service.populateCategoryBasedColumns(r1, CaaersRulesEngineService.SPONSOR_LEVEL, "sp", "in", "ti");
        
    }




    public void testParseOrganizationId()
         throws Exception{

        String orgId = service.parseOrganizationId("gov.nih.nci.cabig.caaers.rules.sponsor.ORG_11.STU_33.abcdefg") ;
        assertEquals("11", orgId);
        assertTrue(StringUtils.isEmpty(service.parseOrganizationId("test")));
    }

    public void testParseStudyId()
         throws Exception{

        String stuId = service.parseStudyId("gov.nih.nci.cabig.caaers.rules.sponsor.ORG_11.STU_33.abcdefg") ;
        assertEquals("33", stuId);
        assertTrue(StringUtils.isEmpty(service.parseOrganizationId("test")));
    }

    public void assertCorrectValuesInList(List<String> list, String... values){
        int i =0;
        for(String v : values){
            assertEquals("[" + v +"]", "[" + list.get(i) + "]");
            i++;
        }
    }

}
