package core.mvc.tobe;

import com.google.common.collect.Maps;
import com.google.common.reflect.Reflection;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        new Reflections(this.basePackage)
            .getTypesAnnotatedWith(Controller.class)
            .forEach(this::setHandlerExecution);
    }

    private void setHandlerExecution(Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getAnnotation(RequestMapping.class) != null)
                .forEach(method -> {
                    putHandlerExecutions(clazz, method);
                });
    }

    private void putHandlerExecutions(Class<?> clazz, Method method) {
        try {
            handlerExecutions.put(createHandlerKey(method), createHandlerExecution(clazz, method));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private HandlerExecution createHandlerExecution(Class<?> clazz, Method method) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return new HandlerExecution(clazz.getDeclaredConstructor().newInstance(), method);
    }

    private HandlerKey createHandlerKey(Method method) {
        final RequestMapping annotation = method.getAnnotation(RequestMapping.class);

        return new HandlerKey(annotation.value(), annotation.method());
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
