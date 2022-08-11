package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.tobe.method.HandlerMethodArgumentResolver;
import core.mvc.tobe.method.HandlerMethodArgumentResolverComposite;
import core.mvc.tobe.method.MethodParameter;
import core.mvc.tobe.method.MethodParameters;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

public class HandlerExecution {

    private static final Class<?> SUPPORTED_RETURN_TYPE = ModelAndView.class;
    private static final HandlerMethodArgumentResolver RESOLVERS = HandlerMethodArgumentResolverComposite.instance();

    private final Object instance;
    private final Method executedMethod;
    private final MethodParameters methodParameters;

    public HandlerExecution(Object instance, Method executedMethod) {
        Assert.notNull(instance, "'instance' must not be null");
        Assert.notNull(executedMethod, "'executedMethod' must not be null");
        validateMethod(instance, executedMethod);
        this.instance = instance;
        this.executedMethod = executedMethod;
        this.methodParameters = MethodParameters.from(executedMethod);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) executedMethod.invoke(instance, extractArgs(request, response));
    }

    private Object[] extractArgs(HttpServletRequest request, HttpServletResponse response) {
        return methodParameters.mapToArray(parameter ->
                resolveParameter(request, response, parameter), Object[]::new);
    }

    private Object resolveParameter(HttpServletRequest request, HttpServletResponse response, MethodParameter parameter) {
        if (!RESOLVERS.supportsParameter(parameter)) {
            throw new IllegalStateException(String.format("could not resolve parameter(%s)", parameter));
        }
        return RESOLVERS.resolveArgument(parameter, request, response);
    }

    private void validateMethodOfInstance(Object instance, Method executedMethod) {
        if (!List.of(instance.getClass().getDeclaredMethods()).contains(executedMethod)) {
            throw new IllegalArgumentException(String.format("instance(%s) must have executedMethod(%s)", instance, executedMethod));
        }
    }

    private void validateMethod(Object instance, Method executedMethod) {
        validateMethodOfInstance(instance, executedMethod);
        validateReturnType(executedMethod);
    }

    private void validateReturnType(Method executedMethod) {
        if (!SUPPORTED_RETURN_TYPE.equals(executedMethod.getReturnType())) {
            throw new IllegalArgumentException(String.format("executedMethod(%s) must be returned %s type", executedMethod, SUPPORTED_RETURN_TYPE));
        }
    }
}
