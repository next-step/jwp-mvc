package core.mvc.tobe;

import core.mvc.tobe.resolver.ArgumentResolverMapping;
import core.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Object controllerInstance;
    private final Method controllerMethod;

    public HandlerExecution(Object controllerInstance, Method controllerMethod) {
        this.controllerInstance = controllerInstance;
        this.controllerMethod = controllerMethod;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] arguments = ArgumentResolverMapping.resolve(controllerMethod, request, response);
        return (ModelAndView) controllerMethod.invoke(controllerInstance, arguments);
    }
}
