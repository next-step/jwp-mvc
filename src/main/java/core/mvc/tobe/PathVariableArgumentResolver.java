package core.mvc.tobe;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.IntStream;

public class PathVariableArgumentResolver implements ArgumentResolver {

    private final HttpServletRequest request;
    private final Class<?>[] parameterTypes;
    private final Map<String, String> pathVariables;
    private final Method method;
    private final Annotation[][] parameterAnnotations;
    private final String[] parameterNames;


    public PathVariableArgumentResolver(HttpServletRequest request, Method method) {
        this.request = request;
        this.method = method;
        this.parameterTypes = method.getParameterTypes();
        this.pathVariables = getPathVariables();
        this.parameterAnnotations = method.getParameterAnnotations();
        this.parameterNames = ((ParameterNameDiscoverer) new LocalVariableTableParameterNameDiscoverer())
                .getParameterNames(method);
    }

    @Override
    public Object[] resolve() {
        return IntStream.range(0, parameterTypes.length)
                .filter(this::isPathVariableContains)
                .mapToObj(this::getPathVariableValue)
                .toArray();
    }

    private boolean isPathVariableContains(int index) {
        return Arrays.stream(parameterAnnotations[index])
                .map(Annotation::annotationType)
                .anyMatch(type -> Objects.equals(type, PathVariable.class));
    }

    private String getPathVariableValue(int index) {
        String parameterName = parameterNames[index];
        String pathVariableValue = pathVariables.get(parameterName);
        Objects.requireNonNull(pathVariableValue, parameterName + "경로가 존재하지 않습니다.");
        return pathVariableValue;
    }

    private Map<String, String> getPathVariables() {
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
