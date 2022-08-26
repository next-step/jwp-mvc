package core.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final ControllerScanner controllerScanner;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... packages) {
        this.controllerScanner = new ControllerScanner(packages);
    }

    public void initialize() {
        var controllers = controllerScanner.findControllers();

        this.handlerExecutions = controllers.stream()
            .flatMap(this::toHandlerExecutions)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));
    }

    private Stream<Map.Entry<HandlerKey, HandlerExecution>> toHandlerExecutions(Class<?> clazz) {
        var object = createTarget(clazz);
        var methods = findTargetMethods(clazz);

        return methods.stream()
            .map(method -> Map.entry(createKey(method), new HandlerExecution(object, method)));
    }

    private List<Method> findTargetMethods(Class<?> targetClass) {
        return Arrays.stream(targetClass.getMethods())
            .filter(it -> it.isAnnotationPresent(RequestMapping.class))
            .collect(Collectors.toList());
    }

    private Object createTarget(Class<?> targetClass) {
        try {
            return targetClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalArgumentException("해당 클래스를 생성할 수 없습니다. " + targetClass.getName(), e);
        }
    }

    private HandlerKey createKey(Method method) {
        var declaredAnnotation = method.getDeclaredAnnotation(RequestMapping.class);

        return new HandlerKey(declaredAnnotation.value(), declaredAnnotation.method());
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
