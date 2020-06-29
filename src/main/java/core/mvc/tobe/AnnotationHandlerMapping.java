package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import org.reflections.Reflections;
import org.slf4j.Logger;


import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class AnnotationHandlerMapping {
    private static final Logger logger = getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Reflections reflections;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.reflections = new Reflections(basePackage);
    }

    public void initialize() {
        Set<Class<?>> controllerClasses = this.reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> clazz : controllerClasses) {

            Object controller = getNewInstance(clazz);

            Set<Method> methods = getMethodsAnnotatedWithRequestMapping(clazz);

            methods.forEach(method -> registerHandlerExecution(method, controller));
        }
    }

    private void registerHandlerExecution(Method method, Object controller) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        HandlerKey handlerKey = new HandlerKey(annotation.value(), annotation.method());

        handlerExecutions.put(handlerKey, new HandlerExecution(method, controller));
    }

    private Set<Method> getMethodsAnnotatedWithRequestMapping(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private Object getNewInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("unable to load controller.", e);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
