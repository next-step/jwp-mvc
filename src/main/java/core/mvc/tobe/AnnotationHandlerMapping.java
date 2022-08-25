package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {
    protected static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initMapping() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        initHandlerExecution(controllerScanner.getControllers());
    }

    private void initHandlerExecution(Map<Class<?>, Object> controllers) {
        Set<Method> methods = getMethods(controllers);
        for (Method method : methods) {
            Class<?> declaringClass = method.getDeclaringClass();
            Controller annotation = declaringClass.getAnnotation(Controller.class);
            addHandlerExecution(controllers.get(declaringClass), annotation.value(), method);
        }
    }

    private Set<Method> getMethods(Map<Class<?>, Object> controllers) {
        Set<Method> methods = new HashSet<>();
        for (Class<?> clazz : controllers.keySet()) {
            methods.addAll(ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class)));
        }
        return methods;
    }

    private void addHandlerExecution(Object controllerInstance, String path, Method method) {
        HandlerKey handlerKey = createHandlerKey(path, method);
        logger.debug("Add RequestMapping. URI: {}, requestMethod: {}", handlerKey, method);
        handlerExecutions.put(handlerKey, new HandlerExecution(controllerInstance, method));
    }

    private HandlerKey createHandlerKey(String path, Method method) {
        String uriPath = method.getAnnotation(RequestMapping.class).value();
        RequestMethod requestMethod = method.getAnnotation(RequestMapping.class).method();
        return new HandlerKey(path + uriPath, requestMethod);
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}
