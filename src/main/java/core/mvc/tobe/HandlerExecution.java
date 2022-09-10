package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.resolver.ArgumentResolver;
import core.mvc.resolver.MethodParameter;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class HandlerExecution {
    private final Object controller;
    private final Method method;
    private final List<ArgumentResolver> argumentResolvers;
    ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public HandlerExecution(List<ArgumentResolver> argumentResolvers, Object controller, Method method) {
        this.argumentResolvers = argumentResolvers;
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Parameter[] methodParameters = method.getParameters();
        Object[] parameters = new Object[methodParameters.length];

        for (int i = 0; i < methodParameters.length; i++) {
            Parameter parameter = methodParameters[i];
            String parameterName = parameterNames[i];
            MethodParameter methodParameter = new MethodParameter(method, parameter, parameterName);
            parameters[i] = getParameter(methodParameter, request, response);

        }
        return (ModelAndView) method.invoke(controller, request, response);
    }

    private Object getParameter(MethodParameter requestParameter, HttpServletRequest request, HttpServletResponse response) {
        for (ArgumentResolver resolver : argumentResolvers) {
            if (resolver.supportsParameter(requestParameter)) {
                return resolver.resolveArgument(requestParameter, request, response);
            }
        }

        throw new IllegalArgumentException("Not Found Adaptable Resolver");
    }
}
