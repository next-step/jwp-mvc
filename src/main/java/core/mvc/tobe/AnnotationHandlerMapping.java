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
import java.util.Set;
import java.util.stream.Stream;

public class AnnotationHandlerMapping {
    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final Set<Class<?>> controllerClasses = findControllers();
        controllerClasses.forEach(controllerClass -> {
            final Stream<Method> actionMethods = findMethods(controllerClass);
            addHandlerExecutions(controllerClass, actionMethods);
        });
    }

    private Set<Class<?>> findControllers() {
        final Reflections reflections = new Reflections(this.basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Stream<Method> findMethods(Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class));
    }

    private void addHandlerExecutions(Class<?> controllerClass, Stream<Method> actionMethods) {
        actionMethods.forEach(method -> {
            final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            final String annotatedRequestUri = annotation.value();
            final HandlerExecution handlerExecution = new HandlerExecution(controllerClass, method);

            final RequestMethod annotatedRequestMethod = annotation.method();
            addHandlerExecution(annotatedRequestUri, handlerExecution, annotatedRequestMethod);
        });
    }

    private void addHandlerExecution(String annotatedRequestUri, HandlerExecution handlerExecution, RequestMethod annotatedRequestMethod) {
        if(RequestMethod.ALL != annotatedRequestMethod) {
            final HandlerKey key = new HandlerKey(annotatedRequestUri, annotatedRequestMethod);
            handlerExecutions.put(key, handlerExecution);
            return;
        }

        final RequestMethod[] requestMethods = RequestMethod.values();
        Arrays.stream(requestMethods).forEach(m -> {
            final HandlerKey handlerKey = new HandlerKey(annotatedRequestUri, m);
            handlerExecutions.put(handlerKey, handlerExecution);
        });
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
