package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();
    private ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        initialize();
    }

    private void initialize() {
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        controllers.forEach((controller, controllerInstance) -> {

            Method[] methods = getMethodsWithAnnotation(controller, RequestMapping.class);

            Arrays.stream(methods)
                    .forEach(method -> {
                        RequestMapping requestMapping = getAnnotationInMethod(method, RequestMapping.class);
                        RequestMethod[] requestMethods = getRequestMethods(requestMapping);

                        List<HandlerKey> handlerKeys = createHandlerKeys(controller, requestMapping, requestMethods);

                        registerMappings(controllerInstance, method, handlerKeys);
                    });

        });
    }

    private List<HandlerKey> createHandlerKeys(Class<?> controller, RequestMapping requestMapping, RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(method -> createHandlerKey(controller, requestMapping.value(), method))
                .collect(Collectors.toList());
    }

    private HandlerKey createHandlerKey(Class<?> controller, String path, RequestMethod method) {
        if (controller.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping annotation = controller.getAnnotation(RequestMapping.class);
            String prefix = annotation.value();
            return new HandlerKey(prefix + path, method);
        }

        return new HandlerKey(path, method);
    }

    private void registerMappings(Object controller, Method method, List<HandlerKey> handlerKeys) {
        handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, new HandlerExecution(controller, method)));
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }

        return requestMethods;
    }

    @Override
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
