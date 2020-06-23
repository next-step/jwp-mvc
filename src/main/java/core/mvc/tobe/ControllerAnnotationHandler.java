package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.*;

public class ControllerAnnotationHandler {

    public static void apply(final Reflections reflections) {
        final Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> clazz : annotated) {
            initByRequestMappingMethod(clazz);
        }
    }

    private static void initByRequestMappingMethod(final Class clazz) {
        final Method[] methods = clazz.getDeclaredMethods();
        for (final Method method : methods) {
            final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            if (Objects.nonNull(requestMapping)) {
                final ControllerHandlerExecution execution = new ControllerHandlerExecution(clazz, method);
                applyExecution(requestMapping, execution);
            }
        }
    }

    private static void applyExecution(RequestMapping requestMapping, HandlerExecution handlerExecution) {
        final Method[] declaredMethods = requestMapping.annotationType().getDeclaredMethods();
        for (final Method method : declaredMethods) {
            if (Objects.nonNull(method.getDefaultValue())) {
                Arrays.stream(RequestMethod.values())
                        .forEach(requestMethod -> AnnotationHandlerMapping.handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMethod), handlerExecution));
                continue;
            }
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());
            AnnotationHandlerMapping.handlerExecutions.put(handlerKey, handlerExecution);
        }
    }
}
