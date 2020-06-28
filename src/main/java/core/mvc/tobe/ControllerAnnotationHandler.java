package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class ControllerAnnotationHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerAnnotationHandler.class);

    public static void apply(final Reflections reflections) {
        final Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> clazz : annotated) {
            initByRequestMappingMethod(clazz);
        }
    }

    private static void initByRequestMappingMethod(final Class clazz) {
        final Set<Method> allMethods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));
        for (final Method method : allMethods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (Objects.nonNull(requestMapping)) {
                final ControllerHandlerExecution execution = new ControllerHandlerExecution(clazz, method);
                applyExecution(requestMapping, execution);
            }
        }
    }

    private static void applyExecution(RequestMapping requestMapping, HandlerExecution handlerExecution) {
        if (requestMapping.method().equals(RequestMethod.ALL)) {
            applyExecutionByAllMethod(requestMapping, handlerExecution);
            return;
        }
        final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());
        AnnotationHandlerMapping.handlerExecutions.put(handlerKey, handlerExecution);
        logger.info("Mapping Info - method : {}, value : {}, Execution : {}", requestMapping.method(), requestMapping.value(), handlerExecution);
    }

    private static void applyExecutionByAllMethod(RequestMapping requestMapping, HandlerExecution handlerExecution) {
        Arrays.stream(RequestMethod.values())
                .forEach(requestMethod -> {
                    if (!requestMethod.equals(RequestMethod.ALL)) {
                        AnnotationHandlerMapping.handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMethod), handlerExecution);
                        logger.info("Mapping Info - method : {}, value : {}, Execution : {}", requestMethod, requestMapping.value(), handlerExecution);
                    }
                });
    }
}
