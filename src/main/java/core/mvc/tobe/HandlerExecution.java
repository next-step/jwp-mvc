package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    HandlerExecution(final Object controller,
                     final Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request,
                               final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controller, request, response);
    }

    Object getController() {
        return controller;
    }
}
