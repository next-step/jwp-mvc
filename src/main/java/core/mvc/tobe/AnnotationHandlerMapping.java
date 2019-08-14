package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final int DEFAULT_MAPPING_METHOD_LENGTH = 0;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();
    private final Object[] basePackage;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        new Reflections(basePackage).getTypesAnnotatedWith(Controller.class)
                .forEach(this::bindController);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private void bindController(final Class<?> clazz) {
        try {
            final Object controller = clazz.getConstructor().newInstance();

            Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> this.bindMethod(controller, method));
        } catch (final Exception e) {
            throw new HandlerInitializeException(e);
        }
    }

    private void bindMethod(final Object controller,
                            final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        Arrays.stream(extractMethods(requestMapping))
                .forEach(requestMethod -> {
                    final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                    final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

                    handlerExecutions.put(handlerKey, handlerExecution);
                });
    }

    private RequestMethod[] extractMethods(final RequestMapping requestMapping) {
        final RequestMethod[] methods = requestMapping.method();
        if (methods.length == DEFAULT_MAPPING_METHOD_LENGTH) {
            return RequestMethod.values();
        }

        return methods;
    }
}
