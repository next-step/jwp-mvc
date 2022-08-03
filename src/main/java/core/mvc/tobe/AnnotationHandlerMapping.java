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
import java.util.Set;

import static org.reflections.scanners.Scanners.TypesAnnotated;

public class AnnotationHandlerMapping {
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
            for (Method method : methods) {
                RequestMapping requestMapping = method.getAnnotation(requestMappingAnnotation);

                handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMapping.method()), new HandlerExecution(aClass, method));
            }
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
