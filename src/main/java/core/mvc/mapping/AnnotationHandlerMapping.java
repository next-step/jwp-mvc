package core.mvc.mapping;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.mvc.handler.HandlerExecution;
import core.mvc.handler.HandlerMapping;
import org.reflections.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();
    private HandlerExecutionRegistry handlerExecutionRegistry = new HandlerExecutionRegistry();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initialize() {
        ControllerScanner scanner = new ControllerScanner(basePackage);
        Set<Class<?>> controllers = scanner.getAllControllerClass();

        controllers.forEach(controller -> putHandlerExecutions(controller, scanner.getControllerInstance(controller)));
    }

    private void putHandlerExecutions(Class<?> controllerClass, Object instance) {
        Set<Method> methods = ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
        methods.forEach(method -> handlerExecutionRegistry.add(instance, method));
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        return handlerExecutionRegistry.getHandlerExecution(request);
    }
}
