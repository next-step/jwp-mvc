package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Object declaredObject;
    private final Method method;

    public HandlerExecution(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        final ParameterResolver parameterResolver = new ParameterResolver(request, response, method);
        final Object[] parameterValues = parameterResolver.resolve();

        try {
            return (ModelAndView) method.invoke(declaredObject, parameterValues);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ServletException(e.getMessage(), e);
        }
    }
}
