package core.mvc.tobe.argumentresolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

public class HandlerMethodArgumentResolver {

    private static final ParameterNameDiscoverer DISCOVERER = new LocalVariableTableParameterNameDiscoverer();
    private static final List<MethodArgumentResolver> ARGUMENT_RESOLVERS = List.of(
        new HttpServletRequestMethodArgumentResolver(),
        new PathVariableMethodArgumentResolver(),
        new PrimitiveTypeMethodArgumentResolver(),
        new CommandObjectMethodArgumentResolver()
    );

    public Object[] resolve(final Method method, final HttpServletRequest request) {
        final Parameter[] parameters = method.getParameters();
        final String[] parameterNames = getParameterNames(method);

        return IntStream.range(0, parameters.length)
            .mapToObj(i -> new MethodParameter(method, parameters[i], parameterNames[i]))
            .map(methodParameter -> getMethodArgumentResolver(methodParameter, request))
            .toArray();
    }

    private static String[] getParameterNames(final Method method) {
        final String[] parameterNames = DISCOVERER.getParameterNames(method);
        if (parameterNames == null) {
            throw new IllegalStateException();
        }
        return parameterNames;
    }

    private static Object getMethodArgumentResolver(final MethodParameter methodParameter, final HttpServletRequest request) {
        return ARGUMENT_RESOLVERS.stream()
            .filter(resolver -> resolver.resolvable(methodParameter))
            .findAny()
            .map(methodArgumentResolver -> methodArgumentResolver.resolve(methodParameter, request))
            .orElseThrow(IllegalArgumentException::new);
    }

}
