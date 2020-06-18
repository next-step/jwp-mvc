package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.tobe.resolver.HandlerArgumentResolver;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
public class HandlerExecution implements ModelAndViewGettable {
    private final Object instance;
    private final Method method;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] arguments = HandlerArgumentResolver.resolveParameters(method, request, response);
        return resolveModelAndView(request, method.invoke(instance, arguments));
    }

    private ModelAndView resolveModelAndView(HttpServletRequest request, Object result) {
        if (result instanceof String) {
            return getModelAndView(request, (String)result);
        }

        return (ModelAndView) result;
    }

    @Override
    public String toString() {
        return "HandlerExecution{" +
                "instance=" + instance +
                ", method=" + method +
                '}';
    }
}
