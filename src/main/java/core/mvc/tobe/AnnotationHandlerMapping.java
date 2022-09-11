package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        try {
            ControllerScanner controllerScanner = new ControllerScanner();
            Map<Class<?>, Object> controllerClass = controllerScanner.getControllerClass();

            for (Class<?> clazz : controllerClass.keySet()) {
                Set<Method> methods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));

                for(Method method : methods) {
                    RequestMapping rm = method.getAnnotation(RequestMapping.class);
                    HandlerKey handlerKey = createHandlerKey(rm);

                    Object controller = controllerClass.get(clazz);
                    HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                    handlerExecutions.put(handlerKey, handlerExecution);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HandlerKey createHandlerKey(RequestMapping rm) {
        return new HandlerKey(rm.value(), rm.method());
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
