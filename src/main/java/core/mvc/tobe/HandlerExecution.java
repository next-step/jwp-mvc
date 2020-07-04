package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.exception.ReflectionsException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;
    private final Object controller;

    public HandlerExecution(Method method, Object controller) {
        this.method = method;
        this.controller = controller;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws ReflectionsException {
        try {
            return (ModelAndView) method.invoke(controller, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionsException("unable to invoke method.", e);
        }
    }
}
