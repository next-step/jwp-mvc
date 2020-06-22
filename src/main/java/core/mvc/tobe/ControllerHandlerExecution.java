package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class ControllerHandlerExecution extends HandlerExecution{

    private Class clazz;
    private Method method;

    public ControllerHandlerExecution(Class clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        method.invoke(clazz.newInstance(), request, response);
        return super.handle(request, response);
    }
}
