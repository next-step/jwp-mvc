package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import org.apache.commons.lang3.ArrayUtils;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> clazz : classes) {
            Method[] declaredMethods = clazz.getDeclaredMethods();

            Arrays.stream(declaredMethods)
                    .filter(it -> it.isAnnotationPresent(RequestMapping.class))
                    .forEach(it -> makeHandlerExecution(clazz, it));
        }
    }

    private void makeHandlerExecution(Class<?> clazz, Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = getRequestMappingMethods(annotation);

        Arrays.stream(requestMethods).forEach(requestMethod -> {
            HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);

            handlerExecutions.put(handlerKey,
                    (request, response) -> (ModelAndView) method.invoke(clazz.newInstance(), request, response));
        });
    }

    private RequestMethod[] getRequestMappingMethods(RequestMapping annotation) {
        RequestMethod[] methods = annotation.method();

        if (ArrayUtils.isEmpty(methods)) {
            return RequestMethod.values();
        }

        return methods;
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
