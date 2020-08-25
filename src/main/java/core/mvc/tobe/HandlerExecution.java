package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Object object;
    private final Method method;

    private HandlerExecution(final Object object, final Method method) {
        this.object = object;
        this.method = method;
    }

    public static HandlerExecution of(final Object object, final Method method) {
        return new HandlerExecution(object, method);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(object, request, response);
    }
}
