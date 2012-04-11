package gov.nih.nci.cabig.caaers.api.impl;

import gov.nih.nci.cabig.caaers.api.DevicesService;
import gov.nih.nci.cabig.caaers.dao.DeviceDao;
import gov.nih.nci.cabig.caaers.dao.query.DeviceQuery;
import gov.nih.nci.cabig.caaers.domain.Device;
import gov.nih.nci.cabig.caaers.integration.schema.common.*;
import gov.nih.nci.cabig.caaers.webservice.DeviceType;
import gov.nih.nci.cabig.caaers.webservice.devices.DevicesType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ion C. Olaru
 *         Date: 4/3/12 -10:03 AM
 */
public class DevicesServiceImpl implements DevicesService, ApplicationContextAware {

    private static Log log = LogFactory.getLog(DevicesServiceImpl.class);

    private ApplicationContext applicationContext;
    private DeviceDao deviceDao;

    public CaaersServiceResponse createOrUpdateDevices(DevicesType devices) {
        CaaersServiceResponse csr = new CaaersServiceResponse();
        csr.setServiceResponse(new ServiceResponse());
        csr.getServiceResponse().setEntityErrorMessages(new EntityErrorMessages());
        csr.getServiceResponse().setStatus(Status.PROCESSED);
        List<EntityErrorMessageType> errors = execute(devices);
        csr.getServiceResponse().getEntityErrorMessages().setEntityErrorMessage(errors);
        return csr;
    }

    private EntityErrorMessageType populateError(String cn, String bi, String message) {
        EntityErrorMessageType e = new EntityErrorMessageType();
        e.setBusinessIdentifier(bi);
        e.setKlassName(cn);
        e.setMessage(new ArrayList<String>(1));
        e.getMessage().add(message);
        return e;
    }

    public List<EntityErrorMessageType> execute(DevicesType devices) {
        List<EntityErrorMessageType> errors = new ArrayList<EntityErrorMessageType>();
        if (devices == null || devices.getDevice() == null || devices.getDevice().size() == 0) return errors;

        for (DeviceType xmlDevice : devices.getDevice()) {
            Device d;
            d = loadPersistentDeviceByCommonName(xmlDevice.getCommonName());
            if (d == null) {
                d = new Device();
                d.setCommonName(xmlDevice.getCommonName());
            }
            d.setBrandName(xmlDevice.getBrandName());
            d.setType(xmlDevice.getType());
            d.setRetiredIndicator(xmlDevice.getStatus().equals(ActiveInactiveStatusType.IN));

            try {
                deviceDao.save(d);
                errors.add(populateError(Device.class.getName(), d.getCommonName(), ""));
            } catch (Exception e) {
                errors.add(populateError(Device.class.getName(), d.getCommonName(), e.getStackTrace().toString()));
            }
        }

        return errors;
    }

    private Device loadPersistentDeviceByCommonName(String commonName) {
        DeviceQuery q = new DeviceQuery();
        q.filterByCommonName(commonName);
        List rs = deviceDao.search(q);
        if (rs.size() > 0) return (Device)rs.get(0);
        return null;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public DeviceDao getDeviceDao() {
        return deviceDao;
    }

    public void setDeviceDao(DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
    }
}