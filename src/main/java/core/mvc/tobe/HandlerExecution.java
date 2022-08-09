package core.mvc.tobe;

import core.mvc.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {
    private final Object instance;
    private final Method method;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(Object... args) throws Exception {
        return (ModelAndView) method.invoke(instance, args);
    }

    public Method getMethod() {
        return method;
    }
}
