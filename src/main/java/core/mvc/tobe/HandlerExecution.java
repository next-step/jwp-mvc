package core.mvc.tobe;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;

import core.mvc.ModelAndView;
import core.mvc.tobe.support.ArgumentResolver;

public class HandlerExecution {

    private static final String NOT_FOUND_RESOLVER = "Not found Resolver";

    private final List<ArgumentResolver> argumentResolvers;
    private final Object instance;
    private final Method method;

    public HandlerExecution(List<ArgumentResolver> argumentResolvers, Object instance, Method method) {
        this.argumentResolvers = argumentResolvers;
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] arguments = getArguments(request, response);
        return (ModelAndView) method.invoke(instance, arguments);
    }

    private Object[] getArguments(HttpServletRequest request, HttpServletResponse response) {
        MethodParameter[] methodParameters = new MethodParameter[method.getParameterCount()];
        Object[] arguments = new Object[methodParameters.length];

        for (int i = 0; i < methodParameters.length; i++) {
            MethodParameter methodParameter = new MethodParameter(method, i);
            arguments[i] = getArgument(methodParameter, request, response);
        }
        return arguments;
    }

    private Object getArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        return argumentResolvers.stream()
                .filter(resolver -> resolver.supportsParameter(methodParameter))
                .findFirst()
                .map(resolver -> resolver.resolveArgument(methodParameter, request, response))
                .orElseThrow(() -> new RuntimeException(NOT_FOUND_RESOLVER));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HandlerExecution that = (HandlerExecution) o;
        return Objects.equals(instance, that.instance) && Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instance, method);
    }
}
