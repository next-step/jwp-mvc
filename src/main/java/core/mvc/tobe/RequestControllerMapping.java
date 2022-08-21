package core.mvc.tobe;

import core.mvc.HandlerMapping;
import core.mvc.asis.RequestMapping;

import javax.servlet.http.HttpServletRequest;

public class RequestControllerMapping implements HandlerMapping {
    private final RequestMapping requestMapping;

    public RequestControllerMapping(RequestMapping requestMapping) {
        this.requestMapping = requestMapping;
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return requestMapping.findController(request.getRequestURI());
    }
}
