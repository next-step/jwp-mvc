package core.mvc.tobe.resolver;

import javassist.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;

public class ArgumentResolverMapping {
    private static final Set<HandlerMethodArgumentResolver> RESOLVERS = Set.of(
            new RequestParamArgumentResolver(),
            new HttpRequestArgumentResolver(),
            new HttpResponseArgumentResolver(),
            new SimpleTypeArgumentResolver(),
            new ModelArgumentResolver(),
            new PathVariableArgumentResolver()
    );

    private ArgumentResolverMapping() {
    }

    public static Object[] resolve(Method method, HttpServletRequest request, HttpServletResponse response) throws NotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Parameter[] parameters = method.getParameters();
        int parameterCount = parameters.length;

        Object[] arguments = new Object[parameterCount];

        for (int idx = 0; idx < parameterCount; idx++) {
            Parameter parameter = parameters[idx];
            MethodParameter methodParameter = new MethodParameter(method, parameter, idx);
            HandlerMethodArgumentResolver resolver = getResolver(methodParameter);
            arguments[idx] = resolver.resolveArgument(methodParameter, request, response);
        }

        return arguments;
    }

    private static HandlerMethodArgumentResolver getResolver(MethodParameter methodParameter) throws NotFoundException {
        return RESOLVERS.stream()
                .filter(resolver -> resolver.supportsParameter(methodParameter))
                .findAny()
                .orElseThrow(() -> new NotFoundException("해당 파라미터를 지원하는 리솔버가 존재하지 않습니다."));
    }

}
