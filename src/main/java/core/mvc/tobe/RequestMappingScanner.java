package core.mvc.tobe;

import static org.reflections.ReflectionUtils.Methods;
import static org.reflections.util.ReflectionUtilsPredicates.withAnnotation;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.exception.UnSupportedControllerInstanceException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;

public class RequestMappingScanner {

    private static final Map<HandlerKey, HandlerExecutable> EMPTY_EXECUTABLE_GROUP = new HashMap<>();

    private RequestMappingScanner() {
        throw new AssertionError();
    }

    public static Map<HandlerKey, HandlerExecutable> getHandlerExecutable(final Class<?> clazz) {
        final Controller annotation = clazz.getAnnotation(Controller.class);
        validateExistenceOfControllerAnnotation(annotation, clazz.getName());

        final Set<Method> methods = ReflectionUtils.get(Methods.of(clazz, withAnnotation(RequestMapping.class)));
        if (methods.isEmpty()) {
            return EMPTY_EXECUTABLE_GROUP;
        }

        return EMPTY_EXECUTABLE_GROUP;
    }

    private static void validateExistenceOfControllerAnnotation(final Controller annotation, final String name) {
        if (annotation == null) {
            throw new UnSupportedControllerInstanceException(name);
        }
    }
}
