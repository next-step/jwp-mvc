package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {
    private Object instance;
    private Method method;
    private HandlerMethodArgumentResolver argumentResolver;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
        this.argumentResolver = new HandlerMethodArgumentResolver();
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] arguments = argumentResolver.getMethodArguments(request, method);
        return (ModelAndView) method.invoke(instance, arguments);
    }
}
