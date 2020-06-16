package core.mvc.tobe.handler;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.handlermapping.exception.InstanceNotCreatedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class HandlerExecutions {
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public HandlerExecutions(Map<HandlerKey, HandlerExecution> handlerExecutions) {
        this.handlerExecutions = Collections.unmodifiableMap(handlerExecutions);
    }


    public static HandlerExecutions init(Class clazz) {
        Map<HandlerKey, HandlerExecution> handlers = Maps.newHashMap();

        Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .map(method -> method.getDeclaredAnnotation(RequestMapping.class))
                .map(annotation -> new HandlerKey(annotation.value(), annotation.method()))
                .forEach(handlerKey -> handlers.put(handlerKey, createHandlerExecution(clazz)));

        return new HandlerExecutions(handlers);
    }

    public HandlerExecution getValueByKey(HandlerKey handlerKey) {
        return this.handlerExecutions.get(handlerKey);
    }

    public Map<HandlerKey, HandlerExecution> getHandlerExecutions() {
        return handlerExecutions;
    }

    private static HandlerExecution createHandlerExecution(Class clazz) {
        return new HandlerExecution(getNewInstance(clazz));
    }

    private static Object getNewInstance(Class clazz) {
        try {
            return validateNull(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new InstanceNotCreatedException(e);
        }
    }

    private static Object validateNull(Object object) {
        if (Objects.isNull(object)) {
            throw new InstanceNotCreatedException();
        }

        return object;
    }
}
