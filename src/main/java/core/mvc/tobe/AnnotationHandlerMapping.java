package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    void initialize() throws Exception {
        logger.debug("basePackage : {}", basePackage);
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        initController(controllers);
    }

    private void initController(Set<Class<?>> controllers) throws InstantiationException, IllegalAccessException {
        for (Class<?> controller : controllers) {
            Object controllerInstance = controller.newInstance();
            Method[] methods = controller.getMethods();

            initMethod(controllerInstance, methods);
        }
    }

    private void initMethod(Object controllerInstance, Method[] methods) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                initHandler(controllerInstance, method, annotation);
            }
        }
    }

    private void initHandler(Object controllerInstance, Method method, RequestMapping annotation) {
        for (RequestMethod requestMethod : annotation.method()) {
            HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
