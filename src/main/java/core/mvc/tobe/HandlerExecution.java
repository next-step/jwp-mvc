package core.mvc.tobe;

import core.mvc.ModelAndView;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerExecution {

    private Object declaredObject;
    private Method method;

    public HandlerExecution(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArgumentScanner argumentScanner = new ArgumentScanner(method, request, response);
        return (ModelAndView) method.invoke(declaredObject, argumentScanner.getArguments());
    }
}
