package core.mvc.tobe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import lombok.extern.slf4j.Slf4j;
import next.util.StringUtils;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class ReflectionUtil {
    public static final Class REQUEST_MAPPING_ANNOTATION_CLASS = RequestMapping.class;
    public static final Class CONTROLLER_ANNOTATION_CLASS = Controller.class;

    @SuppressWarnings("unchecked")
    public static Set<Class<?>> getTypesAnnotatedWith(Reflections reflections, Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }

    @SuppressWarnings("unchecked")
    public static Set<RequestMappingMethod> getControllerMethodsWithRequestMapping(Reflections reflections) {
        Set<RequestMappingMethod> methodMap = Sets.newHashSet();
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(CONTROLLER_ANNOTATION_CLASS);

        for (Class<?> type : types) {
            String baseUri = "";
            Set<RequestMethod> baseRequestMethods = Sets.newHashSet();

            if (containsAnnotation(type, REQUEST_MAPPING_ANNOTATION_CLASS)) {
                RequestMapping annotation = (RequestMapping) getAnnotation(type, REQUEST_MAPPING_ANNOTATION_CLASS);
                baseUri = annotation.value();
                baseRequestMethods.addAll(Arrays.asList(annotation.method()));
            }

            Object instance = null;

            for(Method method : type.getMethods()) {
                if (containsAnnotation(method, REQUEST_MAPPING_ANNOTATION_CLASS)) {
                    Set<RequestMethod> requestMethods = Sets.newHashSet(baseRequestMethods);

                    RequestMapping annotation = (RequestMapping) getAnnotation(method, REQUEST_MAPPING_ANNOTATION_CLASS);
                    instance = newInstance(type, instance);
                    requestMethods.addAll(Arrays.asList(annotation.method()));

                    log.debug("methodName: {}", method.getName());
                    log.debug("uri: {}", baseUri + annotation.value());
                    log.debug("requestMethods: {}", StringUtils.toPrettyJson(requestMethods));

                    methodMap.add(new RequestMappingMethod(method, instance, baseUri + annotation.value(), requestMethods));
                }
            }
        }

        return methodMap;
    }

    public static boolean containsAnnotation(Class<?> annotatedClass, Class<? extends Annotation> annotationClass) {
        return Optional.ofNullable(annotatedClass)
            .filter(clazz -> clazz.isAnnotationPresent(annotationClass))
            .isPresent();
    }

    public static <T extends Annotation> T getAnnotation(Class<?> annotatedClass, Class<? extends Annotation> annotationClass) {
        return (T) Optional.ofNullable(annotatedClass)
            .map(clazz -> clazz.getAnnotation(annotationClass))
            .orElse(null);
    }

    public static boolean containsAnnotation(Method method, Class<? extends Annotation> annotationClass) {
        return Optional.ofNullable(method)
            .filter(m -> m.isAnnotationPresent(annotationClass))
            .isPresent();
    }

    public static <T extends Annotation> T getAnnotation(Method method, Class<? extends Annotation> annotationClass) {
        return (T) Optional.ofNullable(method)
            .map(m -> m.getAnnotation(annotationClass))
            .orElse(null);
    }

    private static Object newInstance(Class<?> declaringClass, Object instance) {
        if (Objects.isNull(instance)) {
            instance = newInstance(declaringClass);
        }
        return instance;
    }

    private static Object newInstance(Class<?> annotatedClass) {
        Object instance = null;
        try {
            instance = annotatedClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage());
        }
        return instance;
    }

}
