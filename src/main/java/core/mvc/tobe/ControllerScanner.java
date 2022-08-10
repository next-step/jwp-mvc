package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.reflections.scanners.Scanners.TypesAnnotated;

public class ControllerScanner {

    private static final String BASE_PACKAGE = "next";
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public Map<HandlerKey, HandlerExecution> findHasMethodRequestMapping() {

        Class<Controller> controllerAnnotation = Controller.class;
        Class<RequestMapping> requestMappingAnnotation = RequestMapping.class;

        Reflections reflections = new Reflections(BASE_PACKAGE, TypesAnnotated);
        Set<Class<?>> controllerAnnotatedWith = reflections.getTypesAnnotatedWith(controllerAnnotation);

        for (Class<?> aClass : controllerAnnotatedWith) {
            Set<Method> allMethods = ReflectionUtils.getAllMethods(aClass, ReflectionUtils.withAnnotation(RequestMapping.class));
            mappingMethods(requestMappingAnnotation, aClass, allMethods);
        }

        return handlerExecutions;
    }

    private void mappingMethods(Class<RequestMapping> requestMappingAnnotation, Class<?> clazz, Set<Method> methods) {
        Object instanceClazz;
        try {
            instanceClazz = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return ;
        }
        for (Method method : methods) {
            requestMappingMethodPut(instanceClazz, method, method.getAnnotation(requestMappingAnnotation));
        }
    }

    private void requestMappingMethodPut(Object clazz, Method method, RequestMapping rm) {
        if (Objects.nonNull(rm)) {

            handlerExecutions.put(createHandlerKey(rm), new HandlerExecution(clazz, method));
        }
    }

    private HandlerKey createHandlerKey(RequestMapping rm) {
        return new HandlerKey(rm.value(), rm.method());
    }
}
