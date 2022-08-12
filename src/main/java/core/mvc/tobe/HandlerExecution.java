package core.mvc.tobe;

import java.lang.reflect.Method;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerExecution {
    private final Object declaredObject;
    private final Method method;

    public HandlerExecution(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(declaredObject, request, response);
    }
}
