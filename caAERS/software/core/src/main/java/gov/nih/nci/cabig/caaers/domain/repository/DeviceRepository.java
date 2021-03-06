/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.domain.repository;

import gov.nih.nci.cabig.caaers.dao.DeviceDao;
import gov.nih.nci.cabig.caaers.dao.query.DeviceQuery;
import gov.nih.nci.cabig.caaers.domain.Device;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

 
/**
 * The Class DeviceRepository.
 * @author Ion C. Olaru
 */
@Transactional(readOnly = true)
public class DeviceRepository {

	/** The log. */
	private static Log log = LogFactory.getLog(StudyRepository.class);
    
    /** The device dao. */
    private DeviceDao deviceDao;

    /**
     * Gets the device dao.
     *
     * @return the device dao
     */
    public DeviceDao getDeviceDao() {
        return deviceDao;
    }

    /**
     * Sets the device dao.
     *
     * @param deviceDao the new device dao
     */
    public void setDeviceDao(DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
    }

    /**
     * Gets the by id.
     *
     * @param id the id
     * @return the by id
     */
    public Device getById(int id) {
        return deviceDao.getById(id);
    }

    /**
     * Search.
     *
     * @param query the query
     * @return the list<? extends object>
     */
    public List<? extends Object> search(final DeviceQuery query) {
        return deviceDao.search(query);
    }

    /**
     * Gets the all devices.
     *
     * @return the all devices
     */
    public List<Device> getAllDevices() {
        return deviceDao.getAllDevices();
    }

    /**
     * Gets the by names.
     *
     * @param commonName the common name
     * @param brandName the brand name
     * @param type the type
     * @return the by names
     */
    public List<Device> getByNames(String commonName, String brandName, String type) {
        DeviceQuery dq = new DeviceQuery();
        dq.filterByCommonName(commonName);
        dq.filterByBrandName(brandName);
        dq.filterByType(type);
        return (List<Device>)deviceDao.search(dq);
    }

    /**
     * Rerturns Devices by match text
     * @param text the text to search by
     * @oaram activeOnly the result will return only active Devices if true
     * @return the by match text
     */
    public List<Device> getByMatchText(String text, boolean activeOnly) {
        DeviceQuery dq = new DeviceQuery();
        dq.filterByMatchText(text);
        if (activeOnly) dq.filterByRetiredStatus(false);
        return (List<Device>)deviceDao.search(dq);
    }

    /**
     * Get the devices by CommonName
     * @param commonName Search devices by this common name
     * @return list of matching devices
     */
    public List<Device> getByCommonName(String commonName) {
        DeviceQuery dq = new DeviceQuery();
        dq.filterByCommonName(commonName);
        return (List<Device>)deviceDao.search(dq);
    }

    public List<Device> getByCommonNameAndBrandName(String commonName, String brandName) {
        DeviceQuery dq = new DeviceQuery();
        dq.filterByCommonName(commonName);
        dq.filterByBrandName(brandName);
        return (List<Device>)deviceDao.search(dq);
    }

    public List<Device> getByCtepDbIdentifier(String id) {
        DeviceQuery dq = new DeviceQuery();
        dq.filterByCtepDbIdentifier(id);
        return (List<Device>)deviceDao.search(dq);
    }

}
