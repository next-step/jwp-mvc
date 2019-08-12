package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;

    private ControllerScanner controllerScanner;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.controllerScanner = new ControllerScanner(basePackage);
    }

    public void initialize() {
        for (Class<?> controller : controllerScanner.getAnnotatedControllers()) {
            for (Method method : ReflectionUtils.getAllMethods(controller)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                handlerExecutions.put(createHandlerKey(requestMapping), HandlerExecution.of(controller, method));
                logger.debug("Add RequestMapping - {} {}.{}", requestMapping.method(), controller.getName(), method.getName());
            }
        }
    }

    private HandlerKey createHandlerKey(RequestMapping requestMapping) {
        String path = requestMapping.value();
        RequestMethod requestMethod = requestMapping.method();
        return new HandlerKey(path, requestMethod);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
