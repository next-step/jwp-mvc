package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HandlerExecution {

    private Class<?> clazz;
    private Method method;

    private HandlerExecution(Class<?> clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return (ModelAndView) method.invoke(clazz.newInstance(), request, response);
    }

    public static HandlerExecution of(Class<?> clazz, Method method) {

        if (clazz == null) {
            throw new IllegalArgumentException("Class is null");
        }

        if (method == null) {
            throw new IllegalArgumentException("Method is null");
        }

        if (method.getReturnType() != ModelAndView.class) {
            throw new UnsupportedOperationException();
        }

        return new HandlerExecution(clazz, method);
    }
}
