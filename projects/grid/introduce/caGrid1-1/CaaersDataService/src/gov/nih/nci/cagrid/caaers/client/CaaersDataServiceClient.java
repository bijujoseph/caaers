package gov.nih.nci.cagrid.caaers.client;

import gov.nih.nci.cagrid.caaers.common.CaaersDataServiceI;
import gov.nih.nci.cagrid.caaers.stubs.CaaersDataServicePortType;
import gov.nih.nci.cagrid.caaers.stubs.service.CaaersDataServiceAddressingLocator;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.introduce.security.client.ServiceSecurityClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.client.Stub;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.globus.gsi.GlobusCredential;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;

/**
 * This class is autogenerated, DO NOT EDIT GENERATED GRID SERVICE ACCESS METHODS.
 *
 * This client is generated automatically by Introduce to provide a clean unwrapped API to the
 * service.
 *
 * On construction the class instance will contact the remote service and retrieve it's security
 * metadata description which it will use to configure the Stub specifically for each method call.
 * 
 * @created by Introduce Toolkit version 1.1
 */
public class CaaersDataServiceClient extends ServiceSecurityClient implements CaaersDataServiceI {	
	protected CaaersDataServicePortType portType;
	private Object portTypeMutex;

	public CaaersDataServiceClient(String url) throws MalformedURIException, RemoteException {
		this(url,null);	
	}

	public CaaersDataServiceClient(String url, GlobusCredential proxy) throws MalformedURIException, RemoteException {
	   	super(url,proxy);
	   	initialize();
	}
	
	public CaaersDataServiceClient(EndpointReferenceType epr) throws MalformedURIException, RemoteException {
	   	this(epr,null);
	}
	
	public CaaersDataServiceClient(EndpointReferenceType epr, GlobusCredential proxy) throws MalformedURIException, RemoteException {
	   	super(epr,proxy);
		initialize();
	}
	
	private void initialize() throws RemoteException {
	    this.portTypeMutex = new Object();
		this.portType = createPortType();
	}

	private CaaersDataServicePortType createPortType() throws RemoteException {

		CaaersDataServiceAddressingLocator locator = new CaaersDataServiceAddressingLocator();
		// attempt to load our context sensitive wsdd file
		InputStream resourceAsStream = getClass().getResourceAsStream("client-config.wsdd");
		if (resourceAsStream != null) {
			// we found it, so tell axis to configure an engine to use it
			EngineConfiguration engineConfig = new FileProvider(resourceAsStream);
			// set the engine of the locator
			locator.setEngine(new AxisClient(engineConfig));
		}
		CaaersDataServicePortType port = null;
		try {
			port = locator.getCaaersDataServicePortTypePort(getEndpointReference());
		} catch (Exception e) {
			throw new RemoteException("Unable to locate portType:" + e.getMessage(), e);
		}

		return port;
	}
	
	public GetResourcePropertyResponse getResourceProperty(QName resourcePropertyQName) throws RemoteException {
		return portType.getResourceProperty(resourcePropertyQName);
	}

	public static void usage(){
		System.out.println(CaaersDataServiceClient.class.getName() + " -url <service url>");
	}
	
	public static void main(String [] args){
	    System.out.println("Running the Grid Service Client");
		try{
			
			GlobusCredential cred = new GlobusCredential("/Users/sakkala/caGrid-1-1/proxy.txt");
	        CaaersDataServiceClient client = new CaaersDataServiceClient("https://localhost:8443/wsrf/services/cagrid/CaaersDataService", cred);
			
			//CaaersDataServiceClient client = new CaaersDataServiceClient("http://localhost:8080/wsrf/services/cagrid/CaaersDataService");
			//java.lang.Object qryObj = ObjectDeserializer.deserialize(new InputSource(new FileInputStream("/Users/sakkala/tech-workspace/caaers/grid/introduce/CaaersDataService/test/resources/all_studies.xml")),CQLQuery.class);
			
			CQLQuery cqlQuery = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object targetObj = new gov.nih.nci.cagrid.cqlquery.Object();
			targetObj.setName("gov.nih.nci.cabig.caaers.domain.Study");
			
			cqlQuery.setTarget(targetObj);
            CQLQueryResults results = client.query(cqlQuery);

			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results, new FileInputStream(new File("src/gov/nih/nci/cagrid/caaers/client/client-config.wsdd")));
	          
			while (iter.hasNext()) {
	        	   gov.nih.nci.cabig.caaers.domain.Study obj = (gov.nih.nci.cabig.caaers.domain.Study) iter.next();
				   System.out.println(obj.getId() +"	"+ obj.getShortTitle());
	           }
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

  public gov.nih.nci.cagrid.cqlresultset.CQLQueryResults query(gov.nih.nci.cagrid.cqlquery.CQLQuery cqlQuery) throws RemoteException, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"query");
    gov.nih.nci.cagrid.data.QueryRequest params = new gov.nih.nci.cagrid.data.QueryRequest();
    gov.nih.nci.cagrid.data.QueryRequestCqlQuery cqlQueryContainer = new gov.nih.nci.cagrid.data.QueryRequestCqlQuery();
    cqlQueryContainer.setCQLQuery(cqlQuery);
    params.setCqlQuery(cqlQueryContainer);
    gov.nih.nci.cagrid.data.QueryResponse boxedResult = portType.query(params);
    return boxedResult.getCQLQueryResultCollection();
    }
  }

}
