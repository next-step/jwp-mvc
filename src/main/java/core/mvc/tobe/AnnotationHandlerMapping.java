package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.asis.DispatcherServlet;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotatedControllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> annotatedController : annotatedControllers) {
            try {
                Object controller = annotatedController.getDeclaredConstructor().newInstance();
                Arrays.stream(annotatedController.getDeclaredMethods())
                        .forEach(method -> addHandlerExecutionIsAnnotationPresent(controller, method));
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                logger.error("Annotation Controller Find Error : {}", e.toString());
            }
        }
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(),
                RequestMethod.requestMethod(request.getMethod())));
    }

    private void addHandlerExecutionIsAnnotationPresent(Object controller, Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            handlerExecutions.put(new HandlerKey(mapping.value(), mapping.method()), new HandlerExecution(controller, method));
        }
    }
}

