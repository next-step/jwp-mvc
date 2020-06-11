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
import java.util.Set;

@Slf4j
public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    private Reflections reflections;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @SuppressWarnings("unchecked")
    public void initialize() {
        reflections = new Reflections(basePackage);
        Set<Class<?>> annotatedClasses = getTypesAnnotatedWith(Controller.class);

        for(Class annotateClass : annotatedClasses) {
            Object instance = null;
            try {
                instance = annotateClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                log.error(e.getMessage());
            }

            log.debug("className: {}", annotateClass.getSimpleName());

            boolean isRequestMappingPresent = annotateClass.isAnnotationPresent(RequestMapping.class);
            log.debug("isClassRequestMappingPresent: {}", isRequestMappingPresent);

            String requestUri = "";

            if (isRequestMappingPresent) {
                RequestMapping annotation = (RequestMapping) annotateClass.getAnnotation(RequestMapping.class);
                requestUri = annotation.value();
            }

            Method[] methods = annotateClass.getMethods();

            for (Method method : methods) {
                boolean isMethodRequestMappingPresent = method.isAnnotationPresent(RequestMapping.class);

                if (isMethodRequestMappingPresent) {
                    log.debug("methodName: {}", method.getName());
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    requestUri = annotation.value();
                    RequestMethod requestMethod = annotation.method();

                    handlerExecutions.put(
                        new HandlerKey(requestUri, requestMethod),
                        new HandlerExecution(instance, method)
                    );
                }
            }
        }
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
}
