package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {
    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();
    private Reflections reflections;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.reflections = new Reflections(this.basePackage);
    }

    public void initialize() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        controllers.forEach(aClass -> {
            Method[] methods = getMethodsWithAnnotation(aClass, RequestMapping.class);
            Arrays.stream(methods)
                    .forEach(method -> {
                        RequestMapping requestMapping = getAnnotationInMethod(method, RequestMapping.class);
                        HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());
                        handlerExecutions.put(handlerKey, new HandlerExecution());
                    });
        });
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private <T extends Annotation> T getAnnotationInMethod(Method method, Class<T> annotation) {
        return method.getAnnotation(annotation);
    }

    private <T extends Annotation> Method[] getMethodsWithAnnotation(Class<?> clazz, Class<T> annotation) {
        Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(annotation))
                .toArray(Method[]::new);
    }
}
