package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.reflections.scanners.Scanners.TypesAnnotated;

public class AnnotationHandlerMapping implements HandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
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

    private void mappingMethods(Class<RequestMapping> requestMappingAnnotation, Class<?> aClass, Method[] methods) {
        for (Method method : methods) {
            requestMappingMethodPut(aClass, method, method.getAnnotation(requestMappingAnnotation));
        }
    }

    private void requestMappingMethodPut(Class<?> aClass, Method method, RequestMapping requestMapping) {
        if (Objects.nonNull(requestMapping)) {

            handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMapping.method()), new HandlerExecution(aClass, method));
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        initialize();
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
