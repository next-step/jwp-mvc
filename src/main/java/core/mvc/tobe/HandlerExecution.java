package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {
    private Object handlerObject;
    private Method handlerMethod;

    public HandlerExecution(Object handlerObject, Method handlerMethod) {
        this.handlerObject = handlerObject;
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) handlerMethod.invoke(handlerObject, request, response);
    }
}
