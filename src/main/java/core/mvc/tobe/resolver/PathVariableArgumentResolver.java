package core.mvc.tobe.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
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

public class PathVariableArgumentResolver implements ArgumentResolver {

    private static final PathVariableArgumentResolver parameterArgumentResolver = new PathVariableArgumentResolver();

    private PathVariableArgumentResolver() {
    }

    public static PathVariableArgumentResolver getInstance() {
        return parameterArgumentResolver;
    }

    @Override
    public Object resolve(HttpServletRequest request, HttpServletResponse response, ArgumentModel argumentModel) {
        if (isPathVariableContains(argumentModel.annotations())) {
            String parameterName = argumentModel.parameterName();
            String pathVariableValue = pathVariables(request, argumentModel.method()).get(parameterName);
            Objects.requireNonNull(pathVariableValue, parameterName + "경로가 존재하지 않습니다.");
            return pathVariableValue;
        }
        return request;
    }

    @Override
    public boolean isSupport(ArgumentModel argumentModel) {
        return argumentModel.type().equals(HttpServletRequest.class);
    }

    private boolean isPathVariableContains(Annotation[] annotations) {
        return Arrays.stream(annotations)
                .map(Annotation::annotationType)
                .anyMatch(type -> Objects.equals(type, PathVariable.class));
    }

    private Map<String, String> pathVariables(HttpServletRequest request, Method method) {
        PathPattern pathPattern = pathPattern(method.getDeclaredAnnotation(RequestMapping.class).value());
        PathPattern.PathMatchInfo pathMatchInfo = pathPattern.matchAndExtract(toPathContainer(request.getRequestURI()));
        if (pathMatchInfo == null) {
            return new HashMap<>();
        }
        return pathMatchInfo.getUriVariables();
    }

    private PathPattern pathPattern(String path) {
        PathPatternParser pathPatternParser = new PathPatternParser();
        pathPatternParser.setMatchOptionalTrailingSeparator(true);
        return pathPatternParser.parse(path);
    }

    private PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
