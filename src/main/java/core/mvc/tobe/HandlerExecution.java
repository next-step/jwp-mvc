package core.mvc.tobe;

import core.mvc.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    HandlerExecution(final Object controller,
                     final Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final Object... args) throws Exception {
        return (ModelAndView) method.invoke(controller, args);
    }

    public Object getController() {
        return controller;
    }

    public Method getMethod() {
        return method;
    }
}
