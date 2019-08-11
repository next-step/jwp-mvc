package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {
    private final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Map<Class<?>, Object> controller = controllerScan(this.basePackage);
        Set<Class<?>> classes = controller.keySet();

        for(Class clazz : classes) {
            Set<Method> allMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());

            setHandlerExecutions(controller.get(clazz), allMethods);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private Map<Class<?>, Object> controllerScan(Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotatedClazz = reflections.getTypesAnnotatedWith(Controller.class);

        return setControllerInstances(annotatedClazz);
    }

    private Map<Class<?>, Object> setControllerInstances(Set<Class<?>> annotatedClazz) {
        Map<Class<?>, Object> scanController = Maps.newHashMap();
        for(Class clazz : annotatedClazz) {
            setControllerInstance(scanController, clazz);
        }

        return scanController;
    }

    private void setControllerInstance(Map<Class<?>, Object> scanController, Class clazz) {
        try {
            scanController.put(clazz, clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            logger.debug("controller register fail");
        }
    }

    private void setHandlerExecutions(Object clazz, Set<Method> allMethods) {
        for(Method method : allMethods) {
            RequestMapping rm = getAnnotation(method, RequestMapping.class);
            handlerExecutions.put(createHandlerKey(rm), createHandlerExecution(clazz, method));
        }
    }

    private <T extends Annotation> T getAnnotation(Method method, Class<T> annotation) {
        return method.getAnnotation(annotation);
    }

    private HandlerKey createHandlerKey(RequestMapping rm) {
        return new HandlerKey(rm.value(), rm.method());
    }

    private HandlerExecution createHandlerExecution(Object clazz, Method method) {
        return new HandlerExecution(clazz, method);
    }
}
