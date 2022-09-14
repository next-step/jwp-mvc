package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {
    public HandlerExecution(Object classInstance,
                            Method method) {
        this.classInstance = classInstance;
        this.method = method;
    }

    Object classInstance;
    Method method;
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(classInstance, request, response);
    }
}