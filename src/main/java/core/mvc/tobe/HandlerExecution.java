package core.mvc.tobe;

import core.mvc.MethodParameter;
import core.mvc.ModelAndView;
import core.mvc.resolver.*;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HandlerExecution {
    private Object instance;
    private Method method;
    private ParameterNameDiscoverer nameDiscoverer;
    private List<MethodParameter> methodParameters;
    private List<HandlerMethodArgumentResolver> argumentResolvers;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
        this.nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        this.methodParameters = initializeMethodParameters();
        this.argumentResolvers = initializeArgumentResolvers();
    }

    private List<MethodParameter> initializeMethodParameters() {
        Parameter[] parameters = this.method.getParameters();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);

        assert parameterNames != null;

        return IntStream.range(0, parameterNames.length)
                .mapToObj(i -> new MethodParameter(parameterNames[i], parameters[i]))
                .collect(Collectors.toList());
    }

    private List<HandlerMethodArgumentResolver> initializeArgumentResolvers() {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
        resolvers.add(new ServletRequestArgumentResolver());
        resolvers.add(new ServletResponseArgumentResolver());
        resolvers.add(new PathVariableArgumentResolver(method));
        resolvers.add(new ParameterArgumentResolver());

        return resolvers;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] arguments = new Object[this.methodParameters.size()];
        for (int i = 0; i < this.methodParameters.size(); i++) {
            MethodParameter methodParameter = methodParameters.get(i);

            Object argument = getResolver(methodParameter).getMethodArgument(methodParameter, request, response);
            arguments[i] = argument;
        }

        return (ModelAndView) method.invoke(instance, arguments);
    }

    private HandlerMethodArgumentResolver getResolver(MethodParameter parameter) {
        return this.argumentResolvers.stream()
                .filter(resolver -> resolver.supports(parameter))
                .findFirst()
                .orElse(null);
    }
}
