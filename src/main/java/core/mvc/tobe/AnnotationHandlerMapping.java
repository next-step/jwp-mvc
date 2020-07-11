package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class AnnotationHandlerMapping implements HandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, AnnotationController> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage, TypeAnnotationsScanner.class);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        controllers.stream()
                .forEach(this::initMapping);
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
        handlerExecutions.put(key, new AnnotationController(controller, method));
    }

    @Override
    public core.mvc.Controller getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
