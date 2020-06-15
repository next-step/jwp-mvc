package core.mvc.tobe.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.ReflectionUtils;
import next.util.StringUtils;
import org.springframework.web.util.pattern.PathPattern;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static core.mvc.tobe.ReflectionUtils.REQUEST_MAPPING_ANNOTATION_CLASS;

public class PathVariableResolver {
    public static final Class<PathVariable> PATH_VARIABLE_ANNOTATION_CLASS = PathVariable.class;

    public static Map<String, String> getPathVariables(Method method, String requestURI) {
        return getPathPattern(method)
            .map(pathPattern -> pathPattern.matchAndExtract(PathPatternUtil.toPathContainer(requestURI)))
            .map(PathPattern.PathMatchInfo::getUriVariables)
            .orElse(Collections.emptyMap());
    }

    public static boolean isPathVariable(Parameter parameter) {
        return parameter.isAnnotationPresent(PATH_VARIABLE_ANNOTATION_CLASS);
    }

    public static void addPathVariable(
        List<Object> arguments,
        Parameter parameter,
        Map<String, String> pathVariables,
        String name,
        Class<?> type
    ) {
        PathVariable pathVariable = parameter.getAnnotation(PATH_VARIABLE_ANNOTATION_CLASS);
        name = StringUtils.getOrDefault(pathVariable.name(), name);
        arguments.add(getPathVariable(pathVariables, name, type));
    }

    public static Object getPathVariable(Map<String, String> pathVariables, String name, Class<?> type) {
        return ReflectionUtils.extractFromSingleValuedMap(pathVariables, name, type);
    }

    public static Optional<PathPattern> getPathPattern(Method method) {
        String baseUri = getBaseUri(method);

        RequestMapping methodRequestMapping = ReflectionUtils.getAnnotation(method, REQUEST_MAPPING_ANNOTATION_CLASS);

        return Optional.ofNullable(methodRequestMapping)
            .map(annotation -> PathPatternUtil.parse(baseUri + annotation.value()));
    }

    public static String getBaseUri(Method method) {
        String baseUri = "";

        RequestMapping typeRequestMapping = ReflectionUtils.getAnnotation(method.getDeclaringClass(), REQUEST_MAPPING_ANNOTATION_CLASS);

        if (Objects.nonNull(typeRequestMapping)) {
            baseUri = typeRequestMapping.value();
        }

        return baseUri;
    }
}
