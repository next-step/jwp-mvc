package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentResolvers {
    private static final List<ArgumentResolver> argumentResolvers;

    static {
        argumentResolvers = new ArrayList<>();
        argumentResolvers.add(new BasicTypeArgumentResolver());
        argumentResolvers.add(new BeanTypeArgumentResolver());
        argumentResolvers.add(new ServletRequestArgumentResolver());
        argumentResolvers.add(new ServletResponseArgumentResolver());
        argumentResolvers.add(new PathVariableArgumentResolver());
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

            final ArgumentResolver argumentResolver = argumentResolvers.stream()
                    .filter(resolver -> resolver.equalsTo(type, method))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("요청한 파라미터 타입을 찾을 수 없습니다."));
            values[i] = argumentResolver.getParameterValue(request, response, new ResolverParameter(method, type, parameterName));
        }

        return values;
    }
}
