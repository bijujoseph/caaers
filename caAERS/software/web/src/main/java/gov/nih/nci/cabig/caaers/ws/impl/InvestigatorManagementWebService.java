package gov.nih.nci.cabig.caaers.ws.impl;

import gov.nih.nci.cabig.caaers.api.InvestigatorMigratorService;
import gov.nih.nci.cabig.caaers.api.impl.DefaultInvestigatorMigratorService;
import gov.nih.nci.cabig.caaers.integration.schema.common.CaaersServiceResponse;
import gov.nih.nci.cabig.caaers.integration.schema.investigator.Staff;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Exposes the web service to manage Investigators. Will delegate the request to DefaultInvestigatorMigratorService.
 * @author  Biju Joseph
 */


@WebService(endpointInterface="gov.nih.nci.cabig.caaers.api.InvestigatorMigratorService",
        serviceName="InvestigatorMigratorService",
        targetNamespace="http://schema.integration.caaers.cabig.nci.nih.gov/investigator")
@SOAPBinding(parameterStyle=SOAPBinding.ParameterStyle.BARE)
public class InvestigatorManagementWebService implements InvestigatorMigratorService {
    private DefaultInvestigatorMigratorService impl;

    @WebMethod
    public CaaersServiceResponse saveInvestigator(@WebParam(name="Staff",
            targetNamespace="http://schema.integration.caaers.cabig.nci.nih.gov/investigator") Staff staff) {
        return  impl.saveInvestigator(staff);
    }

    public DefaultInvestigatorMigratorService getImpl() {
        return impl;
    }

    public void setImpl(DefaultInvestigatorMigratorService impl) {
        this.impl = impl;
    }
}
