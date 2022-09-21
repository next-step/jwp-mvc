package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {
    private Method method;
    private Object instance;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public String handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (String) method.invoke(instance, request, response);
    }
}
