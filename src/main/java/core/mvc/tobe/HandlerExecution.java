package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private Object object;
    private final Method method;

    public HandlerExecution(final Object obj, final Method method) {
        this.object = obj;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            return (ModelAndView) method.invoke(object, request, response);
        } catch (Throwable e) {
            throw new IOException(e);
        }

    }
}
