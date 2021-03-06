/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.service.migrator.report;

import gov.nih.nci.cabig.caaers.dao.AgentDao;
import gov.nih.nci.cabig.caaers.dao.PriorTherapyDao;
import gov.nih.nci.cabig.caaers.domain.Agent;
import gov.nih.nci.cabig.caaers.domain.ExpeditedAdverseEventReport;
import gov.nih.nci.cabig.caaers.domain.PriorTherapy;
import gov.nih.nci.cabig.caaers.domain.PriorTherapyAgent;
import gov.nih.nci.cabig.caaers.domain.SAEReportPriorTherapy;
import gov.nih.nci.cabig.caaers.service.DomainObjectImportOutcome;
import gov.nih.nci.cabig.caaers.service.migrator.Migrator;

import java.util.List;

/**
 * User:medaV
 * Date: 1/24/13
 */
public class SAEReportPriorTherapyMigrator implements Migrator<ExpeditedAdverseEventReport> {
    public PriorTherapyDao getPriorTherapyDao() {
        return priorTherapyDao;
    }

    public void setPriorTherapyDao(PriorTherapyDao priorTherapyDao) {
        this.priorTherapyDao = priorTherapyDao;
    }

    private PriorTherapyDao priorTherapyDao;
    
    private AgentDao agentDao;

	public void setAgentDao(AgentDao agentDao) {
		this.agentDao = agentDao;
	}

	public void migrate(ExpeditedAdverseEventReport aeReportSrc, ExpeditedAdverseEventReport aeReportDest, DomainObjectImportOutcome<ExpeditedAdverseEventReport> outcome) {
    
		List<SAEReportPriorTherapy> srcSAEReportPriorTherapys = aeReportSrc.getSaeReportPriorTherapies();

    	if ( srcSAEReportPriorTherapys == null || srcSAEReportPriorTherapys.isEmpty() ) {
    		outcome.addWarning("WR-SPT-1", "Input doesn't contain any SAEReportPriorTherapy Values.");
    		return;
    	}
		
    	// Copy the SAEReportPriorTherapys Information from Source to Destination.
    	for ( SAEReportPriorTherapy spt : srcSAEReportPriorTherapys) {
            PriorTherapy pt = findPriorTherapy(spt.getPriorTherapy(), outcome);
            if (outcome.hasErrors()) {
                return;
            }
    		validateSAEREportPriorTherapyDates(spt, outcome);
    		SAEReportPriorTherapy destSAEReportPriorTherapy = new SAEReportPriorTherapy();
            destSAEReportPriorTherapy.setPriorTherapy(pt);
            destSAEReportPriorTherapy.setReport(aeReportDest);
    		copyProperties(spt, destSAEReportPriorTherapy);
    		
    		for(PriorTherapyAgent pta : spt.getPriorTherapyAgents()){
    			PriorTherapyAgent priorTherapyAgent = migratePriorTherapyAgent(pta,destSAEReportPriorTherapy);
    			destSAEReportPriorTherapy.addPriorTherapyAgent(priorTherapyAgent);
    		}
    		
    		aeReportDest.addSaeReportPriorTherapies(destSAEReportPriorTherapy);
    	}
	}   
	
	
	private PriorTherapyAgent migratePriorTherapyAgent(PriorTherapyAgent src, SAEReportPriorTherapy dest){
		PriorTherapyAgent priorTherapyAgent = new PriorTherapyAgent();
		Agent agent = agentDao.getByNscNumber(src.getAgent().getNscNumber());
		priorTherapyAgent.setAgent(agent);
		priorTherapyAgent.setSaeReportPriorTherapy(dest);
		return priorTherapyAgent;
	}
	
	
	/**
	 * Copy the Details from the UserInput.
	 * @param src
	 * @param dest
	 */
	private void copyProperties(SAEReportPriorTherapy src, SAEReportPriorTherapy dest) {
		dest.setEndDate(src.getEndDate());
		dest.setStartDate(src.getStartDate());
		dest.setOther(src.getOther());
	}

    /**
     *  find the Prior Therapy from List.
     */
    private PriorTherapy findPriorTherapy(PriorTherapy pt, DomainObjectImportOutcome<ExpeditedAdverseEventReport> outcome) {
                
        List<PriorTherapy> resultLst = priorTherapyDao.searchByExampleIgnoreCase(pt, false);
        if(resultLst == null || resultLst.isEmpty()) {
        	outcome.addError("ER-SPT-1", "Matching prior therapy is not found for " + pt.getText() );
        	return null;
        }
        if(resultLst.size() > 1 ) {
        	outcome.addError("ER-SPT-2", "Multiple matching prior therapies found for " + pt.getText() );
        	return null;
        }
        return resultLst.get(0);
    }

	/**
	 * Validate SAEREportPriorTherapy Dates.
	 * @param saeReportPriorTherapy
	 * @param outcome
	 */
	private void validateSAEREportPriorTherapyDates(SAEReportPriorTherapy saeReportPriorTherapy,  DomainObjectImportOutcome<ExpeditedAdverseEventReport> outcome){
		if(saeReportPriorTherapy.getStartDate() != null && saeReportPriorTherapy.getEndDate() != null){
			if(saeReportPriorTherapy.getStartDate().toDate().after(saeReportPriorTherapy.getEndDate().toDate())){
				 outcome.addError("PAT_PTY1_ERR", "Report PriorTherapy 'end date' cannot be before 'start date'' ");
			}
		}
	}
}
