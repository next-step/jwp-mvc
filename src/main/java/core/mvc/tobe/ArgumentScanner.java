package core.mvc.tobe;

import core.mvc.exception.MethodResolverNotSupportedException;
import core.mvc.resolver.ArgumentResolver;
import core.mvc.resolver.HttpRequestArgumentResolver;
import core.mvc.resolver.HttpResponseArgumentResolver;
import core.mvc.resolver.MethodParameter;
import core.mvc.resolver.PathVariableArgumentResolver;
import core.mvc.resolver.RequestParamArgumentResolver;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

public class ArgumentScanner {

    private static final List<ArgumentResolver> argumentResolvers = Arrays.asList(
        new HttpRequestArgumentResolver(),
        new HttpResponseArgumentResolver(),
        new RequestParamArgumentResolver(),
        new PathVariableArgumentResolver()
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
        return getMethodParameters().stream()
            .map(this::getArgument)
            .toArray();
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
