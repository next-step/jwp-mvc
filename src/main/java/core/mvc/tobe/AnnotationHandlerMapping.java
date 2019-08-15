package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;

    private ControllerScanner controllerScanner;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.controllerScanner = new ControllerScanner(basePackage);
    }

    public void initialize() {
        for (Class<?> controller : controllerScanner.getAnnotatedControllers()) {
            for (Method method : ReflectionUtils.getAllMethods(controller, RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                createHandlerKeys(requestMapping)
                        .stream()
                        .forEach(handlerKey -> handlerExecutions.put(handlerKey, HandlerExecution.of(controller, method)));

                logger.debug("Add RequestMapping - {} {}.{}", requestMapping.method(), controller.getName(), method.getName());
            }
        }
    }

    private Set<HandlerKey> createHandlerKeys(RequestMapping requestMapping) {
        String path = requestMapping.value();
        RequestMethod[] requestMethods = getRequestMethods(requestMapping);

        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(path, requestMethod))
                .collect(Collectors.toSet());
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMapping.method();
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
