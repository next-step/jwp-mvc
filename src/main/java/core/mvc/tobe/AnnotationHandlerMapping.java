package core.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.reflections.ReflectionUtils;

import com.google.common.collect.Maps;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;

public class AnnotationHandlerMapping implements HandlerMapping {
    private final Object[] basePackage;

    private final Map<HandlerKey, ControllerExecutor> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner();
        Set<Class<?>> controllerClass = controllerScanner.findController(this.basePackage);

        controllerClass.forEach(clazz -> {
            ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class)).forEach(method -> {
                try {
                    Controller controller = clazz.getAnnotation(Controller.class);
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                    RequestMethod[] requestMethods = requestMapping.method();

                    if (isRequestMethodEmpty(requestMapping)) {
                        requestMethods = RequestMethod.values();
                    }

                    for (RequestMethod requestMethod : requestMethods) {
                        HandlerKey handlerKey = createHandlerKey(controller, requestMapping, requestMethod);
                        Object instance = clazz.getConstructor().newInstance();
                        handlerExecutions.put(handlerKey, new ControllerExecutor(instance, method));
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    @Override
    public ControllerExecutor getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private HandlerKey createHandlerKey(Controller controller, RequestMapping rm, RequestMethod requestMethod) {
        return new HandlerKey(controller.value() + rm.value(), requestMethod);
    }

    private boolean isRequestMethodEmpty(RequestMapping requestMapping) {
        return requestMapping.method().length == 0;
    }
}
