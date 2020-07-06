package core.mvc.tobe.resolver;

import core.mvc.exception.ArgumentResolverMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public class ArgumentResolverComposite {

    private Method method;
    private List<ArgumentResolver> argumentResolvers;

    public ArgumentResolverComposite(Method method) {
        this.method = method;
        argumentResolvers = Arrays.asList(
                new RequestParameterArgumentResolver(),
                new PathVariableArgumentResolver(),
                new SessionArgumentResolver());
    }

    public Object[] getArguments(HttpServletRequest request) {
        int count = method.getParameterCount();
        Object[] result = new Object[count];
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < count; i++) {
            ArgumentResolver argumentResolver = getArgumentResolver(parameters[i]);
            result[i] = argumentResolver.resolve(request, method, parameters[i], i);
        }
        return result;
    }

    private ArgumentResolver getArgumentResolver(Parameter parameter) {
        return argumentResolvers.stream()
                .filter(argumentResolver -> argumentResolver.supportsParameter(parameter))
                .findFirst()
                .orElseThrow(ArgumentResolverMismatchException::new);
    }
}
