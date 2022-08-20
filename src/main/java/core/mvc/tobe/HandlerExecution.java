package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Object declaredObject;
    private final Method method;

    public HandlerExecution(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return getModelAndView(method.invoke(declaredObject, request, response));
    }

    private ModelAndView getModelAndView(Object invokeObject) {
        if (invokeObject instanceof ModelAndView) {
            return (ModelAndView) invokeObject;
        }

        if (invokeObject instanceof String) {
            return new ModelAndView((String) invokeObject);
        }
        return null;
    }
}
