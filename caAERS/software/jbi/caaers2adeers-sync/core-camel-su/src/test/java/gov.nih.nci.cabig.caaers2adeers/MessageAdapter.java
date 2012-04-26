package gov.nih.nci.cabig.caaers2adeers;

import gov.nih.nci.cabig.caaers2adeers.cronjob.MockMessageGenerator;
import org.apache.camel.impl.DefaultMessage;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Biju Joseph
 */
public class MessageAdapter extends DefaultMessage{

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
    public Object getBody() {
        return MockMessageGenerator.getStudySearchRequest();
    }
}