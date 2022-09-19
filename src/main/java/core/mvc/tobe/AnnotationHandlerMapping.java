package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        setHandlerExecutions(controllers);
    }

    private void setHandlerExecutions(Map<Class<?>, Object> controllers) {
        Set<Class<?>> classes = controllers.keySet();
        List<Method> methodList = classes
                .stream()
                .flatMap(aClass -> Arrays.stream(aClass.getMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());

        for (Method method : methodList) {
            putHandlerExecutions(method);
        }
    }

    private void putHandlerExecutions(Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String requestUrl = requestMapping.value();
        RequestMethod[] requestMethods = getRequestMethods(requestMapping);
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestUrl, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(method);
            handlerExecutions.put(handlerKey, handlerExecution);
            logger.info("RequestMapping URL : {}, method : {}", handlerKey, method);
        }
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        if (requestMapping.method().length > 0) {
            return requestMapping.method();
        }
        return RequestMethod.values();
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
