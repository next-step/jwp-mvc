package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {
    private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);

    public static Map<HandlerKey, HandlerExecution> scan(Object[] basePackages) {
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

        Reflections reflections = new Reflections(basePackages);

        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            addScannedControllerExecutions(handlerExecutions, controllerClass);
        }

        return handlerExecutions;
    }

    private static void addScannedControllerExecutions(Map<HandlerKey, HandlerExecution> handlerExecutions, Class<?> controllerClass) {
        try {
            addExecutionInstances(handlerExecutions, controllerClass);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            logger.error("Scanning Controller Error!", e);
        }
    }

    private static void addExecutionInstances(Map<HandlerKey, HandlerExecution> handlerExecutions, Class<?> controllerClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Object handlerObject;
        Constructor<?> constructor = controllerClass.getDeclaredConstructor();
        handlerObject = constructor.newInstance();

        for (Method handlerMethod : controllerClass.getDeclaredMethods()) {
            addExecution(handlerExecutions, handlerObject, handlerMethod);
        }
    }

    private static void addExecution(Map<HandlerKey, HandlerExecution> handlerExecutions, Object handlerObject, Method handlerMethod) {
        if (!handlerMethod.isAnnotationPresent(RequestMapping.class)) {
            return;
        }

        RequestMapping requestMapping = handlerMethod.getDeclaredAnnotation(RequestMapping.class);
        String requestMappingUrl = requestMapping.value();
        RequestMethod requestMethod = requestMapping.method();

        addExecutionByRequestMethods(handlerExecutions, handlerObject, handlerMethod, requestMappingUrl, requestMethod);
    }

    private static void addExecutionByRequestMethods(Map<HandlerKey, HandlerExecution> handlerExecutions, Object handlerObject, Method handlerMethod, String requestMappingUrl, RequestMethod requestMethod) {
        if (requestMethod != RequestMethod.NONE) {
            HandlerExecution execution = new HandlerExecution(handlerObject, handlerMethod);
            handlerExecutions.put(new HandlerKey(requestMappingUrl, requestMethod), execution);
            return;
        }

        for (RequestMethod requestMethodValue : RequestMethod.REQUEST_METHODS) {
            HandlerExecution execution = new HandlerExecution(handlerObject, handlerMethod);
            handlerExecutions.put(new HandlerKey(requestMappingUrl, requestMethodValue), execution);
        }
    }
}
