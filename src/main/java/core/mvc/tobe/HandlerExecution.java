package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private Object controller;
    private Method method;

    HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(controller, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new HandleException(e);
        }
    }

    @Override
    public String toString() {
        return "HandlerExecution{" +
                "controller=" + controller +
                ", method=" + method +
                '}';
    }
}