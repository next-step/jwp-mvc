package core.mvc.tobe;

import core.mvc.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HandlerExecution {

    private final Object bean;
    private final Method handler;
    private final MethodParameter[] methodParameters;
    private static List<ArgumentResolver> argumentResolvers = new ArrayList<>();

    static {
        argumentResolvers.add(new PrimitiveArgumentResolver());
        argumentResolvers.add(new PathVariableArgumentResolver());
        argumentResolvers.add(new ServletArgumentResolver());
        argumentResolvers.add(new BeanArgumentResolver());
    }

    public HandlerExecution(Object bean, Method handler, MethodParameter[] methodParameters) {
        this.bean = bean;
        this.handler = handler;
        this.methodParameters = methodParameters;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return internalHandle(request, response);
    }

    public ModelAndView internalHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Object[] arguments = new Object[methodParameters.length];

        for (int i = 0; i < methodParameters.length; i++) {
            arguments[i] = resolveArgument(methodParameters[i], request, response);
        }

        return (ModelAndView) handler.invoke(bean, arguments);
    }

    private Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        for (ArgumentResolver methodArgumentResolver : argumentResolvers) {
            if (methodArgumentResolver.supportsParameter(methodParameter)) {
                return methodArgumentResolver.resolveArgument(methodParameter, request, response);
            }
        }
        return null;
    }
}
