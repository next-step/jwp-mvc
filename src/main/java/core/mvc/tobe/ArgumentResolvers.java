package core.mvc.tobe;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArgumentResolvers {
    private static final List<ArgumentResolver> argumentResolvers;

    static {
        argumentResolvers = new ArrayList<>();
        argumentResolvers.add(new BasicTypeArgumentResolver());
        argumentResolvers.add(new BeanTypeArgumentResolver());
        argumentResolvers.add(new ServletRequestArgumentResolver());
        argumentResolvers.add(new ServletResponseArgumentResolver());
    }

    public static Object[] getParameterValues(final Method method, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        String[] parameterNames = Arrays.stream(method.getParameters())
                .map(Parameter::getName)
                .toArray(String[]::new);


        Object[] values = new Object[parameterNames.length];
        Class<?>[] parameterTypes = method.getParameterTypes();

        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            final Class<?> type = parameterTypes[i];

            if (isPathVariable(method)) {
                final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                final List<String> pathValues = getValues(request.getRequestURI(), annotation.value());
                values[i] = getValueByPathVariable(type, pathValues.get(i));
                continue;
            }

            final ArgumentResolver argumentResolver = argumentResolvers.stream()
                    .filter(resolver -> resolver.equalsTo(type))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("요청한 파라미터 타입을 찾을 수 없습니다."));
            values[i] = argumentResolver.getParameterValue(request, response, type, parameterName);
        }

        return values;
    }

    private static boolean isPathVariable(Method method) {
        return Arrays.stream(method.getParameterAnnotations())
                .flatMap(Arrays::stream)
                .anyMatch(annotation -> annotation instanceof PathVariable);
    }

    private static List<String> getNames(String pattern) {
        final String[] split = pattern.split("/");

        return Arrays.stream(split)
                .filter(splitString -> !splitString.isEmpty())
                .filter(word -> word.contains("}"))
                .map(word -> word.substring(0, word.length() - 1))
                .map(word -> word.substring(1))
                .collect(Collectors.toList());
    }

    private static List<String> getValues(String requestUri, String pattern) {
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

    private static Object getValueByPathVariable(Class parameterType, String value) {
        if (parameterType.equals(int.class)) {
            return Integer.parseInt(value);
        }
        if (parameterType.equals(long.class)) {
            return Long.parseLong(value);
        }
        return value;
    }

}
