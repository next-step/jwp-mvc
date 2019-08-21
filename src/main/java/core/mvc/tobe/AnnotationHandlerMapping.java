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

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackages;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackages);
        Map<Class<?>, Object> controllers = controllerScanner.scan();
        Set<Method> requestMappingMethods = getRequestMappingMethods(controllers.keySet());
        requestMappingMethods.forEach(method -> addHandlerExecution(controllers, method));
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> controllerClasses) {
        return controllerClasses.stream()
                .flatMap(controllerClass -> Arrays.stream(controllerClass.getMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void addHandlerExecution(Map<Class<?>, Object> controllers, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = parseRequestMethods(requestMapping);
        for (RequestMethod requestMethod : requestMethods) {
            logger.info("Request Mapping : {}[url={}, requestMethod={}]", method.getDeclaringClass().getSimpleName(), requestMapping.value(), requestMethod);
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controllers.get(method.getDeclaringClass()), method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private RequestMethod[] parseRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] methods = requestMapping.method();
        if (methods.length == 0) {
            return RequestMethod.values();
        }
        return methods;
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
