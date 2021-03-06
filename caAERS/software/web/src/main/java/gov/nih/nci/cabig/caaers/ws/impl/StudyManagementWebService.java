/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.ws.impl;

import gov.nih.nci.cabig.caaers.integration.schema.common.CaaersServiceResponse;
import gov.nih.nci.cabig.caaers.integration.schema.study.Studies;
import gov.nih.nci.cabig.caaers.ws.StudyProcessor;
import gov.nih.nci.cabig.caaers.api.impl.StudyProcessorImpl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Will expose the webservice methods and delegates it to StudyProcessorImpl
 *
 * @author Biju Joseph
 * @author Ion C. Olaru
 */
@WebService(endpointInterface = "gov.nih.nci.cabig.caaers.ws.StudyProcessor", serviceName = "StudyService", targetNamespace = "http://schema.integration.caaers.cabig.nci.nih.gov/study")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class StudyManagementWebService implements StudyProcessor {

    private StudyProcessorImpl impl;

    /**
     * This operation will accept a Study which is a jaxb study and creates it.
     * This operation should allow for response.
     * Need to modify schema for Response
     *
     * @param xmlStudies
     */
    @WebMethod
    public CaaersServiceResponse createStudy(@WebParam(name = "Studies", targetNamespace = "http://schema.integration.caaers.cabig.nci.nih.gov/study") Studies xmlStudies) {
        return impl.createStudy(xmlStudies);
    }

    /**
     * This operation will accept a Study which is a jaxb Study and updates it.
     * This operation should allow for response.
     * Need to modify schema for Response
     *
     * @param xmlStudies
     */
    @WebMethod
    public CaaersServiceResponse updateStudy(@WebParam(name = "Studies", targetNamespace = "http://schema.integration.caaers.cabig.nci.nih.gov/study") Studies xmlStudies) {
        return impl.updateStudy(xmlStudies);
    }

    public StudyProcessorImpl getImpl() {
        return impl;
    }

    public void setImpl(StudyProcessorImpl impl) {
        this.impl = impl;
    }
}
