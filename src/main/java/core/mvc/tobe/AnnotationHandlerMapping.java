package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;
import org.reflections.scanners.AbstractScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final AbstractScanner[] SCANNERS = { new SubTypesScanner(), new TypeAnnotationsScanner() };

    private Object[] basePackages;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public void initialize() {
        Arrays.stream(basePackages)
                .peek(basePackage -> logger.info("Scan package [{}]", basePackage))
                .map(it -> new Reflections(it, SCANNERS))
                .flatMap(it -> it.getTypesAnnotatedWith(Controller.class).stream())
                .flatMap(controllerClass -> Arrays.stream(controllerClass.getMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(this::parseRequestMapping);
    }

    private void parseRequestMapping(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        Arrays.stream(parseRequestMethods(requestMapping))
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .forEach(handlerKey -> createHandlerExecution(handlerKey, method));
    }

    private RequestMethod[] parseRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] methods = requestMapping.method();
        if (methods.length == 0) {
            return RequestMethod.values();
        }
        return methods;
    }

    private void createHandlerExecution(HandlerKey handlerKey, Method method) {
        try {
            handlerExecutions.put(handlerKey, new HandlerExecution(method));
            logger.info("Request Mapping : {}[{}]", method.getDeclaringClass().getSimpleName(), handlerKey);
        } catch (Exception e) {
            logger.error("Failed to create HandlerExecution. class : {}, method : {}",
                    method.getDeclaringClass().getSimpleName(), method.getName(), e);
        }
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
