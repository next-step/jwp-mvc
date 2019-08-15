package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Arrays.stream(basePackage)
                .peek(it -> logger.info("Scan package [{}]", it))
                .map(it -> new Reflections(it, new SubTypesScanner(), new TypeAnnotationsScanner()))
                .flatMap(it -> it.getTypesAnnotatedWith(Controller.class).stream())
                .flatMap(it -> Arrays.stream(it.getMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> createHandlerExecution(method));
    }

    private void createHandlerExecution(Method method) {
        Class<?> controllerClass = method.getDeclaringClass();
        try {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String url = requestMapping.value();
            RequestMethod requestMethod = requestMapping.method();
            logger.info("Request mapping : {} [value = {}, method = {}]", controllerClass.getSimpleName(), url, requestMethod);
            handlerExecutions.put(new HandlerKey(url, requestMethod), new HandlerExecution(method));
        } catch (Exception e) {
            logger.error("Failed to create HandlerExecution. class : {}, method : {}",
                    controllerClass.getSimpleName(), method.getName(), e);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
