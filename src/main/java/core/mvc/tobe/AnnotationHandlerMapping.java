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
import java.util.ArrayList;
import java.util.Arrays;
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
        @SuppressWarnings("unchecked")
        Set<Method> methods = getAllMethods(controllerType, withAnnotation(RequestMapping.class));

        return methods.stream()
                .flatMap(method-> {
                    List<Map.Entry<HandlerKey, HandlerExecution>> list = createHandlerEntry(controllerType, method);
                    return list.stream();
                }).collect(Collectors.toList());
    }

    private List<Map.Entry<HandlerKey, HandlerExecution>> createHandlerEntry(Class<?> controllerType, Method method) {
        Controller cAnno = controllerType.getDeclaredAnnotation(Controller.class);
        RequestMapping rmAnno = method.getAnnotation(RequestMapping.class);
        HandlerExecution handlerExecution = new HandlerExecution(beanFactory.getBean(controllerType), method);

        RequestMethod[] requestMethods = rmAnno.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        return Arrays.stream(requestMethods).map(m -> {
            HandlerKey handlerKey = new HandlerKey(cAnno.path() + rmAnno.value(), m);
            return Map.entry(handlerKey, handlerExecution);
        }).collect(Collectors.toList());
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());

        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
