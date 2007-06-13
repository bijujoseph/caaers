package gov.nih.nci.cabig.caaers.web.rule.notification;

import gov.nih.nci.cabig.caaers.dao.report.ReportDefinitionDao;
import gov.nih.nci.cabig.caaers.domain.report.ReportDefinition;
import gov.nih.nci.cabig.caaers.web.rule.RuleInputCommand;
import gov.nih.nci.cabig.ctms.web.tabs.AbstractTabbedFlowFormController;
import gov.nih.nci.cabig.ctms.web.tabs.Flow;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Sujith Vellat Thayyilthodi
 * */
public class NotificationController extends AbstractTabbedFlowFormController<RuleInputCommand> {

	ReportDefinitionDao rdDao;
	List<String> allRoles;
	
	public NotificationController() {
		initFlow();
		//setCommandClass(NotificationCommand.class);
	}

    protected void initFlow() {
        setFlow(new Flow<RuleInputCommand>(getFlowName()));
        FirstTab firstTab = new FirstTab();
        SecondTab secondTab = new SecondTab();
        secondTab.setAllRoles(allRoles);
        ThirdTab thirdTab = new ThirdTab();
        
        getFlow().addTab(firstTab);
        getFlow().addTab(secondTab);
        getFlow().addTab(thirdTab);
    }
	
	protected String getFlowName() {
		return "Create Report Calendar";
	}
	
	@Override
	public Object formBackingObject(HttpServletRequest request) {
		//return new NotificationCommand(allRoles, map, notificationDao);
		NotificationCommand cmd = new NotificationCommand();
		cmd.setReportDefinition(new ReportDefinition());
		cmd.setReportDefinitionDao(rdDao);
		cmd.setAllRoles(allRoles);
		return cmd;
	}

	@Override
	protected ModelAndView processFinish(HttpServletRequest arg0, HttpServletResponse arg1, Object command, BindException arg3) throws Exception {
		NotificationCommand notificationCommand = (NotificationCommand)command;
		
		notificationCommand.save();
        Map<String, Object> model = new ModelMap();
        //model.put("study", command.getStudy().getId());
        return new ModelAndView("redirectToNotificationList", model);
	}

	/**
	 * @return the reportCalendarTemplateDao
	 */
	public ReportDefinitionDao getRdDao() {
		return rdDao;
	}

	/**
	 * @param rdDao the {@link ReportDefinitionDao} to set
	 */
	public void setRdDao(ReportDefinitionDao rdDao) {
		this.rdDao = rdDao;
	}
	
	public void setAllRoles(List<String> roleList){
		this.allRoles = roleList;
	}

	public List<String> getAllRoles(){
		return allRoles;
	}
//
//	public Map getMap() {
//		return map;
//	}
//
//	public void setMap(Map map) {
//		this.map = map;
//	}
//
	

}