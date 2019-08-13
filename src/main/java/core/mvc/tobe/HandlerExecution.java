package core.mvc.tobe;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.ModelAndView;

public class HandlerExecution {
    private final Object instance;
    private final Method method;

    private HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public static HandlerExecution of(Object instance, Method method) {
        return new HandlerExecution(instance, method);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) this.method.invoke(this.instance, request, response);
    }

    public Object getInstance() {
        return instance;
    }

    public Method getMethod() {
        return method;
    }
}
