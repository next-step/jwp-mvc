package core.mvc.tobe;

import core.annotation.web.PathVariable;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class ParameterUtils {

    public static Object decideParameter(String parameter, Class<?> parameterType) {
        if (parameterType.equals(int.class)) {
            return Integer.parseInt(parameter);
        }
        if (parameterType.equals(long.class)) {
            return Long.parseLong(parameter);
        }

        return parameter;
    }

    public static PathPattern parsePath(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

    public static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }

    public static boolean isPathVariable(Method method) {
        return Arrays.stream(method.getParameters())
                .flatMap(p -> Arrays.stream(p.getAnnotations()))
                .anyMatch(a -> a.annotationType().equals(PathVariable.class));
    }

    // TODO: 28/06/2020 Annotation으로 변경하기 전 type으로 구분
    public static boolean isString(Method method) {
        return Arrays.stream(method.getParameters())
                .map(Parameter::getType)
                .anyMatch(ParameterUtils::isStringType);
    }

    public static boolean isHttpRequestType(Method method) {
        return Arrays.stream(method.getParameters())
                .map(Parameter::getType)
                .anyMatch(ParameterUtils::isHttpRequestParameterType);
    }

    public static boolean isPrimitive(Method method) {
        return Arrays.stream(method.getParameters())
                .map(Parameter::getType)
                .allMatch(Class::isPrimitive);
    }

    public static boolean isStringType(Class<?> parameterType) {
        return parameterType.getName().contains("String");
    }

    private static boolean isHttpRequestParameterType(Class<?> parameterType) {
        return parameterType.getName().contains("HttpServletRequest");
    }
}
