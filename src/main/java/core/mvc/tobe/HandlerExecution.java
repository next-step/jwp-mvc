package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Object invokeInstance;
    private final Method invokeMethod;

    private HandlerExecution(Object invokeInstance, Method invokeMethod) {
        this.invokeInstance = invokeInstance;
        this.invokeMethod = invokeMethod;
    }

    public static HandlerExecution of(Object invokeInstance, Method invokeMethod) {
        return new HandlerExecution(invokeInstance, invokeMethod);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) this.invokeMethod.invoke(this.invokeInstance, request, response);
    }

    public Object getInvokeInstance() {
        return invokeInstance;
    }

    public Method getInvokeMethod() {
        return invokeMethod;
    }
}
