package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerAdapter> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(this.basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        Set<Class<?>> classes = controllers.keySet();
        for (Class<?> clazz : classes) {
            Set<Method> requestMappingMethods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));
            for (Method method : requestMappingMethods) {
                HandlerKey handlerKey = createHandlerKey(method.getAnnotation(RequestMapping.class));
                handlerExecutions.put(handlerKey, new HandlerAdapter(controllers.get(clazz), method));
            }
        }
    }

    public HandlerAdapter getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private HandlerKey createHandlerKey(RequestMapping rm) {
        return new HandlerKey(rm.value(), rm.method());
    }

}
