package core.mvc.tobe.argumentresolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPattern.PathMatchInfo;
import org.springframework.web.util.pattern.PathPatternParser;

public class PathVariableMethodArgumentResolver implements MethodArgumentResolver {
    private static final PathPatternParser PATH_PATTERN_PARSER = new PathPatternParser();

    @Override
    public boolean resolvable(final MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotationPresent(PathVariable.class);
    }

    @Override
    public Object resolve(final MethodParameter methodParameter, final HttpServletRequest request) {
        final RequestMapping requestMapping = methodParameter.getMethod().getAnnotation(RequestMapping.class);
        final PathMatchInfo pathMatchInfo = getPathMatchInfo(requestMapping, request.getRequestURI());
        if (Objects.isNull(pathMatchInfo)) {
            return getValueWithMatchingType(methodParameter.getParameterType(), null);
        }

        final Map<String, String> uriVariables = pathMatchInfo.getUriVariables();
        final String pathValue = methodParameter.getParameter().getAnnotation(PathVariable.class).value();

        return getPathVariableValue(methodParameter, uriVariables, pathValue);
    }

    private Object getPathVariableValue(final MethodParameter methodParameter, final Map<String, String> uriVariables, final String pathValue) {
        if (Objects.nonNull(pathValue) && !pathValue.isBlank()) {
            return getValueWithMatchingType(methodParameter.getParameterType(), uriVariables.get(pathValue));
        }
        return getValueWithMatchingType(methodParameter.getParameterType(), uriVariables.get(methodParameter.getParameterName()));
    }

    private static PathMatchInfo getPathMatchInfo(final RequestMapping requestMapping, final String requestURI) {
        final PathPattern pathPattern = PATH_PATTERN_PARSER.parse(requestMapping.value());
        return pathPattern.matchAndExtract(PathContainer.parsePath(requestURI));
    }

    private Object getValueWithMatchingType(Class<?> parameterType, String requestParameter) {
        final PrimitiveParameter primitiveParameter = PrimitiveParameter.from(parameterType);

        if (isBlank(requestParameter)) {
            return primitiveParameter.getDefaultValue();
        }
        return primitiveParameter.convert(requestParameter);
    }

    private static boolean isBlank(final String requestParameter) {
        return Objects.isNull(requestParameter) || requestParameter.isBlank();
    }
}
