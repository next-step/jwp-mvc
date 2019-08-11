package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final int DEFAULT_REQUEST_METHOD_COUNT = 1;

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Set<Class<?>> controllers = controllerScanner.getControllers();

        for (Class<?> controller : controllers) {
            generateHandlerExecutions(controller, getRequestMappingAnnotatedMethods(controller));
        }
    }

    private Set<Method> getRequestMappingAnnotatedMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();

        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void generateHandlerExecutions(Class<?> clazz, Set<Method> methods) {
        for (Method method : methods) {
            try {
                generate(clazz, method);
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void generate(Class<?> clazz, Method method) throws InstantiationException, IllegalAccessException {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length < DEFAULT_REQUEST_METHOD_COUNT) {
            requestMethods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(getHandlerKey(requestMapping.value(), requestMethod), new HandlerExecution(clazz.newInstance(), method));
        }
    }

    private HandlerKey getHandlerKey(String value, RequestMethod requestMethod) {
        return new HandlerKey(value, requestMethod);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
