package core.mvc.tobe;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;

import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import core.mvc.tobe.scanner.ControllerScanner;
import core.mvc.tobe.scanner.RequestMappingScanner;

public class AnnotationHandlerMapping implements HandlerMapping {
    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        RequestMappingScanner requestMappingScanner = new RequestMappingScanner();

        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Class<?> clazz : controllers.keySet()) {
            Object instance = controllers.get(clazz);
            String path = controllerScanner.getControllerUriPath(clazz);

            handlerExecutions.putAll(requestMappingScanner.getHandlerExecutions(path, clazz, instance));
        }
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
