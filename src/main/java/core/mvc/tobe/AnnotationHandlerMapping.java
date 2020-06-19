package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.AnnotationScanner;
import org.reflections.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        AnnotationScanner annotationScanner = new AnnotationScanner(this.basePackage);
        Set<Class<?>> controllers = annotationScanner.findAnnotationClass(Controller.class);

        for (Class<?> controller : controllers) {
            this.handlerExecutions.putAll(Arrays.stream(controller.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .collect(Collectors.toMap(method -> {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        return new HandlerKey(requestMapping.value(), requestMapping.method());
                    }, method -> new HandlerExecution(controller, method))));
        }

    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    public boolean containsExecution(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return this.handlerExecutions.containsKey(new HandlerKey(requestUri, rm));
    }
}
