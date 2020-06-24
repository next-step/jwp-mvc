package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        initialize();
    }

    @SuppressWarnings("unchecked")
    public void initialize() {
        try {
            Map<Class<?>, Object> controllers = ControllerScanner.getControllers(basePackage);
            for (Class<?> clazz : controllers.keySet()) {
                Set<Method> methods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));
                putHandlerExecutions(controllers.get(clazz), methods);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.keySet().stream()
                .filter(h -> h.isMatchKey(requestUri, requestMethod))
                .findFirst()
                .map(handlerExecutions::get)
                .orElseThrow(() -> new IllegalArgumentException("Handler를 찾을 수 없습니다."));
    }

    private void putHandlerExecutions(Object instance, Set<Method> methods) {
        for (Method declaredMethod : methods) {
            RequestMapping annotation = declaredMethod.getAnnotation(RequestMapping.class);
            handlerExecutions.put(createHandlerKey(annotation), new HandlerExecution(instance, declaredMethod));
        }
    }

    private HandlerKey createHandlerKey(RequestMapping rm) {
        if (rm.method().length == 0) {
            return new HandlerKey(rm.value(), RequestMethod.values());
        }
        return new HandlerKey(rm.value(), rm.method());
    }

}
