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
import java.util.Set;
import java.util.stream.Stream;

public class AnnotationHandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(this::findRequestMappingMethods);

    }

    private void findRequestMappingMethods(Class<?> aClass) {
        try {
            Method[] methods = aClass.getDeclaredMethods();
            Object instance = aClass.getDeclaredConstructor().newInstance();
            Stream.of(methods).forEach(method -> addHandlerExecutions(instance, method));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void addHandlerExecutions(Object controller, Method method) {
        Class<? extends RequestMapping> requestMappingClass = RequestMapping.class;
        if (!method.isAnnotationPresent(requestMappingClass)) {
            return;
        }

        RequestMapping requestMapping = method.getAnnotation(requestMappingClass);
        HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());
        HandlerExecution handleExecution = (request, response) ->
                (ModelAndView) method.invoke(controller, request, response);
        loggingMappingInfo(handlerKey, method);
        handlerExecutions.put(handlerKey, handleExecution);
    }

    private void loggingMappingInfo(HandlerKey handlerKey, Method method) {
        String methodInfo = String.format("%s.%s", method.getDeclaringClass().getName(), method.getName());
        logger.info("{}, execution method: {}", handlerKey, methodInfo);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    public Set<HandlerKey> getHandlerKeys() {
        return this.handlerExecutions.keySet();
    }

    public boolean isHandlerKeyPresent(String url, RequestMethod method) {
        return this.handlerExecutions.containsKey(new HandlerKey(url, method));
    }
}
