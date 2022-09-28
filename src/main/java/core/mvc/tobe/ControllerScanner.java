package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.resolver.*;
import org.apache.commons.lang3.ArrayUtils;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class ControllerScanner {
    private static final List<ArgumentResolver> argumentResolvers = asList(
            new HttpRequestArgumentResolver(),
            new HttpResponseArgumentResolver(),
            new RequestParamArgumentResolver(),
            new PathVariableArgumentResolver(),
            new RequestBodyArgumentResolver(),
            new SimpleTypeArgumentResolver()
    );

    private static final Class<?>[] EMPTY_CLASS_ARRAY = {};
    private final Map<Class, Object> controllers = Maps.newHashMap();
    private Map<HandlerKey, HandlerExecution> result = Maps.newHashMap();


    public ControllerScanner(Object... basePackage) {
        Set<Class<?>> controllerClazz = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
        controllerClazz.forEach(clazz -> this.controllers.put(clazz, newInstance(clazz)));
        initialize();
    }

    private void initialize() {
        this.controllers.values().forEach(clazz -> {
            Set<Method> methods = ReflectionUtils.getAllMethods(clazz.getClass(), ReflectionUtils.withAnnotation(RequestMapping.class));
            methods.forEach(method -> addHandlerExecution(clazz, method));
        });
    }

    private void addHandlerExecution(Object object, Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        List<HandlerKey> handlerKeys = getHandlerKeys(requestMapping, object);
        handlerKeys.forEach(handlerKey -> result.put(handlerKey, new HandlerExecution(argumentResolvers, object, method)));
    }

    private List<HandlerKey> getHandlerKeys(RequestMapping requestMapping, Object object) {
        Controller controller = object.getClass().getDeclaredAnnotation(Controller.class);
        RequestMethod[] requestMethods = requestMapping.method();

        if (ArrayUtils.isEmpty(requestMethods)) {
            requestMethods = RequestMethod.values();
        }

        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(controller.value() + requestMapping.value(), requestMethod))
                .collect(Collectors.toList());
    }

    private Object newInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor(EMPTY_CLASS_ARRAY).newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Map<? extends HandlerKey, ? extends HandlerExecution> getHandlerExecutions() {
        return this.result;
    }
}
