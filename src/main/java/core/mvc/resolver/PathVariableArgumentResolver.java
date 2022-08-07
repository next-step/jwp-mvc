package core.mvc.resolver;

import core.annotation.Component;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.MethodParameter;
import core.mvc.exception.NoSuchArgumentResolverException;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Component
public class PathVariableArgumentResolver implements MethodArgumentResolver{

    public static final String STRING_EMPTY = "";

    @Override
    public boolean support(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PathVariable.class);
    }

    @Override
    public Object resolve(HttpServletRequest request, HttpServletResponse response, String parameterName, MethodParameter parameter) {
        RequestMapping rm = parameter.getMethodAnnotation(RequestMapping.class);
        PathVariable ano = parameter.getParameterAnnotation(PathVariable.class);
        String path = Objects.requireNonNull(rm).value();


        if (!STRING_EMPTY.equals(Objects.requireNonNull(ano).value())) {
            parameterName = ano.value();
        }

        Map<String, String> pathValueMap = extractPathValueMap(request, path);

        if (ano.required() && !pathValueMap.containsKey(parameterName)) {
            throw new NoSuchArgumentResolverException(parameter);
        }
        String result = pathValueMap.getOrDefault(parameterName, null);

        if (Objects.isNull(result)) {
            return null;
        }

        return cast(parameter.getParameterType(), result);
    }

    private Map<String, String> extractPathValueMap(HttpServletRequest request, String path) {
        PathPattern pattern = PathPatternParser.defaultInstance.parse(path);
        PathContainer pathContainer = toPathContainer(request.getRequestURI());

        if (Objects.isNull(pathContainer)) {
            return Collections.emptyMap();
        }

        PathPattern.PathMatchInfo pathMatchInfo = pattern.matchAndExtract(pathContainer);

        if (Objects.isNull(pathMatchInfo)) {
            return Collections.emptyMap();
        }

        return pathMatchInfo.getUriVariables();
    }

    private PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
