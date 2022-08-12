package core.mvc.tobe.handler;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.tobe.ControllerScanner;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerKey;
import core.mvc.tobe.adapter.AnnotationHandlerAdapter;
import org.reflections.ReflectionUtils;
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
    private ControllerScanner controllerScanner;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = findHasMethodRequestMapping();
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        logger.info("AnnotationHandlerMapping getHandler");
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    public Map<HandlerKey, HandlerExecution> findHasMethodRequestMapping() {

        Class<RequestMapping> requestMappingAnnotation = RequestMapping.class;

        for (Class<?> aClass : controllerScanner.getControllers()) {
            Set<Method> allMethods = ReflectionUtils.getAllMethods(aClass, ReflectionUtils.withAnnotation(RequestMapping.class));
            mappingMethods(requestMappingAnnotation, aClass, allMethods);
        }

        return handlerExecutions;
    }

    private void mappingMethods(Class<RequestMapping> requestMappingAnnotation, Class<?> clazz, Set<Method> methods) {
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

    private void requestMappingMethodPut(Object clazz, Method method, RequestMapping rm) {
        if (Objects.nonNull(rm)) {

            handlerExecutions.put(createHandlerKey(rm), new HandlerExecution(clazz, method));
        }
    }

    private HandlerKey createHandlerKey(RequestMapping rm) {
        return new HandlerKey(rm.value(), rm.method());
    }
}
