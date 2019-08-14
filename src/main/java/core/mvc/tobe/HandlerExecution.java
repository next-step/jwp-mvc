package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object bean;
    private final Method handler;

    public HandlerExecution(Object bean, Method handler) {
        this.bean = bean;
        this.handler = handler;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) handler.invoke(bean, request, response);
    }
}
