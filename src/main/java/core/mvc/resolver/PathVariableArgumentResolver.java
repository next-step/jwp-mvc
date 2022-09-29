package core.mvc.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.resolver.util.ParameterUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

public class PathVariableArgumentResolver implements ArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return Arrays.stream(methodParameter.getParameterAnnotations())
                .anyMatch(annotation -> annotation.annotationType() == PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        String pattern = getPattern(methodParameter.getMethod());
        String uri = request.getRequestURI();
        String argumentName = new LocalVariableTableParameterNameDiscoverer()
                .getParameterNames(methodParameter.getMethod())[methodParameter.getParameterIndex()];

        String value = getUriVariables(pattern, uri, argumentName);

        if (Objects.isNull(value)) {
            throw new RuntimeException("PathVariable is invalid.");
        }

        return ParameterUtils.getObjectByParameterType(value, methodParameter.getParameterType());
    }

    private String getUriVariables(String pattern, String uri, String key) {
        PathPattern.PathMatchInfo pathMatchInfo = new PathPatternParser().parse(pattern).matchAndExtract(PathContainer.parsePath(uri));
        Map<String, String> uriVariables = Optional.ofNullable(pathMatchInfo.getUriVariables()).orElse(Collections.emptyMap());

        return uriVariables.get(key);
    }

    private String getPattern(Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            return annotation.value();
        }
        throw new RuntimeException("request not supported");
    }
}
