package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.exception.CoreException;
import core.exception.CoreExceptionStatus;
import lombok.Getter;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

@Getter
public class AnnotationHandlerMapping {
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
                findMethod(clazz);
            }
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private void findMethod(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);

            if (isRequestMapping) {
                putHandlerExecution(clazz, method);
            }
        }
    }

    private void putHandlerExecution(Class<?> clazz, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());

        try {
            HandlerExecution handlerExecution = new HandlerExecution(clazz.newInstance(), method);
            handlerExecutions.put(handlerKey, handlerExecution);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new CoreException(CoreExceptionStatus.CLASS_NEW_INSTANCE_FAIL, e);
        }
    }
}
