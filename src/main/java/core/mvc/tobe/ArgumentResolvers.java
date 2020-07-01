package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArgumentResolvers {
    private static final List<ArgumentResolver> argumentResolvers;

    static {
        argumentResolvers = new ArrayList<>();
        argumentResolvers.add(new BasicTypeArgumentResolver());
        argumentResolvers.add(new BeanTypeArgumentResolver());
    }

    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public Object[] getParameterValues(final Method method, final HttpServletRequest request) throws Exception {
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] values = new Object[parameterNames.length];

        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            final Class<?>[] parameterTypes = method.getParameterTypes();
            for (final Class<?> parameterType : parameterTypes) {
                final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                PathPattern pp = parse(annotation.value());
                if (pp.matches(toPathContainer(request.getRequestURI()))) {
                    final Map<String, String> uriVariables = pp.matchAndExtract(toPathContainer(request.getRequestURI()))
                            .getUriVariables();
                    values[i] = getValueByPathVariable(parameterType, uriVariables.get(parameterName));
                    continue;
                }

                final ArgumentResolver argumentResolver = argumentResolvers.stream()
                        .filter(resolver -> resolver.equalsTo(parameterType))
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("요청한 파라미터 타입을 찾을 수 없습니다."));
                values[i] = argumentResolver.getParameterValue(request, parameterType, parameterName);
            }
        }
        return values;
    }


    private PathPattern parse(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

    private PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
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

}
