package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class AnnotationHandlerMapping {

    private static final Class<Controller> HANDLER_CLASS_ANNOTATION = Controller.class;
    private static final Class<RequestMapping> HANDLER_METHOD_ANNOTATION = RequestMapping.class;

    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() throws ReflectiveOperationException {
        log.debug("handler mapping initialize");
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(HANDLER_CLASS_ANNOTATION, true);
        for (Class<?> clazz : classes) {
            log.debug("handler class={}", clazz);

            Object instance = clazz.getDeclaredConstructor().newInstance();

            List<Method> handlerMethods = findHandlerMethods(clazz);
            for (Method handlerMethod : handlerMethods) {
                log.debug("handler method={}", handlerMethod);

                HandlerKey handlerKey = createHandlerKeyFrom(handlerMethod);
                handlerExecutions.put(handlerKey,
                        (request, response) -> (ModelAndView) handlerMethod.invoke(instance, request, response));
            }
        }
        log.debug("handler mapping initialization is over");
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());

        return handlerExecutions.get(new HandlerKey(requestUri, requestMethod));
    }

    private List<Method> findHandlerMethods(Class<?> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        return Arrays.stream(declaredMethods)
                .filter(method -> method.isAnnotationPresent(HANDLER_METHOD_ANNOTATION))
                .collect(Collectors.toList());
    }

    private HandlerKey createHandlerKeyFrom(Method handlerMethod) {
        RequestMapping annotation = handlerMethod.getAnnotation(HANDLER_METHOD_ANNOTATION);
        return new HandlerKey(annotation.value(), annotation.method());
    }
}
