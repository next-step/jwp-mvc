package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
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

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage
                , new TypeAnnotationsScanner()
                , new MethodAnnotationsScanner()
                , new SubTypesScanner());

        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Controller.class);
        initializeHandlerExecutions(annotatedClasses);
    }

    private void initializeHandlerExecutions(Set<Class<?>> annotatedClasses) {
        for (Class annotatedClass : annotatedClasses) {
            scanMethods(annotatedClass);
        }
    }

    private void scanMethods(Class annotatedClass) {
        Method[] methods = annotatedClass.getDeclaredMethods();
        for (Method method : methods) {
            addHandlerExecution(annotatedClass, method, RequestMapping.class);
        }
    }

    private void addHandlerExecution(Class<?> annotatedClass, Method method, Class<RequestMapping> annotationType) {
        if (!method.isAnnotationPresent(annotationType)) {
            return;
        }

        try {
            RequestMapping requestMapping = method.getAnnotation(annotationType);
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());
            handlerExecutions.put(handlerKey, new HandlerExecution(annotatedClass.newInstance(), method));

            logger.debug("Path : [{}], Controller : [{}]", requestMapping.value(), method.getDeclaringClass());

        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
