/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.dao;

import gov.nih.nci.cabig.caaers.DaoTestCase;
import gov.nih.nci.cabig.caaers.dao.query.ReportVersionDTOQuery;
import gov.nih.nci.cabig.caaers.dao.report.ReportVersionDao;
import gov.nih.nci.cabig.caaers.domain.ReportStatus;
import gov.nih.nci.cabig.caaers.domain.report.ReportVersion;
import gov.nih.nci.cabig.caaers.domain.report.ReportVersionDTO;
import gov.nih.nci.cabig.caaers.utils.DateUtils;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/*
*
* @author Ion C. Olaru
* 
* */
public class ReportVersionDaoTest extends DaoTestCase<ReportVersionDao> {
    private ReportVersionDao rsDao;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        rsDao = getDao();
    }

    public void testByDays() {
    	List l = rsDao.getAllSubmittedReportsInLastGivenNumberOfDays(30);
        assertEquals(0, l.size());
    }

    public void testSearchReportVersionDTO() {
        ReportVersionDTOQuery q = new ReportVersionDTOQuery();
        q.filterByReportStatus(ReportStatus.COMPLETED);
    	List<ReportVersionDTO> l = rsDao.searchDTO(q);
        interruptSession();

        assertEquals(1, l.size());
        assertEquals(-88, l.get(0).getRv().getId().intValue());
        assertEquals("Label for RCT-222", l.get(0).getReportName());
        assertEquals("Vasile", l.get(0).getSubjectFirstName());
        assertEquals("Boamba", l.get(0).getSubjectLastName());
        assertEquals("Id-02", l.get(0).getSubjectPrimaryIdentifier());
        assertEquals("Study Short Title", l.get(0).getStudyShortTitle());
        assertEquals(-1, l.get(0).getAeReportID().intValue());
        assertEquals(-223, l.get(0).getReportID().intValue());
        assertEquals(33, l.get(0).getPeriodCycle().intValue());
        assertEquals("2006-09-30 00:00:00.0", l.get(0).getPeriodStartDate().toString());
        assertEquals("NCI CODE 0190", l.get(0).getStudySiteCode());
        assertEquals("New Site", l.get(0).getStudySiteName());
    }
    
    public void testGetById(){
    	ReportVersion rv = rsDao.getById(-88);
    	assertNotNull(rv);
    	assertNotNull(rv.getDueOn());
    	System.out.println(rv.getDueOn());
    	System.out.println(DateUtils.getDateWithTimeZone(rv.getDueOn()));
    	
    	ReportVersion newRv = new ReportVersion();
    	newRv.setDueOn(new Date());
    	System.out.println(newRv.getDueOn());
    }
}
