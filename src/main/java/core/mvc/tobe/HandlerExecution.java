package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private Class<?> instantiateClazz;
    private Method method;

    public HandlerExecution(Class<?> instantiateClazz, Method method) {
        this.instantiateClazz = instantiateClazz;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        method.invoke(instantiateClazz.newInstance(), request, response);
        return null;
    }
}
