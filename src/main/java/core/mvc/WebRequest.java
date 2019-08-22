package core.mvc;

import core.annotation.web.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class WebRequest {

    private final RequestMapping requestMapping;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final Map<String, String> uriVariables;

    private WebRequest(RequestMapping requestMapping, HttpServletRequest request, HttpServletResponse response, Map<String, String> uriVariables) {
        this.requestMapping = requestMapping;
        this.request = request;
        this.response = response;
        this.uriVariables = uriVariables;
    }

    public static WebRequest of(RequestMapping requestMapping, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> uriVariables = UriPathPatterns.getInstance()
                .getPathVariables(requestMapping.value(), request.getRequestURI());

        return new WebRequest(requestMapping, request, response, uriVariables);
    }

    public RequestMapping getRequestMapping() {
        return requestMapping;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public Map<String, String> getUriVariables() {
        return uriVariables;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
