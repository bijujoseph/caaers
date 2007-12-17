package gov.nih.nci.cabig.caaers.rules.deploy;

import java.rmi.RemoteException;

import javax.jws.WebService;

import gov.nih.nci.cabig.caaers.rules.deploy.sxml.RepositoryConfiguration;
import gov.nih.nci.cabig.caaers.rules.deploy.sxml.RuleSetInfo;

/**
 * The Service Interface for deploying the Business Rules.
 * 
 * @author Sujith Vellat Thayyilthodi
 * */
@WebService(
        serviceName = "RuleDeploymentService" , endpointInterface = "gov.nih.nci.cabig.caaers.rules.deploy.RuleDeploymentService"
)
public interface RuleDeploymentService {

	/**
	 * Yes! You need to login...but later..i let u go thru now since i am under busy schedule...!!!
	 * 
	 * @param userName
	 * @param password
	 * @throws RemoteException 
	 * */
	public abstract void login(String userName, String password) throws RemoteException;
	
	/**
	 * Specify and Configure the repository which Rule Deployment Service should make use of.
	 *  
	 * @param repositoryConfiguration Configuration information for the Jackrabbit. 
	 * @throws RemoteException 
	 * */
	public abstract void configureRepository(RepositoryConfiguration repositoryConfiguration) throws RemoteException;
	
	/**
	 * Register the Rule Set. I mean deploy a drools package if you are a JBoss Rules fan...
	 * 
	 * @param bindUri
	 * @param ruleSetName
	 * @throws RemoteException 
	 * */
	public abstract void registerRuleSet(String bindUri, String ruleSetName) throws RemoteException;
	
	/**
	 * Register the RuleSet, deploy the drools package
	 * @param bindUri
	 * @param ruleXml
	 * @throws RemoteException
	 */
	void registerRuleXml(String bindUri, String ruleXml) throws RemoteException;
	
	/**
	 * De-Register the Rule Set. I mean UN-deploy a drools package if you are a JBoss Rules fan...
	 * 
	 * @param bindUri
	 * */
	public abstract void deregisterRuleSet(String bindUri) throws RemoteException;;
	
	/**
	 * List the information of all RuleSets currently deployed in the Server.
	 * */
	public RuleSetInfo[] listRegistrations() throws RemoteException;;

}