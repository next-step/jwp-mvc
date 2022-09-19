package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotatedClass = reflections.getTypesAnnotatedWith(Controller.class);
        setHandlerExecutions(annotatedClass);
    }

    private void setHandlerExecutions(Set<Class<?>> annotatedClass) {
        List<Method> methodList = annotatedClass
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
        RequestMethod[] requestMethods = new RequestMethod[0];
        if (requestMapping.method().length > 0) {
            requestMethods = requestMapping.method();
        }
        if (requestMapping.method().length == 0) {
            requestMethods = RequestMethod.values();
        }
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestUrl, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(method);
            handlerExecutions.put(handlerKey, handlerExecution);
            logger.info("RequestMapping URL : {}, method : {}", handlerKey, method);
        }
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
