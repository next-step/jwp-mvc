package core.mvc.tobe;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PathVariableArgumentResolver implements ArgumentResolver {

    @Override
    public boolean equalsTo(final Class parameterType, final Method method) {
        return isPathVariable(method);
    }

    @Override
    public Object getParameterValue(final HttpServletRequest request, final HttpServletResponse response, final ResolverParameter resolverParameter) throws Exception {
        Method method = resolverParameter.getMethod();
        Class parameterType = resolverParameter.getType();
        final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        final String value = getValues(request.getRequestURI(), annotation.value()).stream()
                .findFirst()
                .orElseThrow(IllegalAccessError::new);

        return getValueByPathVariable(parameterType, value);
    }

    private List<String> getValues(String requestUri, String pattern) {
        final String[] split = pattern.split("/");

        final List<String> pathNames = Arrays.stream(split)
                .filter(splitString -> !splitString.isEmpty())
                .filter(word -> !word.contains("}"))
                .collect(Collectors.toList());

        final String[] strings = requestUri.split("/");
        return Arrays.stream(strings)
                .filter(splitString -> !splitString.isEmpty())
                .filter(s -> !pathNames.contains(s))
                .collect(Collectors.toList());
    }

    private Object getValueByPathVariable(Class parameterType, String value) {
        if (parameterType.equals(int.class)) {
            return Integer.parseInt(value);
        }
        if (parameterType.equals(long.class)) {
            return Long.parseLong(value);
        }
        return value;
    }

    public static boolean isPathVariable(Method method) {
        return Arrays.stream(method.getParameterAnnotations())
                .flatMap(Arrays::stream)
                .anyMatch(annotation -> annotation instanceof PathVariable);
    }
}
