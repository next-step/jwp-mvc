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

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        try {
            initializeHandlerExecution(reflections);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("AnnotationHandlerMapping initiallize Exception!! : {}", e.getMessage());
        }
    }

    private void initializeHandlerExecution(Reflections reflections) throws InstantiationException, IllegalAccessException {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : controllerClasses) {
            Controller controllerAnnotation = controllerClass.getAnnotation(Controller.class);
            String controllerLevelPath = controllerAnnotation.value();

            requestMethodScan(controllerLevelPath, controllerClass);
        }
    }

    private void requestMethodScan(String controllerLevelPath, Class<?> controllerClass) throws InstantiationException, IllegalAccessException {
        Object controllerInstance = controllerClass.newInstance();
        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            addHendlerExecution(controllerInstance, controllerLevelPath, method);
        }
    }

    private void addHendlerExecution(Object controllerInstance, String controllerLevelPath, Method method) {
        if(method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String path = requestMapping.value();
            RequestMethod requestMethod = requestMapping.method();

            HandlerKey handlerKey = new HandlerKey(controllerLevelPath + path, requestMethod);
            logger.info("Add RequestMapping URL : {}, method : {}", handlerKey, method);
            handlerExecutions.put(handlerKey, new HandlerExecution(controllerInstance, method));
        }
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
