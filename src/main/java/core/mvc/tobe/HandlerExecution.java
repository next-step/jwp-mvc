package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class HandlerExecution {

    private static final Class<HttpServletRequest> SUPPORTED_REQUEST_TYPE = HttpServletRequest.class;
    private static final Class<HttpServletResponse> SUPPORTED_RESPONSE_TYPE = HttpServletResponse.class;
    private static final Set<Class<?>> SUPPORTED_PARAMETER_TYPES = Set.of(SUPPORTED_REQUEST_TYPE, SUPPORTED_RESPONSE_TYPE);
    private static final Class<?> SUPPORTED_RETURN_TYPE = ModelAndView.class;

    private final Object instance;
    private final Method executedMethod;

    public HandlerExecution(Object instance, Method executedMethod) {
        Assert.notNull(instance, "'instance' must not be null");
        Assert.notNull(executedMethod, "'executedMethod' must not be null");
        validateMethod(instance, executedMethod);
        this.instance = instance;
        this.executedMethod = executedMethod;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (SUPPORTED_REQUEST_TYPE.equals(executedMethod.getParameterTypes()[0])) {
            return (ModelAndView) executedMethod.invoke(instance, request, response);
        }
        return (ModelAndView) executedMethod.invoke(instance, response, request);
    }

    private void validateMethodOfInstance(Object instance, Method executedMethod) {
        if (!List.of(instance.getClass().getDeclaredMethods()).contains(executedMethod)) {
            throw new IllegalArgumentException(String.format("instance(%s) must have executedMethod(%s)", instance, executedMethod));
        }
    }

    private void validateMethod(Object instance, Method executedMethod) {
        validateMethodOfInstance(instance, executedMethod);
        validateParameterTypes(executedMethod);
        validateReturnType(executedMethod);
    }

    private void validateReturnType(Method executedMethod) {
        if (!SUPPORTED_RETURN_TYPE.equals(executedMethod.getReturnType())) {
            throw new IllegalArgumentException(String.format("executedMethod(%s) must be returned %s type", executedMethod, SUPPORTED_RETURN_TYPE));
        }
    }

    private void validateParameterTypes(Method executedMethod) {
        if (!SUPPORTED_PARAMETER_TYPES.equals(Set.of(executedMethod.getParameterTypes()))) {
            throw new IllegalArgumentException(String.format("executedMethod(%s) must be had %s types", executedMethod, SUPPORTED_PARAMETER_TYPES));
        }
    }
}
