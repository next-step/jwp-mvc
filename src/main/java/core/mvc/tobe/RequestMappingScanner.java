package core.mvc.tobe;

import static java.util.stream.Collectors.toMap;
import static org.reflections.ReflectionUtils.Methods;
import static org.reflections.util.ReflectionUtilsPredicates.withAnnotation;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.exception.UnSupportedControllerInstanceException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;

public class RequestMappingScanner {

    private static final Map<HandlerKey, HandlerExecutable> EMPTY_EXECUTABLE_GROUP = new HashMap<>();

    private RequestMappingScanner() {
        throw new AssertionError();
    }

    public static Map<HandlerKey, HandlerExecutable> getHandlerExecutable(final Object instance) {
        final Class<?> clazz = instance.getClass();
        final Controller annotation = clazz.getAnnotation(Controller.class);
        validateExistenceOfControllerAnnotation(annotation, clazz.getName());

        final Set<Method> methods = ReflectionUtils.get(Methods.of(clazz, withAnnotation(RequestMapping.class)));
        if (methods.isEmpty()) {
            return EMPTY_EXECUTABLE_GROUP;
        }

        return getExecutableGroup(instance, methods);
    }

    private static Map<HandlerKey, HandlerExecutable> getExecutableGroup(final Object instance, final Set<Method> methods) {
        final RequestMapping controllerRequestMapping = instance.getClass().getAnnotation(RequestMapping.class);

        Map<HandlerKey, HandlerExecutable> handlerExecutableGroup = new HashMap<>();

        for (final Method method : methods) {
            final RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
            final List<HandlerKey> handlerKeys = HandlerKeyGenerator.generate(controllerRequestMapping, methodRequestMapping);
            final HandlerExecutable handlerExecution = new HandlerExecution(instance, method);

            handlerExecutableGroup.putAll(toExecutableGroup(handlerKeys, handlerExecution));
        }

        return handlerExecutableGroup;
    }

    private static Map<HandlerKey, HandlerExecutable> toExecutableGroup(final List<HandlerKey> handlerKeys, final HandlerExecutable handlerExecution) {
        return handlerKeys.stream()
            .collect(toMap(handlerKey -> handlerKey, it -> handlerExecution));
    }

    private static void validateExistenceOfControllerAnnotation(final Controller annotation, final String name) {
        if (annotation == null) {
            throw new UnSupportedControllerInstanceException(name);
        }
    }
}
