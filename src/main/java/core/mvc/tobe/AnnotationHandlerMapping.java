package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @SuppressWarnings("unchecked")
    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner();
        try {
            controllerScanner.initiateControllers(basePackage);
            Map<Class<?>, Object> controllers = controllerScanner.getControllers();
            for (Class<?> clazz : controllers.keySet()) {
                List<Method> methods = findRequestMappingMethods(clazz);
                putHandlerExecutions(controllers.get(clazz), methods);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
    }


    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.keySet().stream()
                .filter(h -> h.isMatchKey(requestUri, requestMethod))
                .findFirst()
                .map(key -> handlerExecutions.get(key))
                .orElseThrow(() -> new IllegalArgumentException("Handler를 찾을 수 없습니다."));
    }

    private List<Method> findRequestMappingMethods(Class<?> instantiateClazz) {
        return Arrays.stream(instantiateClazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void putHandlerExecutions(Object instance, List<Method> methods) {
        for (Method declaredMethod : methods) {
            RequestMapping annotation = declaredMethod.getAnnotation(RequestMapping.class);
            putHandlerExecution(instance, declaredMethod, annotation);
        }
    }

    private void putHandlerExecution(Object instance, Method declaredMethod, RequestMapping annotation) {
        RequestMethod[] requestMethods = getRequestMethods(annotation);
        handlerExecutions.put(
                new HandlerKey(annotation.value(), requestMethods),
                new HandlerExecution(instance, declaredMethod));
    }

    private RequestMethod[] getRequestMethods(RequestMapping annotation) {
        if (annotation.method().length == 0) {
            return RequestMethod.values();
        }
        return annotation.method();
    }

}
