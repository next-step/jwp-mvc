package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.di.factory.BeanFactory;
import core.mvc.HandlerMapping;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.util.ReflectionUtilsPredicates.withAnnotation;

public class AnnotationHandlerMapping implements HandlerMapping {
    private BeanFactory beanFactory;
    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections(this.basePackage);
        Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);

        this.beanFactory = new BeanFactory(controllerTypes);
        beanFactory.initialize();

        this.handlerExecutions.putAll(getHandlerExecutions(controllerTypes));
    }

    private Map<HandlerKey, HandlerExecution> getHandlerExecutions(Set<Class<?>> controllerTypes) {
        return controllerTypes.stream()
                .map(this::createHandlerExecution)
                .flatMap(List::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<Map.Entry<HandlerKey, HandlerExecution>> createHandlerExecution(Class<?> controllerType) {
        Controller controllerAnno = controllerType.getDeclaredAnnotation(Controller.class);
        String controllerPath = controllerAnno.path();

        @SuppressWarnings("unchecked")
        Set<Method> methods = getAllMethods(controllerType, withAnnotation(RequestMapping.class));

        if (methods.isEmpty()) {
            return Collections.emptyList();
        }

        return methods.stream()
                .map(method-> {
                    RequestMapping rm = method.getAnnotation(RequestMapping.class);
                    HandlerKey handlerKey = new HandlerKey(controllerPath + rm.value(), rm.method());
                    HandlerExecution handlerExecution = new HandlerExecution(beanFactory.getBean(controllerType), method);

                    return Map.entry(handlerKey, handlerExecution);
                }).collect(Collectors.toList());
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        HandlerExecution handlerExecution = handlerExecutions.get(new HandlerKey(requestUri, rm));

        if (Objects.isNull(handlerExecution)) {
            return handlerExecutions.get(new HandlerKey(requestUri, RequestMethod.NONE));
        }
        return handlerExecution;
    }
}
