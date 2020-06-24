package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ControllerAnnotationHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerAnnotationHandler.class);

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
            if (requestMapping.method().equals(RequestMethod.ALL)) {
                Arrays.stream(RequestMethod.values())
                        .forEach(requestMethod -> {
                            if (!requestMethod.equals(RequestMethod.ALL)) {
                                AnnotationHandlerMapping.handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMethod), handlerExecution);
                                logger.info("Mapping Info - method : {}, value : {}, Execution : {}", requestMethod, requestMapping.value(), handlerExecution);
                            }
                        });
                return;
            }
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());
            AnnotationHandlerMapping.handlerExecutions.put(handlerKey, handlerExecution);
            logger.info("Mapping Info - method : {}, value : {}, Execution : {}", requestMapping.method(), requestMapping.value(), handlerExecution);
    }
}
