package core.mvc.tobe;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;

import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import core.mvc.tobe.scanner.ControllerScanner;

public class AnnotationHandlerMapping implements HandlerMapping {
    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        initialize();
    }

    private void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        handlerExecutions.putAll(controllerScanner.getHandlerExecutions());
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
