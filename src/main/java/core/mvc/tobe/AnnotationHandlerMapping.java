package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.*;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Reflections reflections =  new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        handlerExecutions = controllers.stream()
                .map(this::initializeMethodsOf)
                .flatMap(map -> map.entrySet().stream())
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));
    }

    private Map<HandlerKey, HandlerExecution> initializeMethodsOf(Class<?> controller) {
        Object instance = createInstance(controller);
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(toMap(
                        HandlerKey::from,
                        method -> createHandlerExecution(method, instance)));
    }

    private Object createInstance(Class<?> controller) {
        try {
            return controller.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.debug("fail to initialize method of {}", controller.getSimpleName());
            throw new IllegalArgumentException();
        }
    }

    private HandlerExecution createHandlerExecution(Method method, Object instance) {
        return (request, response) -> (ModelAndView) method.invoke(instance, request, response);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
