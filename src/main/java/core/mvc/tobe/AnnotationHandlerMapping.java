package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();
    private Reflections reflections;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.reflections = new Reflections(this.basePackage);
    }

    public void initialize() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        controllers.forEach(controller -> {
            Method[] methods = getMethodsWithAnnotation(controller, RequestMapping.class);
            Arrays.stream(methods)
                    .forEach(method -> registerMapping(controller, method));
        });
    }

    private void registerMapping(Class<?> controller, Method method) {
        RequestMapping requestMapping = getAnnotationInMethod(method, RequestMapping.class);
        RequestMethod[] requestMethods = getRequestMethods(requestMapping);

        Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(requestMapping.value(), requestMethod))
                .forEach(handlerKey -> registerMapping(controller, method, handlerKey));
    }

    private void registerMapping(Class<?> controller, Method method, HandlerKey handlerKey) {
        try {
            handlerExecutions.put(handlerKey, new HandlerExecution(controller.newInstance(), method));
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("register handler failed. {}", e);
        }
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }

        return requestMethods;
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private <T extends Annotation> T getAnnotationInMethod(Method method, Class<T> annotation) {
        return method.getAnnotation(annotation);
    }

    private <T extends Annotation> Method[] getMethodsWithAnnotation(Class<?> clazz, Class<T> annotation) {
        Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(annotation))
                .toArray(Method[]::new);
    }
}
