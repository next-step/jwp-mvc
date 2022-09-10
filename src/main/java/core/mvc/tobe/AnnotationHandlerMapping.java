package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Set<Class<?>> controllers = findControllers();

        Set<Method> methods = findMethods(controllers);

        addHandlerExecutions(methods);
    }

    private Set<Class<?>> findControllers() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Set<Method> findMethods(Set<Class<?>> controllers) {
        return controllers.stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .filter(it -> it.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void addHandlerExecutions(Set<Method> methods) {
        for (Method method : methods) {
            Set<HandlerKey> handlerKeys = createHandlerKey(method);
            HandlerExecution handlerExecution = createHandlerExecution(method);

            for (HandlerKey handlerKey : handlerKeys) {
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    private Set<HandlerKey> createHandlerKey(Method method) {
        RequestMapping annotation = method.getDeclaredAnnotation(RequestMapping.class);
        String url = annotation.value();
        RequestMethod requestMethod = annotation.method();

        if (requestMethod != null) {
            return Set.of(new HandlerKey(url, requestMethod));
        }

        return Arrays.stream(RequestMethod.values())
                .map(it -> new HandlerKey(url, it))
                .collect(Collectors.toSet());
    }

    private HandlerExecution createHandlerExecution(Method method) {
        try {
            Class<?> declaringClass = method.getDeclaringClass();
            Object classInstance = declaringClass.getDeclaredConstructor().newInstance();

            return new HandlerExecution(classInstance, method);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
