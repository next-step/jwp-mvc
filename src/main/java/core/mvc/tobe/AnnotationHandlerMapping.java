package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.IncompleteAnnotationException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class, true);

        try {
            for (Class<?> controllerClass : controllers) {
                Constructor constructor = controllerClass.getDeclaredConstructor();
                Object controllerInstance = constructor.newInstance();
                Set<Method> handlers = getAllMethods(controllerClass, withAnnotation(RequestMapping.class));

                for (Method handler : handlers) {
                    RequestMapping requestMapping = handler.getAnnotation(RequestMapping.class);
                    final String path = requestMapping.value();


                    List<RequestMethod> httpMethods = new ArrayList<>();

                    try {
                        RequestMethod httpMethod = requestMapping.method();
                        httpMethods.add(httpMethod);
                    } catch (IncompleteAnnotationException e) {
                        httpMethods.addAll(Arrays.asList(RequestMethod.values()));
                    }

                    final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, handler);

                    for (RequestMethod httpMethod : httpMethods) {
                        final HandlerKey handlerKey = new HandlerKey(path, httpMethod);
                        if (httpMethods.size() > 1 && handlerExecutions.get(handlerKey) != null) {
                            continue;
                        }
                        handlerExecutions.put(handlerKey, handlerExecution);
                    }
                }
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored) {
            ignored.printStackTrace();
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
