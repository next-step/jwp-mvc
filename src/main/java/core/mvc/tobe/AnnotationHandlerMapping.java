package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.exception.CoreException;
import core.exception.CoreExceptionStatus;
import core.mvc.HandlerMapping;
import lombok.Getter;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

@Getter
public class AnnotationHandlerMapping implements HandlerMapping {
    private String[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(String... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        for (String basePackage : basePackage) {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class, true);
            for (Class<?> clazz : classes) {
                putHandlerExecution(clazz);
            }
        }
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private void putHandlerExecution(Class<?> clazz) {
        Object instance = getInstance(clazz);
        for (Method method : clazz.getDeclaredMethods()) {
            boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
            if (isRequestMapping) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());

                HandlerExecution handlerExecution = new HandlerExecution(instance, method);
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    private Object getInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new CoreException(CoreExceptionStatus.CLASS_NEW_INSTANCE_FAIL, e);
        }
    }
}
