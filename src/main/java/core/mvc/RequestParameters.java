package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class RequestParameters {
    private static final ObjectMapper mapper = new ObjectMapper();

    private Map<String, Object> parameters = new HashMap<>();

    public RequestParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet()) {
            String key = parameterEntry.getKey();
            String[] value = parameterEntry.getValue();

            parameters.put(key, getValue(value));
        }
    }

    private Object getValue(String[] values) {
        if (values.length > 1) {
            return values;
        }
        if (values.length == 1) {
            return values[0];
        }
        return null;
    }

    public <T> T getBodyObject(Class<T> type) {
        return mapper.convertValue(this.parameters, type);
    }

    public Object getParameter(String name) {
        return parameters.get(name);
    }
}
