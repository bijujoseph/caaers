/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.domain;

import junit.framework.TestCase;
/**
 *
 * @author Ion C. Olaru
 *
 */
public class DeviceTest extends TestCase {
	Device device;

    protected void setUp() throws Exception {
		super.setUp();
		device = new Device();
        device.setBrandName("Init brand name");
        device.setCommonName("Init common name");
        device.setType("Init type");
	}

    public void testEqualsAll() {
        Device other = new Device();
        other.setBrandName("Init brand name");
        other.setCommonName("Init common name");
        other.setType("Init type");

        assertTrue(device.equals(other));
        assertTrue(device.hashCode() == other.hashCode());
    }

    public void testBrandNameDiff() {
        Device other = new Device();
        other.setBrandName("Other brand name");
        other.setCommonName("Init common name");
        other.setType("Init type");

        assertFalse(device.equals(other));
        assertFalse(device.hashCode() == other.hashCode());
    }

    public void testCommonNameDiff() {
        Device other = new Device();
        other.setBrandName("Init brand name");
        other.setCommonName("Other common name");
        other.setType("Init type");

        assertFalse(device.equals(other));
        assertFalse(device.hashCode() == other.hashCode());
    }

    public void testCommonNameNotEmpty() {
        Device other = new Device();
        other.setBrandName("Init brand name");
        assertFalse(device.equals(other));
        assertFalse(device.hashCode() == other.hashCode());
    }

    /**
     * A null value of a String field is considered to be equal to an empty String value of the same field
     * */
    public void testCommonNameNotEmptyOtherEmptyOrNull() {
        device.setBrandName("Init brand name   ");
        device.setCommonName("");
        device.setType(null);
        Device other = new Device();
        other.setBrandName("Init brand name");
        assertTrue(device.equals(other));
        assertTrue(device.hashCode() == other.hashCode());
    }

    public void testAllFieldNullOrEmpty() {
        device.setBrandName("");
        device.setCommonName("");
        device.setType(null);
        Device other = new Device();
        assertTrue(device.equals(other));
        assertTrue(device.hashCode() == other.hashCode());
    }

    public void testDisplayName() {
        device.setBrandName("A");
        device.setCommonName("");
        device.setType("C");
        assertEquals("A, C", device.getDisplayName());
    }
    
    public void testEquals(){
        Device d1 = new Device();
        Device d2 = new Device();
        Device d3 = new Device();
        d1.setCommonName("x"); d2.setCommonName("x");   d3.setCommonName("y");
        assertTrue(d1.equals(d2));
        assertFalse(d1.equals(d3));
        d1.setCtepDbIdentifier("121");
        assertFalse(d1.equals(d2));
        d2.setCtepDbIdentifier("121");
        assertTrue(d1.equals(d2));
    }
}
