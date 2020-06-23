package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestMappingHandlerUtils {

    private final Class<?> controller;

    public RequestMappingHandlerUtils(final Class<?> clazz) {
        this.controller = clazz;
    }

    public Map<HandlerKey, HandlerExecution> makeRequestMappingHandlerMap() {
        Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

        Set<Method> methods = getMethodsAnnotatedWith(RequestMapping.class);
        methods.stream()
                .forEach(m -> {
                    try {
                        RequestMapping r = m.getAnnotation(RequestMapping.class);
                        HandlerKey key = new HandlerKey(r.value(), r.method());
                        HandlerExecution execution = createMethodHandlerExecution(m);
                        handlerExecutions.put(key, execution);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

        return handlerExecutions;
    }

    public Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
        return Arrays.stream(this.controller.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

    public HandlerExecution createMethodHandlerExecution(Method m) throws Exception {
        return new HandlerExecution(controller.newInstance(), m);
    }
}
