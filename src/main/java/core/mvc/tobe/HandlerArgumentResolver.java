package core.mvc.tobe;

import com.google.common.collect.Lists;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import lombok.extern.slf4j.Slf4j;
import next.util.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Slf4j
public class HandlerArgumentResolver {
    private static ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private static final String JAVA_LANG_PACKAGE_PREFIX = "java.lang";

    public static Object[] resolve(Method method, HttpServletRequest request, HttpServletResponse response) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);

        List<Object> arguments = Lists.newArrayList();
        Map<String, String> pathVariables = getPathVariables(method, request.getRequestURI());
        log.debug("pathVariables: {}", StringUtils.toPrettyJson(pathVariables));

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> type = parameter.getType();
            String parameterName = parameterNames[i];

            log.debug("parameterName: {}, parameterType: {}", parameterName, type);

            if (isPathVariable(parameter)) {
                PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
                parameterName = StringUtils.getOrDefault(pathVariable.name(), parameterName);
                arguments.add(getPathVariable(pathVariables, parameterName, type));
                continue;
            }

            if (type.equals(HttpServletRequest.class)) {
                arguments.add(request);
                continue;
            }

            if (type.equals(HttpServletResponse.class)) {
                arguments.add(response);
                continue;
            }

            if (isJavaType(type)) {
                addJavaTypeParameter(arguments, request, parameterName, type);
                continue;
            }

            addCustomObject(arguments, request.getParameterMap(), type);
        }

        return arguments.toArray(new Object[0]);
    }

    private static boolean isPathVariable(Parameter parameter) {
        return parameter.isAnnotationPresent(PathVariable.class);
    }


    private static Object getPathVariable(Map<String, String> pathVariables, String name, Class<?> type) {
        return ReflectionUtils.extractFromSingleValuedMap(pathVariables, name, type);
    }

    private static Map<String, String> getPathVariables(Method method, String requestURI) {
        return getPathPattern(method)
            .map(pathPattern -> pathPattern.matchAndExtract(PathPatternUtil.toPathContainer(requestURI)))
            .map(PathPattern.PathMatchInfo::getUriVariables)
            .orElse(Collections.emptyMap());
    }

    private static Optional<PathPattern> getPathPattern(Method method) {
        String baseUri = "";
        RequestMapping typeRequestMapping = method.getDeclaringClass().getAnnotation(RequestMapping.class);

        if (Objects.nonNull(typeRequestMapping)) {
            baseUri = typeRequestMapping.value();
        }

        RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
        String finalBaseUri = baseUri;

        return Optional.ofNullable(methodRequestMapping)
                        .map(annotation -> PathPatternUtil.parse(finalBaseUri + annotation.value()));
    }

    private static boolean isJavaType(Class<?> type) {
        return Optional.ofNullable(type)
            .filter(t -> t.isPrimitive() || t.getName().startsWith(JAVA_LANG_PACKAGE_PREFIX))
            .isPresent();
    }

    private static void addJavaTypeParameter(List<Object> arguments, HttpServletRequest request, String name, Class<?> type) {
        arguments.add(ReflectionUtils.extractFromMultiValuedMap(request.getParameterMap(), name, type));
    }

    private static void addCustomObject(
        List<Object> arguments,
        Map<String, String[]> multiValuedMap,
        Class<?> type
    ) {
        try {
            Object[] parameters = ReflectionUtils.extractFromMultiValuedMap(multiValuedMap, type.getDeclaredFields());
            Object instance = ReflectionUtils.newInstance(type, parameters);

            arguments.add(instance);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            arguments.add(null);
        }
    }
}
