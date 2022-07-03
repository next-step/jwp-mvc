package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.supporter.HandlerMethodArgumentResolverHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecutionImpl implements HandlerExecution{
    private Method method;
    private Object instance;

    public HandlerExecutionImpl(Method method, Object instance) {
        this.method = method;
        this.instance = instance;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] argumentsValues = new HandlerMethodArgumentResolverHelper().getMethodArgumentsValues(method, request);
        return (ModelAndView) method.invoke(instance, argumentsValues);
    }
}
