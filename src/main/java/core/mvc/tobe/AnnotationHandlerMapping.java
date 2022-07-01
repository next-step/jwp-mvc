package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        controllers.forEach((clazz, instance) -> {
            Set<Method> methods = controllerScanner.getMethodsWithRequestMapping(clazz);
            methods.forEach(it -> makeHandlerExecution(instance, it));
        });
    }

    private void makeHandlerExecution(Object clazzInstance, Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = getRequestMappingMethods(annotation);

        Arrays.stream(requestMethods)
                .forEach(requestMethod -> {
                    HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
                    handlerExecutions.put(handlerKey,
                            (request, response) -> (ModelAndView) method.invoke(clazzInstance, request, response));
                });
    }

    private RequestMethod[] getRequestMappingMethods(RequestMapping annotation) {
        RequestMethod[] methods = annotation.method();

        if (ArrayUtils.isEmpty(methods)) {
            return RequestMethod.values();
        }

        return methods;
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
