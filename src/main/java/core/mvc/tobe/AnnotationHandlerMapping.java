package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import exception.NotFoundException;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

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
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        if (controllers.isEmpty()) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        handlerExecutions.putAll(initHandlerExecution(controllers));
    }

    private Map<HandlerKey, HandlerExecution> initHandlerExecution(Map<Class<?>, Object> controllers) {
        Map<HandlerKey, HandlerExecution> tmp = Maps.newHashMap();
        Set<Method> methods = getMethods(controllers);
        for (Method method : methods) {
            Class<?> declaringClass = method.getDeclaringClass();
            Controller annotation = declaringClass.getAnnotation(Controller.class);
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            HandlerKey handlerKey = createHandlerKey(requestMapping, annotation.value());
            tmp.put(handlerKey, new HandlerExecution(controllers.get(declaringClass), method));
        }
        return tmp;
    }

    private Set<Method> getMethods(Map<Class<?>, Object> controllers) {
        Set<Method> methods = new HashSet<>();
        for (Class<?> clazz : controllers.keySet()) {
            methods.addAll(ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class)));
        }
        return methods;
    }

    private HandlerKey createHandlerKey(RequestMapping requestMapping, String path) {
        String uriPath = requestMapping.value();
        RequestMethod requestMethod = requestMapping.method();
        return new HandlerKey(path + uriPath, requestMethod);
    }

    private HandlerKey createHandlerKey(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return new HandlerKey(requestURI, requestMethod);
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = createHandlerKey(request);
        return handlerExecutions.entrySet()
                .stream()
                .filter(entry -> entry.getKey().matches(handlerKey))
                .map(Map.Entry::getValue)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("지원하지 않는 요청입니다. %s", request)));
    }

    @Override
    public boolean isSupported(HttpServletRequest request) {
        HandlerKey handlerKey = createHandlerKey(request);
        return handlerExecutions.keySet()
                .stream()
                .anyMatch(key -> key.matches(handlerKey));
    }
}
