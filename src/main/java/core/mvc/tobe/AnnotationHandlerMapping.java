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

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final Set<Class<?>> controllerClasses = this.getControllerClasses();
        logger.info("{} Class with @Controller found under package {}: {}",
                controllerClasses.size(),
                Arrays.toString(this.basePackage),
                controllerClasses.stream().map(Class::getName).collect(Collectors.toList()));

        for (Class<?> controllerClass : controllerClasses) {
            this.registerRequestMappings(controllerClass);
        }
    }

    private Set<Class<?>> getControllerClasses() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void registerRequestMappings(Class<?> controller) {
        Set<Method> requestMappingMethods = this.getRequestMappingMethods(controller);

        for (Method requestMappingMethod : requestMappingMethods) {
            this.registerRequestMapping(controller, requestMappingMethod);
        }
    }

    private Set<Method> getRequestMappingMethods(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void registerRequestMapping(final Class<?> controllerClass, Method requestMappingMethod) {
        final RequestMapping requestMapping = requestMappingMethod.getAnnotation(RequestMapping.class);
        final String url = requestMapping.value();
        final RequestMethod[] methods = ArrayUtils.isEmpty(requestMapping.method()) ?
                RequestMethod.values() : requestMapping.method();

        final Object controller = this.initializeController(controllerClass);
        final HandlerExecution handlerExecution = (request, response) ->
                (ModelAndView) requestMappingMethod.invoke(controller, request, response);

        for (final RequestMethod method : methods) {
            final HandlerKey handlerKey = new HandlerKey(url, method);
            this.handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Object initializeController(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
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
