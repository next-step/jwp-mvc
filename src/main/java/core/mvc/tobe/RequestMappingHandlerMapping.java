package core.mvc.tobe;

import core.mvc.asis.Controller;
import core.mvc.asis.RequestMapping;

import javax.servlet.http.HttpServletRequest;

public class RequestMappingHandlerMapping implements HandlerMapping {

    private final RequestMapping requestMapping;

    public RequestMappingHandlerMapping(RequestMapping requestMapping) {
        this.requestMapping = requestMapping;
    }

    @Override
    public Controller getHandler(HttpServletRequest request) {
        return requestMapping.findController(request.getRequestURI());
    }

}
