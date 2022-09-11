package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
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
                        .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                        .forEach(method -> addHandlerExecution(controller, method));
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                logger.error("Annotation Controller Find Error : {}", e.toString());
            }
        }
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.requestMethod(request.getMethod());
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), requestMethod));
    }

    private void addHandlerExecution(Object controller, Method method) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        if (mapping.method().equals(RequestMethod.ALL)) {
            Arrays.stream(RequestMethod.values())
                    .forEach(value -> handlerExecutions.put(new HandlerKey(mapping.value(), value), new HandlerExecution(controller, method)));
            return;
        }

        handlerExecutions.put(new HandlerKey(mapping.value(), mapping.method()), new HandlerExecution(controller, method));
    }
}

