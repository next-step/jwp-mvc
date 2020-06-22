package core.mvc;

import core.annotation.web.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {
    private Object[] basePackage;
    private HandlerExecutions handlerExecutions = new HandlerExecutions();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final ControllerScanner scanner = new ControllerScanner(basePackage);
        final Set<Class<?>> scannedClasses = scanner.getControllers();

        for (Class<?> clazz : scannedClasses) {
            final Object instance = scanner.getInstance(clazz);
            handlerExecutions.add(clazz, instance);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());

        final HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
