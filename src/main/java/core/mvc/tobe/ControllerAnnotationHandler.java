package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

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
                ArgumentResolvers.initialize(clazz, method);
            }
        }
    }
}
