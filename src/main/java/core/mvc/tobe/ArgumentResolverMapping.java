package core.mvc.tobe;

import core.annotation.Component;
import core.di.factory.BeanFactory;
import core.mvc.tobe.resolver.ArgumentResolver;
import core.mvc.tobe.resolver.HttpResponseArgumentResolver;
import core.mvc.tobe.resolver.ModelArgumentResolver;
import core.mvc.tobe.resolver.PathVariableArgumentResolver;
import core.mvc.tobe.resolver.RequestParamArgumentResolver;
import core.mvc.tobe.resolver.SessionArgumentResolver;
import org.reflections.Reflections;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class ArgumentResolverMapping {
    private static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final List<ArgumentResolver> resolvers = List.of(
            new HttpResponseArgumentResolver(),
            new HttpResponseArgumentResolver(),
            new ModelArgumentResolver(),
            new PathVariableArgumentResolver(),
            new RequestParamArgumentResolver(),
            new SessionArgumentResolver()
    );
    Reflections reflections;

    public ArgumentResolverMapping(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Object[] resolve(Method method, HttpServletRequest request, HttpServletResponse response) {
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        int parameterCount= method.getParameterCount();

        Object[] params = new Object[parameterCount];

        for (int idx = 0; idx < parameterCount; idx++) {
            Parameter[] parameter = method.getParameters();
            MethodParameter methodParameter = new MethodParameter(method, parameter[idx]);
            ArgumentResolver resolver = findResolver(methodParameter);
            params[idx] = resolver.resolveArgument(methodParameter, request, response, parameterNames[idx]);
        }

        return params;
    }

    private ArgumentResolver findResolver(MethodParameter methodParameter) {
        return resolvers.stream()
                .filter(resolver -> resolver.support(methodParameter))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
