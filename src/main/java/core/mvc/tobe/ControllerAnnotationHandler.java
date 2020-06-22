package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.*;

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
        for (final Class<?> clazz : annotated) {
            initByRequestMappingMethod(clazz);
        }
    }

    private void initByRequestMappingMethod(final Class clazz) {
        final Method[] methods = clazz.getDeclaredMethods();
        for (final Method method : methods) {
            final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            if (Objects.nonNull(requestMapping)) {
                final ControllerHandlerExecution execution = new ControllerHandlerExecution(clazz, method);
                applyExecution(requestMapping, execution);
            }
        }
    }

    private void applyExecution(RequestMapping requestMapping, HandlerExecution handlerExecution) {
        final Method[] declaredMethods = requestMapping.annotationType().getDeclaredMethods();
        for (final Method method : declaredMethods) {
            if (Objects.nonNull(method.getDefaultValue())) {
                Arrays.stream(RequestMethod.values())
                        .forEach(requestMethod -> executionMap.put(new HandlerKey(requestMapping.value(), requestMethod), handlerExecution));
                continue;
            }
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());
            executionMap.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Map<HandlerKey, HandlerExecution> getExecutionMap() {
        return new HashMap<>(executionMap);
    }
}
