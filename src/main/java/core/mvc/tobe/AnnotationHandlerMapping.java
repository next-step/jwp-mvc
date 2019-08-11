package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.tobe.support.ArgumentResolver;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static core.util.ReflectionUtils.newInstance;

public class AnnotationHandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();
    private List<ArgumentResolver> argumentResolvers;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void setArgumentResolvers(List<ArgumentResolver> argumentResolvers) {
        this.argumentResolvers = argumentResolvers;
    }

    public void initialize() {
        logger.info("## Initialized Annotation Handler Mapping");
        Reflections reflections = new Reflections(basePackage, new TypeAnnotationsScanner(), new SubTypesScanner(), new MethodAnnotationsScanner());

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            Object target = newInstance(controller);
            addHandlerExecution(target, controller.getMethods());
        }
    }

    private void addHandlerExecution(final Object target, Method[] methods) {
        Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .forEach(method -> {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());
                HandlerExecution handlerExecution = new HandlerExecution(argumentResolvers, target, method);
                handlerExecutions.put(handlerKey, handlerExecution);
                logger.info("Add - method: {}, path: {}, HandlerExecution: {}", requestMapping.method(), requestMapping.value(), method.getName());
            });
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
