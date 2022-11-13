package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Object classInstance;
    private final Method method;

    public HandlerExecution(Object classInstance, Method method) {
        this.classInstance = classInstance;
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public ModelAndView handle(HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(classInstance, request, response);
    }

    public ModelAndView handle(Object... args) throws InvocationTargetException, IllegalAccessException {
        return (ModelAndView) method.invoke(classInstance, args);
    }
}