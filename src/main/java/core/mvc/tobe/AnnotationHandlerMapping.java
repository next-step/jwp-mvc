package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMethod;
import core.mvc.tobe.util.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

@Slf4j
public class AnnotationHandlerMapping {
    private final Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    private Reflections reflections;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @SuppressWarnings("unchecked")
    public void initialize() {
        this.reflections = new Reflections(basePackage);
        initializeHandlerExecutions();
    }

    private void initializeHandlerExecutions() {
        Set<RequestMappingMethod> methods = ReflectionUtils.getControllerMethodsWithRequestMapping(reflections);

        for(RequestMappingMethod method : methods) {
            for (RequestMethod requestMethod : method.getRequestMethods()) {
                handlerExecutions.put(
                    new HandlerKey(method.getUrl(), requestMethod),
                    new HandlerExecution(method.getInstance(), method.getMethod())
                );
            }
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        HandlerKey handlerKey = new HandlerKey(requestUri, rm);
        return handlerExecutions.getOrDefault(handlerKey, getPathPatternMatchedHandler(handlerKey));
    }

    private HandlerExecution getPathPatternMatchedHandler(HandlerKey handlerKey) {
        return handlerExecutions.entrySet()
                .stream()
                .filter(entry -> entry.getKey().matchesPathPattern(handlerKey))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }
}
