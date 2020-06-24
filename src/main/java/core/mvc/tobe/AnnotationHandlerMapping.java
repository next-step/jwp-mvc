package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections8.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements RequestHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class, true);

        controllers.stream()
                .forEach(c -> {
                    handlerExecutions.putAll(makeRequestMappingHandlerMap(c));
                });
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    public Map<HandlerKey, HandlerExecution> makeRequestMappingHandlerMap(final Class<?> controller) {
        Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

        Set<Method> methods = getMethodsAnnotatedWith(controller, RequestMapping.class);
        methods.stream()
                .forEach(m -> {
                    try {
                        RequestMapping r = m.getAnnotation(RequestMapping.class);
                        HandlerKey key = new HandlerKey(r.value(), r.method());
                        HandlerExecution execution = createMethodHandlerExecution(controller, m);
                        handlerExecutions.put(key, execution);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

        return handlerExecutions;
    }

    public Set<Method> getMethodsAnnotatedWith(final Class<?> controller, final Class<? extends Annotation> annotation) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

    public HandlerExecution createMethodHandlerExecution(final Class<?> controller, Method m) throws Exception {
        return new HandlerExecution(controller.newInstance(), m);
    }
}
