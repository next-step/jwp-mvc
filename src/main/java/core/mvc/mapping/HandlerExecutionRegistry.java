package core.mvc.mapping;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.handler.HandlerExecution;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandlerExecutionRegistry {

    private final PathPatternParser pathPatternParser = new PathPatternParser();

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();
    private final Map<PathPattern, HandlerExecution> pathPatternHandlerExecutions = new HashMap<>();

    public void add(Object controller, Method method) {
        String mappingValue = getRequestMappingValue(method);
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        if (hasPathVariable(mappingValue)) {
            putPathPattern(mappingValue, handlerExecution);
        }
        handlerExecutions.put(createHandlerKey(method), handlerExecution);
    }

    private String getRequestMappingValue(Method method) {
        return method.getAnnotation(RequestMapping.class).value();
    }

    private void putPathPattern(String mappingValue, HandlerExecution handlerExecution) {
        pathPatternHandlerExecutions.put(pathPatternParser.parse(mappingValue), handlerExecution);
    }

    private boolean hasPathVariable(String requestMappingValue) {
        int startIndex = requestMappingValue.indexOf('{');
        if (startIndex < 0) {
            return false;
        }
        return startIndex < requestMappingValue.indexOf('}');
    }

    private HandlerKey createHandlerKey(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        return new HandlerKey(requestMapping.value(), requestMapping.method());
    }

    public HandlerExecution getHandlerExecution(HttpServletRequest request) {
        HandlerKey handlerKey = createHandlerKey(request);
        if (handlerExecutions.containsKey(handlerKey)) {
            return handlerExecutions.get(handlerKey);
        }
        return getPathPatternHandlerExecution(request.getRequestURI());
    }

    private HandlerKey createHandlerKey(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return new HandlerKey(requestUri, rm);
    }

    private HandlerExecution getPathPatternHandlerExecution(String uri) {
        PathContainer pathContainer = PathContainer.parsePath(uri);
        return pathPatternHandlerExecutions.keySet().stream()
                .filter(pathPattern -> pathPattern.matches(pathContainer))
                .findFirst()
                .map(pathPatternHandlerExecutions::get)
                .orElse(null);
    }

}
