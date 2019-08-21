package core.mvc.tobe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {
    private final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private ControllerScanner controllerScanner;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
    }

    @Override
    public void init() {
        Map<Class<?>, Object> controller = controllerScanner.controllerScan();
        Set<Class<?>> classes = controller.keySet();

        for(Class clazz : classes) {
            Set<Method> allMethods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));
            setHandlerExecutions(controller.get(clazz), allMethods);
        }
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }


    private void setHandlerExecutions(Object clazz, Set<Method> allMethods) {
        for(Method method : allMethods) {
            RequestMapping rm = getAnnotation(method, RequestMapping.class);
            Set<RequestMethod> requestMethods = setRequestMappingMethod(rm);

            requestMethods.forEach(requestMethod ->
                handlerExecutions.put(createHandlerKey(rm.value(), requestMethod), createHandlerExecution(clazz, method)));
        }
    }

    private Set<RequestMethod> setRequestMappingMethod(RequestMapping rm) {
        RequestMethod[] method = rm.method();
        if(method.length == 0) {
            return Sets.newHashSet(RequestMethod.values());
        }

        return Sets.newHashSet(method);
    }

    private <T extends Annotation> T getAnnotation(Method method, Class<T> annotation) {
        return method.getAnnotation(annotation);
    }

    private HandlerKey createHandlerKey(String value, RequestMethod method) {
        return new HandlerKey(value, method);
    }

    private HandlerExecution createHandlerExecution(Object clazz, Method method) {
        return new HandlerExecution(clazz, method);
    }
}
