package core.mvc.tobe;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.springframework.web.util.pattern.PathPattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameters methodParameters, HttpRequestParameters requestParameters) {
        return methodParameters.isParameterAnnotationPresent(PathVariable.class);
    }

    @Override
    public Object[] resolveArgument(MethodParameters methodParameters,
                                    HttpRequestParameters requestParameters,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {

        final RequestMapping requestMapping = (RequestMapping) methodParameters.getAnnotation(RequestMapping.class);
        final String path = requestMapping.value();
        final String uri = request.getRequestURI();
        final Map<String, String> pathVariableMap = toPathVariableMap(path, uri);

        return methodParameters.stream()
                .map(entry -> {
                    final String key = entry.getKey();
                    final MethodParameter value = entry.getValue();

                    final String pathVariable = pathVariableMap.getOrDefault(key, "");

                    return value.convertStringTo(pathVariable);
                })
                .toArray();
    }

    private Map<String, String> toPathVariableMap(final String path, final String uri) {
        final PathPattern.PathMatchInfo pathMatchInfo = PathPatternUtils.parse(path)
                .matchAndExtract(PathPatternUtils.toPathContainer(uri));

        if (pathMatchInfo == null) {
            return new HashMap<>();
        }

        return pathMatchInfo.getUriVariables();
    }
}
