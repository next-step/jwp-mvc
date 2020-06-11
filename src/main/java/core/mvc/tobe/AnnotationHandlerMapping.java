package core.mvc.tobe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    private Reflections reflections;
    private static final Class REQUEST_MAPPING_ANNOTATION = RequestMapping.class;
    private static final Class CONTROLLER_ANNOTATION = Controller.class;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @SuppressWarnings("unchecked")
    public void initialize() {
        this.reflections = new Reflections(basePackage);
        initHandlerExecutions();
    }

    private void initHandlerExecutions() {
        Map<Method, Object> methods = ReflectionUtil.getControllerRequestMappingMethods(reflections);
/*
        for(Map.Entry<Method, Object> methodEntry : methods.entrySet()) {
            addHandlerExecutions(annotatedClass, methodEntry.getValue(), methodEntry.getKey(), handlerKey);
        }*/
    }

    private boolean isRequestMappingAnnotationPresent(Class<?> annotatedClass) {
        return annotatedClass.isAnnotationPresent(REQUEST_MAPPING_ANNOTATION);
    }

    private RequestMapping getRequestMappingAnnotation(Class<?> annotatedClass) {
        return (RequestMapping) annotatedClass.getAnnotation(REQUEST_MAPPING_ANNOTATION);
    }

    private Object getInstance(Class<?> annotateClass) {
        Object instance = null;
        try {
            instance = annotateClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage());
        }
        return instance;
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    //@SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }
/*
    private void addHandlerExecutions(Class<?> annotatedClass, Object instance, Method method, HandlerKey handlerKey) {
        handlerExecutions.put(
            getOverriddenHandlerKey(handlerKey, annotation),
            getHandlerExecution(annotatedClass, instance, method)
        );
    }*/

    private HandlerKey getOverriddenHandlerKey(HandlerKey handlerKey, RequestMapping annotation) {
        String requestUri = getBaseRequestUri(handlerKey) + annotation.value();
        return new HandlerKey(requestUri, annotation.method());
    }

    private String getBaseRequestUri(HandlerKey handlerKey) {
        if (Objects.isNull(handlerKey)) {
            return "";
        }

        return handlerKey.getUrl();
    }

    private HandlerExecution getHandlerExecution(Class<?> annotatedClass, Object instance, Method method) {
        if (Objects.isNull(instance)) {
            instance = getInstance(annotatedClass);
        }

        return new HandlerExecution(instance, method);
    }
}
