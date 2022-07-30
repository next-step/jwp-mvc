package core.mvc.tobe;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.ModelAndView;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(Method method) {
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object handler = method.getDeclaringClass().getDeclaredConstructor().newInstance();
        return (ModelAndView) method.invoke(handler, request, response);
    }
}
