package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.Handler;
import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Set<Class<?>> controllers = ComponentScanner.scan(Controller.class, basePackage);
        handlerExecutions = controllers.stream()
                .map(this::initializeMethodsOf)
                .flatMap(map -> map.entrySet().stream())
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));

        handlerExecutions.keySet().forEach(handlerKey -> log.info(handlerKey.toString()));
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

    @Override
    public Handler getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
