package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

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
        Reflections reflections = new Reflections("core.mvc",
                new TypeAnnotationsScanner(),
                new SubTypesScanner());

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class clazz : controllers) {
            Method[] methods = clazz.getMethods();
            Arrays.stream(methods)
                    .filter(method -> method.getDeclaredAnnotation(RequestMapping.class) != null)
                    .map(method -> method.getDeclaredAnnotation(RequestMapping.class))
                    .map(annotation -> new HandlerKey(annotation.value(), annotation.method()))
                    .forEach(handlerKey -> handlerExecutions.put(handlerKey, new HandlerExecution(getNewInstance(clazz))));
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, method));
    }

    private Object getNewInstance(Class clazz) {
        Object controller = null;

        try {
            controller = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return controller;
    }
}
