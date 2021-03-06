/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers2adeers;

import gov.nih.nci.cabig.caaers2adeers.cronjob.EntityOperation;
import gov.nih.nci.cabig.caaers2adeers.cronjob.PayloadGenerator;
import org.apache.camel.impl.DefaultMessage;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Biju Joseph
 */
public class MessageAdapter extends DefaultMessage{

    String body;
    Map<String, Object> headers = new LinkedHashMap<String, Object>();

    @Override
    public Map<String, Object> getHeaders() {
        return headers;
    }

    @Override
    public Object getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public void setHeader(String name, Object value){
       headers.put(name, value);
    }

    @Override
    public void setBody(Object body) {
       this.body = String.valueOf(body);
    }

    @Override
    public Object getBody() {
        if(StringUtils.isNotEmpty(body)) return body;
        return new PayloadGenerator().getRequest(EntityOperation.PRIOR_THERAPY);
    }
}
