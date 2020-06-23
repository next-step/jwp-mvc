package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import core.mvc.tobe.exception.HandlerMappingInitializeFailedException;
import core.utils.ComponentScanner;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class AnnotationHandlerMapping {

    private static final Class<Controller> HANDLER_CLASS_ANNOTATION = Controller.class;
    private static final Class<RequestMapping> HANDLER_METHOD_ANNOTATION = RequestMapping.class;

    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        try {
            log.debug("handler mapping initialize");
            ComponentScanner componentScanner = new ComponentScanner(basePackage);

            Set<Class<?>> classes = componentScanner.scanClassesBy(HANDLER_CLASS_ANNOTATION);
            for (Class<?> clazz : classes) {
                log.debug("handler class={}", clazz);

                Object instance = clazz.getDeclaredConstructor().newInstance();

                List<Method> handlerMethods = componentScanner.scanMethodsBy(clazz, HANDLER_METHOD_ANNOTATION);
                for (Method handlerMethod : handlerMethods) {
                    log.debug("handler method={}", handlerMethod);

                    HandlerKey handlerKey = createHandlerKeyFrom(handlerMethod);
                    handlerExecutions.put(handlerKey,
                            (request, response) -> (ModelAndView) handlerMethod.invoke(instance, request, response));
                }
            }
            log.debug("handler mapping initialization is over");
        } catch (ReflectiveOperationException e) {
            throw new HandlerMappingInitializeFailedException();
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());

        return handlerExecutions.get(new HandlerKey(requestUri, requestMethod));
    }

    private HandlerKey createHandlerKeyFrom(Method handlerMethod) {
        RequestMapping annotation = handlerMethod.getAnnotation(HANDLER_METHOD_ANNOTATION);
        return new HandlerKey(annotation.value(), annotation.method());
    }
}
