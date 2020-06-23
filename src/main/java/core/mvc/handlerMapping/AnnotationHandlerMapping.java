package core.mvc.handlerMapping;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.handler.HandlerExecution;
import core.mvc.handler.HandlerExecutions;
import core.mvc.handler.HandlerKey;
import core.mvc.scanner.ControllerScanner;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            final List<Method> handlers = convertHandlers(clazz);

            for (Method handler : handlers) {
                HandlerExecution handlerExecution = new HandlerExecution(instance, handler);
                handlerExecutions.add(handlerExecution);
            }
        }
    }

    private List<Method> convertHandlers(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());

        final HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
