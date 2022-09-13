package core.mvc.tobe;

import core.annotation.web.Controller;
import core.mvc.ModelAndView;
import core.mvc.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

public class HandlerExecution {

    private Object controller;
    private Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

//    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        return null;
//    }

    public String handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Class<Controller> clazz = (Class<Controller>) controller.getClass();
        Object obj = method.invoke(clazz.newInstance(), request, response);

        return (String) obj;
    }
}
