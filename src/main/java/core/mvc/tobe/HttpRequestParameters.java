package core.mvc.tobe;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class HttpRequestParameters {

    private final MultiValueMap<String, String> parameterValues = new LinkedMultiValueMap<>();

    public HttpRequestParameters(final HttpServletRequest request) {
        final Map<String, String[]> parameterMap = request.getParameterMap();

        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                parameterValues.add(key, value);
            }
        });
    }

    public boolean containsKey(final String key) {
        return parameterValues.containsKey(key);
    }

    public String getFirst(final String key) {
        return parameterValues.getFirst(key);
    }
}
