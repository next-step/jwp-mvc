package core.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

public class AnnotationHandlerMapping {
    private static final int EMPTY_METHOD_LENGTH = 0;
    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        for (Class<?> controller : controllerScanner.getControllers()) {
            addHandlerExecutions(controllerScanner, controller);
        }
    }

    private void addHandlerExecutions(ControllerScanner controllerScanner, Class<?> controller) {
        Controller annotation = controller.getAnnotation(Controller.class);
        Object handler = controllerScanner.getHandlerInstance(controller);
        Set<Method> methods = controllerScanner.getMethods(controller);

        methods.forEach(method -> addHandlerExecution(annotation.value(), handler, method));
    }

    private void addHandlerExecution(String value, Object handler, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] methods = requestMapping.method();

        if (methods.length == EMPTY_METHOD_LENGTH) {
            methods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : methods) {
            handlerExecutions.put(new HandlerKey(value + requestMapping.value(), requestMethod), new HandlerExecution(handler, method));
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
