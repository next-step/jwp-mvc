package core.mvc.tobe.handler.mapping;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.tobe.ControllerScanner;
import core.mvc.tobe.handler.resolver.utils.PatternMatcher;
import org.reflections.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Class<RequestMapping> ANNOTATION_CLASS_FOR_METHOD = RequestMapping.class;
    private static final List<RequestMethod> DEFAULT_REQUEST_METHODS = List.of(RequestMethod.values());

    private ControllerScanner controllerScanner;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    private final PatternMatcher patternMatcher;

    public AnnotationHandlerMapping(ControllerScanner controllerScanner, PatternMatcher patternMatcher) {
        this.controllerScanner = controllerScanner;
        this.patternMatcher = patternMatcher;
    }

    public void initialize() {
        controllerScanner.initialize();
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Class<?> aClass : controllers.keySet()) {
            addHandlerExecution(controllers.get(aClass), getHandlerMethods(aClass));
        }
    }

    private Set<Method> getHandlerMethods(Class<?> controllerClass) {
        return ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(ANNOTATION_CLASS_FOR_METHOD));
    }

    private void addHandlerExecution(Object invoker, Set<Method> methods) {
        for (Method method : methods) {
            addHandlerExecutionEachMethod(invoker, method);
        }
    }

    private void addHandlerExecutionEachMethod(Object invoker, Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(ANNOTATION_CLASS_FOR_METHOD);
        String url = requestMapping.value();
        List<RequestMethod> requestMethods = getRequestMethods(requestMapping);
        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(new HandlerKey(url, requestMethod), new HandlerExecution(invoker, method));
        }
    }

    private List<RequestMethod> getRequestMethods(RequestMapping requestMapping) {
        List<RequestMethod> methods = List.of(requestMapping.method());
        if (methods.isEmpty()) {
            return DEFAULT_REQUEST_METHODS;
        }

        return methods;
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = HandlerKey.from(request);
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);

        // 핸들러에 표현된 URI가 Path Pattern기반이 아닌 실제 Direct Path라면 바로 핸들러반환
        if (Objects.nonNull(handlerExecution)) {
            return handlerExecution;
        }

        // 핸들러에 표현된 URI가 Path Pattern 기반이라면, 실제 Request URI와 일치하지 않기 때문에, 표현식 밸리데이션을 추가로 거친다.
        return findHandlerExecutionByPathPattern(handlerKey);
    }

    private HandlerExecution findHandlerExecutionByPathPattern(HandlerKey handlerKey) {
        Set<HandlerKey> savedHandlerKeys = handlerExecutions.keySet();
        for (HandlerKey savedHandlerKey : savedHandlerKeys) {
            String urlPattern = savedHandlerKey.getUrl();
            boolean matches = patternMatcher.matches(urlPattern, handlerKey.getUrl());

            if (matches && (handlerKey.sameMethod(savedHandlerKey))) {
                return handlerExecutions.get(savedHandlerKey);
            }
        }

        return null;
    }
}
