package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ControllerAnnotationHandler implements AnnotationHandler {

    private final Reflections reflections;
    private final Map<HandlerKey, HandlerExecution> executionMap;

    public ControllerAnnotationHandler(final Reflections reflections) {
        this.reflections = reflections;
        this.executionMap = new HashMap<>();
    }

    @Override
    public void init() {
        final Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> aClass : annotated) {
            final Method[] methods = aClass.getDeclaredMethods();
            for (final Method method : methods) {

                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                // requestmapping 인자가 있냐 없냐에 따라 분기 post, get (servlet 처럼
                if (Objects.nonNull(requestMapping)) {
                    executionMap.put(new HandlerKey(requestMapping.value(), requestMapping.method()), new HandlerExecution());
                }
            }
        }
    }

    public Map<HandlerKey, HandlerExecution> getExecutionMap() {
        return new HashMap<>(executionMap);
    }


}
