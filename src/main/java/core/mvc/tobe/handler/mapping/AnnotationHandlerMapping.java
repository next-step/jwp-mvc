package core.mvc.tobe.handler.mapping;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.tobe.ControllerScanner;
import org.reflections.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Class<RequestMapping> ANNOTATION_CLASS_FOR_METHOD = RequestMapping.class;
    private static final List<RequestMethod> DEFAULT_REQUEST_METHODS = List.of(RequestMethod.values());

    private ControllerScanner controllerScanner;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(ControllerScanner controllerScanner) {
        this.controllerScanner = controllerScanner;
    }

    public void initialize() {
        controllerScanner.initialize();
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Class<?> aClass : controllers.keySet()) {
            addHandlerExecution(controllers.get(aClass), getHandlerMethods(aClass));
        }
    }

    private Set<Method> getHandlerMethods(Class<?> controllerClass) {
        return ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(ANNOTATION_CLASS_FOR_METHOD));
    }

    private void addHandlerExecution(Object invoker, Set<Method> methods) {
        for (Method method : methods) {
            addHandlerExecutionEachMethod(invoker, method);
        }
    }

    private void addHandlerExecutionEachMethod(Object invoker, Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(ANNOTATION_CLASS_FOR_METHOD);
        String url = requestMapping.value();
        List<RequestMethod> requestMethods = getRequestMethods(requestMapping);
        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(new HandlerKey(url, requestMethod), new HandlerExecution(invoker, method));
        }
    }

    private List<RequestMethod> getRequestMethods(RequestMapping requestMapping) {
        List<RequestMethod> methods = List.of(requestMapping.method());
        if (methods.isEmpty()) {
            return DEFAULT_REQUEST_METHODS;
        }

        return methods;
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, requestMethod));
    }
}
