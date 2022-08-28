package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        controllers.keySet()
            .forEach(clazz -> addHandlerMapping(clazz, controllers));
    }

    private void addHandlerMapping(Class<?> clazz, Map<Class<?>, Object> controllers) {
        getRequestMappingMethod(clazz)
            .forEach(method -> {
                    handlerExecutions.put(
                        createHandlerKey(method, clazz),
                        new HandlerExecution(controllers.get(method.getDeclaringClass()), method));
            });
    }

    private static Set<Method> getRequestMappingMethod(Class<?> clazz) {
        return ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    private static HandlerKey createHandlerKey(Method method, Class<?> clazz) {
        Controller controller = clazz.getAnnotation(Controller.class);
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        return new HandlerKey(controller.value() + requestMapping.value(), requestMapping.method());
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
