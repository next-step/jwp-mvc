package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.utils.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {
    private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);

    public static Map<HandlerKey, HandlerExecution> scan(final Object[] basePackage) {
        Map<HandlerKey, HandlerExecution> executionMap = Maps.newHashMap();
        final Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class, true);
        controllers.forEach(aClass -> {
            final Object target = ReflectionUtils.newInstance(aClass);
            final Method[] declaredMethods = aClass.getDeclaredMethods();
            addHandlerExecution(executionMap, target, declaredMethods);
        });
        return executionMap;
    }

    private static void addHandlerExecution(final Map<HandlerKey, HandlerExecution> executionMap,
                                            final Object target,
                                            final Method[] methods) {
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> {
                    RequestMapping rm = method.getAnnotation(RequestMapping.class);
                    executionMap.put(new HandlerKey(rm.value(), rm.method()), HandlerExecution.of(target, method));
                    logger.info("Mapped \"{[{}],methods=[{}]}\" onto {}", rm.value(), rm.method(), method.getName());
                });
    }
}
