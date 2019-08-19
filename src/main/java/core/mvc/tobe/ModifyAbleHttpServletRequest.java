package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ModifyAbleHttpServletRequest extends HttpServletRequestWrapper {
    private Map<String, String[]> params;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     * @throws IllegalArgumentException if the request is null
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public ModifyAbleHttpServletRequest(HttpServletRequest request) {
        super(request);
        this.params = new HashMap(request.getParameterMap());
    }

    public void setParameter(String name, String value) {
        String[] param = {value};
        setParameter(name, param);
    }

    public void setParameter(String key, String[] values) {
        this.params.put(key, values);
    }

    @Override
    public String getParameter(String name) {
        String[] value = this.params.get(name);
        if (value == null) {
            return null;
        }
        return value[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(params);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(params.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {

        String[] result = null;
        String[] temp = params.get(name);

        if (temp != null) {
            result = new String[temp.length];
            System.arraycopy(temp, 0, result, 0, temp.length);
        }

        return result;
    }
}
