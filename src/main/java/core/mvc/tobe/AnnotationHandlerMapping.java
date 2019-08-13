package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AnnotationHandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final String METHOD_INFO_FORMAT = "%s.%s";
    private static final String MAPPING_INFO_LOG_FORMAT = "{}, execution method: {}";
    private final Class<? extends RequestMapping> requestMappingClass = RequestMapping.class;

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        for (Class<?> aClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            findRequestMappingMethods(aClass);
        }
    }

    private void findRequestMappingMethods(Class<?> aClass) {
        Optional<Object> instanceOptional = getInstance(aClass);
        if (!instanceOptional.isPresent()) {
            return;
        }

        Object instance = instanceOptional.get();
        for (Method method : aClass.getDeclaredMethods()) {
            addHandlerExecutions(instance, method);
        }
    }

    private Optional<Object> getInstance(Class<?> aClass) {
        try {
            return Optional.of(aClass.getDeclaredConstructor().newInstance());
        } catch (ReflectiveOperationException e) {
            logger.error(e.getMessage());
        }

        return Optional.empty();
    }

    private void addHandlerExecutions(Object controller, Method method) {
        if (!method.isAnnotationPresent(requestMappingClass)) {
            return;
        }

        HandlerKey handlerKey = getHandlerKey(method);
        handlerExecutions.put(handlerKey, getHandlerExecution(controller, method));
        loggingMappingInfo(handlerKey, method);
    }

    private HandlerExecution getHandlerExecution(Object controller, Method method) {
        return (request, response) -> (ModelAndView) method.invoke(controller, request, response);
    }

    private HandlerKey getHandlerKey(Method method) {
        RequestMapping requestMapping = method.getAnnotation(requestMappingClass);
        return new HandlerKey(requestMapping.value(), requestMapping.method());
    }

    private void loggingMappingInfo(HandlerKey handlerKey, Method method) {
        String methodInfo = String.format(METHOD_INFO_FORMAT, method.getDeclaringClass().getName(), method.getName());
        logger.info(MAPPING_INFO_LOG_FORMAT, handlerKey, methodInfo);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, requestMethod));
    }

    public Set<HandlerKey> getHandlerKeys() {
        return this.handlerExecutions.keySet();
    }

    public boolean isHandlerKeyPresent(String url, RequestMethod method) {
        return this.handlerExecutions.containsKey(new HandlerKey(url, method));
    }
}
