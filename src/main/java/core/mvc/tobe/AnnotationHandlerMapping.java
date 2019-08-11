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

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() throws Exception {
        Map<Class<?>, Object> scan = controllerScan(this.basePackage);
        Set<Class<?>> classes = scan.keySet();

        for(Class clazz : classes) {
            Set<Method> allMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());

            setHandlerExecutions(clazz, allMethods);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private HandlerKey createHandlerKey(RequestMapping rm) {
        return new HandlerKey(rm.value(), rm.method());
    }

    private HandlerExecution createHandlerExecution(Class clazz, Method method) {
        return new HandlerExecution(clazz, method);
    }

    private void setHandlerExecutions(Class clazz, Set<Method> allMethods) {
        for(Method method : allMethods) {
            RequestMapping rm = getAnnotation(method, RequestMapping.class);
            handlerExecutions.put(createHandlerKey(rm), createHandlerExecution(clazz, method));
        }
    }

    private <T extends Annotation> T getAnnotation(Method method, Class<T> annotation) {
        return method.getAnnotation(annotation);
    }

    private Map<Class<?>, Object> controllerScan(Object[] basePackage) throws Exception {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotatedClazz = reflections.getTypesAnnotatedWith(Controller.class);

        return scan(annotatedClazz);
    }

    private Map<Class<?>, Object> scan(Set<Class<?>> annotatedClazz) throws Exception {
        Map<Class<?>, Object> scanController = Maps.newHashMap();
        for(Class clazz : annotatedClazz) {
            Object newInstance = clazz.newInstance();
            scanController.put(clazz, newInstance);
        }

        return scanController;
    }
}
