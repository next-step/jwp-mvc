package core.mvc;

import core.mvc.exception.MethodResolverNotSupportedException;
import core.mvc.resolver.ArgumentResolver;
import core.mvc.resolver.MethodParameter;
import core.mvc.resolver.RequestParamArgumentResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

public class ArgumentScanner {

    private static final List<ArgumentResolver> argumentResolvers = asList(
            new RequestParamArgumentResolver()
    );
    private static final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private final Method method;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public ArgumentScanner(Method method, HttpServletRequest request, HttpServletResponse response) {
        this.method = method;
        this.request = request;
        this.response = response;
    }

    public Object[] getArguments() {
        if (isLegacyMethod()) {
            return new Object[]{request, response};
        }
        return getMethodParameters().stream()
                .map(this::getArgument)
                .toArray();
    }

    private boolean isLegacyMethod() {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return parameterTypes.length == 2
                && parameterTypes[0].equals(HttpServletRequest.class)
                && parameterTypes[1].equals(HttpServletResponse.class);
    }

    private List<MethodParameter> getMethodParameters() {
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        return IntStream.range(0, method.getParameterCount())
                .mapToObj(i -> new MethodParameter(method, parameterTypes[i], parameterAnnotations[i], parameterNames[i]))
                .collect(Collectors.toList());
    }

    private Object getArgument(MethodParameter methodParameter) {
        return argumentResolvers.stream()
                .filter(resolver -> resolver.supports(methodParameter))
                .findAny()
                .orElseThrow(() -> new MethodResolverNotSupportedException(methodParameter.getType()))
                .resolveArgument(methodParameter, request, response);
    }
}
