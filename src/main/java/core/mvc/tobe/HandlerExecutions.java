package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HandlerExecutions {

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public HandlerExecutions(Map<HandlerKey, HandlerExecution> handlerExecutions) {
        this.handlerExecutions = handlerExecutions;
    }

    public static HandlerExecutions of(Map<Class, Object> controllers) {
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

        for (Class<?> controller : controllers.keySet()) {
            Set<Method> methods = ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class));
            initHandlers(controllers, handlerExecutions, controller, methods);
        }

        return new HandlerExecutions(handlerExecutions);
    }

    private static void initHandlers(Map<Class, Object> controllers, Map<HandlerKey, HandlerExecution> handlerExecutions, Class<?> controller, Set<Method> methods) {
        methods.forEach(method -> {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            List<HandlerKey> handlerKeys = initHandlerKeys(requestMapping);

            handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey,
                    (request, response) -> (ModelAndView) method.invoke(controllers.get(controller), request, response)));
        });
    }

    private static List<HandlerKey> initHandlerKeys(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            return HandlerKey.allMethodsKey(requestMapping.value());
        }
        
        return HandlerKey.listOf(requestMapping.value(), requestMethods);
    }

    public HandlerExecution get(HandlerKey handlerKey) {
        return handlerExecutions.get(handlerKey);
    }
}
