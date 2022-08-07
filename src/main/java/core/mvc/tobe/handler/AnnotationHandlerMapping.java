package core.mvc.tobe.handler;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerKey;
import core.mvc.tobe.adapter.AnnotationHandlerAdapter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.reflections.scanners.Scanners.TypesAnnotated;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        initialize();
    }

    public void initialize() {
        Class<Controller> controllerAnnotation = Controller.class;
        Class<RequestMapping> requestMappingAnnotation = RequestMapping.class;

        Reflections reflections = new Reflections(basePackage, TypesAnnotated);
        Set<Class<?>> controllerAnnotatedWith = reflections.getTypesAnnotatedWith(controllerAnnotation);

        for (Class<?> aClass : controllerAnnotatedWith) {
            Method[] methods = aClass.getMethods();
            mappingMethods(requestMappingAnnotation, aClass, methods);
        }
    }

    private void mappingMethods(Class<RequestMapping> requestMappingAnnotation, Class<?> clazz, Method[] methods) {
        Object instanceClazz;
        try {
            instanceClazz = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return ;
        }
        for (Method method : methods) {
            requestMappingMethodPut(instanceClazz, method, method.getAnnotation(requestMappingAnnotation));
        }
    }

    private void requestMappingMethodPut(Object clazz, Method method, RequestMapping requestMapping) {
        if (Objects.nonNull(requestMapping)) {

            handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMapping.method()), new HandlerExecution(clazz, method));
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        logger.info("AnnotationHandlerMapping getHandler");
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
