package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.RequestHandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AnnotationHandlerMapping implements RequestHandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
    private final List<HandlerKey> handlers = new ArrayList<>();

    public AnnotationHandlerMapping(final Object... basePackage) {
        validate(basePackage);

        this.basePackage = basePackage;
    }

    private void validate(final Object[] basePackage) {
        if (Objects.isNull(basePackage) || basePackage.length == 0) {
            throw new IllegalArgumentException("BasePackage can't be empty");
        }
    }

    public void initialize() {
        Set<Class<?>> controllers = AnnotatedTargetScanner.loadClasses(Controller.class, basePackage);
        controllers.forEach(this::convertClassToHandlerExecution);

        handlers.addAll(handlerExecutions.keySet());
        Collections.sort(handlers);

        logger.info("Initialized Request Mapping!");
        handlers.forEach(handler -> logger.info("Url : {}, Method : {}", handler.getUrl(), handler.getRequestMethod()));
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        return handlers.stream()
                .filter(handlerKey -> handlerKey.isSupport(request)) // url check
                .filter(handlerKey -> handlerExecutions.get(handlerKey).isSupport(request)) // parameter check
                .map(handlerExecutions::get)
                .findFirst()
                .orElse(null);
    }

    private void convertClassToHandlerExecution(final Class<?> clazz) {
        Object instance = newInstance(clazz);

        AnnotatedTargetScanner.loadMethodsFromClass(clazz, RequestMapping.class)
                .forEach(method -> putHandlerExecution(method, instance));
    }

    private void putHandlerExecution(final Method method, final Object instance) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        getRequestMethods(requestMapping)
                .forEach(requestMethod ->
                        {
                            HandlerExecution handlerExecution = new HandlerExecution(method, instance);
                            HandlerKey key = new HandlerKey(
                                    requestMapping.value(),
                                    requestMethod,
                                    handlerExecution.getParameters()
                            );

                            handlerExecutions.put(key, handlerExecution);
                        }
                );
    }

    private List<RequestMethod> getRequestMethods(final RequestMapping requestMapping) {
        RequestMethod[] method = requestMapping.method();

        if (method.length != 0) {
            return Arrays.asList(method);
        }

        return Arrays.asList(RequestMethod.values());
    }

    private Object newInstance(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor()
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Fail to create new instance of " + clazz.getName());
        }
    }

    public int getNumOfHandler() {
        return handlers.size();
    }
}
