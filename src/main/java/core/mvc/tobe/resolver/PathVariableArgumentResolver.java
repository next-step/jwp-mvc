package core.mvc.tobe.resolver;

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
import java.util.*;

public class PathVariableArgumentResolver implements ArgumentResolver {

    private static final PathVariableArgumentResolver parameterArgumentResolver = new PathVariableArgumentResolver();

    private PathVariableArgumentResolver() {}

    public static PathVariableArgumentResolver getInstance() {
        return parameterArgumentResolver;
    }

    @Override
    public Object[] resolve(HttpServletRequest request, HttpServletResponse response, Method method) {
        List<String> list = new ArrayList<>();
        String[] parameterNames = ((ParameterNameDiscoverer) new LocalVariableTableParameterNameDiscoverer()).getParameterNames(method);

        for (int i = 0; i < method.getParameterTypes().length; i++) {
            Annotation[] annotationArray = method.getParameterAnnotations()[i];
            if (isPathVariableContains(annotationArray)) {
                String pathVariableValue = getPathVariableValue(request, method, Objects.requireNonNull(parameterNames)[i]);
                list.add(pathVariableValue);
            }
        }
        return list.toArray();
    }

    private boolean isPathVariableContains(Annotation[] annotationArray) {
        return Arrays.stream(annotationArray)
                .map(Annotation::annotationType)
                .anyMatch(type -> Objects.equals(type, PathVariable.class));
    }

    private String getPathVariableValue(HttpServletRequest request, Method method, String parameterName) {
        String pathVariableValue = pathVariables(request, method).get(parameterName);
        Objects.requireNonNull(pathVariableValue, parameterName + "경로가 존재하지 않습니다.");
        return pathVariableValue;
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
