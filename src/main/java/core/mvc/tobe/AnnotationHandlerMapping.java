package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.ReflectionUtils.*;

public class AnnotationHandlerMapping implements HandlerMapping {
    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class)
            .forEach(this::mappingHandlerExecutions);
    }

    private void mappingHandlerExecutions(Class<?> controller) {
        Set<Method> methods = get(Methods.of(controller, withAnnotation(RequestMapping.class)));
        Object handler = createHandlerInstance(controller);
        methods.forEach(method -> {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            List<HandlerKey> handlerKeys = getHandlerKeys(annotation);
            HandlerExecution execution = new HandlerExecution(handler, method);
            handlerKeys.forEach(key -> handlerExecutions.put(key, execution));
        });
    }

    private Object createHandlerInstance(Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private List<HandlerKey> getHandlerKeys(RequestMapping annotation) {
        String url = annotation.value();
        RequestMethod[] requestMethods = annotation.method();

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        return Arrays.stream(requestMethods)
            .map(requestMethod -> new HandlerKey(url, requestMethod))
            .collect(Collectors.toList());
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        HandlerKey handlerKey = handlerExecutions.keySet()
            .stream()
            .filter(key -> key.matches(new HandlerKey(requestUri, rm)))
            .findFirst()
            .orElse(null);

        return handlerExecutions.get(handlerKey);
    }
}
