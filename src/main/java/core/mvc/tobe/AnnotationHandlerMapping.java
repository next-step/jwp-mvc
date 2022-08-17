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

public class AnnotationHandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;

    private Map<String, HandlerAdapter> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(this.basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        Set<Class<?>> classes = controllers.keySet();
        for (Class<?> clazz : classes) {
            String controllerPath = clazz.getAnnotation(Controller.class).value();

            Set<Method> requestMappingMethods
                = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));
            for (Method method : requestMappingMethods) {
                HandlerKey handlerKey = getHandlerKey(controllerPath, method);
                handlerExecutions.put(handlerKey.toString(), new HandlerAdapter(controllers.get(clazz), method));
            }
        }
    }

    private HandlerKey getHandlerKey(String controllerPath, Method method) {
        HandlerKey handlerKey = createHandlerKey(controllerPath, method.getAnnotation(RequestMapping.class));
        logger.info(handlerKey.toString());
        if (handlerExecutions.containsKey(handlerKey.toString())) {
            throw new IllegalArgumentException("중복된 핸들러어댑터키");
        }
        return handlerKey;
    }

    public HandlerAdapter getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, requestMethod).toString());
    }

    private HandlerKey createHandlerKey(String controllerPath, RequestMapping requestMapping) {
        return new HandlerKey(controllerPath + requestMapping.value(), requestMapping.method());
    }

}
