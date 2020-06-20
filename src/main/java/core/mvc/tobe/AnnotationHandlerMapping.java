package core.mvc.tobe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {
    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @SuppressWarnings("unchecked")
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> preInstantiateClazz = getTypesAnnotatedWith(reflections, Controller.class);
        for (Class<?> instantiateClazz : preInstantiateClazz) {
            List<Method> methods = findRequestMappingMethods(instantiateClazz);
            putHandlerExecutions(instantiateClazz, methods);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private List<Method> findRequestMappingMethods(Class<?> instantiateClazz) {
        return Arrays.stream(instantiateClazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void putHandlerExecutions(Class<?> instantiateClazz, List<Method> methods) {
        for (Method declaredMethod : methods) {
            RequestMapping annotation = declaredMethod.getAnnotation(RequestMapping.class);
            putHandlerExecution(instantiateClazz, declaredMethod, annotation);
        }
    }

    private void putHandlerExecution(Class<?> instantiateClazz, Method declaredMethod, RequestMapping annotation) {
        List<RequestMethod> requestMethods = getRequestMethods(annotation);
        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(
                    new HandlerKey(annotation.value(), requestMethod),
                    new HandlerExecution(instantiateClazz, declaredMethod)
            );
        }
    }

    private List<RequestMethod> getRequestMethods(RequestMapping annotation) {
        if (annotation.method().length == 0) {
            return Arrays.asList(RequestMethod.values());
        }
        return Arrays.asList(annotation.method());
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Reflections reflections, Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation, true));
        }
        return beans;
    }
}
