package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AnnotationHandlerExecution implements HandlerExecution {

    private final Class clazz;
    private final Method method;

    AnnotationHandlerExecution(final Class clazz, final Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(clazz.newInstance(), request, response);
    }
}
