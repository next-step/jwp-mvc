package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AnnotationHandlerMapping {
    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Set<Class<?>> controllers = AnnotatedTargetScanner.loadClasses(Controller.class, basePackage);

        controllers.forEach(this::convertClassToHandlerExecution);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());

        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private void convertClassToHandlerExecution(final Class<?> clazz) {
        Object instance = newInstance(clazz);

        AnnotatedTargetScanner.loadMethods(clazz, RequestMapping.class)
                .forEach(method -> putHandlerExecution(method, instance));
    }

    private void putHandlerExecution(final Method method, final Object instance) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        HandlerExecution handlerExecution = new HandlerExecution(method, instance);

        getRequestMethods(requestMapping)
                .forEach(requestMethod ->
                        handlerExecutions.put(
                                new HandlerKey(requestMapping.value(), requestMethod),
                                handlerExecution
                        )
                );
    }

    private List<RequestMethod> getRequestMethods(final RequestMapping requestMapping) {
        RequestMethod[] method = requestMapping.method();

        if (method.length != 0) {
            return Arrays.asList(method);
        }

        return Arrays.asList(RequestMethod.values());
    }

    private Object newInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor()
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Fail to create new instance of " + clazz.getName());
        }
    }
}
