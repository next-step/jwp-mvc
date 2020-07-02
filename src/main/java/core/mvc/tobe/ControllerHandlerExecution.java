package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ControllerHandlerExecution implements HandlerExecution {
    private final Class clazz;
    private final Method method;

    public ControllerHandlerExecution(final Class clazz, final Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object[] values = ArgumentResolvers.getParameterValues(method, request, response);
        System.out.println(Arrays.toString(values));
        return (ModelAndView) method.invoke(clazz.newInstance(), values);
    }
}
