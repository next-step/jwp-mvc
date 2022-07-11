package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import core.mvc.tobe.resolver.HandlerMethodArgumentResolvers;
import next.support.PathPatternUtils;
import org.reflections.ReflectionUtils;
import org.springframework.http.server.PathContainer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class HandlerExecutions {

    private static final HandlerMethodArgumentResolvers handlerMethodArgumentResolvers = new HandlerMethodArgumentResolvers();
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
                    (request, response) -> {
                        Object[] arguments = handlerMethodArgumentResolvers.resolve(method, request, response);
                        return (ModelAndView) method.invoke(controllers.get(controller), arguments);
                    }));
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
        String url = handlerKey.getUrl();

        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        if (Objects.nonNull(handlerExecution)) {
            return handlerExecution;
        }

        HandlerKey pathVariableHandlerKey = getPathVariableHandlerKey(url);
        return handlerExecutions.get(pathVariableHandlerKey);
    }

    private HandlerKey getPathVariableHandlerKey(String url) {
        PathContainer pathContainer = PathPatternUtils.toPathContainer(url);
        Set<HandlerKey> handlerKeys = handlerExecutions.keySet();

        return handlerKeys.stream()
                   .filter(key -> PathPatternUtils.parse(key.getUrl()).matches(pathContainer))
                   .findAny()
                   .orElseThrow(() -> new IllegalArgumentException("지원하지 않은 URI 입니다."));
    }
}
