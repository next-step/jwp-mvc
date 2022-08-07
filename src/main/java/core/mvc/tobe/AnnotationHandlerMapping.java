package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.di.factory.BeanFactory;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotationHandlerMapping {

    private final BeanFactory beanFactory;
    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(BeanFactory beanFactory, Object... basePackage) {
        this.beanFactory = beanFactory;
        this.basePackage = basePackage;
    }

    public void initialize() {
        handlerExecutions.putAll(handlerKeyExecutionsMap());
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private Map<HandlerKey, HandlerExecution> handlerKeyExecutionsMap() {
        return new Reflections(basePackage).getTypesAnnotatedWith(Controller.class)
                .stream()
                .flatMap(controllerClass -> Stream.of(controllerClass.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toMap(this::toHandlerKey, method ->
                        new HandlerExecution(beanFactory.getBean(method.getDeclaringClass()), method)));
    }

    private HandlerKey toHandlerKey(Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        return new HandlerKey(requestMapping.value(), requestMapping.method());
    }
}
