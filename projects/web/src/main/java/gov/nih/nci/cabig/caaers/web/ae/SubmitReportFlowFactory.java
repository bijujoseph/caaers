package gov.nih.nci.cabig.caaers.web.ae;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.FlowFactory;

/**
 * @author Krikor Krumlian
 * 
 */
public class SubmitReportFlowFactory implements FlowFactory<ExpeditedAdverseEventInputCommand> {
    protected String flowName;
    private Flow<ExpeditedAdverseEventInputCommand> submitReportFlow;

    public SubmitReportFlowFactory(String flowName) {
        this.flowName = flowName;
    }

    ////// LOGIC

    
    protected void addTabs(Flow<ExpeditedAdverseEventInputCommand> flow){
    	flow.addTab(new SubmitterTab());
    	flow.addTab(new SubmitReportTab());
    	
    }

    public Flow<ExpeditedAdverseEventInputCommand> createFlow(ExpeditedAdverseEventInputCommand command) {
    	return getSubmitReportFlow(); 
    }

    private Flow<ExpeditedAdverseEventInputCommand> createEmptyFlow() {
        return new Flow<ExpeditedAdverseEventInputCommand>(flowName);
    }
    
    private Flow<ExpeditedAdverseEventInputCommand> getSubmitReportFlow() {
        if (submitReportFlow == null) {
        	submitReportFlow = createEmptyFlow();
            addTabs(submitReportFlow);
        }
        return submitReportFlow;
    }
}
