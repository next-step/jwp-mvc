package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private Object[] basePackage;

    private Map<HandlerKey, AnnotationHandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);

        for (final Class<?> controllerClazz : typesAnnotatedWith) {
            initMapping(controllerClazz);
        }
    }

    private void initMapping(Class<?> controller) {
        final Method[] methods = controller.getMethods();
        final List<Method> methodsWithRequestMapping = asList(methods).stream()
                .filter(method -> method.isAnnotationPresent(RequestMapping.class)).collect(toList());

        methodsWithRequestMapping.forEach(method -> getMethodConsumer(controller, method));
    }

    private void getMethodConsumer(final Class<?> controller, Method method) {
        RequestMapping declaredAnnotation = method.getDeclaredAnnotation(RequestMapping.class);
        final HandlerKey key = new HandlerKey(declaredAnnotation.value(), declaredAnnotation.method());

        handlerExecutions.put(key, new AnnotationHandlerExecution(controller, method));
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        logger.debug("handlerExecution getHandler - request: {}", request);
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
