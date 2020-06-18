package core.mvc.tobe.util;

import core.annotation.web.RequestMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static core.mvc.tobe.util.ReflectionUtils.REQUEST_MAPPING_ANNOTATION_CLASS;

public class PathVariableUtil {
    public static Map<String, String> getPathVariables(Method method, String requestURI) {
        return Optional.ofNullable(ReflectionUtils.getAnnotation(method, REQUEST_MAPPING_ANNOTATION_CLASS))
            .map(annotation -> PathPatternUtil.parse(getBaseUri(method) + annotation.value()))
            .map(pathPattern -> pathPattern.matchAndExtract(PathPatternUtil.toPathContainer(requestURI)))
            .map(PathPattern.PathMatchInfo::getUriVariables)
            .orElse(Collections.emptyMap());
    }

    private static String getBaseUri(Method method) {
        return Optional.ofNullable(ReflectionUtils.getAnnotation(method.getDeclaringClass(), REQUEST_MAPPING_ANNOTATION_CLASS))
            .map(RequestMapping::value)
            .orElse("");
    }
}
