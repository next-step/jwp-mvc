package core.mvc.tobe;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.ModelAndView;

public class HandlerExecution {

    private final Object handler;
    private final Method method;
    private final List<ArgumentResolver> argumentResolvers = List.of(
        new HttpServletRequestArgumentResolver(),
        new HttpServletResponseArgumentResolver()
    );

    public HandlerExecution(Object handler, Method method) {
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        MethodParameter[] methodParameters = getMethodParameters();
        Object[] arguments = new Object[methodParameters.length];
        for (int i = 0; i < methodParameters.length; i++) {
            arguments[i] = resolveArgument(methodParameters[i], request, response);
        }
        return (ModelAndView) method.invoke(handler, arguments);
    }

    private MethodParameter[] getMethodParameters() {
        Class<?>[] parameterTypes = method.getParameterTypes();
        MethodParameter[] methodParameters = new MethodParameter[parameterTypes.length];
        for (int index = 0; index < parameterTypes.length; index++) {
            methodParameters[index] = new MethodParameter(parameterTypes[index], method);
        }
        return methodParameters;
    }

    private Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        return argumentResolvers.stream()
            .filter(resolver -> resolver.supportsParameter(parameter))
            .findFirst()
            .orElseThrow(IllegalStateException::new)
            .resolveArgument(parameter, request, response);
    }
}
