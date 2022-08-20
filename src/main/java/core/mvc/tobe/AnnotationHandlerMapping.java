package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements RequestMapping {
    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage, Scanners.TypesAnnotated);
        ControllerScanner controllerScanner = new ControllerScanner(reflections);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.keySet().forEach(key -> setHandlerExecutions(key, controllers.get(key)));
    }

    @SuppressWarnings("unchecked")
    private void setHandlerExecutions(Class<?> controllerClass, Object ControllerObject) {
        Set<Method> allMethods = ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(core.annotation.web.RequestMapping.class));
        for (Method method : allMethods) {
            core.annotation.web.RequestMapping requestMapping = method.getAnnotation(core.annotation.web.RequestMapping.class);
            handlerExecutions.put(new HandlerKey(requestMapping), new HandlerExecution(ControllerObject, method));
        }
    }

    @Override
    public Object findHandler(HttpServletRequest request) {
        return getHandler(request);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
