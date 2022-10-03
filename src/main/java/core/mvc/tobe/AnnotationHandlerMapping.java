package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import org.reflections.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping  {
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
                initHandlerExecutions(controllerClass, clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initHandlerExecutions(Map<Class<?>, Object> controllerClass, Class<?> clazz) {
        Set<Method> methods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));

        for(Method method : methods) {
            HandlerKey handlerKey = createHandlerKey(method);

            Object controller = controllerClass.get(clazz);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private HandlerKey createHandlerKey(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        Controller controllerAnnotation = declaringClass.getAnnotation(Controller.class);
        String controllerPath = controllerAnnotation.value();

        RequestMapping rm = method.getAnnotation(RequestMapping.class);

        return new HandlerKey(controllerPath + rm.value(), rm.method());
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());

        HandlerKey handlerKey = null;
        for(HandlerKey key : handlerExecutions.keySet()) {
            if (key.isSameHandlerKey(new HandlerKey(requestUri, rm))) {
                handlerKey = key;
                break;
            }
        }

        return handlerExecutions.get(handlerKey);
    }
}
