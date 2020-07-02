package core.mvc.tobe;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ArgumentResolvers {
    private static final List<ArgumentResolver> argumentResolvers;

    static {
        argumentResolvers = new ArrayList<>();
        argumentResolvers.add(new BasicTypeArgumentResolver());
        argumentResolvers.add(new BeanTypeArgumentResolver());
        argumentResolvers.add(new ServletRequestArgumentResolver());
        argumentResolvers.add(new ServletResponseArgumentResolver());
    }

    private static ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public static Object[] getParameterValues(final Method method, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] values = new Object[parameterNames.length];
        Class<?>[] parameterTypes = method.getParameterTypes();

        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            final Class<?> type = parameterTypes[i];

            if (isPathVariable(method)) {
                final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                PathPattern pp = parse(annotation.value());
                if (pp.matches(toPathContainer(request.getRequestURI()))) {
                    final Map<String, String> uriVariables = pp.matchAndExtract(toPathContainer(request.getRequestURI()))
                            .getUriVariables();
                    values[i] = getValueByPathVariable(type, uriVariables.get(parameterName));
                    continue;
                }
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

    private static PathPattern parse(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
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
