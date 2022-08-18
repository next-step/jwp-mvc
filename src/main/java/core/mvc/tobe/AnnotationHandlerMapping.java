package core.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.reflections.Reflections;

import com.google.common.collect.Maps;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        var reflections = new Reflections(basePackage);
        var targetClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> targetClass : targetClasses) {
            var targetObject = createTarget(targetClass);

            var methods = Arrays.stream(targetClass.getMethods())
                .filter(it -> it.getDeclaredAnnotation(RequestMapping.class) != null)
                .collect(Collectors.toList());

            var handlers = methods.stream()
                .map(it -> Map.entry(createKey(it), new HandlerExecution(targetObject, it)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            handlerExecutions.putAll(handlers);
        }
    }

    private HandlerKey createKey(Method it) {
        var declaredAnnotation = it.getDeclaredAnnotation(RequestMapping.class);

        return new HandlerKey(declaredAnnotation.value(), declaredAnnotation.method());
    }

    private Object createTarget(Class<?> targetClass) {
        try {
            return targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("해당 클래스를 생성할 수 없습니다. " + targetClass.getName(), e);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
