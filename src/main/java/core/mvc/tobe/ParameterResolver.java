package core.mvc.tobe;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ParameterResolver {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final Class<?>[] parameterTypes;
    private final String[] parameterNames;
    private final Annotation[][] parameterAnnotations;
    private final Map<String, String> pathVariables;
    private final Method method;

    public ParameterResolver(HttpServletRequest request, HttpServletResponse response, Method method) {
        this.request = request;
        this.response = response;
        this.method = method;
        this.parameterTypes = method.getParameterTypes();
        this.parameterNames = ((ParameterNameDiscoverer) new LocalVariableTableParameterNameDiscoverer())
                .getParameterNames(method);
        this.parameterAnnotations = method.getParameterAnnotations();
        this.pathVariables = getPathVariables();
    }

    private Map<String, String> getPathVariables() {
        final String requestURI = request.getRequestURI();
        final PathPattern parse = parse(method.getDeclaredAnnotation(RequestMapping.class).value());
        final PathPattern.PathMatchInfo pathMatchInfo = parse.matchAndExtract(toPathContainer(requestURI));
        if (pathMatchInfo == null) {
            return new HashMap<>();
        }
        return pathMatchInfo.getUriVariables();
    }

    public Object[] resolve() {
        final Object[] parameterValues = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameterValues[i] = getParameterValue(parameterTypes[i], i);
        }
        return parameterValues;
    }

    private Object getParameterValue(Class<?> parameterType, int index) {
        if (parameterType.equals(HttpServletRequest.class)) {
            return request;
        }
        if (parameterType.equals(HttpServletResponse.class)) {
            return response;
        }
        if (isPathVariableContains(index)) {
            return getPathVariableValue(index);
        }
        return getParameterValue(index);
    }

    private PathPattern parse(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }

    private boolean isPathVariableContains(int index) {
        return Arrays.stream(parameterAnnotations[index])
                .map(Annotation::annotationType)
                .anyMatch(type -> Objects.equals(type, PathVariable.class));
    }

    private String getPathVariableValue(int index) {
        final String pathVariableValue = pathVariables.get(parameterNames[index]);
        if (pathVariableValue == null) {
            throw new IllegalStateException(String.format("URI에 '{%s}' 경로가 존재해야 합니다.", parameterNames[index]));
        }
        return pathVariableValue;
    }

    private String getParameterValue(int index) {
        final String parameterValue = request.getParameter(parameterNames[index]);
        if (parameterValue == null) {
            throw new IllegalStateException(String.format("'%s' 파라미터가 필요합니다.", parameterNames[index]));
        }
        return parameterValue;
    }
}
