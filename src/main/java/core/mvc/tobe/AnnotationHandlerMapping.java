package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import org.reflections.Reflections;
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
        Reflections reflections = new Reflections(basePackage);
        try {
            initializeHandlerExecution(reflections);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("AnnotationHandlerMapping initiallize Exception!! : {}", e.getMessage());
        }
    }

    private void initializeHandlerExecution(Reflections reflections)
        throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : controllerClasses) {
            Controller controllerAnnotation = controllerClass.getAnnotation(Controller.class);
            String controllerLevelPath = controllerAnnotation.value();

            requestMethodScan(controllerLevelPath, controllerClass);
        }
    }

    private void requestMethodScan(String controllerLevelPath, Class<?> controllerClass)
        throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            addHendlerExecution(controllerInstance, controllerLevelPath, method);
        }
    }

    private void addHendlerExecution(Object controllerInstance, String controllerLevelPath, Method method) {
        if(isExistAnnotaionPresent(method)) {
            RequestMapping requestMapping = getRequestMapping(method);
            addHandlerExecution(controllerInstance, controllerLevelPath, method, requestMapping);
        }
    }

    private boolean isExistAnnotaionPresent(Method method) {
        return method.isAnnotationPresent(RequestMapping.class);
    }

    private RequestMapping getRequestMapping(Method method) {
        return method.getAnnotation(RequestMapping.class);
    }

    private void addHandlerExecution(Object controllerInstance, String controllerLevelPath, Method method, RequestMapping requestMapping) {
        HandlerKey handlerKey = new HandlerKey(controllerLevelPath + requestMapping.value(), requestMapping.method());
        handlerExecutions.put(handlerKey, new HandlerExecution(controllerInstance, method));
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
