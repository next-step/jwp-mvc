package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        controllerClasses.stream()
                .forEach(clazz -> {
                    Set<Method> methods = getRequestMethods(clazz);
                    addHandlerExecution(clazz, methods);
                });
    }

    private Set<Method> getRequestMethods(Class<?> clazz) {
        return ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    private void addHandlerExecution(Class<?> clazz, Set<Method> methods) {
        try {
            Object object = clazz.getDeclaredConstructor().newInstance();
            methods.forEach(method -> {
                RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
                handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMapping.method()), new HandlerExecution(object, method));
            });
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        HandlerExecution handlerExecution = handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod().toUpperCase())));

        if (Objects.isNull(handlerExecution)) {
            return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.NONE));
        }
        return handlerExecution;
    }
}
