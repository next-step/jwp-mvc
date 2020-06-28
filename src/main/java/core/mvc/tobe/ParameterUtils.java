package core.mvc.tobe;

import core.annotation.web.PathVariable;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public static boolean isRequestType(Method method) {

        return isPrimitive(method) ||
                Arrays.stream(method.getParameters())
                        .map(Parameter::getType)
                        .anyMatch(ParameterUtils::isRequestParameterTye);
    }

    private static boolean isPrimitive(Method method) {
        return Arrays.stream(method.getParameters())
                .map(Parameter::getType)
                .allMatch(Class::isPrimitive);
    }

    public static boolean isRequestParameterTye(Class<?> parameterType) {
        Object parameter = null;
        try {
            parameter = parameterType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (parameter instanceof String) {
            return true;
        }

        if (parameter instanceof HttpServletRequest) {
            return true;
        }

        if (parameter instanceof HttpServletResponse) {
            return true;
        }

        return false;
    }
}
