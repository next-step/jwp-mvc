package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import core.utils.ArrayUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ControllerScanner {
    private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);

    public Map<HandlerKey, HandlerExecution> scan(Object... basePackage) {
        final Set<Class<?>> controllerClasses = this.getControllerClasses(basePackage);
        logger.info("{} Class with @Controller found under package {}: {}",
                controllerClasses.size(),
                Arrays.toString(basePackage),
                controllerClasses.stream().map(Class::getName).collect(Collectors.toList()));

        return this.toHandlerExecutions(controllerClasses);
    }

    private Set<Class<?>> getControllerClasses(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Map<HandlerKey, HandlerExecution> toHandlerExecutions(final Set<Class<?>> controllerClasses) {
        final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

        for (Class<?> controllerClass : controllerClasses) {
            this.addHandlerExecutions(handlerExecutions, controllerClass);
        }

        return handlerExecutions;
    }

    private void addHandlerExecutions(final Map<HandlerKey, HandlerExecution> handlerExecutions, Class<?> controller) {
        Set<Method> requestMappingMethods = this.getRequestMappingMethods(controller);

        for (Method requestMappingMethod : requestMappingMethods) {
            this.addHandlerExecutions(handlerExecutions, controller, requestMappingMethod);
        }
    }

    private Set<Method> getRequestMappingMethods(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void addHandlerExecutions(final Map<HandlerKey, HandlerExecution> handlerExecutions, final Class<?> controllerClass, Method requestMappingMethod) {
        final RequestMapping requestMapping = requestMappingMethod.getAnnotation(RequestMapping.class);
        final String url = requestMapping.value();
        final RequestMethod[] methods = ArrayUtils.isEmpty(requestMapping.method()) ?
                RequestMethod.values() : requestMapping.method();

        final Object controller = this.initializeController(controllerClass);
        final HandlerExecution handlerExecution = (request, response) ->
                (ModelAndView) requestMappingMethod.invoke(controller, request, response);

        for (final RequestMethod method : methods) {
            final HandlerKey handlerKey = new HandlerKey(url, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Object initializeController(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
