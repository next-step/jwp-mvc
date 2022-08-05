package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Enumeration;

public class HandlerExecution {
    private Object object;
    private Method method;

    public HandlerExecution(Object object, Method method) {
        this.object = object;
        this.method= method;
    }

    public Object handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return method.invoke(object, request, response);
    }
}
