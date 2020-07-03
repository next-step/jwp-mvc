package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.scanner.WebApplicationScanner;
import core.mvc.tobe.exception.HandlerMappingInitializeFailedException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static core.mvc.utils.UriPathPatternParser.match;

@Slf4j
public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Class<Controller> HANDLER_CLASS_ANNOTATION = Controller.class;
    private static final Class<RequestMapping> HANDLER_METHOD_ANNOTATION = RequestMapping.class;

    private WebApplicationScanner webApplicationScanner;
    private Map<HandlerKey, HandlerMethod> handlerMethods = Maps.newHashMap();

    public AnnotationHandlerMapping(WebApplicationScanner webApplicationScanner) {
        this.webApplicationScanner = webApplicationScanner;
    }

    @Override
    public void initialize() {
        try {
            log.debug("handler mapping initialize.");

            Set<Class<?>> handlerClasses = webApplicationScanner.scanClassesBy(HANDLER_CLASS_ANNOTATION);
            for (Class<?> clazz : handlerClasses) {
                log.debug("handler class={}", clazz);

                Object instance = clazz.getDeclaredConstructor().newInstance();

                List<Method> methods = webApplicationScanner.scanMethodsBy(clazz, HANDLER_METHOD_ANNOTATION);
                for (Method method : methods) {
                    log.debug("handler method={}", method);

                    HandlerKey handlerKey = createHandlerKeyFrom(method);
                    HandlerMethod handlerMethod = new HandlerMethod(method, handlerKey.getRequestMappingUri(), instance);
                    this.handlerMethods.put(handlerKey, handlerMethod);
                }
            }
            log.debug("handler mapping initialization is over.");
        } catch (ReflectiveOperationException e) {
            throw new HandlerMappingInitializeFailedException();
        }
    }

    @Override
    public boolean supports(HttpServletRequest request) {
        HandlerKey handlerKey = createHandlerKeyFrom(request);

        return handlerMethods.containsKey(handlerKey);
    }

    @Override
    public HandlerMethod getHandlerMethod(HttpServletRequest request) {
        HandlerKey handlerKey = createHandlerKeyFrom(request);

        return handlerMethods.get(handlerKey);
    }

    private HandlerKey createHandlerKeyFrom(Method handlerMethod) {
        RequestMapping annotation = handlerMethod.getAnnotation(HANDLER_METHOD_ANNOTATION);
        return new HandlerKey(annotation.value(), annotation.method());
    }

    private HandlerKey createHandlerKeyFrom(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());

        return this.handlerMethods.keySet().stream()
                .filter(key -> match(key.getRequestMappingUri(), requestUri))
                .findFirst()
                .orElseGet(() -> new HandlerKey(requestUri, requestMethod));
    }
}
