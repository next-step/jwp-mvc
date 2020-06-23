package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

public class AnnotationHandlerMapping implements HandlerMapping {

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Set<Class<?>> annotatedTypedClazz = AnnotatedTypeScanner.getAnnotatedTypedClazz(Controller.class, basePackage);
        for (Class<?> clazz : annotatedTypedClazz) {
            Set<Method> methods = AnnotatedTypeScanner.getAnnotatedTypedMethods(clazz, RequestMapping.class);
            methods.forEach(method -> fillHandlerMap(clazz, method));
        }
    }

    private void fillHandlerMap(Class<?> clazz, Method method) {
        HandlerKey key = createHandlerKey(method.getAnnotation(RequestMapping.class));
        HandlerExecution value = createHandlerExecution(clazz, method);
        this.handlerExecutions.put(key, value);
    }

    private HandlerExecution createHandlerExecution(Class<?> clazz, Method method) {
        return new HandlerExecution(AnnotatedTypeScanner.getClazzObject(clazz), method);
    }

    private HandlerKey createHandlerKey(RequestMapping rm) {
        return new HandlerKey(rm.value(), rm.method());
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
